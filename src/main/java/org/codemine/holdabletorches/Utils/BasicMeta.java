package org.codemine.holdabletorches.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

/**
 * Name: BasicMeta.java Created: 24 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class BasicMeta<T> implements IMeta {


    public BasicMeta(T t) {


    }

    @Override
    public boolean addEnchant(Enchantment enchantment, int level) {
        return false;
    }

    @Override
    public boolean addUnSafeEnchant(Enchantment enchantment, int level, boolean forceEnchant) {
        return false;
    }

    @Override
    public void setDisplayName(String name) {

    }

    @Override
    public void addLoreLine(String line) {

    }

    @Override
    public ItemMeta getItemMeta(Material mat) {
        return null;
    }

    @Override
    public ItemStack getItemStack(Material mat, int amount) {
        return null;
    }

    @Override
    public ItemStack getItemStack(MaterialData data, int amount) {
        return null;
    }

    @Override
    public ItemStack setItemMeta(ItemMeta itemMeta) {
        return null;
    }

    @Override
    public Object getType() {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    @Override
    public T setType(Object o) {
        return null;
    }

    @Override
    public void addLore(List loreList) {

    }
}
