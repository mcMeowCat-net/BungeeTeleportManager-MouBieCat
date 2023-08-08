package net.moubiecat.bungeeteleportmanager.data.database;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class TeleportHistoryData {
    private final long time;
    private final UUID uuid;
    private final String server;
    private final PlayerTeleportEvent.TeleportCause cause;
    private final Location fromLocation;
    private final Location toLocation;

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param cause  傳送原因
     * @param from   來源位置
     * @param to     目標位置
     */
    public TeleportHistoryData(@NotNull UUID uuid, @NotNull String server, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull Location from, @Nullable Location to) {
        this(System.currentTimeMillis(), uuid, server, cause, from, to);
    }

    /**
     * 建構子
     *
     * @param uuid   玩家UUID
     * @param server 伺服器名稱
     * @param cause  傳送原因
     * @param from   來源位置
     * @param to     目標位置
     */
    public TeleportHistoryData(long time, @NotNull UUID uuid, @NotNull String server, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull Location from, @Nullable Location to) {
        this.time = time;
        this.uuid = uuid;
        this.server = server;
        this.cause = cause;
        this.fromLocation = from;
        this.toLocation = to;
    }

    /**
     * 取得傳送時間
     *
     * @return 時間
     */
    public long getTime() {
        return this.time;
    }

    @NotNull
    public UUID getPlayer() {
        return this.uuid;
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
     * 取得傳送原因
     *
     * @return 傳送原因
     */
    @NotNull
    public PlayerTeleportEvent.TeleportCause getCause() {
        return this.cause;
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
