package net.moubiecat.bungeeteleportmanager.listener;

import com.google.inject.Inject;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.data.database.HistoryTable;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlayerListener implements Listener {
    private final HistoryTable database;
    private final CacheManager cacheManager;
    private final ConfigYaml config;

    /**
     * 建構子
     *
     * @param database     資料庫
     * @param cacheManager 快取管理器
     */
    @Inject
    public PlayerListener(@NotNull HistoryTable database, @NotNull CacheManager cacheManager, @NotNull ConfigYaml config) {
        this.database = database;
        this.cacheManager = cacheManager;
        this.config = config;
    }

    /**
     * 玩家傳送事件
     *
     * @param event 傳送事件
     */
    @EventHandler
    public void onTeleportEvent(@NotNull PlayerTeleportEvent event) {
        // 獲取傳送資訊
        final Player player = event.getPlayer();
        final PlayerTeleportEvent.TeleportCause cause = event.getCause();
        final Location from = event.getFrom();
        final Location to = event.getTo();

        // 過濾傳送原因、位置
        if (this.config.getCauses().contains(cause.name())) {
            // 儲存傳送資訊
            final CacheData cacheData = this.cacheManager.getCacheData(player.getUniqueId());

            final List<HistoryData> data = cacheData.getData();
            // 是否已經超出最大傳送紀錄
            if (data.size() >= this.config.MaxTeleportHistory()) {
                // 移除到小於最大傳送紀錄
                data.subList(this.config.MaxTeleportHistory() - 1, data.size()).clear();
            }

            // 添加傳送資訊
            cacheData.addData(new HistoryData(
                    player.getUniqueId(),
                    BungeeTeleportManager.getPlugin().getServername(),
                    cause, from, to));
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

        // 取得玩家傳送紀錄
        final List<HistoryData> dataList = this.database.selectData(player.getUniqueId());

        // 建立快取
        final CacheData cacheData = new CacheData(player.getUniqueId());
        // 排序傳送時間 TeleportHistoryData::getTime() 新 -> 舊，並添加到快取，只取 ::MaxTeleportHistory 個
        dataList.stream()
                .sorted((data1, data2) -> (int) (data2.getTime() - data1.getTime()))
                .limit(this.config.MaxTeleportHistory())
                .forEach(cacheData::addData);

        // 儲存快取
        this.cacheManager.addCacheData(player.getUniqueId(), cacheData);
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
