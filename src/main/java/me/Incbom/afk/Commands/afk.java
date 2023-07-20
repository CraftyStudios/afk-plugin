package me.Incbom.afk.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.Incbom.afk.Main;
import me.Incbom.afk.Items.ItemManager;

public class afk implements CommandExecutor, TabCompleter {

    private Main plugin;
    public afk(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("afk")) {
            if (args.length == 1) {
                // Add possible subcommands to the completions list
                completions.add("reload");
                completions.add("wand");
            }
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String noPermission = plugin.getConfig().getString("no-permission");
        String prefix = plugin.getConfig().getString("prefix").replaceAll("&", "§");
        String reloadMessage = plugin.getConfig().getString("reload");

        sender.sendMessage("§cAFK §7- §fVersion: 1.0.1");
        sender.sendMessage("§cAFK §7- §cCommands:\n§c/afk wand - Get the wand\n§c/afk reload - Reload the config");
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("afk.reload")) {
                sender.sendMessage(prefix + reloadMessage.replaceAll("&", "§"));
                plugin.reloadConfig();
                return true;
            } else {
                sender.sendMessage(noPermission);
                return false;
            }
        } else if (args[0].equalsIgnoreCase("wand")) {
            if (sender.hasPermission("afk.wand")) {
                Player player = (Player) sender;
                sender.sendMessage(prefix + "§a§lWand given!");
                player.getInventory().addItem(ItemManager.afkWand);
                return true;
            } else {
                sender.sendMessage(noPermission);
                return false;
            }
        }
        
        return false;
    }

}
