package net.moubiecat.bungeeteleportmanager;

import com.onarandombox.MultiverseCore.MultiverseCore;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import net.moubiecat.bungeeteleportmanager.data.cache.CacheManager;
import net.moubiecat.bungeeteleportmanager.data.database.Database;
import net.moubiecat.bungeeteleportmanager.data.database.PlayerDatabase;
import net.moubiecat.bungeeteleportmanager.data.database.handler.ServerLocationTypeHandler;
import net.moubiecat.bungeeteleportmanager.data.database.handler.UUIDTypeHandler;
import net.moubiecat.bungeeteleportmanager.listener.CommandListener;
import net.moubiecat.bungeeteleportmanager.listener.InventoryListener;
import net.moubiecat.bungeeteleportmanager.listener.PlayerListener;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import net.moubiecat.bungeeteleportmanager.settings.HistoryInventoryYaml;
import net.moubiecat.bungeeteleportmanager.settings.HomeInventoryYaml;
import net.moubiecat.bungeeteleportmanager.utils.BungeeTeleportManagerProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        DATABASE_REGISTRATION.registerTypeHandler(ServerLocation.class, ServerLocationTypeHandler.class);
        DATABASE_REGISTRATION.registerMapper(Database.class);
        DATABASE_REGISTRATION.buildSqlSessionFactory();
        // 綁定插件實例
        INJECT_REGISTRATION.bindPluginInstance(MouBieCat.class, this);
        INJECT_REGISTRATION.bindPluginInstance(MultiverseCore.class, JavaPlugin.getPlugin(MultiverseCore.class));
        INJECT_REGISTRATION.addProvider(BungeeTeleportManager.class, BungeeTeleportManagerProvider.class);
        // 設定檔
        INJECT_REGISTRATION.bindInstance(ConfigYaml.class, new ConfigYaml(this));
        INJECT_REGISTRATION.bindInstance(HistoryInventoryYaml.class, new HistoryInventoryYaml(this));
        INJECT_REGISTRATION.bindInstance(HomeInventoryYaml.class, new HomeInventoryYaml(this));
        // 資料庫
        INJECT_REGISTRATION.bindInstance(Database.class, PlayerDatabase.getInstance());
        INJECT_REGISTRATION.bindInstance(CacheManager.class, CacheManager.getInstance());
        // 綁定注入
        INJECT_REGISTRATION.bindInjector();

        // 創建 MySQL 資料表
        PlayerDatabase.getInstance().createTable();
        // 註冊事件
        Bukkit.getPluginManager().registerEvents(MouBieCat.getInstance(PlayerListener.class), this);
        Bukkit.getPluginManager().registerEvents(MouBieCat.getInstance(InventoryListener.class), this);
        Bukkit.getPluginManager().registerEvents(MouBieCat.getInstance(CommandListener.class), this);
    }

    @Override
    public void onDisable() {
        getInstance(ConfigYaml.class).save();
        getInstance(HistoryInventoryYaml.class).save();
    }

    public void onReload() {
        getInstance(ConfigYaml.class).load();
        getInstance(HistoryInventoryYaml.class).load();
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
     * 取得插件實例
     *
     * @return 插件實例
     */
    @NotNull
    public static MouBieCat getPlugin() {
        return JavaPlugin.getPlugin(MouBieCat.class);
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
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("MBBungeeTeleportManager.reload")) {
            // 如果輸入參數為 reload，則重載插件
            this.onReload();
            sender.sendMessage("§6The plugin BungeeTeleportManager-MouBieCat has been reloaded.");
            return true;
        }

        return false;
    }
}
