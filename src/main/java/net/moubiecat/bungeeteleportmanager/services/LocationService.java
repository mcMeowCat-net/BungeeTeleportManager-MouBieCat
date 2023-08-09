package net.moubiecat.bungeeteleportmanager.services;

import com.google.gson.Gson;
import main.java.me.avankziar.general.object.ServerLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LocationService {
    private final Gson gson = new Gson();

    /**
     * 序列化
     *
     * @param serverLocation 伺服器位置
     * @return 序列化後的字串
     */
    @NotNull
    public String serialize(@NotNull ServerLocation serverLocation) {
        return gson.toJson(serverLocation);
    }

    /**
     * 反序列化
     *
     * @param json 序列化後的字串
     * @return 伺服器位置
     */
    @Nullable
    public ServerLocation deserialize(@NotNull String json) {
        return gson.fromJson(json, ServerLocation.class);
    }
}
