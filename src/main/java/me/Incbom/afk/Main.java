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
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;


  

public final class Main extends JavaPlugin implements Listener {
  public WorldGuardPlugin worldguardplugin;
  private final Map<Player, Integer> afkTime = new HashMap<>();
  private final AtomicInteger afkTimeCounter = new AtomicInteger();
  private Economy economy;
 
  
  @EventHandler
  public void onRegionEnter(RegionEnterEvent e) {
      Player player = e.getPlayer();
      String regionId = e.getRegion().getId();
  
      if (regionId.equals(this.getConfig().getString("afk-region"))) {
          final AtomicInteger afkTime = new AtomicInteger(this.afkTime.getOrDefault(player, 0));
          Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
              public void run() {
                  // Increment afkTime
                  afkTime.incrementAndGet();
                  // Update the afkTime value for the player
                  afkTimeCounter.set(afkTime.get());
                                  if (getConfig().getBoolean("Debug")) {
                    getLogger().info(player.getName() + " has been AFK for " + afkTime.get() + " seconds");
                }
                  // Update the boss bar title with the new afk time value
                  
              }
          }, 0L, 20L);
      }
  

  
      int rewardTime = this.getConfig().getInt("time");
      String rewards = this.getConfig().getString("item-rewards");
      int money = this.getConfig().getInt("money-rewards");
      String commands = this.getConfig().getString("console-commands");
      int afkTime = this.afkTime.getOrDefault(player, 0);
      String regionIdEquals = this.getConfig().getString("afk-region");
  
      Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
          public void run() {
              if (regionId.equals(regionIdEquals)) {
                  // Get the current AFK time for the player
  
                  // Checking the afk time if it's greater than the time you set in the config
                  if (afkTime >= rewardTime) {
                      // Give itemstacks
                      
                      // Give item rewards
                      for (String reward : rewards.split(",")) {
                          String[] rewardSplit = reward.split(":");
                          String material = rewardSplit[0];
                          int amount = Integer.parseInt(rewardSplit[1]);
                          player.getInventory().addItem(new ItemStack(Material.getMaterial(material), amount));
                      }
                      
                      
  
                      // Money Rewards
                      economy.depositPlayer(player, money);
  
                      // Execute console commands
                      ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                      for (String command : commands.split(",")) {
                          Bukkit.dispatchCommand(console, command.replace("{player}", player.getName()));
                      }
                  }
                  if (getConfig().getBoolean("Debug")) {
                    getLogger().info(player.getName() + " has received " + money + " " + rewards + " " + commands + " for being AFK for " + afkTime + " seconds");
                    getLogger().info(player.getName() + " has console command executed " + commands + " for them during the AFK Reward proccess");
              }
          }
        }
      }, 0L, 20L * rewardTime);
  

}
  
  @EventHandler
  public void onRegionLeave(RegionLeaveEvent e) {
      Player player = e.getPlayer();
  
      // Remove the task that increments the AFK time
      Bukkit.getScheduler().cancelTasks(this);
  
      // Reset the AFK time for the player
      this.afkTime.put(player, 0);
  }
            
    @Override
    public void onLoad() {
    }
    @Override
    public void onEnable() {
      if(getServer().getPluginManager().getPlugin("WGRegionEvents") == null) {
        getServer().getPluginManager().disablePlugin(this);
        Logger.log(Logger.LogLevel.ERROR, "WGRegionEvents not found! Disabling plugin...");
      }
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