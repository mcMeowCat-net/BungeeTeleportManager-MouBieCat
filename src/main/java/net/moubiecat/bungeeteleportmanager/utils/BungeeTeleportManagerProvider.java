package net.moubiecat.bungeeteleportmanager.utils;

import com.google.inject.Provider;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public final class BungeeTeleportManagerProvider implements Provider<BungeeTeleportManager> {
    @Override
    public BungeeTeleportManager get() {
        // 我也不知道為什麼 BungeeTeleportManager 不能直接 bindPluginInstance()
        // 所以就用這個 Provider 來繞過去，之後再看看有沒有更好的方法。
        return BungeeTeleportManager.getPlugin();
    }
}
