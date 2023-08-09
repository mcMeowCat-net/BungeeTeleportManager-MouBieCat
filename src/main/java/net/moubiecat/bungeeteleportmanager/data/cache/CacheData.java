package net.moubiecat.bungeeteleportmanager.data.cache;

import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class CacheData {
    private final UUID uuid;
    private final List<HistoryData> data = new LinkedList<>();

    /**
     * 建構子
     *
     * @param uuid 玩家UUID
     */
    public CacheData(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * 取得玩家UUID
     *
     * @return 玩家UUID
     */
    @NotNull
    public UUID getPlayer() {
        return this.uuid;
    }

    /**
     * 添加玩家傳送資料
     *
     * @param data 傳送資料
     */
    public void addData(@NotNull HistoryData data) {
        this.data.add(data);
    }

    /**
     * 取得玩家傳送資料
     *
     * @return 傳送資料
     */
    @NotNull
    public List<HistoryData> getData() {
        return this.data;
    }
}
