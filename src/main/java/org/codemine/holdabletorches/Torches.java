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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.codemine.holdabletorches.Commands.CommandHandler;
import org.codemine.holdabletorches.Commands.CommandManager;
import org.codemine.holdabletorches.Commands.FlashLight;
import org.codemine.holdabletorches.Commands.SightViewer;
import org.codemine.holdabletorches.Listeners.PlayerDeath;
import org.codemine.holdabletorches.Utils.MessageUtil;
import org.codemine.holdabletorches.Utils.RecipeBuilder;
import org.codemine.holdabletorches.Utils.RecipeManager;

import java.util.*;

/**
 * Name: Torches.java Created: 21 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Torches extends JavaPlugin implements Listener {

    public static Torches instance;
    public ItemStack torch;
    public ItemMeta torchMeta;
    public CommandManager cm;
    public RecipeBuilder ironSight;
    public RecipeBuilder goldSight;
    public CommandHandler commandHandler;
    public ShapedRecipe sh;
    public RecipeManager recipeManager;
    public List<ItemStack> customItems = new ArrayList<>();

    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        recipeManager = new RecipeManager();
        MessageUtil.logInfoFormatted("Plugin made by: " + getDescription().getAuthors());
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommand(new FlashLight(commandHandler, "flashlight"));
        cm = new CommandManager();

        getCommand("goldsight").setExecutor(cm);

        applyLightMeta();
        this.torch = new ItemStack(Material.TORCH, 1);
        this.torch.setItemMeta(this.torchMeta);
        this.goldSight = recipeManager.getRecipeBuilder(Material.REDSTONE_TORCH_ON)
                .getBuilder()
                .setEmptyChar('X')
                .setItemDisplayName("&6GoldSight")
                .setItemLore(Arrays.asList("&6GoldSight Gives 20 seconds on night vision", "", "&aHold the item and right click to turn on", "", "&cTime left: 20"))
                .setTopRow(" J ")
                .setMiddleRow(" H ")
                .setBottomRow(" R ")
                .setIngredient('J', Material.GOLD_INGOT)
                .setIngredient('H', Material.REDSTONE)
                .setIngredient('R', Material.STICK);
        this.goldSight.Build(true, true);


        this.ironSight = recipeManager.getRecipeBuilder(Material.REDSTONE_TORCH_ON)
                .getBuilder()
                .setEmptyChar('X')
                .setItemDisplayName("&6IronSight")
                .setItemLore(Arrays.asList("&6IronSight Gives 10 seconds on night vision", "", "&aHold the item and right click to turn on", "", "&cTime left: 10"))
                .setTopRow(" I ")
                .setMiddleRow(" L ")
                .setBottomRow(" P ")
                .setIngredient('I', Material.IRON_INGOT)
                .setIngredient('L', Material.REDSTONE)
                .setIngredient('P', Material.STICK);
        this.ironSight.Build(true, true);

        sh = new ShapedRecipe(torch);
        sh.shape(" N ", " R ", " S ").setIngredient('N', Material.IRON_INGOT).setIngredient('R', Material.REDSTONE).setIngredient('S', Material.STICK);


        if (getServer().addRecipe(sh)) {
            MessageUtil.logInfoFormatted("New Recipe For : " + sh.getResult().getType() + "\n" + Arrays.toString(sh.getShape()));

        } else {
            MessageUtil.logServereFormatted("Unable to create new recipe");
        }

        commandHandler.registerCommand(new SightViewer(commandHandler, "torchviewer", recipeManager.getValidNames()));
        recipeManager.addRecipeName(torchMeta.getDisplayName());
        Iterator<Permission> per = getServer().getPluginManager().getPermissions().iterator();
        while (per.hasNext()) {
            Permission p = per.next();
            System.out.println("Permission name: " + p.getName());

            Map<String, Boolean> map = p.getChildren();
            if (map.size() != 0) {
                System.out.println("Child perms found = " + map.keySet().toString());
            }
        }

        getServer().getPluginManager().registerEvents(new PlayerDeath(Arrays.asList(torch, ironSight.getResult())), this);
        customItems.add(torch);
        customItems.add(ironSight.getResult());
        customItems.add(goldSight.getResult());
        System.out.println(recipeManager.getValidNames().toString());

        //RecipeBuilder.setLineFormat();

/*        Iterator<Recipe> recipeList=getServer().recipeIterator();
        while(recipeList.hasNext()){
            Recipe recipes =recipeList.next();
            if(recipes.getResult().hasItemMeta()){

                System.out.println("Recipe: " + recipes.getResult().getItemMeta().getDisplayName());
                System.out.println("Matrix is " + recipes.getResult());
            }
        }*/


    }

    public void onDisable() {
        getServer().clearRecipes();
    }

    public static Torches getInstance() {
        return instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pq(PlayerQuitEvent e) {
        if (e.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void pj(PlayerJoinEvent e) {


    }

    @EventHandler
    public void invCraft(CraftItemEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (e.getSlotType().equals(InventoryType.SlotType.CRAFTING)) {
            MessageUtil.sendMessage(player, "Not Crafting");
            return;
        }
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;


        String item = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

        if (recipeManager.isValidName(item)) {

            switch (item.toLowerCase()) {
                case "ironsight":
                    if (!player.hasPermission("torchiron.use")) {
                        e.setCancelled(true);
                        MessageUtil.sendMessage(player, "No perms");
                    }
                    break;
                case "goldsight":
                    if (!player.hasPermission("torchgold.use")) {
                        e.setCancelled(true);
                        MessageUtil.sendMessage(player, "No perms");
                    }
                    break;
                default:
                    return;


            }


        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void bp(BlockPlaceEvent e) {
        if (customItems.contains(e.getItemInHand())) {
            e.setCancelled(true);
        }
    }

    @SuppressWarnings("all")
    @EventHandler(priority = EventPriority.LOW)
    public void onHold(final PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return; //If action was not right click air do nothing
        if (!customItems.contains(e.getItem())) {
            return;
        }

        final Player player = e.getPlayer();
        if (!player.hasPermission("holdabletorches.use")) return; //if the player does not have the perm do nothing

        if (e.getMaterial().equals(Material.TORCH)) {


            //NOTE WHEN I SAVE DO NOTHING, NOTICE i HAVE NOT CANCELLED THE EVENT. RETURN JUST ENDS THIS LISTENER

            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    player.updateInventory();
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);
                        MessageUtil.sendMessage(player, "Lights OUT");
                        player.removeMetadata("HATMETA", getPlug());

                    } else {

                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, true));
                        player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 0.0f);
                        MessageUtil.sendMessage(player, "Lights ON");
                        player.setMetadata("HATMETA", new FixedMetadataValue(getPlug(), Boolean.TRUE));


                    }
                }
            }, 1l);

        }
        if (e.getMaterial().equals(Material.REDSTONE_TORCH_ON)) {
            player.updateInventory();
            List<String> lo = e.getItem().getItemMeta().getLore();
            String last = ChatColor.stripColor(lo.get(lo.size() - 1));
            String[] sp = last.split(":");
            int ti = Integer.parseInt(sp[1].trim()) * 20;
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, ti, 1, true));
            MessageUtil.sendMessage(player, "Time left is " + ti / 20);


        }
    }

    @EventHandler
    public void pH(PlayerItemHeldEvent e) {
        if (!e.getPlayer().hasMetadata("HATMETA")) return;
        e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 5.0f, 1.0f);
        e.getPlayer().removeMetadata("HATMETA", getPlug());
        MessageUtil.sendMessage(e.getPlayer(), "Lights AUTO OFF");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pD(PlayerDropItemEvent e) {

        if (!customItems.contains(e.getItemDrop().getItemStack())) {
            return;
        }

        e.setCancelled(true);
        final Player player = e.getPlayer();
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }, 1l);

    }

    private void applyLightMeta() {
        this.torchMeta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
        this.torchMeta.setDisplayName(ChatColor.GOLD + "HoldAble Torch");
        this.torchMeta.setLore(Arrays.asList(ChatColor.GREEN + "Keep this Torch in your hand to have constant visibility", ChatColor.YELLOW + "Remove it from your hand and the lights go out"));
        //this.redMeta = Bukkit.getItemFactory().getItemMeta(Material.REDSTONE_TORCH_ON);


    }

    private Plugin getPlug() {
        return this;
    }

}
