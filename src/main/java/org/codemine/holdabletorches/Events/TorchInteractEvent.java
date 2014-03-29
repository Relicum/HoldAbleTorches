package org.codemine.holdabletorches.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Name: TorchInteractEvent.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class TorchInteractEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private Player player;
    private ItemStack torch;
    private ItemMeta meta;
    private String displayName;

    public TorchInteractEvent(Player player, ItemStack torch) {
        this.player = player;
        this.torch = torch;
        Item item = (Item) torch;

        if (torch.hasItemMeta()) {
            this.meta = torch.getItemMeta();
        }
        if (this.meta.hasDisplayName()) {
            this.displayName = ChatColor.stripColor(this.meta.getDisplayName());
        }
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getTorch() {
        return torch;
    }

    public Player getPlayer() {
        return player;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
