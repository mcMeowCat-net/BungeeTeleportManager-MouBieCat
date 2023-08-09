package net.moubiecat.bungeeteleportmanager;

import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.data.database.HistoryTable;
import net.moubiecat.bungeeteleportmanager.data.database.HistoryTableImpl;
import net.moubiecat.bungeeteleportmanager.data.database.handler.LocationTypeHandler;
import net.moubiecat.bungeeteleportmanager.data.database.handler.UUIDTypeHandler;
import net.moubiecat.bungeeteleportmanager.listener.InventoryListener;
import net.moubiecat.bungeeteleportmanager.listener.PlayerListener;
import net.moubiecat.bungeeteleportmanager.menu.HistoryMenu;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import net.moubiecat.bungeeteleportmanager.settings.HistoryInventoryYaml;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class MouBieCat extends JavaPlugin {
    private static final InjectRegistration INJECT_REGISTRATION = new InjectRegistration();
    private static final DatabaseRegistration DATABASE_REGISTRATION = new DatabaseRegistration();

    @Override
    public void onEnable() {
        // 資料庫依賴
        DATABASE_REGISTRATION.registerTypeHandler(UUID.class, UUIDTypeHandler.class);
        DATABASE_REGISTRATION.registerTypeHandler(Location.class, LocationTypeHandler.class);
        DATABASE_REGISTRATION.registerMapper(HistoryTable.class);
        DATABASE_REGISTRATION.buildSqlSessionFactory();
        // 其它注入依賴
        INJECT_REGISTRATION.register(MouBieCat.class, this);
        INJECT_REGISTRATION.register(ConfigYaml.class, new ConfigYaml(this));
        INJECT_REGISTRATION.register(HistoryInventoryYaml.class, new HistoryInventoryYaml(this));
        INJECT_REGISTRATION.register(HistoryTable.class, new HistoryTableImpl());
        INJECT_REGISTRATION.register(CacheManager.class, new CacheManager());
        INJECT_REGISTRATION.bindInjector();

        // 創建 MySQL 資料表
        InjectRegistration.INJECTOR.getInstance(HistoryTable.class).createTable();
        // 註冊事件
        Bukkit.getPluginManager().registerEvents(MouBieCat.getInstance(PlayerListener.class), this);
        Bukkit.getPluginManager().registerEvents(MouBieCat.getInstance(InventoryListener.class), this);
    }

    @Override
    public void onDisable() {
        InjectRegistration.INJECTOR.getInstance(ConfigYaml.class).save();
        InjectRegistration.INJECTOR.getInstance(HistoryInventoryYaml.class).save();
    }

    public void onReload() {
        InjectRegistration.INJECTOR.getInstance(ConfigYaml.class).load();
        InjectRegistration.INJECTOR.getInstance(HistoryInventoryYaml.class).load();
    }

    /**
     * 從注入器取得實例
     *
     * @param clazz 類
     * @param <T>   類型
     * @return 實例
     */
    @NotNull
    public static <T> T getInstance(@NotNull Class<T> clazz) {
        return InjectRegistration.INJECTOR.getInstance(clazz);
    }

    /**
     * 取得 SqlSessionFactory
     *
     * @return SqlSessionFactory
     */
    @NotNull
    public static SqlSessionFactory getSqlSessionFactory() {
        return DatabaseRegistration.SQL_SESSION_FACTORY;
    }

    /**
     * 指令
     *
     * @param sender  來源
     * @param command 指令
     * @param label   標籤
     * @param args    參數
     * @return 是否成功
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length == 1 && args[0].equalsIgnoreCase("history")) {
            // 如果輸入參數為 history，則顯示歷史紀錄
            MouBieCat.getInstance(HistoryMenu.class).open(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("MBBungeeTeleportManager.reload")) {
            // 如果輸入參數為 reload，則重載插件
            this.onReload();
            sender.sendMessage("§6The plugin BungeeTeleportManager-MouBieCat has been reloaded.");
            return true;
        }

        return false;
    }
}
