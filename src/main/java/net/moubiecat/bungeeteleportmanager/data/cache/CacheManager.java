package net.moubiecat.bungeeteleportmanager.data.cache;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CacheManager {
    private final Map<UUID, CacheData> cacheData = new HashMap<>();

    /**
     * 取得快取資料
     *
     * @param uuid 玩家UUID
     * @param data 傳送資料
     */
    public void addCacheData(@NotNull UUID uuid, @NotNull CacheData data) {
        this.cacheData.put(uuid, data);
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
    @NotNull
    public CacheData getCacheData(@NotNull UUID uuid) {
        if (!this.cacheData.containsKey(uuid))
            this.cacheData.put(uuid, new CacheData(uuid));

        return this.cacheData.get(uuid);
    }
}
