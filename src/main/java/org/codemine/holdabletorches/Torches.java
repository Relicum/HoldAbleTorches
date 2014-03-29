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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.codemine.holdabletorches.Commands.CommandHandler;
import org.codemine.holdabletorches.Commands.CommandManager;
import org.codemine.holdabletorches.Commands.FlashLight;
import org.codemine.holdabletorches.Commands.SightViewer;
import org.codemine.holdabletorches.Listeners.DropItems;
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
    public CommandManager cm;
    public RecipeBuilder ironSight;
    public RecipeBuilder goldSight;
    public CommandHandler commandHandler;
    public RecipeManager recipeManager;
    public List<ItemStack> customItems = new ArrayList<>();
    public Map<String, Object> flashSettings;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();


        flashSettings = getConfig().getConfigurationSection("settings.flashlight").getValues(true);

        if (getConfig().getBoolean("settings.general.firstload")) {
            setUpTorch();
        } else if ((Boolean) flashSettings.get("enable")) {
            this.torch = getConfig().getItemStack("flashlight");
        }

        recipeManager = new RecipeManager();
        MessageUtil.logInfoFormatted("Plugin made by: " + getDescription().getAuthors());
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommand(new FlashLight(commandHandler, "flashlight"));
        cm = new CommandManager();

        getCommand("goldsight").setExecutor(cm);

        //applyLightMeta();
        //this.torch = new ItemStack(Material.TORCH, 1);
        //this.torch.setItemMeta(this.torchMeta);
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


        commandHandler.registerCommand(new SightViewer(commandHandler, "torchviewer", recipeManager.getValidNames()));
        recipeManager.addRecipeName(torch.getItemMeta().getDisplayName());
        Iterator<Permission> per = getServer().getPluginManager().getPermissions().iterator();
        while (per.hasNext()) {
            Permission p = per.next();
            System.out.println("Permission name: " + p.getName());

            Map<String, Boolean> map = p.getChildren();
            if (map.size() != 0) {
                System.out.println("Child perms found = " + map.keySet().toString());
            }
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(Arrays.asList(torch, ironSight.getResult())), this);
        getServer().getPluginManager().registerEvents(new DropItems(recipeManager.getValidNames()), this);
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
        getServer().getScheduler().cancelAllTasks();
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
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
            if (player.hasMetadata("MENUOPENID") || player.hasMetadata("MENUOPEN")) {
                e.setCancelled(true);
                List<MetadataValue> values = player.getMetadata("MENUOPENID");
                if (values != null) {
                    int id = values.get(0).asInt();
                    getServer().getScheduler().cancelTask(id);
                    e.getView().getTopInventory().clear();
                    e.getView().close();
                    player.closeInventory();
                    //MessageUtil.sendMessage(player, "The menu meta open id is " + id);

                }
                player.removeMetadata("MENUOPENID", this);
                player.removeMetadata("MENUOPEN", this);

                //MessageUtil.sendMessage(player, "Menu click event in workbench");
            }

        }
    }


    public void pj(final PlayerJoinEvent e) {

        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                int first = e.getPlayer().getInventory().firstEmpty();
                e.getPlayer().getInventory().setItem(first, torch);

            }
        }, 10l);


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
    public void bp(final BlockPlaceEvent e) {
        if (!e.getItemInHand().getType().equals(Material.TORCH) && !e.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON))
            return;
        for (int i = 0; i < customItems.size(); i++) {
            if (e.getItemInHand().isSimilar(customItems.get(i))) {
                e.setCancelled(true);
                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().updateInventory();
                    }
                }, 1l);
            }
        }


    }

    @SuppressWarnings("all")
    @EventHandler(priority = EventPriority.LOW)
    public void onHold(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return; //If action was not right click air do nothing
        if (!e.getItem().getType().equals(Material.TORCH) && !e.getItem().getType().equals(Material.REDSTONE_TORCH_ON))
            return;
        if (!e.getItem().hasItemMeta()) return;
        if (e.getItem().getAmount() > 1) return;

        Player player = e.getPlayer();
        if (!player.hasPermission("holdabletorches.use")) return; //if the player does not have the perm do nothing

        if (e.getMaterial().equals(Material.TORCH)) {


            //NOTE WHEN I SAVE DO NOTHING, NOTICE i HAVE NOT CANCELLED THE EVENT. RETURN JUST ENDS THIS LISTENER


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

            return;
        }
        if (e.getMaterial().equals(Material.REDSTONE_TORCH_ON)) {
            ItemMeta meta = e.getItem().getItemMeta();
            List<String> lo = meta.getLore();
            String last = ChatColor.stripColor(lo.get(lo.size() - 1));
            String[] sp = last.split(":");
            int ti = Integer.parseInt(sp[1].trim()) * 20;
 /*           if (player.hasMetadata("MENUOPENID") || player.hasMetadata("MENUOPEN")) {
                List<MetadataValue> id = player.getMetadata("MENUOPENID");
                if (id != null) {
                    MessageUtil.sendMessage(player, "The menu meta open is " + id.size());
                    return;
                }
                MessageUtil.sendMessage(player, "menu open can not read meta properly");
                return;
            }*/

            if (player.hasMetadata("HATREDMETA")) {
                List<MetadataValue> metas = player.getMetadata("HATREDMETA");
                long past = metas.get(0).asLong();
                String newTime = parseTorchTime(String.valueOf((System.currentTimeMillis() - past) / 1000));
                System.out.println(newTime);
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);
                lo.remove(lo.size() - 1);
                lo.add(ChatColor.translateAlternateColorCodes('&', newTime));
                meta.setLore(lo);
                e.getItem().setItemMeta(meta);
                //e.getPlayer().setItemInHand(e.getItem());
                MessageUtil.sendMessage(player, "Time past is " + (System.currentTimeMillis() - past) / 1000);
                player.removeMetadata("HATREDMETA", getPlug());
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, ti, 1, true));
                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 0.0f);
                player.setMetadata("HATREDMETA", new FixedMetadataValue(getPlug(), System.currentTimeMillis()));
                MessageUtil.sendMessage(player, "Time left is " + ti / 20);
            }


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


    private Plugin getPlug() {
        return this;
    }

    private void setUpTorch() {
        ItemMeta torchMeta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
        torchMeta.setDisplayName(defaultTorchName());
        torchMeta.setLore(defaultTorchLore());
        this.torch = new ItemStack(Material.TORCH);
        this.torch.setItemMeta(torchMeta);

        getConfig().set("flashlight", this.torch);
        getConfig().set("settings.general.firstload", false);
        saveConfig();
        reloadConfig();

        MessageUtil.logInfoFormatted("setup is enabled");
    }

    private List<String> defaultTorchLore() {
        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.GREEN + "Keep this Torch in your hand to have constant visibility");
        lore.add(1, ChatColor.YELLOW + "Remove it from your hand and the lights go out");
        return lore;
    }

    private String defaultTorchName() {
        return ChatColor.GOLD + "HoldAble Torch";
    }

    private String parseTorchTime(String rp) {

        return String.format("&cTime left: %s", rp);
    }
}
