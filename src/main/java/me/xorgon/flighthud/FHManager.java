package me.xorgon.flighthud;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elijah on 01/03/2017.
 */
public class FHManager {

    private Map<Player, HUD> huds;
    private FlightHUDPlugin plugin;

    public FHManager(FlightHUDPlugin plugin) {
        this.plugin = plugin;
        huds = new HashMap<Player, HUD>();
    }

    public void addHUD(Player player) {
        huds.put(player, new HUD(player, plugin, this));
    }

    public void removeHUD(Player player) {
        if (huds.containsKey(player)) {
            huds.get(player).stop();
            huds.remove(player);
        }
    }
}
