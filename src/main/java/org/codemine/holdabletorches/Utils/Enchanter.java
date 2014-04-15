package org.codemine.holdabletorches.Utils;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

/**
 * Name: Enchanter.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Enchanter {

    public static UnDropAbleEnchantment unDropAbleEnchantment = new UnDropAbleEnchantment(120);

    public Enchanter()
    {

    }

    public void registerAll()
    {

        try
        {

            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if(Enchantment.getByName("UNDROPABLE") != null)
        {
            MessageUtil.logInfoFormatted("Enchant is registered");
            return;
        }

        Enchantment.registerEnchantment(unDropAbleEnchantment);
    }

}
