package net.moubiecat.bungeeteleportmanager.settings;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class ConnectionYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public ConnectionYaml(@NotNull Plugin plugin) {
        super(plugin, "connection.yml");
    }

    @NotNull
    public String getHost() {
        return this.configuration.getString("Mysql.Host", "localhost");
    }

    public int getPort() {
        return this.configuration.getInt("Mysql.Port", 3306);
    }

    @NotNull
    public String getDatabase() {
        return this.configuration.getString("Mysql.DatabaseName", "BungeeTeleportManager");
    }

    public String getUser() {
        return this.configuration.getString("Mysql.User");
    }

    public String getPassword() {
        return this.configuration.getString("Mysql.Password");
    }

    public boolean getSSLEnabled() {
        return this.configuration.getBoolean("Mysql.SSLEnabled");
    }

    public boolean getAutoReconnect() {
        return this.configuration.getBoolean("Mysql.AutoReconnect");
    }
}
