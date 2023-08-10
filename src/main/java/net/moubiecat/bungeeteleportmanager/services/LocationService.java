package net.moubiecat.bungeeteleportmanager.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.java.me.avankziar.general.object.ServerLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LocationService {
    /**
     * 序列化
     *
     * @param serverLocation 伺服器位置
     * @return 序列化後的字串
     */
    public static @NotNull String serialize(@NotNull ServerLocation serverLocation) {
        return new Gson().toJson(serverLocation);
    }

    /**
     * 反序列化
     *
     * @param json 序列化後的字串
     * @return 伺服器位置
     */
    public static @Nullable ServerLocation deserialize(@NotNull String json) throws JsonSyntaxException {
        return new Gson().fromJson(json, ServerLocation.class);
    }

    /**
     * 轉換為伺服器位置
     *
     * @param server   伺服器名稱
     * @param location 位置
     * @return
     */
    public static @NotNull ServerLocation covert(@NotNull String server, @NotNull Location location) {
        return new ServerLocation(server, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
