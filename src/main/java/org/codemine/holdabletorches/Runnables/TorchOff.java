package org.codemine.holdabletorches.Runnables;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.codemine.holdabletorches.Utils.I18N;

/**
 * Name: TorchOff.java Created: 02 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class TorchOff extends BukkitRunnable {

    public long end = System.currentTimeMillis();
    private Player player;
    private ItemStack stack;
    private int itemSlot;
    private ItemMeta meta;
    private int timeLine;

    public TorchOff(Player player, ItemStack itemStack)
    {

        Validate.isTrue(itemStack.getType().equals(Material.REDSTONE_TORCH_ON), I18N.STRING("torchoff.validation"));
        this.player = player;
        this.stack = itemStack;
        this.meta = itemStack.getItemMeta();
        this.timeLine = meta.getLore().size() - 1;
        this.itemSlot = player.getInventory().getHeldItemSlot();

    }

    @Override
    public void run()
    {

    }

    private String getLastLore()
    {

        return meta.getLore().get(timeLine);
    }

    private String parseTorchTime(String rp)
    {

        return String.format(ChatColor.translateAlternateColorCodes('&', "&cTime left: %s"), rp);
    }

    private Integer getTorchTime(String line)
    {

        Validate.notNull(line);

        String[] tmp = ChatColor.stripColor(line).split(":");
        if(tmp.length == 2)

        {
            return Integer.parseInt(tmp[1].trim());
        }
        else
        {
            return -1;
        }

    }

}
