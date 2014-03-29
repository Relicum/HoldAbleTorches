package org.codemine.holdabletorches.Runnables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.codemine.holdabletorches.Torches;

/**
 * Name: OpenView.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class OpenView extends BukkitRunnable {

    public Player player;
    public CraftingInventory inventory;

    public OpenView(Player player, CraftingInventory inv) {
        this.player = player;
        this.inventory = inv;

    }


    @Override
    public void run() {
        inventory.clear();
        player.closeInventory();
        player.removeMetadata("MENUOPENID", Torches.getInstance());
        player.removeMetadata("MENUOPEN", Torches.getInstance());

    }
}
