package net.moubiecat.bungeeteleportmanager.listener;

import com.google.inject.Inject;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.data.database.Database;
import net.moubiecat.bungeeteleportmanager.services.ServerLocationService;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class PlayerListener implements Listener {
    private @Inject MouBieCat plugin;
    private @Inject BungeeTeleportManager teleportManager;
    private @Inject ConfigYaml config;
    private @Inject Database database;
    private @Inject CacheManager cacheManager;

    /**
     * 玩家傳送事件
     *
     * @param event 傳送事件
     */
    @EventHandler
    public void onTeleportEvent(@NotNull PlayerTeleportEvent event) {
        final UUID player = event.getPlayer().getUniqueId();

        // BungeeTeleportManager 會在傳送到其他伺服器時調用 teleport 方法
        // 但是 PlayerJoinEvent 我們必須延遲執行，避免資料庫連線問題
        // 所以這裡選擇忽略，等待 PlayerJoinEvent 執行。不過該事件也沒有儲存來源伺服器名稱，所以忽略也沒關係。
        // 過濾傳送原因、位置，並且玩家有快取資料
        final CacheData cacheData = this.cacheManager.getCacheData(player);
        if (cacheData != null && this.config.getCauses().contains(event.getCause().name()) && event.getTo() != null) {
            // 獲取伺服器名稱
            final String server = teleportManager.getServername();
            // 轉換位置
            final ServerLocation fromLocation = ServerLocationService.covert(server, event.getFrom());
            final ServerLocation toLocation = ServerLocationService.covert(server, event.getTo());
            // 添加資料
            cacheData.addData(new HistoryData(player, fromLocation, toLocation));
        }
    }

    /**
     * 玩家加入事件
     *
     * @param event 加入事件
     */
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        final UUID player = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            // 查詢資料庫
            final List<HistoryData> dataList = this.database.selectData(player);

            // 建立快取
            final CacheData cacheData = new CacheData(player);
            dataList.stream()
                    .sorted((data1, data2) -> (int) (data2.getTime() - data1.getTime()))
                    .forEach(cacheData::addData);

            // 保存快取
            this.cacheManager.addCacheData(player, cacheData);
        }, 5L);
    }

    /**
     * 玩家離開事件
     *
     * @param event 離開事件
     */
    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        final UUID player = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            // 獲取快取
            final CacheData cacheData = this.cacheManager.getCacheData(player);
            if (cacheData == null)
                return;

            // 保存到資料庫，首先刪除資料庫中的資料
            this.database.deleteData(player);
            cacheData.getData().forEach(this.database::insertData);

            // 移除快取
            this.cacheManager.removeCacheData(player);
        });
    }
}
