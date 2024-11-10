package org.aqu0ryy.spectate;

import org.aqu0ryy.spectate.listeners.SpectateListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loader extends JavaPlugin {

    private static Loader inst;

    @Override
    public void onEnable() {
        inst = this;
        saveDefaultConfig();

        loadListeners();
        loadCommands();
        loadCompleter();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static Loader getInst() {
        return inst;
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new SpectateListener(), this);
    }

    public void loadCommands() {
        getCommand("aqspectate").setExecutor(new Commands());
    }

    public void loadCompleter() {
        getCommand("aqspectate").setTabCompleter(new Commands());
    }
}
