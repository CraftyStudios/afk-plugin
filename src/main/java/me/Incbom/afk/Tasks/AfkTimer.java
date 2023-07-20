package me.Incbom.afk.Tasks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Incbom.afk.Main;
import net.milkbowl.vault.economy.Economy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfkTimer {
    private static Main plugin;
    public AfkTimer(Main plugin) {
        AfkTimer.plugin = plugin;
    }
    private Economy economy;

    public void giveItems(Player player) {
        List<String> rewards = plugin.getConfig().getStringList("item-rewards");
        for (String reward : rewards) {
            String[] parts = reward.split(" x");
            Material material = Material.matchMaterial(parts[0]);
            int quantity = Integer.parseInt(parts[1]);
            ItemStack itemStack = new ItemStack(material, quantity);
            player.getInventory().addItem(itemStack);
        }
        
    }
    private boolean isVaultEnabled() {
        Plugin vaultPlugin = plugin.getServer().getPluginManager().getPlugin("Vault");
        return vaultPlugin != null && vaultPlugin.isEnabled();
    }
    public void giveMoney(Player player) {
        List<Integer> money = plugin.getConfig().getIntegerList("money-rewards");
        for (Integer reward : money) {
            economy.depositPlayer(player, reward);
        }
    }
    public void executeCommands(Player player) {
        List<String> commands = plugin.getConfig().getStringList("console-commands");
        for (String reward : commands) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), reward.replace("{player}", player.getName()));
        }
    }
    private static Map<Player, BukkitRunnable> timers = new HashMap<>();

    public void startTimer(Player player, int intervalSeconds) {
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                giveItems(player);
                if (isVaultEnabled()) {
                    giveMoney(player);
                }
                executeCommands(player);
                // Perform actions to give rewards to the player
                player.sendMessage("You have received rewards!");
            }
        };
        timer.runTaskTimer(plugin, intervalSeconds* 20L, intervalSeconds * 20L); // Convert seconds to ticks

        timers.put(player, timer);
    }

    public static void resetTimer(Player player) {
        BukkitRunnable timer = timers.get(player);
        if (timer != null) {
            timer.cancel();
            timers.remove(player);
        }
    }
}
