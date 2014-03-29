package org.codemine.holdabletorches.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Name: PlayerDeath.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayerDeath implements Listener {

    public List<ItemStack> torch;

    public PlayerDeath(List<ItemStack> torch) {
        this.torch = new ArrayList<>(torch.size());
        this.torch.addAll(torch);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        List<ItemStack> drops = e.getDrops();
        ListIterator<ItemStack> litr = drops.listIterator();


        while (litr.hasNext()) {

            ItemStack stack = litr.next();


            if (torch.contains(stack)) {

                litr.remove();
                System.out.println("Torch Removed after death");
            }
        }
    }

}

