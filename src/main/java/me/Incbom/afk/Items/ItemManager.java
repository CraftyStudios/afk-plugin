package me.Incbom.afk.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
    public static ItemStack afkWand;

    public static void init() {
        createAfkWand();
    }

    public static void createAfkWand() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aSelect Wand");
        List<String> lore = new ArrayList<>();
        lore.add("§rSelect the afk boundries");
        meta.setLore(lore);
        item.setItemMeta(meta);
        afkWand = item;
    }
    
}
