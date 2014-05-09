package org.codemine.holdabletorches.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.codemine.holdabletorches.Torches;

/**
 * Name: OpenView.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class OpenView implements Runnable {

    private final Player player;
    private final CraftingInventory inventory;

    public OpenView(final Player player, final CraftingInventory inv)
    {

        this.player = player;
        this.inventory = inv;

    }

    @Override
    public void run()
    {

        inventory.clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Torches.getInstance(), new Runnable() {

            @Override
            public void run()
            {

                player.getOpenInventory().close();
            }
        }, 1l);

        player.removeMetadata("MENUOPENID", Torches.getInstance());
        player.removeMetadata("MENUOPEN", Torches.getInstance());

    }
}
