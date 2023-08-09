package net.moubiecat.bungeeteleportmanager.menu;

import com.google.inject.Inject;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheData;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.services.ItemService;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class HistoryMenu extends Menu {
    private final static NamespacedKey ACTION_KEY = new NamespacedKey(MouBieCat.getInstance(MouBieCat.class), "action");

    private final static int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 24, 25, 26};
    private final static int PREVIOUS_PAGE_SLOT = 9;
    private final static int NEXT_PAGE_SLOT = 17;
    private final static int HISTORY_BUTTON_SLOTS = 7;

    private final ItemStack borderItem = ItemService.build(Material.BLACK_STAINED_GLASS_PANE)
            .name(" ")
            .build()
            .orElseThrow();
    private final ItemStack previousItem = ItemService.build(Material.ARROW)
            .name("§f<-")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "previous")
            .build()
            .orElseThrow();
    private final ItemStack nextItem = ItemService.build(Material.ARROW)
            .name("§f->")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "next")
            .build()
            .orElseThrow();

    private final CacheManager manager;

    /**
     * 建構子
     */
    @Inject
    public HistoryMenu(@NotNull CacheManager manager) {
        super(MenuSize.THREE, "        §8 ▼ 玩家傳送歷史選單 ▼ ");
        this.manager = manager;
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
        Arrays.stream(BORDER_SLOT).forEach(slot -> this.inventory.setItem(slot, this.borderItem));
        // 設置翻頁按鈕
        this.inventory.setItem(PREVIOUS_PAGE_SLOT, this.previousItem);
        this.inventory.setItem(NEXT_PAGE_SLOT, this.nextItem);
        // 設置傳送歷史按鈕
        this.manager.getCacheData(view.getUniqueId())
                .getData()
                .stream()
                .skip((long) (page - 1) * HISTORY_BUTTON_SLOTS)
                .limit(HISTORY_BUTTON_SLOTS)
                .forEach(history -> this.inventory.addItem(history.buildHistoryItemStack(view, ACTION_KEY)));
    }

    /**
     * 是否有下一頁
     *
     * @return 是否有下一頁
     */
    @Override
    protected boolean hasNextPage(@NotNull Player player, int page) {
        final CacheData cacheData = manager.getCacheData(player.getUniqueId());
        return cacheData.getData().size() > page * HISTORY_BUTTON_SLOTS;
    }

    /**
     * InventoryClickEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        // 取消點擊事件
        event.setCancelled(true);

        // 獲取點擊的物品
        final Player player = (Player) event.getWhoClicked();
        final ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getItemMeta() == null)
            return;
        // 獲取物品的 Action
        final ItemMeta itemMeta = currentItem.getItemMeta();
        final String action = itemMeta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
        if (action == null)
            return;

        // 根據 Action 執行對應的動作
        switch (action) {
            case "previous" -> this.previous(player);
            case "next" -> this.next(player);
        }

        // 更新選單
        this.refresh(player);
    }
}
