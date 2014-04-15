package org.codemine.holdabletorches.Items;

/**
 * Name: ArmorItem.java Created: 03 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public enum ArmorItem implements Equipment {
    GOLD_BOOTS, GOLD_LEGGINGS, GOLD_CHESTPLATE, GOLD_HELMET, IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET, DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET, LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET;

    /**
     * Search the enum to see if a given item is on the list
     * <p>
     * Return true if it is false if it's not
     *
     * @param item the item {@link org.bukkit.Material} in it's String format
     * @return the boolean
     */
    public static boolean find(String item)
    {

        for(ArmorItem v : values())
        {
            if(v.name().equalsIgnoreCase(item))
            {
                return true;
            }
        }
        return false;
    }

}
