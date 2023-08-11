package net.moubiecat.bungeeteleportmanager.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import main.java.me.avankziar.general.object.ServerLocation;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public final class ServerLocationService {
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
     * @return 伺服器位置
     */
    @SuppressWarnings("ConstantConditions")
    public static @NotNull ServerLocation covert(@NotNull String server, @NotNull Location location) {
        return new ServerLocation(server, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * 格式化
     */
    public static @NotNull Formatter formatter() {
        return MouBieCat.getInstance(Formatter.class);
    }

    public static final class Formatter {
        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");
        private static final String FORMAT_PATTERN_SERVER = "§b%s §e%s §8- §7%s§8, §7%s§8, §7%s§r";
        private static final String FORMAT_PATTERN_LOCATION = "§e%s §8- §7%s§8, §7%s§8, §7%s§r";

        private @Inject MultiverseCore multiverseCore;

        /**
         * 格式化
         *
         * @param location 伺服器位置
         * @return 格式化後的字串
         */
        public @NotNull String format_server_world_x_y_z(@NotNull ServerLocation location) {
            // 取得世界別名
            final String worldAlias = this.getWorldAlias(location.getWorldName());
            // 格式化
            return String.format(FORMAT_PATTERN_SERVER,
                    location.getServer(),
                    worldAlias,
                    DECIMAL_FORMAT.format(location.getX()),
                    DECIMAL_FORMAT.format(location.getY()),
                    DECIMAL_FORMAT.format(location.getZ())
            );
        }

        /**
         * 格式化
         *
         * @param location 伺服器位置
         * @return 格式化後的字串
         */
        public @NotNull String format_world_x_y_z(@NotNull ServerLocation location) {
            // 取得世界別名
            final String worldAlias = this.getWorldAlias(location.getWorldName());
            // 格式化
            return String.format(FORMAT_PATTERN_LOCATION,
                    worldAlias,
                    DECIMAL_FORMAT.format(location.getX()),
                    DECIMAL_FORMAT.format(location.getY()),
                    DECIMAL_FORMAT.format(location.getZ())
            );
        }

        /**
         * 取得世界別名
         *
         * @param world 世界名稱
         * @return 別名
         */
        private @NotNull String getWorldAlias(@NotNull String world) {
            final MultiverseWorld mvWorld = multiverseCore.getCore().getMVWorldManager().getMVWorld(world);
            if (mvWorld != null)
                return mvWorld.getAlias();
            return world;
        }
    }
}
