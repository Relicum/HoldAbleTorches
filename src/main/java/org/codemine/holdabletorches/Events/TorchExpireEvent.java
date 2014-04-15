package org.codemine.holdabletorches.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Name: TorchExpireEvent.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class TorchExpireEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers()
    {

        return handlers;
    }

    public static HandlerList getHandlerList()
    {

        return handlers;
    }

    @Override
    public boolean isCancelled()
    {

        return cancelled;
    }

    @Override
    public void setCancelled(boolean b)
    {

        this.cancelled = b;
    }

}
