package org.codemine.holdabletorches.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.codemine.holdabletorches.Torches;
import org.codemine.holdabletorches.Utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: SightViewer.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@CmdInfo(
        description = "Allows players to see the recipes for custom craftable torches",
        usage = "/torchviewer [recipe]",
        permission = "holdabletorches.player.view",
        playerOnly = true,
        minArgs = 1,
        maxArgs = 1,
        aliases = {"tv"}
)
public class SightViewer extends SimpleCommand {

    public static List<String> RECIPES;


    public SightViewer(CommandHandler commandHandler, String name, List<String> recipe) {
        super(commandHandler, name);
        RECIPES = new ArrayList<>(recipe.size());
        RECIPES.addAll(recipe);
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
        final Player player = (Player) sender;
        ItemStack[] matrix;

        switch (args[0].toLowerCase()) {
            case "ironsight":
                matrix = Torches.getInstance().ironSight.getMatrix();
                break;
            case "goldsight":
                matrix = Torches.getInstance().goldSight.getMatrix();
                break;
            default:
                MessageUtil.sendErrorMessage(player, "Invalid Recipe");
                return true;
        }

        InventoryView inventoryView = player.openWorkbench(null, true);

        final CraftingInventory inventory = (CraftingInventory) inventoryView.getTopInventory();
        inventory.setMatrix(matrix);


        sender.sendMessage("You have selected " + args[0]);

        Bukkit.getScheduler().runTaskLater(Torches.getInstance(), new Runnable() {
            @Override
            public void run() {

                inventory.clear();
                player.closeInventory();

            }
        }, 100l);

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

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], RECIPES, new ArrayList<String>(RECIPES.size()));
        }
        return null;
    }
}
