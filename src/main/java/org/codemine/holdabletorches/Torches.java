package org.codemine.holdabletorches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.codemine.holdabletorches.Utils.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Name: Torches.java Created: 21 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Torches extends JavaPlugin implements Listener {

    public ArrayList<String> light = new ArrayList<>();
    public ItemStack torch;
    public ItemMeta torchMeta;
    public ItemMeta redMeta;

    public void onEnable() {
        PluginDescriptionFile pdf = getDescription();
        MessageUtil.logInfoFormatted("Plugin made by: " + pdf.getAuthors());
        getServer().getPluginManager().registerEvents(this, this);
        applyLightMeta();
        this.torch = new ItemStack(Material.TORCH, 1);
        this.torch.setItemMeta(this.torchMeta);

    }

    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pq(PlayerQuitEvent e) {
        if (e.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void pj(PlayerJoinEvent e) {
        e.getPlayer().getInventory().setItem(0, this.torch.clone());


    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void bp(BlockPlaceEvent e) {
        if (e.getItemInHand().getType().equals(Material.TORCH) && e.getItemInHand().getItemMeta().hasDisplayName() && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("HoldAble Torch")) {
            e.setCancelled(true);

        }
    }

    @SuppressWarnings("all")
    @EventHandler(priority = EventPriority.LOW)
    public void onHold(final PlayerInteractEvent e) {

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return; //If action was not right click air do nothing

        if (!e.getMaterial().isBlock()) return; //if the item in hand is not of block type do nothing

        final Player player = e.getPlayer();

        if (!e.getMaterial().equals(Material.TORCH) || !e.getMaterial().equals(Material.REDSTONE_TORCH_ON) && (!e.getItem().getItemMeta().hasDisplayName() || !ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("HoldAble Torch"))) {
            return;
        }
        //If item is not a torch or redstone touch do nothing and it has incorrect Display Name


        if (!player.hasPermission("holdabletorches.use")) return; //if the player does not have the perm do nothing

        if (e.getMaterial().equals(Material.TORCH)) {


            //NOTE WHEN I SAVE DO NOTHING, NOTICE i HAVE NOT CANCELLED THE EVENT. RETURN JUST ENDS THIS LISTENER

            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    player.updateInventory();
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        player.playSound(player.getLocation(), Sound.PISTON_RETRACT, 5.0f, 1.0f);
                        MessageUtil.sendMessage(player, "Lights OUT");
                        e.getPlayer().removeMetadata("HATMETA",getPlug());

                    } else {

                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, true));
                        player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 5.0f, 1.0f);
                        MessageUtil.sendMessage(player, "Lights ON");
                        e.getPlayer().setMetadata("HATMETA", new FixedMetadataValue(getPlug(), Boolean.TRUE));


                    }
                }
            }, 1l);

        }
    }

    @EventHandler
    public void pH(PlayerItemHeldEvent e) {
        if (!e.getPlayer().hasMetadata("HATMETA")) return;

            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 5.0f, 1.0f);
            e.getPlayer().removeMetadata("HATMETA",getPlug());
            MessageUtil.sendMessage(e.getPlayer(), "Lights AUTO OFF");
    }

    private void applyLightMeta() {
        this.torchMeta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
        this.torchMeta.setDisplayName(ChatColor.GOLD + "HoldAble Torch");
        this.torchMeta.setLore(Arrays.asList(ChatColor.GREEN + "Keep this Torch in your hand to have constant visibility", ChatColor.YELLOW + "Remove it from your hand and the lights go out"));
        this.redMeta = Bukkit.getItemFactory().getItemMeta(Material.REDSTONE_TORCH_ON);


    }

    private Plugin getPlug() {
        return this;
    }

}
