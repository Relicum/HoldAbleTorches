package org.codemine.holdabletorches.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.codemine.holdabletorches.Torches;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: DropItems.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class DropItems implements Listener {

    public List<String> torch;

    public DropItems(List<String> torch)
    {

        this.torch = new ArrayList<>(torch.size());
        this.torch.addAll(torch);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(final PlayerDropItemEvent e)
    {

        ItemStack drops = e.getItemDrop().getItemStack();
        if(!drops.getType().equals(Material.TORCH) && !drops.getType().equals(Material.REDSTONE_TORCH_ON)) return;
        if(!drops.hasItemMeta()) return;

        if(!drops.getItemMeta().hasEnchant(Enchantment.getByName("UNDROPABLE"))) return;

        //   if (torch.contains(ChatColor.stripColor(drops.getItemMeta().getDisplayName()))) {
        e.setCancelled(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Torches.getInstance(), new Runnable() {

            @Override
            public void run()
            {

                e.getPlayer().updateInventory();
            }
        }, 1l);
        System.out.println("Can not drop them");
        // }

    }
}
