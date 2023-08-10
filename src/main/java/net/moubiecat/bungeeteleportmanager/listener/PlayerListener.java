package net.moubiecat.bungeeteleportmanager.listener;

import com.google.inject.Inject;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.data.database.HistoryTable;
import net.moubiecat.bungeeteleportmanager.services.ServerLocationService;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlayerListener implements Listener {
    private @Inject MouBieCat plugin;
    private @Inject BungeeTeleportManager teleportManager;

    private @Inject ConfigYaml config;

    private @Inject HistoryTable database;
    private @Inject CacheManager cacheManager;

    /**
     * 玩家傳送事件
     *
     * @param event 傳送事件
     */
    @EventHandler
    public void onTeleportEvent(@NotNull PlayerTeleportEvent event) {
        // 過濾傳送原因、位置
        if (this.config.getCauses().contains(event.getCause().name())) {
            // 取得玩家資料
            final Player player = event.getPlayer();
            final CacheData cacheData = this.cacheManager.getCacheData(player.getUniqueId());

            // 轉換為伺服器位置
            // 事實上 Servername 是多餘的，因為 BungeeCord 跨分流時並不會觸發該該事件。
            // 且 BungeeTeleportManager 也沒有事件可以監聽 BungeeCord 傳送事件。
            // 我只是為了將來而考量，目前暫且不是太重要的事情。
            final String servername = teleportManager.getServername();
            final ServerLocation fromLocation = ServerLocationService.covert(servername, event.getFrom());
            final ServerLocation toLocation = ServerLocationService.covert(servername, event.getTo());

            // 添加資料
            cacheData.addData(new HistoryData(player.getUniqueId(), fromLocation, toLocation));
        }
    }

    /**
     * 玩家加入事件
     *
     * @param event 加入事件
     */
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // 延遲執行，避免資料庫連線問題
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            final List<HistoryData> dataList = this.database.selectData(player.getUniqueId());
            final CacheData cacheData = new CacheData(player.getUniqueId());
            dataList.stream()
                    .sorted((data1, data2) -> (int) (data2.getTime() - data1.getTime()))
                    .forEach(cacheData::addData);
            this.cacheManager.addCacheData(player.getUniqueId(), cacheData);
        }, 5L);
    }

    /**
     * 玩家離開事件
     *
     * @param event 離開事件
     */
    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        // 獲取快取
        final CacheData cacheData = this.cacheManager.getCacheData(player.getUniqueId());
        // 保存到資料庫，首先刪除資料庫中的資料
        this.database.deleteData(player.getUniqueId());
        // 將快取中的資料保存到資料庫
        cacheData.getData().forEach(this.database::insertData);
        // 移除快取
        this.cacheManager.removeCacheData(player.getUniqueId());
    }
}
