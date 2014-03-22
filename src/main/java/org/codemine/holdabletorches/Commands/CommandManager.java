package org.codemine.holdabletorches.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        if (command.getName().equalsIgnoreCase("flashlight")) {
            if (!(sender instanceof Player)) {
                MessageUtil.sendErrorMessage(sender, "You can only run this from in game");
                return true;
            }

            Player player = (Player) sender;

            if (player.getInventory().contains(Material.TORCH)) {
                System.out.println("Torch found");
                for (ItemStack itemStack : player.getInventory().all(Material.TORCH).values()) {
                    if (itemStack.getItemMeta().getDisplayName() != null && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase("HoldAble Torch")) {
                        MessageUtil.sendErrorMessage(player, "You already have one flash light in your inventory, please use that");
                        return true;
                    }
                }


            }
            player.getInventory().setItem(player.getInventory().firstEmpty(), Torches.getInstance().torch.clone());
            MessageUtil.sendMessage(player, "To switch on the Torch, hold it in your hand and right click");
            return true;
        }

        return false;
    }
}
