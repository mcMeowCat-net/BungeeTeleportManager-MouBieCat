package net.moubiecat.bungeeteleportmanager.settings;

import org.bukkit.Material;
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

    public @NotNull String getInventoryTitle() {
        return this.configuration.getString("Title", "");
    }

    public @NotNull Material getHistoryItemMaterial() {
        return Material.valueOf(this.configuration.getString("HistoryItem.Material", "AIR"));
    }

    public @NotNull String getHistoryItemDisplay() {
        return this.configuration.getString("HistoryItem.Display", "");
    }

    public @NotNull List<String> getHistoryItemLore() {
        return this.configuration.getStringList("HistoryItem.Lore");
    }

    public @NotNull Material getServerItemMaterial() {
        return Material.valueOf(this.configuration.getString("ServerItem.Material", "AIR"));
    }

    public @NotNull String getServerItemDisplay() {
        return this.configuration.getString("ServerItem.Display", "");
    }

    public @NotNull List<String> getServerItemLore() {
        return this.configuration.getStringList("ServerItem.Lore");
    }

    public @NotNull String getServerItemCommand() {
        return this.configuration.getString("ServerItem.Command", "");
    }

    public @NotNull Material getSpawnItemMaterial() {
        return Material.valueOf(this.configuration.getString("SpawnItem.Material", "AIR"));
    }

    public @NotNull String getSpawnItemDisplay() {
        return this.configuration.getString("SpawnItem.Display", "");
    }

    public @NotNull List<String> getSpawnItemLore() {
        return this.configuration.getStringList("SpawnItem.Lore");
    }

    public @NotNull String getSpawnItemCommand() {
        return this.configuration.getString("SpawnItem.Command", "");
    }

    public @NotNull Material getBackItemMaterial() {
        return Material.valueOf(this.configuration.getString("BackItem.Material", "AIR"));
    }

    public @NotNull String getBackItemDisplay() {
        return this.configuration.getString("BackItem.Display", "");
    }

    public @NotNull List<String> getBackItemLore() {
        return this.configuration.getStringList("BackItem.Lore");
    }

    public @NotNull String getBackItemCommand() {
        return this.configuration.getString("BackItem.Command", "");
    }

    public @NotNull Material getClearItemMaterial() {
        return Material.valueOf(this.configuration.getString("ClearItem.Material", "AIR"));
    }

    public @NotNull String getClearItemDisplay() {
        return this.configuration.getString("ClearItem.Display", "");
    }

    public @NotNull List<String> getClearItemLore() {
        return this.configuration.getStringList("ClearItem.Lore");
    }
}
