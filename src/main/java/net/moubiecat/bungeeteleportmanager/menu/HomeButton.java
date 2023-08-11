package net.moubiecat.bungeeteleportmanager.menu;

import main.java.me.avankziar.general.object.Home;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import net.moubiecat.bungeeteleportmanager.services.ServerLocationService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HomeButton implements Button {
    private final ServerLocationService.Formatter formatter = ServerLocationService.formatter();

    private final boolean selected;
    private final Home home;
    private final String display;
    private final List<String> lore;

    /**
     * 建構子
     *
     * @param selected 是否為選取的家
     * @param home     家
     * @param display  顯示名稱
     * @param lore     說明
     */
    public HomeButton(boolean selected, @NotNull Home home, @NotNull String display, @NotNull List<String> lore) {
        this.selected = selected;
        this.home = home;
        this.display = display;
        this.lore = lore;
    }

    @Override
    public @NotNull ItemStack build() {
        this.lore.replaceAll(line -> {
            line = line.replace("{name}", home.getHomeName());
            line = line.replace("{server}", home.getLocation().getServer());
            line = line.replace("{location}", formatter.format_world_x_y_z(home.getLocation()));
            return line;
        });
        return ItemService.build(selected ? Material.RED_BED : Material.WHITE_BED)
                .name(this.display)
                .lore(this.lore)
                .addPersistentDataContainer(HomeMenu.ACTION_KEY, PersistentDataType.STRING, home.getHomeName())
                .build()
                .orElseThrow();
    }
}
