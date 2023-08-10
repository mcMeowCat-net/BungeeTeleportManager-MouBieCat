package net.moubiecat.bungeeteleportmanager.data;

import main.java.me.avankziar.general.object.ServerLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class HistoryData {
    private final UUID uuid;
    private final long time;
    private final ServerLocation fromLocation;
    private final ServerLocation toLocation;

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param from   來源位置
     * @param to     目標位置
     */
    public HistoryData(@NotNull UUID uuid, @NotNull ServerLocation from, @Nullable ServerLocation to) {
        this(uuid, System.currentTimeMillis(), from, to);
    }

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param from   來源位置
     * @param to     目標位置
     */
    public HistoryData(@NotNull UUID uuid, long time, @NotNull ServerLocation from, @Nullable ServerLocation to) {
        this.uuid = uuid;
        this.time = time;
        this.fromLocation = from;
        this.toLocation = to;
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
     * 取得傳送時間
     *
     * @return 時間
     */
    public long getTime() {
        return this.time;
    }

    /**
     * 取得來源位置
     *
     * @return 來源位置
     */
    @NotNull
    public ServerLocation getFrom() {
        return this.fromLocation;
    }

    /**
     * 取得目標位置
     *
     * @return 目標位置
     */
    @NotNull
    public ServerLocation getTo() {
        return this.toLocation;
    }
}
