package net.moubiecat.bungeeteleportmanager.data;

import main.java.me.avankziar.general.object.ServerLocation;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import net.moubiecat.bungeeteleportmanager.services.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
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
     * @param action  動作
     * @param display 顯示名稱
     * @param lore    說明
     * @return 物品堆
     */
    @SuppressWarnings("ConstantConditions")
    public @NotNull ItemStack buildHistoryItemStack(@NotNull NamespacedKey action, @NotNull String display, @NotNull List<String> lore) {
        // 替換佔位符
        lore.replaceAll(s -> s.replace("{time}", new java.text.SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 時 mm 分 ss 秒").format(this.time)));
        lore.replaceAll(s -> s.replace("{server}", this.server));
        lore.replaceAll(s -> s.replace("{cause}", this.cause.name()));
        lore.replaceAll(s -> s.replace("{from}", this.fromLocation.getX() + ", " + this.fromLocation.getY() + ", " + this.fromLocation.getZ()));
        lore.replaceAll(s -> s.replace("{to}", this.toLocation.getX() + ", " + this.toLocation.getY() + ", " + this.toLocation.getZ()));

        // 序列化
        final LocationSerialization serialization = new LocationSerialization();
        final ServerLocation location = new ServerLocation(
                this.server,
                this.fromLocation.getWorld().getName(),
                this.fromLocation.getX(), this.fromLocation.getY(), this.fromLocation.getZ(),
                this.fromLocation.getYaw(), this.fromLocation.getPitch());

        // 建立物品堆
        return ItemService.build(Material.ENDER_PEARL)
                .name(display)
                .lore(lore)
                .addPersistentDataContainer(action, PersistentDataType.STRING, serialization.serialize(location))
                .build()
                .orElse(new ItemStack(Material.AIR));
    }
}
