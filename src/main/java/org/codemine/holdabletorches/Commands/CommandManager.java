package org.codemine.holdabletorches.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codemine.holdabletorches.Torches;
import org.codemine.holdabletorches.Utils.MessageUtil;

/**
 * Name: CommandManager.java Created: 22 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class CommandManager implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("flashlight")){
            if(!(sender instanceof Player)){
                MessageUtil.sendErrorMessage(sender,"You can only run this from in game");
                return true;
            }

            Player player =(Player)sender;
            player.getInventory().setItem(player.getInventory().firstEmpty(), Torches.getInstance().torch.clone());
            MessageUtil.sendMessage(player,"To switch on the Torch, hold it in your hand and right click");
            return true;
        }

        return false;
    }
}
