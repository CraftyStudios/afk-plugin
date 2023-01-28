package me.Incbom.afk;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.event.EventHandler;
import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import de.netzkronehd.wgregionevents.events.RegionEnteredEvent;
import de.netzkronehd.wgregionevents.events.RegionLeaveEvent;
import de.netzkronehd.wgregionevents.events.RegionLeftEvent;
import org.bukkit.event.Listener;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import me.Incbom.afk.utils.Logger;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public final class Main extends JavaPlugin implements Listener {
  public WorldGuardPlugin worldguardplugin; 
  
            /* May clean up later into /listeners folder */

 

            private final Map<Player, Integer> afkTime = new HashMap<>();
            private final AtomicInteger afkTimeCounter = new AtomicInteger();
           private Economy economy;
            
            @EventHandler
            public void onRegionEnter(RegionEnterEvent e) {
                Player player = e.getPlayer();
                String regionId = e.getRegion().getId();
                if (regionId.equals(this.getConfig().getString("afk-region"))) {
                    int afkTime = this.afkTime.getOrDefault(player, 0);
                    afkTimeCounter.set(afkTime);
                    Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                        public void run() {
                            afkTimeCounter.incrementAndGet();
                        }
                    }, 20L, 20L);
                }
            }
            
            @EventHandler
            public void onRegionLeave(RegionLeaveEvent e) {
                Player player = e.getPlayer();
                String regionId = e.getRegion().getId();
                if (regionId.equals(this.getConfig().getString("afk-region"))) {
                    // Get the current AFK time for the player
                    int afkTime = this.afkTime.getOrDefault(player, 0);
                    //Checking the afk time if it's greater than the time you set in the config
                    if (afkTime > this.getConfig().getInt("afk-time-limit")){
                        //Retrieving the rewards from the config
                        List<String> rewards = this.getConfig().getStringList("rewards");
                        for (String item : rewards) {
                            Material material = Material.valueOf(item);
                            ItemStack itemStack = new ItemStack(material, 1);
                            player.getInventory().addItem(itemStack);
                        }
                        //Retrieving the money reward from the config
                        double moneyReward = this.getConfig().getDouble("money-reward");
                       economy.depositPlayer(player, moneyReward);
                    }
                    // Remove the task that increments the AFK time
                    Bukkit.getScheduler().cancelTasks(this);
                    // Reset the AFK time for the player
                    this.afkTime.put(player, 0);
                }
            }
            
            
            
          







            /* Add + 1 every second in the afk zone */
      
      
          /*   @EventHandler
            public void onRegionEnter(RegionEnterEvent e) {
              for (e.getRegion().getId().equals(this.getConfig().getString("afk-region")); afktime < 60; afktime++) {
                afktime = afktime + 1;
              }

                if (afktime == (this.getConfig().getInt("afk-time"))) {
                  e.getPlayer().sendMessage("You have been afk for 1 minute.");
                  afktime = 0;
                }else {
                afktime = afktime;
                }
            }
          }
            @EventHandler
            public void onRegionEnter(RegionEnterEvent e) {
              if (e.getRegion().getId().equals(this.getConfig().getString("afk-region"))) {
                e.getPlayer().sendMessage("You are entering§e "+e.getRegion().getId()+"§7.");
              }else {
              }
            }
            @EventHandler
            public void onRegionEntered(RegionEnteredEvent e) {
              if (e.getRegion().getId().equals(this.getConfig().getString("afk-region"))) {
                afktime = afktime + 1; 
                if (afktime == 60) {
                  e.getPlayer().sendMessage("You have been afk for 1 minute.");
                  afktime = 0;
                }else {           
                  afktime = afktime;
                }
              }
            }
            @EventHandler
            public void onRegionLeave(RegionLeaveEvent e) {
              if (e.getRegion().getId().equals(this.getConfig().getString("afk-region"))) {
                afktime = 0;
              }else {
              }
            }
            @EventHandler
            public void onRegionLeft(RegionLeftEvent e) {
              if (e.getRegion().getId().equals(this.getConfig().getString("afk-region"))) {
                afktime = 0;
              }else {
              }
            }
      
       */
            
      
          

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this);
      worldguardplugin = getWorldGuard();
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Loading AFK Rewards...");
      Logger.log(Logger.LogLevel.SUCCESS, "Loaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");


    }
    

    @Override
    public void onDisable() {
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloading AFK Rewards...");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      }
      public WorldGuardPlugin getWorldGuard() {
        org.bukkit.plugin.Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
      }
      
  }
