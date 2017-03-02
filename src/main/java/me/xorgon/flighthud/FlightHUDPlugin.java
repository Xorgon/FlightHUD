package me.xorgon.flighthud;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Elijah on 01/03/2017.
 */
public class FlightHUDPlugin extends JavaPlugin {

    private static FlightHUDPlugin instance;
    private FHManager manager;

    @Override
    public void onEnable() {
        manager = new FHManager(this);
        Bukkit.getPluginManager().registerEvents(new FHListeners(manager), this);
    }

    public void onDisable() {

    }

    public static FlightHUDPlugin getInstance() {
        return instance;
    }

    public FHManager getManager() {
        return manager;
    }
}
