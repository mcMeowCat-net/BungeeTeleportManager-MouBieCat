package net.moubiecat.bungeeteleportmanager.settings;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HistoryInventoryYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public HistoryInventoryYaml(@NotNull Plugin plugin) {
        super(plugin, "history-inventory.yml");
    }

    @NotNull
    public String getHistoryDisplay() {
        return this.configuration.getString("HistoryItem.Display", "§6History Display Name");
    }

    @NotNull
    public List<String> getHistoryLore() {
        return this.configuration.getStringList("HistoryItem.Lore");
    }

    @NotNull
    public String getServerCommand() {
        return this.configuration.getString("ServerItem.Command", "server");
    }

    @NotNull
    public String getServerDisplay() {
        return this.configuration.getString("ServerItem.Display", "§6Server Display Name");
    }

    @NotNull
    public String getSpawnCommand() {
        return this.configuration.getString("SpawnItem.Command", "spawn");
    }

    @NotNull
    public String getSpawnDisplay() {
        return this.configuration.getString("SpawnItem.Display", "§6Spawn Display Name");
    }

    @NotNull
    public String getBackCommand() {
        return this.configuration.getString("BackItem.Command", "back");
    }

    @NotNull
    public String getBackDisplay() {
        return this.configuration.getString("BackItem.Display", "§6Back Display Name");
    }
}
