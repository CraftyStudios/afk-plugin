package me.Incbom.afk.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class afkrewards implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    public afkrewards(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("afkrewards")) {
            if (args.length == 1) {
                // Add possible subcommands to the completions list
            }
        }
        return completions;
    }







    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if( args.length == 0) {
                if (sender.hasPermission("afkrewards.help")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + " &c&lCommands:"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + " &c&l/afkrewards help &7- &fShows this help menu"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + " &c&l/afkrewards reload &7- &fReloads the config"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("no-permission")));
                }
 

            /* 
            if(args.length > 1 || args[1].equalsIgnoreCase("set-region")) {
                if (sender.hasPermission("afkrewards.setregion")) {
                    if (args.length == 2) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("afk-region")));
                    }else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + "&c&lUsage: /afkrewards region <region>"));
                    }
  
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("no-permission")));
                }

            }
            if(args.length == 1 || args[1].equalsIgnoreCase("reload")) { 
                if (sender.hasPermission("afkrewards.reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("reload-message")));
            
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("no-permission")));

                }
            }
            
        
        return false;
    
}*/return false;
    }return false; 
}
}

