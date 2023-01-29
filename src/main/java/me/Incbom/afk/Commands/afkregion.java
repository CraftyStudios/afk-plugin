package me.Incbom.afk.Commands;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class afkregion implements CommandExecutor, TabCompleter{
    private final JavaPlugin plugin;

    public afkregion(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("afkregion")) {
            if (args.length == 1) {
                // Add possible subcommands to the completions list
                completions.add("set");
            }
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0 && args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission("afkrewards.setregion")) {
                if (args.length == 2) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + "&a&lRegion set to: " + args[1]));
                    plugin.getConfig().set("afk-region", args[1]);
                    plugin.saveConfig();
                    return true;
                }else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + "&c&lUsage: /afkrewards region <region>"));
                    return false;
                }
            }else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("no-permission")));
                return false;
            }
        }
        return false;
    }
}
