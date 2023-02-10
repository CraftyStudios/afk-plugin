package me.Incbom.afk;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import de.netzkronehd.wgregionevents.events.RegionLeaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.Incbom.afk.Commands.afkregion;
import me.Incbom.afk.Commands.afkreload;
import me.Incbom.afk.Commands.afkrewards;
import me.Incbom.afk.utils.Logger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import org.bukkit.command.Command;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.ItemStack;


  

public final class Main extends JavaPlugin implements Listener {
  public WorldGuardPlugin worldguardplugin; 

  
            /* May clean up later into /listeners folder */


            private Map<Player, Double> afkSpentMoney = new HashMap<>();

 
        


            private final Map<Player, Integer> afkTime = new HashMap<>();
            private final AtomicInteger afkTimeCounter = new AtomicInteger();
           private Economy economy;
           BarColor barColor = BarColor.valueOf(this.getConfig().getString("boss-bar-color").toUpperCase());
           BossBar bossBar = Bukkit.createBossBar(getConfig().getString("boss-bar-message").replace("{afktime}", Integer.toString(afkTimeCounter.get())), barColor, BarStyle.SOLID);
           
            
            @EventHandler
            public void onRegionEnter(RegionEnterEvent e) {
                
                Player player = e.getPlayer();
                String regionId = e.getRegion().getId();
     

    // parse the color from the config string
    

  

                if (regionId.equals(this.getConfig().getString("afk-region"))) {
                    int afkTime = this.afkTime.getOrDefault(player, 0);
                    afkTimeCounter.set(afkTime);
                    Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                        public void run() {
                            afkTimeCounter.incrementAndGet();
                        }
                    }, 20L, 20L);
                }
               
                if (this.getConfig().getBoolean("boss-bar")) {
                    
                    
                    bossBar.addPlayer(player);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                      public void run() {
                        bossBar.setTitle(getConfig().getString("boss-bar-message").replace("{afktime}", Integer.toString(afkTimeCounter.get())));
                       
                        bossBar.setProgress(afkTimeCounter.get() / (double) getConfig().getInt("afk-time-limit"));
                      }
                    }, 20L, 20L);  
                  }
                  if (this.getConfig().getBoolean("afk-spent-money") == true) {
                    double moneySpent = economy.getBalance(player);
                    afkSpentMoney.put(player, moneySpent);
                  

            }
          }
            
            
            
          @EventHandler
 public void onRegionLeave(RegionLeaveEvent e) {
     Player player = e.getPlayer();
     String regionId = e.getRegion().getId();
     if (regionId.equals(this.getConfig().getString("afk-region"))) {
         // Get the current AFK time for the player
         int afkTime = this.afkTime.getOrDefault(player, 0);
         // Check the rewards list to see if the AFK time matches any of the times in the list
         List < Map < ? , ? >> rewards = this.getConfig().getMapList("afk-rewards");

         for (Map < ? , ? > reward : rewards) {
             int time = (int) reward.get("time");
             if (afkTime >= time) {
                 String rewardString = (String) reward.get("reward");
                 String[] items = rewardString.split(" ");
                 Material material = Material.valueOf(items[0]);
                 int amount = Integer.parseInt(items[1].substring(1));
                 ItemStack itemStack = new ItemStack(material, amount);
                 player.getInventory().addItem(itemStack);
                 // Check if there is a command associated with the reward
                 if (reward.containsKey("command")) {
                     String command = (String) reward.get("command");
                     command = command.replace("{player}", player.getName());
                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                 }
                 // Check if there is a money reward associated with the reward
                 if (reward.containsKey("money-reward")) {
                     double moneyReward = (double) reward.get("money-reward");
                     economy.depositPlayer(player, moneyReward);
                 }
                 break;
             }
         }
         // Remove the task that increments the AFK time
         Bukkit.getScheduler().cancelTasks(this);
         bossBar.removePlayer(player);
         // Reset the AFK time for the player
         this.afkTime.put(player, 0);
     }
 }
      
            
      
          

    @Override
    public void onLoad() {
    }


    @Override
    public void onEnable() {
      /*if (Bukkit.getPluginManager().getPlugin("WorldGuardRegionEvents") != null) {
        Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
        Logger.log(Logger.LogLevel.SUCCESS, "WorldGuardRegionEvents found!");
        Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");

        Bukkit.getPluginManager().registerEvents(this, this);
    } else {
        Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
        Logger.log(Logger.LogLevel.ERROR, "WorldGuardRegionEvents not found!");
        Logger.log(Logger.LogLevel.ERROR, "Please install WorldGuardRegionEvents to use this plugin!");
        Logger.log(Logger.LogLevel.ERROR, "https://www.spigotmc.org/resources/worldguard-region-events-updated.61490/");
        Logger.log(Logger.LogLevel.ERROR, "Disabling plugin...");
        Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");

        Bukkit.getPluginManager().disablePlugin(this);
    }*/


    
      saveDefaultConfig();
      Bukkit.getPluginManager().registerEvents(this, this);
      worldguardplugin = getWorldGuard();
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Loading AFK Rewards...");
      Logger.log(Logger.LogLevel.SUCCESS, "Loaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Command command = getCommand("afkrewards");
      Command command2 = getCommand("afkregion");
      Command command3 = getCommand("afkreload");
      afkrewards afkrewards = new afkrewards(this);
      afkregion afkregion = new afkregion(this);
      afkreload afkreload = new afkreload(this);
      this.getCommand("afkreload").setExecutor(afkreload);
      this.getCommand("afkreload").setTabCompleter(afkreload);
      this.getCommand("afkrewards").setExecutor(afkrewards);
      this.getCommand("afkregion").setExecutor(afkregion);
      this.getCommand("afkrewards").setTabCompleter(afkrewards);
      this.getCommand("afkregion").setTabCompleter(afkregion);





    }
    

    @Override
    public void onDisable() {
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloading AFK Rewards...");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      this.saveConfig();
      }
      public WorldGuardPlugin getWorldGuard() {
        org.bukkit.plugin.Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
      }
    }
  
