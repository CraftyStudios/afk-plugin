package me.Incbom.afk.ItemActions;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Incbom.afk.Main;
import me.Incbom.afk.Items.ItemManager;

public class Afkwand implements Listener {
    private Main plugin;
    public Afkwand(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.afkWand.getItemMeta())) {
                    Block clickedBlockR = event.getClickedBlock();
                    
                    if (clickedBlockR != null) {
                        event.getPlayer().sendMessage("§a§lSelection 2 set!");
                        plugin.getConfig().set("coords-2-x", clickedBlockR.getX());
                        plugin.getConfig().set("coords-2-y", clickedBlockR.getY());
                        plugin.getConfig().set("coords-2-z", clickedBlockR.getZ());
                    }

                    else {
                        event.getPlayer().sendMessage("§c§lYou must click a block!");
                    }
                }
            }
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.afkWand.getItemMeta())) {
                    event.setCancelled(true);
                    Block clickedBlockL = event.getClickedBlock();

                    if (clickedBlockL != null) {
                        event.getPlayer().sendMessage("§a§lSelection 1 set!");
                        plugin.getConfig().set("coords-1-x", clickedBlockL.getX());
                        plugin.getConfig().set("coords-1-y", clickedBlockL.getY());
                        plugin.getConfig().set("coords-1-z", clickedBlockL.getZ());
                    }

                    else {
                        event.getPlayer().sendMessage("§c§lYou must click a block!");
                    }
                }
            }
        }

    }
    
}
