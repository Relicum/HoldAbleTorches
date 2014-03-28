package org.codemine.holdabletorches.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

/**
 * Name: IMeta.java Created: 24 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface IMeta<T> {


    public ItemStack stack=null;
    public ItemMeta meta=null;
    public Material material = null;
    public String displayName = null;
    public List<EnchantmentWrapper> enchantmentWrappers = null;
    public List<String> loreList = null;

    public static char EMPTY='\n';

    public String heading=null;
    public char nl='\n';
    public String[] rows=new String[3];
    public Enchantment enchant=null;
    public int enchantLevel=0;
    public boolean allowUnsafe = true;

    //HEADING
    //NL
    //SP ROW1  XIX
    //SP ROW2  XRX
    //SP ROW3  XSX
    //NL
    //SUB HEADING
    //KEY1
    //KEY2
    //KEY3
    //EMPTYKEY
    //NL
    //INFO1
    //INFO2

    public boolean addEnchant(Enchantment enchantment, int level);

    public boolean addUnSafeEnchant(Enchantment enchantment, int level,boolean forceEnchant);

    public void setDisplayName(String name);

    public void addLoreLine(String line);

    public void addLore(List<String> loreList);

    public ItemMeta getItemMeta(Material mat);

    public ItemStack getItemStack(Material mat,int amount);

    public ItemStack getItemStack(MaterialData data,int amount);

    public ItemStack setItemMeta(ItemMeta itemMeta);
    public <T> T setType(T t);

    public T getType();

    public Map<String,Object> serialize();
}
