package org.codemine.holdabletorches.Commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codemine.holdabletorches.Torches;
import org.codemine.holdabletorches.Utils.MessageUtil;

import java.util.List;

/**
 * Name: FlashLight.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@CmdInfo(
        description = "Get a new HoldAble Torch that you can turn on and off",
        usage = "/flashlight",
        permission = "holdabletorches.player.flashlight",
        playerOnly = true,
        minArgs = 0,
        maxArgs = 0,
        aliases = {"flight"}
)
public class FlashLight extends SimpleCommand {

    public FlashLight(CommandHandler commandHandler, String name) {
        super(commandHandler, name);

    }

    /**
     * {@inheritDoc}
     *
     * @param sender
     * @param command
     * @param args
     */
    @Override
    public boolean onCommand(CommandSender sender, String command, String[] args) {

        Player player = (Player) sender;

        if (player.getInventory().contains(Material.TORCH)) {

            for (ItemStack itemStack : player.getInventory().all(Material.TORCH).values()) {
                if (itemStack.hasItemMeta()) {
                    if (itemStack.getItemMeta().hasDisplayName()) {
                        if (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase("HoldAble Torch")) {
                            MessageUtil.sendErrorMessage(player, "You already have one flash light in your inventory, please use that");
                            return true;
                        }
                    }
                }

            }
        }

        player.getInventory().setItem(player.getInventory().firstEmpty(), Torches.getInstance().torch.clone());
        MessageUtil.sendMessage(player, "To switch on the Torch, hold it in your hand and right click");
        return true;

    }

    /**
     * {@inheritDoc}
     *
     * @param sender
     * @param s
     * @param args
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String s, String[] args) {
        return ImmutableList.of();
    }
}
