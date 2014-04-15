package org.codemine.holdabletorches.Utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * Name: AbstractEnchant.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class AbstractEnchant extends Enchantment {

    public AbstractEnchant(int id)
    {

        super(id);

    }

    @Override
    public String getName()
    {

        return null;
    }

    @Override
    public int getMaxLevel()
    {

        return 1;
    }

    @Override
    public int getStartLevel()
    {

        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget()
    {

        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment)
    {

        return true;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack)
    {

        return true;
    }

    public String getLore(int lvl)
    {

        return null;
    }

}
