package net.moubiecat.bungeeteleportmanager.menu;

import com.google.inject.Inject;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import net.moubiecat.bungeeteleportmanager.services.ServerLocationService;
import net.moubiecat.bungeeteleportmanager.settings.HomeInventoryYaml;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HomeMenu extends Menu {
    // 按鈕動作
    private static final NamespacedKey ACTION_KEY = new NamespacedKey(MouBieCat.getPlugin(), "menu_action");

    // 邊框格子
    private static final int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    private static final ItemStack BORDER_ITEM = ItemService.build(Material.BLACK_STAINED_GLASS_PANE)
            .name(" ")
            .build()
            .orElseThrow();

    // 可用格子
    private static final int[] AVAILABLE_SLOT = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    private static final int PREVIOUS_PAGE_SLOT = 9;
    private static final ItemStack PREVIOUS_PAGE_ITEM = ItemService.build(Material.ARROW)
            .name("§f<-")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "previous")
            .build()
            .orElseThrow();
    private final static int NEXT_PAGE_SLOT = 17;
    private static final ItemStack NEXT_PAGE_ITEM = ItemService.build(Material.ARROW)
            .name("§f->")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "next")
            .build()
            .orElseThrow();

    private static final int TELEPORT_SLOT = 38;
    private final ItemStack teleportItem;
    private static final int DELETE_SLOT = 42;
    private final ItemStack deleteItem;

    private @Inject MouBieCat plugin;
    private @Inject BungeeTeleportManager teleportManager;

    private @Inject HomeInventoryYaml yaml;

    private String currentHome = String.valueOf(0);

    /**
     * 建構子
     *
     * @param yaml 配置檔
     */
    @Inject
    public HomeMenu(@NotNull HomeInventoryYaml yaml) {
        super(MenuSize.SIX, yaml.getInventoryTitle());
        teleportItem = ItemService.build(yaml.getTeleportHomeItemMaterial())
                .name(yaml.getTeleportHomeItemDisplay())
                .lore(yaml.getTeleportHomeItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "teleport")
                .build()
                .orElseThrow();
        deleteItem = ItemService.build(yaml.getDeleteHomeItemMaterial())
                .name(yaml.getDeleteHomeItemDisplay())
                .lore(yaml.getDeleteHomeItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "delete")
                .build()
                .orElseThrow();
    }

    /**
     * 初始化選單內容
     *
     * @param view 開啟選單的玩家
     * @param page 頁數
     */
    @Override
    protected void initialize(@NotNull Player view, int page) {
        this.inventory.clear();
        // 設置選單邊框
        Arrays.stream(BORDER_SLOT).forEach(slot -> this.inventory.setItem(slot, BORDER_ITEM));
        // 設置其它按鈕
        this.inventory.setItem(PREVIOUS_PAGE_SLOT, PREVIOUS_PAGE_ITEM);
        this.inventory.setItem(NEXT_PAGE_SLOT, NEXT_PAGE_ITEM);
        this.inventory.setItem(TELEPORT_SLOT, teleportItem);
        this.inventory.setItem(DELETE_SLOT, deleteItem);
        // 設置可用格子
        final ArrayList<String> homeNames = teleportManager.homes.get(view.getName());
        try {
            for (int index = 0; index < AVAILABLE_SLOT.length; index++) {
                // 獲取格子位置和家的名稱
                final int slot = AVAILABLE_SLOT[index];
                final String homeName = homeNames.get((page - 1) * AVAILABLE_SLOT.length + index);
                final Home home = (Home) teleportManager.getMysqlHandler().getData(MysqlHandler.Type.HOME, "`player_uuid` = ? AND `home_name` = ?", new Object[]{view.getUniqueId().toString(), homeName});
                // 獲取配置檔資料
                final String display = this.yaml.getHomeItemDisplay();
                final List<String> lore = this.yaml.getHomeItemLore();
                // 轉換佔位符資訊
                final ServerLocationService.Formatter formatter = ServerLocationService.formatter();
                lore.replaceAll(line -> {
                    line = line.replace("{name}", home.getHomeName());
                    line = line.replace("{server}", home.getLocation().getServer());
                    line = line.replace("{location}", formatter.format_world_x_y_z(home.getLocation()));
                    return line;
                });
                // 設置物品
                this.inventory.setItem(slot, ItemService.build(this.currentHome.equalsIgnoreCase(homeName) ? Material.RED_BED : Material.WHITE_BED)
                        .name(display)
                        .lore(lore)
                        .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, home.getHomeName())
                        .build()
                        .orElseThrow());
            }
        } catch (final IndexOutOfBoundsException ignored) {
        }
    }

    /**
     * 是否有下一頁
     *
     * @param player
     * @param page
     * @return 是否有下一頁
     */
    @Override
    protected boolean hasNextPage(@NotNull Player player, int page) {
        return page * AVAILABLE_SLOT.length < this.teleportManager.homes.get(player.getName()).size();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        final Player player = (Player) event.getWhoClicked();
        final ItemStack currentItem = event.getCurrentItem();
        final ItemMeta itemMeta = currentItem.getItemMeta();
        final String action = itemMeta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
        if (action == null)
            return;

        switch (action) {
            case "previous" -> this.previous(player);
            case "next" -> this.next(player);
            case "teleport" -> this.teleportManager.getHomeHelper().homeTo(player, new String[]{this.currentHome});
            case "delete" -> {
                this.teleportManager.getHomeHelper().homeRemove(player, new String[]{this.currentHome});
                // 延遲刷新選單
                Bukkit.getScheduler().runTaskLater(plugin, () -> this.refresh(player), 5L);
                return;
            }
            default -> {
                this.currentHome = action;
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }

        // 刷新選單
        this.refresh(player);
    }
}
