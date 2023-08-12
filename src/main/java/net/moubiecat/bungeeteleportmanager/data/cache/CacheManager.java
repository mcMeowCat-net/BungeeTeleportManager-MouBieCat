package net.moubiecat.bungeeteleportmanager.data.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CacheManager {
    private static final CacheManager INSTANCE = new CacheManager();

    private final Map<UUID, CacheData> cacheData = new HashMap<>();

    /**
     * 建構子
     * 這裡是單例模式
     */
    CacheManager() {
    }

    /**
     * 取得快取資料
     *
     * @param uuid 玩家UUID
     * @param data 傳送資料
     */
    public void addCacheData(@NotNull UUID uuid, @NotNull CacheData data) {
        this.cacheData.putIfAbsent(uuid, data);
    }

    /**
     * 刪除快取資料
     *
     * @param uuid 玩家UUID
     */
    public void removeCacheData(@NotNull UUID uuid) {
        this.cacheData.remove(uuid);
    }

    /**
     * 取得快取資料
     *
     * @param uuid 玩家UUID
     * @return 快取資料
     */
    public @Nullable CacheData getCacheData(@NotNull UUID uuid) {
        return this.cacheData.get(uuid);
    }

    /**
     * 取得快取管理器
     *
     * @return 快取管理器
     */
    public static @NotNull CacheManager getInstance() {
        return INSTANCE;
    }
}
