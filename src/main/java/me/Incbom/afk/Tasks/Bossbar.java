package me.Incbom.afk.Tasks;

import org.bukkit.entity.Player;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import me.Incbom.afk.Main;

public class Bossbar {
    private BossBar bossBar;
    private BarColor bossBarColor;
    private String bossBarMessage;
    

    private Main plugin;
    public Bossbar(Main plugin) {
        this.plugin = plugin;
        

        // Get the boss bar color from the config
        String bossBarColorName = plugin.getConfig().getString("boss-bar-color");

        // Set the boss bar color based on the value in the config
        try {
            this.bossBarColor = BarColor.valueOf(bossBarColorName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid color value
            this.bossBarColor = BarColor.BLUE; // Default color
        }

        // Get the boss bar message from the config
        this.bossBarMessage = plugin.getConfig().getString("boss-bar-message", "You have been afk for {afktime} seconds!");

        // Create the boss bar with the determined color and style
        this.bossBar = plugin.getServer().createBossBar("", bossBarColor, BarStyle.SOLID);
    }

    public void startBossBar(Player player, int durationSeconds) {
        bossBar.addPlayer(player);
        bossBar.setProgress(1.0);
    
        int ticks = durationSeconds * 20;
        int interval = durationSeconds / ticks;
    
        int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int remainingTicks = ticks;
    
            @Override
            public void run() {
                bossBar.setTitle(bossBarMessage.replace("{afktime}", String.valueOf(remainingTicks / 20)));
                bossBar.setProgress((float) remainingTicks / ticks); // Update progress

                if (remainingTicks <= 0) {
                    bossBar.setProgress(1.0f); // Reset progress to full (1.0)
                    remainingTicks = ticks; // Reset remaining ticks
                }

                remainingTicks--;
            }
        }, 0L, interval * 20L); // Convert seconds to ticks
    }
    public void stopBossBar(Player player) {
        bossBar.removePlayer(player);
    }
    
    
}
