package me.xorgon.flighthud;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

/**
 * Created by Elijah on 01/03/2017.
 */
public class FHListeners implements Listener{

    FHManager manager;

    public FHListeners(FHManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerFly(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.isGliding()) {
                manager.addHUD(player);
            } else {
                manager.removeHUD(player);
            }
        }
    }
}
