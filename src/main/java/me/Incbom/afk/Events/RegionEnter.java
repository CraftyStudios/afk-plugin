package me.Incbom.afk.Events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Incbom.afk.Main;
import me.Incbom.afk.Tasks.AfkTimer;
import me.Incbom.afk.Tasks.Bossbar;

public class RegionEnter implements Listener {

    private Main plugin;
    private AfkTimer afkTimer;
    private Bossbar bossbar;
    private Map<UUID, Boolean> isInRegion = new HashMap<>();

    public RegionEnter(Main plugin) {
        this.plugin = plugin;
        this.afkTimer = new AfkTimer(plugin);
        this.bossbar = new Bossbar(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        // Retrieve the coordinates from the config.yml
        double coord1X = plugin.getConfig().getDouble("coords-1-x");
        double coord1Y = plugin.getConfig().getDouble("coords-1-y");
        double coord1Z = plugin.getConfig().getDouble("coords-1-z");

        double coord2X = plugin.getConfig().getDouble("coords-2-x");
        double coord2Y = plugin.getConfig().getDouble("coords-2-y");
        double coord2Z = plugin.getConfig().getDouble("coords-2-z");

        // Create the region boundaries using the coordinates
        Location boundary1 = new Location(player.getWorld(), coord1X, coord1Y, coord1Z);
        Location boundary2 = new Location(player.getWorld(), coord2X, coord2Y, coord2Z);

        // Check if the player's new location is within the region boundaries
        boolean isWithinRegion = isWithinRegion(to, boundary1, boundary2);

        UUID playerId = player.getUniqueId();
        boolean isbossbarEnabled = plugin.getConfig().getBoolean("bossbar");

        if (isWithinRegion) {
            // Player has entered the region, start the timer if not already in the region
            if (!isInRegion.getOrDefault(playerId, false)) {
                isInRegion.put(playerId, true);
                afkTimer.startTimer(player, plugin.getConfig().getInt("time"));
                if (isbossbarEnabled) {
                    bossbar.startBossBar(player, plugin.getConfig().getInt("time"));
                }
                // Player has entered the region
                player.sendMessage(plugin.getConfig().getString("prefix").replaceAll("&", "§") + plugin.getConfig().getString("region-enter-message").replaceAll("&", "§"));
            }
        } else {
            // Player has left the region, stop the timer and boss bar if they were in the region
            if (isInRegion.getOrDefault(playerId, false)) {
                isInRegion.put(playerId, false);
                AfkTimer.resetTimer(player);
                if (isbossbarEnabled) {
                    bossbar.stopBossBar(player);
                }
                // Player has left the region
                player.sendMessage(plugin.getConfig().getString("prefix").replaceAll("&", "§") + plugin.getConfig().getString("region-leave-message").replaceAll("&", "§"));
            }
        }
    }

    private boolean isWithinRegion(Location location, Location boundary1, Location boundary2) {
        double minX = Math.min(boundary1.getX(), boundary2.getX());
        double minY = Math.min(boundary1.getY(), boundary2.getY());
        double minZ = Math.min(boundary1.getZ(), boundary2.getZ());
        double maxX = Math.max(boundary1.getX(), boundary2.getX());
        double maxY = Math.max(boundary1.getY(), boundary2.getY());
        double maxZ = Math.max(boundary1.getZ(), boundary2.getZ());

        double locX = location.getX();
        double locY = location.getY();
        double locZ = location.getZ();

        return locX >= minX && locX <= maxX &&
                locY >= minY && locY <= maxY &&
                locZ >= minZ && locZ <= maxZ;
    }
}
