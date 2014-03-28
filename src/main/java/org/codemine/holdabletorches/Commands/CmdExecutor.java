package org.codemine.holdabletorches.Commands;

import org.bukkit.command.CommandSender;

/**
 * Name: CmdExecutor.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface CmdExecutor {

    /**
     * Runs the command
     *
     * @param sender the sender
     * @param label  the label
     * @param args   the args
     * @return the boolean
     */
    public abstract boolean onCommand(CommandSender sender, String label, String[] args);
}
