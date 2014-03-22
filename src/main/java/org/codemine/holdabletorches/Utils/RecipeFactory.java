package org.codemine.holdabletorches.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Name: RecipeFactory.java Created: 22 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class RecipeFactory {


    public static ShapedRecipe getShapedRecipe(Material material){

        ItemMeta Imeta = Bukkit.getItemFactory().getItemMeta(Material.REDSTONE_TORCH_ON);
        ItemStack iron = new ItemStack(Material.REDSTONE_TORCH_ON,1);
        Imeta.setDisplayName(ChatColor.GOLD + "Iron Sight");
        Imeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Iron Sight HoldAble Torch", ChatColor.GREEN + "Hold the torch and right click to power on", "", ChatColor.GOLD + "Gives you 10 seconds of light", ChatColor.GOLD + "But only while you hold it"));
        Imeta.addEnchant(Enchantment.DURABILITY, 10, true);
        iron.setItemMeta(Imeta);
        ShapedRecipe sh = new ShapedRecipe(iron);
        sh.shape("OIO","ORO","OTO").setIngredient('O',Material.AIR).setIngredient('I',Material.IRON_INGOT).setIngredient('R',Material.REDSTONE).setIngredient('T',Material.STICK);

        return null;

    }
}
