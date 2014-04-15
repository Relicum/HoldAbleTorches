package org.codemine.holdabletorches.Items;

import java.util.List;

/**
 * Name: Ilore.java Created: 03 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface Ilore extends IMetable {

    /**
     * Gets lore attached to the ItemStack
     *
     * @return the lore
     */
    public List<String> getLore();

    /**
     * Sets lore to be attached to the Item Stack
     *
     * @param lore in List<String> format
     */
    public void setLore(List<String> lore);

    /**
     * Gets safe colored lore. And converts it to use the default
     * Minecraft Chat Color character
     *
     * @return the safe colored lore in {@link java.util.List<java.lang.String>} format
     */
    public List<String> getColoredLore();

    /**
     * Sets safe colored lore. To use the Character & as the Color Code Character
     *
     * @param lore the lore to parse in {@link java.util.List<java.lang.String>} format
     */
    public void setSafeColoredLore(List<String> lore);
}
