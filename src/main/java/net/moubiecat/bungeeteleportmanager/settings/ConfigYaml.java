package net.moubiecat.bungeeteleportmanager.settings;

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

    public @NotNull List<String> getCauses() {
        return this.configuration.getStringList("LogCauses");
    }

    public @NotNull List<String> getCommandHomes() {
        return this.configuration.getStringList("CommandHomes");
    }
}
