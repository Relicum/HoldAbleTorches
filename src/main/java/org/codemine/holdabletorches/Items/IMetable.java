package org.codemine.holdabletorches.Items;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Name: IMetable.java Created: 03 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface IMetable {

    /**
     * Gets ItemMeta as specified by the Material and MetaTypes args
     *
     * @param material the {@link org.bukkit.Material} to be used to create the ItemMeta
     * @param types the {@link org.codemine.holdabletorches.Items.MetaTypes} if unsure use type of ItemMeta
     * @return the {@link org.bukkit.inventory.meta.ItemMeta} created by the method, used to apply to an ItemStack
     */
    ItemMeta getItemMeta(Material material, MetaTypes types);

    /**
     * Gets material that the ItemMeta is for
     *
     * @return the {@link org.bukkit.Material}
     */
    Material getMaterial();
}
