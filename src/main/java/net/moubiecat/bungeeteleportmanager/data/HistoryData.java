package net.moubiecat.bungeeteleportmanager.data;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class HistoryData {
    private final UUID uuid;
    private final long time;
    private final String server;
    private final Location fromLocation;
    private final Location toLocation;

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param from   來源位置
     * @param to     目標位置
     */
    public HistoryData(@NotNull UUID uuid, @NotNull String server, @NotNull Location from, @Nullable Location to) {
        this(uuid, System.currentTimeMillis(), server, from, to);
    }

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param from   來源位置
     * @param to     目標位置
     */
    public HistoryData(@NotNull UUID uuid, long time, @NotNull String server, @NotNull Location from, @Nullable Location to) {
        this.uuid = uuid;
        this.time = time;
        this.server = server;
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
     * 取得伺服器名稱
     *
     * @return 伺服器名稱
     */
    @NotNull
    public String getServer() {
        return this.server;
    }

    /**
     * 取得來源位置
     *
     * @return 來源位置
     */
    @NotNull
    public Location getFrom() {
        return this.fromLocation;
    }

    /**
     * 取得目標位置
     *
     * @return 目標位置
     */
    @Nullable
    public Location getTo() {
        return this.toLocation;
    }
}
