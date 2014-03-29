package org.codemine.holdabletorches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name: HoldAbleTorch.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class HoldAbleTorch implements ConfigurationSerializable {

    private ItemStack torch;
    private ItemMeta torchMeta;
    private boolean enabled;
    private boolean usePerm;
    private boolean canDrop;
    private boolean unlimited;
    private int maxLength;
    private int coolDown;
    private String itemName;
    private Material material;


    public HoldAbleTorch(Material mat) {
        this.torchMeta = Bukkit.getItemFactory().getItemMeta(mat);
        this.torch = new ItemStack(mat);
        this.material = mat;
    }

    public void addDescription(String des) {
        this.torchMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', des));
    }

    public void addLore(List<String> lo) {
        this.torchMeta.setLore(lo);
    }

    public void addEnchant(Enchantment en, int level, boolean unSafe) {
        this.torchMeta.addEnchant(en, level, unSafe);
    }

    public void applyMeta() {
        this.torch.setItemMeta(this.torchMeta);
    }

    public ItemStack getTorchStack() {

        return applyMetas(getTorch());

    }

    private ItemStack getTorch() {
        return new ItemStack(this.material);
    }

    private ItemStack applyMetas(ItemStack it) {
        it.setItemMeta(this.torchMeta);
        return it;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isUnlimited() {
        return unlimited;
    }

    public void setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    public boolean isCanDrop() {
        return canDrop;
    }

    public void setCanDrop(boolean canDrop) {
        this.canDrop = canDrop;
    }

    public boolean isUsePerm() {
        return usePerm;
    }

    public void setUsePerm(boolean usePerm) {
        this.usePerm = usePerm;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("stack", this.torch);

        return null;
    }
}
