package me.xorgon.flighthud;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        obj.getScore(ChatColor.GREEN + "Airspeed b/m").setScore(0);
        obj.getScore(ChatColor.GREEN + "Ground speed").setScore(0);
        obj.getScore(ChatColor.GREEN + "Heading").setScore(0);

        player.setScoreboard(scoreboard);

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new HUDScheduler(this), 0, 2);
    }

    public void setValues(int airspeed, int groundSpeed, int heading) {
        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        obj.setDisplayName("FlightHUD");
        obj.getScore(ChatColor.GREEN + "Airspeed b/m").setScore(airspeed);
        obj.getScore(ChatColor.GREEN + "Ground speed").setScore(groundSpeed);
        obj.getScore(ChatColor.GREEN + "Heading").setScore(heading);
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

            int airspeed = (int) Math.round(60 * v.length());
            int groundSpeed = (int) Math.round(60 * Math.sqrt(v.getX() * v.getX() + v.getZ() * v.getZ()));
            int heading = Math.round(player.getLocation().getYaw());
            if (heading < 0) {
                heading += 360;
            }
            hud.setValues(airspeed, groundSpeed, heading);
        }
    }
}
