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
}
