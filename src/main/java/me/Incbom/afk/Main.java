package me.Incbom.afk;

import org.bukkit.plugin.java.JavaPlugin;

import me.Incbom.afk.Commands.afk;
import me.Incbom.afk.Events.RegionEnter;
import me.Incbom.afk.ItemActions.Afkwand;
import me.Incbom.afk.Items.ItemManager;
import me.Incbom.afk.utils.Logger;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Loading AFK Areas...");
      Logger.log(Logger.LogLevel.SUCCESS, "Loaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      saveDefaultConfig();

      getCommand("afk").setExecutor(new afk(this));
      getServer().getPluginManager().registerEvents(new Afkwand(this), this);
      getServer().getPluginManager().registerEvents(new RegionEnter(this), this);

      ItemManager.init();
    }   

    @Override
    public void onDisable() {
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloading AFK Areas...");
      Logger.log(Logger.LogLevel.SUCCESS, "Unloaded!");
      Logger.log(Logger.LogLevel.OUTLINE, "------------------------------------");
      this.saveConfig();
      }
    }