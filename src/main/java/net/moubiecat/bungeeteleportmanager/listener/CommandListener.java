package net.moubiecat.bungeeteleportmanager.listener;

import com.google.inject.Inject;
import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.menu.HistoryMenu;
import net.moubiecat.bungeeteleportmanager.menu.HomeMenu;
import net.moubiecat.bungeeteleportmanager.settings.ConfigYaml;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public final class CommandListener implements Listener {
    private @Inject ConfigYaml configYaml;

    /**
     * 指令 /home
     *
     * @param event 事件
     */
    @EventHandler
    public void onCommandHomes(@NotNull PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (configYaml.getCommandHomes().contains(event.getMessage())) {
            event.setCancelled(true);
            MouBieCat.getInstance(HomeMenu.class).open(player);
        }
    }

    /**
     * 指令 /back
     *
     * @param event 事件
     */
    @EventHandler
    public void onCommandBack(@NotNull PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (configYaml.getCommandBack().contains(event.getMessage())) {
            event.setCancelled(true);
            MouBieCat.getInstance(HistoryMenu.class).open(player);
        }
    }
}
