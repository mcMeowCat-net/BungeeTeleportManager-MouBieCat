package net.moubiecat.bungeeteleportmanager.menu;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Button {
    /**
     * Build the button
     *
     * @return the button
     */
    @NotNull ItemStack build();
}
