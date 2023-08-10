package net.moubiecat.bungeeteleportmanager.menu;

import com.google.inject.Inject;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import net.moubiecat.bungeeteleportmanager.services.LocationService;
import net.moubiecat.bungeeteleportmanager.settings.HistoryInventoryYaml;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public final class HistoryMenu extends Menu {
    // 格式化工具
    private static final DecimalFormat FORMAT = new DecimalFormat("#0.0");
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 時 mm 分 ss 秒");

    // 按鈕動作
    private static final NamespacedKey ACTION_KEY = new NamespacedKey(JavaPlugin.getPlugin(MouBieCat.class), "menu_action");

    // 邊框格子
    private static final int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 24, 25};
    private static final ItemStack BORDER_ITEM = ItemService.build(Material.BLACK_STAINED_GLASS_PANE)
            .name(" ")
            .build()
            .orElseThrow();

    // 可用格子
    private static final int[] AVAILABLE_SLOT = {10, 11, 12, 13, 14, 15, 16};

    // 其它按鈕格子
    private static final int SERVER_SLOT = 21;
    private final ItemStack serverItem;
    private static final int SPAWN_SLOT = 22;
    private final ItemStack spawnItem;
    private static final int BACK_SLOT = 23;
    private final ItemStack backItem;
    private static final int CLEAR_SLOT = 26;
    private final ItemStack clearItem;

    // 玩家資料相關管理器
    private @Inject CacheManager manager;
    private @Inject HistoryInventoryYaml yaml;

    /**
     * 建構子
     *
     * @param yaml 配置檔
     */
    @Inject
    public HistoryMenu(@NotNull HistoryInventoryYaml yaml) {
        super(MenuSize.THREE, yaml.getInventoryTitle());
        this.yaml = yaml;
        serverItem = ItemService.build(yaml.getServerItemMaterial())
                .name(yaml.getServerItemDisplay())
                .lore(yaml.getServerItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, yaml.getServerItemCommand())
                .build().orElseThrow();
        spawnItem = ItemService.build(yaml.getSpawnItemMaterial())
                .name(yaml.getSpawnItemDisplay())
                .lore(yaml.getSpawnItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, yaml.getSpawnItemCommand())
                .build().orElseThrow();
        backItem = ItemService.build(yaml.getBackItemMaterial())
                .name(yaml.getBackItemDisplay())
                .lore(yaml.getBackItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, yaml.getBackItemCommand())
                .build().orElseThrow();
        clearItem = ItemService.build(yaml.getClearItemMaterial())
                .name(yaml.getClearItemDisplay())
                .lore(yaml.getClearItemLore())
                .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, String.valueOf(0))
                .build().orElseThrow();
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
        this.inventory.setItem(SERVER_SLOT, this.serverItem);
        this.inventory.setItem(SPAWN_SLOT, this.spawnItem);
        this.inventory.setItem(BACK_SLOT, this.backItem);
        this.inventory.setItem(CLEAR_SLOT, this.clearItem);
        // 設置玩家傳送歷史
        try {
            final List<HistoryData> dataList = this.manager.getCacheData(view.getUniqueId()).getData();
            for (int index = 0; index < AVAILABLE_SLOT.length; index++) {
                final int slot = AVAILABLE_SLOT[index];
                final HistoryData data = dataList.get(index + (page - 1) * AVAILABLE_SLOT.length);
                // 獲取配置檔資料
                final Material material = this.yaml.getHistoryItemMaterial();
                final String display = this.yaml.getHistoryItemDisplay();
                final List<String> lore = this.yaml.getHistoryItemLore();
                // 轉換佔位符資訊
                lore.replaceAll(line -> line.replace("{time}", DATA_FORMAT.format(data.getTime()))
                        .replace("{server}", data.getServer())
                        .replace("{from}", FORMAT.format(data.getFrom().getX()) + ", " + FORMAT.format(data.getFrom().getY()) + ", " + FORMAT.format(data.getFrom().getZ()))
                        .replace("{to}", FORMAT.format(data.getTo().getX()) + ", " + FORMAT.format(data.getTo().getY()) + ", " + FORMAT.format(data.getTo().getZ())));
                // 轉換為伺服器位置
                final ServerLocation serverLocation = LocationService.covert(data.getServer(), data.getFrom());
                // 設置物品
                this.inventory.setItem(slot, ItemService.build(material)
                        .name(display)
                        .lore(lore)
                        .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, LocationService.serialize(serverLocation))
                        .build().orElseThrow());
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
        return false;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        final int slot = event.getSlot();
        final ItemStack currentItem = event.getCurrentItem();
        final ItemMeta itemMeta = currentItem.getItemMeta();
        final String action = itemMeta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
        if (action == null)
            return;

        switch (slot) {
            case SERVER_SLOT, SPAWN_SLOT, BACK_SLOT -> Bukkit.dispatchCommand(event.getWhoClicked(), action);
            case CLEAR_SLOT -> this.manager.getCacheData(event.getWhoClicked().getUniqueId()).clearData();
            default -> {
                try {
                    final ServerLocation serverLocation = LocationService.deserialize(action);
                    BungeeTeleportManager.getPlugin().getTeleportHandler().sendTpPos((Player) event.getWhoClicked(), serverLocation);
                } catch (final Exception ignored) {
                }
            }
        }

        // 刷新選單
        this.refresh((Player) event.getWhoClicked());
    }
}
