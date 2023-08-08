package net.moubiecat.bungeeteleportmanager.settings;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ConfigYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public ConfigYaml(@NotNull Plugin plugin) {
        super(plugin, "config.yml");
    }

    public int MaxTeleportHistory() {
        return this.configuration.getInt("MaxTeleportHistory");
    }

    @NotNull
    public List<String> getCauses() {
        return this.configuration.getStringList("LogCauses");
    }

    @NotNull
    public String getCauseName(@NotNull PlayerTeleportEvent.TeleportCause cause) {
        return this.configuration.getString("CauseName." + cause.name(), cause.name());
    }
}
