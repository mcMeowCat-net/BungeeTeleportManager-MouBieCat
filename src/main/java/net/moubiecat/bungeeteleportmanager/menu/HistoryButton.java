package net.moubiecat.bungeeteleportmanager.menu;

import main.java.me.avankziar.general.object.ServerLocation;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import net.moubiecat.bungeeteleportmanager.services.ServerLocationService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

public final class HistoryButton implements Button {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final ServerLocationService.Formatter formatter = ServerLocationService.formatter();

    private final HistoryData historyData;
    private final Material material;
    private final String display;
    private final List<String> lore;

    public HistoryButton(@NotNull HistoryData historyData, @NotNull Material material, @NotNull String display, @NotNull List<String> lore) {
        this.historyData = historyData;
        this.material = material;
        this.display = display;
        this.lore = lore;
    }

    @Override
    public @NotNull ItemStack build() {
        final ServerLocation fromLocation = historyData.getFrom();
        final ServerLocation toLocation = historyData.getTo();

        this.lore.replaceAll(line -> {
            line = line.replace("{time}", SIMPLE_DATE_FORMAT.format(historyData.getTime()));
            line = line.replace("{from_server}", fromLocation.getServer());
            line = line.replace("{from}", formatter.format_world_x_y_z(fromLocation));
            line = line.replace("{to_server}", toLocation.getServer());
            line = line.replace("{to}", formatter.format_world_x_y_z(toLocation));
            return line;
        });

        return ItemService.build(this.material)
                .name(this.display)
                .lore(this.lore)
                .addPersistentDataContainer(HistoryMenu.ACTION_KEY, PersistentDataType.STRING, ServerLocationService.serialize(fromLocation))
                .build().orElseThrow();
    }
}
