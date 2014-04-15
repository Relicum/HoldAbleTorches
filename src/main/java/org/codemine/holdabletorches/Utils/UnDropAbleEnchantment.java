package org.codemine.holdabletorches.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * Name: UnDropAbleEnchantment.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class UnDropAbleEnchantment extends AbstractEnchant implements IEnchantLore {

    public UnDropAbleEnchantment(int id)
    {

        super(id);
    }

    @Override
    public String getName()
    {

        return "UNDROPABLE";
    }

    @Override
    public int getMaxLevel()
    {

        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget()
    {

        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack)
    {

        if(itemStack.getType().equals(Material.TORCH) || itemStack.getType().equals(Material.REDSTONE_TORCH_ON))
        {
            return true;
        }
        return false;
    }

    @Override
    public String getLore(int lvl)
    {

        return getName();
    }
}
