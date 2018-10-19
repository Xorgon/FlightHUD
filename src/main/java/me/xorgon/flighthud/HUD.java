package me.xorgon.flighthud;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

/**
 * Created by Elijah on 01/03/2017.
 */
public class HUD {

    private FlightHUDPlugin plugin;
    private FHManager manager;
    private Scoreboard scoreboard;
    private Scoreboard previousScoreboard;
    private Player player;
    private int taskID;

    public HUD(Player player, FlightHUDPlugin plugin, FHManager manager) {

        previousScoreboard = player.getScoreboard();
        this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.player = player;
        this.manager = manager;

        Objective obj = scoreboard.registerNewObjective("hud", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.GREEN + "Airspeed b/s").setScore(0);
        obj.getScore(ChatColor.GREEN + "Ground speed").setScore(0);
        obj.getScore(ChatColor.GREEN + "Heading").setScore(0);
        obj.getScore(ChatColor.GREEN + "Sink rate b/s").setScore(0);
        obj.getScore(ChatColor.GREEN + "Durability %").setScore(0);

        player.setScoreboard(scoreboard);

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new HUDScheduler(this), 0, 2);
    }

    public void setValues(int airspeed, int groundSpeed, int heading, int sinkRate, int durability) {
        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        obj.setDisplayName("FlightHUD");
        obj.getScore(ChatColor.GREEN + "Airspeed b/s").setScore(airspeed);
        obj.getScore(ChatColor.GREEN + "Ground speed").setScore(groundSpeed);
        obj.getScore(ChatColor.GREEN + "Heading").setScore(heading);
        obj.getScore(ChatColor.GREEN + "Sink rate b/s").setScore(sinkRate);
        obj.getScore(ChatColor.GREEN + "Durability %").setScore(durability);
    }

    public Player getPlayer() {
        return player;
    }

    public FHManager getManager() {
        return manager;
    }

    public void stop() {
        player.setScoreboard(previousScoreboard);
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public class HUDScheduler implements Runnable {

        private HUD hud;

        public HUDScheduler(HUD hud) {
            this.hud = hud;
        }

        public void run() {

            if (!player.isGliding()) {
                hud.getManager().removeHUD(hud.getPlayer());
            }

            Vector v = hud.getPlayer().getVelocity();

            int airspeed = (int) Math.round(20 * v.length());
            int sinkRate = -(int) Math.round(20 * v.getY());
            int groundSpeed = (int) Math.round(20 * Math.sqrt(v.getX() * v.getX() + v.getZ() * v.getZ()));
            int heading = Math.round(player.getLocation().getYaw());
            if (heading < 0) {
                heading += 360;
            }
            ItemStack elytra = player.getInventory().getChestplate();
            short cur_dur = elytra.getDurability();
            short max_dur = elytra.getType().getMaxDurability();
            int durability = Math.round(100 - 100 * cur_dur / max_dur);

            hud.setValues(airspeed, groundSpeed, heading, sinkRate, durability);
        }
    }
}
