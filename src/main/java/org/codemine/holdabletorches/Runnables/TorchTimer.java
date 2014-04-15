package org.codemine.holdabletorches.Runnables;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.codemine.holdabletorches.Torches;
import org.codemine.holdabletorches.Utils.MessageUtil;

/**
 * Name: TorchTimer.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class TorchTimer implements Runnable {

    private long start;
    private long end;
    private Player player;
    private ItemStack itemStack;
    private int currentSlot;

    public TorchTimer(Player player, ItemStack itemStack)
    {

        start = System.currentTimeMillis();
        this.player = player;
        this.itemStack = itemStack;
        this.currentSlot = player.getInventory().getHeldItemSlot();

    }

    @Override
    public void run()
    {

        end = System.currentTimeMillis();
        this.player.getInventory().clear(this.currentSlot);
        this.player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);
        player.removeMetadata("HATREDMETA", Torches.getInstance());
        player.removeMetadata("HATREDTASKID", Torches.getInstance());
        MessageUtil.sendMessage(player, "The torch " + itemStack.getItemMeta().getDisplayName() + " has run out of power");

        this.itemStack = null;
    }
}
