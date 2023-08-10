package net.moubiecat.bungeeteleportmanager.settings;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HomeInventoryYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public HomeInventoryYaml(@NotNull Plugin plugin) {
        super(plugin, "home-inventory.yml");
    }

    public @NotNull String getInventoryTitle() {
        return this.configuration.getString("Title", "");
    }

    public @NotNull String getHomeItemDisplay() {
        return this.configuration.getString("HomeItem.Display", "");
    }

    public @NotNull List<String> getHomeItemLore() {
        return this.configuration.getStringList("HomeItem.Lore");
    }

    public @NotNull Material getTeleportHomeItemMaterial() {
        return Material.valueOf(this.configuration.getString("TeleportItem.Material", "AIR").toUpperCase());
    }

    public @NotNull String getTeleportHomeItemDisplay() {
        return this.configuration.getString("TeleportItem.Display", "");
    }

    public @NotNull List<String> getTeleportHomeItemLore() {
        return this.configuration.getStringList("TeleportItem.Lore");
    }

    public @NotNull Material getDeleteHomeItemMaterial() {
        return Material.valueOf(this.configuration.getString("DeleteItem.Material", "AIR").toUpperCase());
    }

    public @NotNull String getDeleteHomeItemDisplay() {
        return this.configuration.getString("DeleteItem.Display", "");
    }

    public @NotNull List<String> getDeleteHomeItemLore() {
        return this.configuration.getStringList("DeleteItem.Lore");
    }
}
