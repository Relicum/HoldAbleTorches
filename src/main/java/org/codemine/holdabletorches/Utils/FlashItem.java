package org.codemine.holdabletorches.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Name: FlashItem.java Created: 15 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class FlashItem {

    private static final String displayName = ChatColor.translateAlternateColorCodes('&', I18N.STRING("flashlight.displayName"));
    private static final List<String> lore =
      Arrays.asList(altCol(I18N.STRING("flashlight.lore1")), altCol(I18N.STRING("flashlight.lore2")), altCol(I18N.STRING("flashlight.lore3")));
    private static final Enchantment enchantment = Enchantment.getByName("UNDROPABLE");
    private static final ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
    private static final ItemStack torch = new ItemStack(Material.TORCH, 1);

    static
    {
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.addEnchant(enchantment, 1, true);
        torch.setItemMeta(meta);
    }

    public static ItemStack getFlashLight()
    {

        return torch.clone();
    }

   /* private static ItemStack newTorch(){
        ItemStack stack = new ItemStack(Material.TORCH);
        stack.setItemMeta(makeMeta());
        return stack;
    }

    private static ItemMeta makeMeta(){
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.addEnchant(enchantment,1,true);
        return itemMeta.clone();
    }*/

    private static String altCol(String s)
    {

        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
