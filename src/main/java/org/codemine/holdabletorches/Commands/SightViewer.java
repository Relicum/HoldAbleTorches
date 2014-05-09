package org.codemine.holdabletorches.Commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.StringUtil;
import org.codemine.holdabletorches.Runnables.OpenView;
import org.codemine.holdabletorches.Torches;
import org.codemine.holdabletorches.Utils.I18N;
import org.codemine.holdabletorches.Utils.MessageUtil;
import org.codemine.holdabletorches.Utils.TorchTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Name: SightViewer.java Created: 29 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@CmdInfo(
          description = "Allows players to see the recipes for custom craftable torches" ,
          usage = "/torchviewer [recipe]" ,
          permission = "holdabletorches.player.view" ,
          playerOnly = true ,
          minArgs = 1 ,
          maxArgs = 1 ,
          aliases = {"tv"}
)
public class SightViewer extends SimpleCommand {

    private static List<String> RECIPES = null;

    @SuppressWarnings("MethodParameterOfConcreteClass")
    public SightViewer(final CommandHandler commandHandler, final String name, final Collection<String> recipe)
    {

        super(commandHandler, name);
        RECIPES = ImmutableList.copyOf(recipe);
    }

    /**
     * {@inheritDoc}
     *
     * @param sender
     * @param command
     * @param args
     */
    @Override
    public boolean onCommand(final CommandSender sender, final String command, final String[] args)
    {

        final Player player = (Player) sender;
        ItemStack[] matrix;
        String torchName;

        switch(args[0].toLowerCase())
        {
            case "ironsight":
                matrix = Torches.getInstance().ironSight.getMatrix();
                torchName = (String) Torches.getInstance().eMap.get(TorchTypes.IRONSIGHTS);
                player.setMetadata("MENUOPEN", new FixedMetadataValue(Torches.getInstance(), Boolean.TRUE));
                break;
            case "goldsight":
                matrix = Torches.getInstance().goldSight.getMatrix();
                torchName = (String) Torches.getInstance().eMap.get(TorchTypes.GOLDSIGHTS);
                player.setMetadata("MENUOPEN", new FixedMetadataValue(Torches.getInstance(), Boolean.TRUE));
                break;
            default:
                MessageUtil.sendErrorMessage(player, I18N.STRING("sightviewer.invalid.recipe"));
                return true;
        }

        final InventoryView inventoryView = player.openWorkbench(null, true);

        final CraftingInventory inventory = (CraftingInventory) inventoryView.getTopInventory();

        inventory.setMatrix(matrix);
        inventory.setResult(new ItemStack(Material.REDSTONE_TORCH_ON));
        inventory.setMaxStackSize(1);

        MessageUtil.sendMessage(player, I18N.STRING("sightviewer.recipe.1", torchName));
        MessageUtil.sendMessage(player, I18N.STRING("sightviewer.recipe.2"));
        MessageUtil.sendMessage(player, I18N.STRING("sightviewer.recipe.3", torchName, 20));
        int task = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Torches.getInstance(), new OpenView(player, inventory), 200l);
        player.setMetadata("MENUOPENID", new FixedMetadataValue(Torches.getInstance(), task));
        System.out.println("Number of tasks remaining is " + Bukkit.getScheduler().getPendingTasks().size());

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
    public List<String> tabComplete(final CommandSender sender, final String s, final String[] args)
    {

        if(args.length == 1)
        {
            return StringUtil.copyPartialMatches(args[0], RECIPES, new ArrayList<String>(RECIPES.size()));
        }
        return ImmutableList.of();
    }
}
