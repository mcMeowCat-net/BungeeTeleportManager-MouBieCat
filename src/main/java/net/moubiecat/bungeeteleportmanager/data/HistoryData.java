package net.moubiecat.bungeeteleportmanager.data;

import net.moubiecat.bungeeteleportmanager.services.ItemService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public final class HistoryData {
    private final UUID uuid;
    private final long time;
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
    public HistoryData(@NotNull UUID uuid, @NotNull String server, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull Location from, @Nullable Location to) {
        this(uuid, System.currentTimeMillis(), server, cause, from, to);
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
    public HistoryData(@NotNull UUID uuid, long time, @NotNull String server, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull Location from, @Nullable Location to) {
        this.uuid = uuid;
        this.time = time;
        this.server = server;
        this.cause = cause;
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

    /**
     * 建立物品堆
     *
     * @return 物品堆
     */
    public @NotNull ItemStack buildHistoryItemStack(@NotNull Player player, @NotNull NamespacedKey key) {
        // 建立基礎頻道物品
        return ItemService.build(Material.ENDER_PEARL)
                .name("&6傳送點紀錄")
                .lore(List.of(
                        "",
                        "  &8傳送時間： " + new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.time),
                        "  &8分流伺服： " + this.server,
                        "  &8傳送原因： " + this.cause.name(),
                        "  &8來源位置： " + this.fromLocation.getX() + ", " + this.fromLocation.getY() + ", " + this.fromLocation.getZ(),
                        "  &8目的位置： " + this.toLocation.getX() + ", " + this.toLocation.getY() + ", " + this.toLocation.getZ(),
                        ""))
                .build()
                .orElse(new ItemStack(Material.AIR));
    }
}
