package org.codemine.holdabletorches;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
import org.bukkit.scheduler.BukkitTask;
import org.codemine.holdabletorches.Commands.CommandHandler;
import org.codemine.holdabletorches.Commands.CommandManager;
import org.codemine.holdabletorches.Commands.FlashLight;
import org.codemine.holdabletorches.Commands.SightViewer;
import org.codemine.holdabletorches.Listeners.DropItems;
import org.codemine.holdabletorches.Listeners.PlayerDeath;
import org.codemine.holdabletorches.Objects.SerializedCraftSettings;
import org.codemine.holdabletorches.Objects.SerializedSettings;
import org.codemine.holdabletorches.Objects.TorchesConfig;
import org.codemine.holdabletorches.Runnables.TorchTimer;
import org.codemine.holdabletorches.Utils.*;

import java.util.*;
import java.util.logging.Level;

/**
 * Name: Torches.java Created: 21 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Torches extends JavaPlugin implements Listener {

    public static Torches instance;

    public TorchesConfig config = null;
    public ItemStack torch;
    public CommandManager cm;
    public RecipeBuilder ironSight;
    public RecipeBuilder goldSight;
    public CommandHandler commandHandler;
    public RecipeManager recipeManager;
    public List<ItemStack> customItems = new ArrayList<>();
    public Map<String,Object> flashSettings;
    public Enchanter enchanter = new Enchanter();
    public BukkitTask task;
    public ItemStack wool;
    private int nextId;
    public static Locale locale;
    public static SerializedSettings ss;
    public Map<TorchTypes,Object> eMap = Collections.synchronizedMap(new EnumMap<>(TorchTypes.class));

    public static void main(String[] args)
    {

    }

    public void onEnable()
    {
        //Register Custom Serialized classes
        ConfigurationSerialization.registerClass(SerializedCraftSettings.class);
        ConfigurationSerialization.registerClass(SerializedSettings.class);

        instance = this;

        this.config = new TorchesConfig(this);
        try
        {
            this.config.init();
        }
        catch(InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        String l = getConfig().getString("settings.general.locale");

        //Set Up Locale to use currently only English soon to be Dutch to

        locale = new Locale(l.substring(0, 1), l.substring(3, 4));
        Locale.setDefault(locale);
        saveResource("MessagesBundle.properties", true);
        saveResource("MessagesBundle_en_GB.properties", true);
        saveResource("MessagesBundle_nl_NL.properties", true);

        //TODO Default Crafting settings will remove before release
        //SerializedCraftSettings si=new SerializedCraftSettings("IronSight",true,true,true,true,true,10);
        //SerializedCraftSettings sg=new SerializedCraftSettings("GoldSight",true,true,true,true,true,20);
        //SerializedCraftSettings sd=new SerializedCraftSettings("DiamondSight",true,true,true,true,true,45);
        //SerializedCraftSettings se=new SerializedCraftSettings("EmeraldSight",true,true,true,true,true,60);

        //Check if plugin should be enabled at all
        if(!getConfig().getBoolean("settings.general.enable"))
        {
            MessageUtil.log(Level.INFO, I18N.STRING("internal.setasdisabled"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Create EnumMap to store crafting names more efficient than a normal map
        eMap.put(TorchTypes.IRONSIGHTS, addAltColor(I18N.STRING("craftlight.ironSight.displayName")));
        eMap.put(TorchTypes.GOLDSIGHTS, addAltColor(I18N.STRING("craftlight.diamondSight.displayName")));
        eMap.put(TorchTypes.DIAMONDSIGHTS, addAltColor(I18N.STRING("craftlight.diamondSight.displayName")));
        eMap.put(TorchTypes.EMERALDSIGHTS, addAltColor(I18N.STRING("craftlight.emeraldSight.displayName")));

        ss = (SerializedSettings) getConfig().get("settings.craftable");

        //Register Custom Enchants
        this.enchanter.registerAll();

        //Get FlashLight User Settings
        flashSettings = getConfig().getConfigurationSection("settings.flashlight").getValues(true);

        //Check if FlashLight is to be used or not

        //Start Flash Light SetUp

        if((Boolean) flashSettings.get("enable"))
        {
            this.torch = FlashItem.getFlashLight();
        }

        recipeManager = new RecipeManager();
        MessageUtil.logInfoFormatted(I18N.STRING("internal.madeby", getDescription().getAuthors()));
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommand(new FlashLight(commandHandler, "flashlight"));

        cm = new CommandManager();

        getCommand("goldsight").setExecutor(cm);

        this.goldSight = recipeManager.getRecipeBuilder(Material.REDSTONE_TORCH_ON)
                                      .getBuilder()
                                      .setEmptyChar('X')
                                      .setItemDisplayName("&6GoldSight")
                                      .setItemLore(Arrays.asList("&6GoldSight Gives 20 seconds on night vision", "", "&aHold the item and right click to turn on", "",
                                                                  "&cTime left: 20"))
                                      .setTopRow(" J ")
                                      .setMiddleRow(" H ")
                                      .setBottomRow(" R ")
                                      .setIngredient('J', Material.GOLD_INGOT)
                                      .setIngredient('H', Material.REDSTONE)
                                      .setIngredient('R', Material.STICK);
        this.goldSight.Build(true, true);

        wool = new ItemStack(Material.WOOL, 1, (short) 5);

        this.ironSight = recipeManager.getRecipeBuilder(Material.REDSTONE_TORCH_ON)
                                      .getBuilder()
                                      .setEmptyChar('X')
                                      .setItemDisplayName("&6IronSight")
                                      .setItemLore(Arrays.asList("&6IronSight Gives 10 seconds on night vision", "", "&aHold the item and right click to turn on", "",
                                                                  "&cTime left: 10"))
                                      .setTopRow(" I ")
                                      .setMiddleRow(" L ")
                                      .setBottomRow(" P ")
                                      .setIngredient('I', Material.IRON_INGOT)
                                      .setIngredient('L', Material.REDSTONE)
                                      .setIngredient('P', Material.STICK);
        this.ironSight.Build(true, true);
        recipeManager.addRecipeName(torch.getItemMeta().getDisplayName());
        commandHandler.registerCommand(new SightViewer(commandHandler, "torchviewer", recipeManager.getValidNames()));

        Iterator<Permission> per = getServer().getPluginManager().getPermissions().iterator();
        while(per.hasNext())
        {
            Permission p = per.next();
            System.out.println("Permission name: " + p.getName());

            Map<String,Boolean> map1 = p.getChildren();
            if(!map1.isEmpty())
            {
                System.out.println("Child perms found = " + map1.keySet().toString());
            }
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(Arrays.asList(torch, ironSight.getResult())), this);
        getServer().getPluginManager().registerEvents(new DropItems(recipeManager.getValidNames()), this);

        customItems.add(torch);
        customItems.add(ironSight.getResult());
        customItems.add(goldSight.getResult());
        System.out.println(recipeManager.getValidNames().toString());

    }

    public void onDisable()
    {

        saveConfig();
        getServer().getScheduler().cancelAllTasks();
        config.saveConfig();
        getServer().clearRecipes();
    }

    public static Torches getInstance()
    {

        return instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pq(PlayerQuitEvent e)
    {

        if(e.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION))
        {
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {

        if(!(e.getWhoClicked() instanceof Player))
        {
            return;
        }
        HumanEntity player = e.getWhoClicked();
        if(e.getInventory().getType().equals(InventoryType.WORKBENCH))
        {
            if(player.hasMetadata("MENUOPENID") && player.hasMetadata("MENUOPEN"))
            {
                e.setCancelled(true);
                List<MetadataValue> values = player.getMetadata("MENUOPENID");
                if(values != null)
                {
                    player.closeInventory();
                }
            }

        }
    }

    @EventHandler
    public void pj(final PlayerJoinEvent e)
    {

        String p = "§";
        String g = "a";
        String r = "c";
        String b = "o";
        boolean t = true;
        StrBuilder sb = new StrBuilder();
        sb.setNewLineText("\n");
        for(int i = 0 ; i < 53 ; i++)
        {
            if(t)
            {
                sb.append(p + b + p + g + "-");
                t = false;
            }
            else
            {
                sb.append(p + b + p + r + "-");
                t = true;
            }

        }

        sb.append(ChatColor.RESET);
        String header = sb.toString() + "\n";

        sb.clear();
        for(int i = 0 ; i < 53 ; i++)
        {
            if(t)
            {
                sb.append(p + b + p + r + "-");
                t = false;
            }
            else
            {
                sb.append(p + b + p + g + "-");
                t = true;
            }

        }

        sb.append(ChatColor.RESET);
        String footer = sb.toString() + "\n";

        sb.clear();
        sb.setLength(53);
        sb.setNewLineText("\n");
        sb.insert(23, "~ HoldAbleTorches ~");

        e.setJoinMessage(header + ChatColor.YELLOW + ChatColor.ITALIC + "                    |-- HoldAbleTorches --|        \n" + footer);






/*        e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().firstEmpty(), FlashItem.getFlashLight());
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run()
            {

                e.getPlayer().updateInventory();
            }
        }, 1l);*/

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, 200, 5, true);
        potionEffect.apply(e.getPlayer());

    }

    @EventHandler
    public void invCraft(final CraftItemEvent e)
    {

        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getSlotType().equals(InventoryType.SlotType.CRAFTING))
        {
            MessageUtil.sendMessage(player, "Not Crafting");
            return;
        }
        if(!e.getCurrentItem().hasItemMeta()) return;
        if(!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String item = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

        if(recipeManager.isValidName(item))
        {

            switch(item.toLowerCase())
            {
                case "ironsight":
                    if(!player.hasPermission("torchiron.use"))
                    {
                        e.setCancelled(true);
                        MessageUtil.sendMessage(player, "No perms");
                    }
                    break;
                case "goldsight":
                    if(!player.hasPermission("torchgold.use"))
                    {
                        e.setCancelled(true);
                        MessageUtil.sendMessage(player, "No perms");
                    }
                    break;
                default:
                    return;

            }

        }
    }

    @EventHandler
    public void closeInv(InventoryCloseEvent e)
    {

        if(e.getView().getTopInventory().getType().equals(InventoryType.WORKBENCH) && e.getPlayer().hasMetadata("MENUOPEN"))
        {
            e.getView().getTopInventory().clear();
            List<MetadataValue> values = e.getPlayer().getMetadata("MENUOPENID");
            if(values != null)
            {
                int id = values.get(0).asInt();
                getServer().getScheduler().cancelTask(id);
            }
            e.getPlayer().removeMetadata("MENUOPENID", this);
            e.getPlayer().removeMetadata("MENUOPEN", this);
            e.getView().close();
            System.out.println("Number of tasks remaining is " + Bukkit.getScheduler().getPendingTasks().size());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void entInter(PlayerInteractEntityEvent e)
    {

        if(e.getPlayer().getItemInHand().getItemMeta().hasEnchant(Enchantment.getByName("UNDROPABLE")))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void bp(final BlockPlaceEvent e)
    {

        if(!e.getItemInHand().getType().equals(Material.TORCH) && !e.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON))
        {
            return;
        }
        if(e.getItemInHand().hasItemMeta())
        {
            e.setBuild(false);
            e.setCancelled(true);
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                @Override
                public void run()
                {

                    e.getPlayer().updateInventory();
                }
            }, 1l);
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHold(PlayerInteractEvent e)
    {

        if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            return; //If action was not right click air do nothing
        }
        if(e.getItem() == null) return;
        if(!e.getItem().getType().equals(Material.TORCH) && !e.getItem().getType().equals(Material.REDSTONE_TORCH_ON))
        {
            return;
        }
        if(!e.getItem().hasItemMeta()) return;
        if(e.getItem().getAmount() > 1) return;

        Player player = e.getPlayer();
        if(!player.hasPermission("holdabletorches.use")) return; //if the player does not have the perm do nothing

        if(e.getMaterial().equals(Material.TORCH))
        {

            //NOTE WHEN I SAVE DO NOTHING, NOTICE i HAVE NOT CANCELLED THE EVENT. RETURN JUST ENDS THIS LISTENER

            if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);
                MessageUtil.sendMessage(player, "Lights OUT");
                player.removeMetadata("HATMETA", getPlug());

            }
            else
            {

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, true));
                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 0.0f);
                MessageUtil.sendMessage(player, "Lights ON");
                player.setMetadata("HATMETA", new FixedMetadataValue(getPlug(), Boolean.TRUE));

            }

            return;
        }
        if(e.getMaterial().equals(Material.REDSTONE_TORCH_ON))
        {
            ItemMeta meta = e.getItem().getItemMeta();
            List<String> lo = meta.getLore();

            Integer ti = getTorchTime(lo.get(lo.size() - 1));

            if(player.hasMetadata("HATREDMETA") && player.hasMetadata("HATREDTASKID"))
            {
                List<MetadataValue> metas = player.getMetadata("HATREDMETA");
                List<MetadataValue> ids = player.getMetadata("HATREDTASKID");
                long past = metas.get(0).asLong();
                int id = ids.get(0).asInt();
                getServer().getScheduler().cancelTask(id);
                long pa = (ti) - (System.currentTimeMillis() - past) / 1000;

                String newTime = parseTorchTime(String.valueOf(pa));
                System.out.println(newTime);
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);

                lo.set(lo.size() - 1, newTime);
                meta.setLore(lo);

                e.getItem().setItemMeta(meta);
                //e.getPlayer().setItemInHand(e.getItem());
                MessageUtil.sendMessage(player, "Time past is " + (System.currentTimeMillis() - past) / 1000);

                player.removeMetadata("HATREDTASKID", this);
            }
            else
            {

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, ti * 20, 1, true));

                player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 0.0f);

                this.task = Bukkit.getServer().getScheduler().runTaskLater(this, new TorchTimer(player, e.getItem()), (long) ti * 20);

                player.setMetadata("HATREDMETA", new FixedMetadataValue(this, System.currentTimeMillis()));
                player.setMetadata("HATREDTASKID", new FixedMetadataValue(this, task.getTaskId()));

                MessageUtil.sendMessage(player, "Time left is " + ti);
            }

        }
    }

    @EventHandler
    public void pH(PlayerItemHeldEvent e)
    {
        //if(!e.getPlayer().getInventory().getItem(e.getPreviousSlot()).hasItemMeta())return;
        if(e.getPlayer().hasMetadata("HATMETA"))
        {
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 5.0f, 1.0f);
            e.getPlayer().removeMetadata("HATMETA", getPlug());
            MessageUtil.sendMessage(e.getPlayer(), "Lights AUTO OFF");
        }
        Player player = e.getPlayer();

        if(player.hasMetadata("HATREDMETA") && player.hasMetadata("HATREDTASKID"))
        {
            ItemMeta meta = player.getInventory().getItem(e.getPreviousSlot()).getItemMeta();
            List<String> lo = meta.getLore();

            Integer ti = getTorchTime(lo.get(lo.size() - 1));
            List<MetadataValue> metas = player.getMetadata("HATREDMETA");
            List<MetadataValue> ids = player.getMetadata("HATREDTASKID");
            long past = metas.get(0).asLong();
            int id = ids.get(0).asInt();
            getServer().getScheduler().cancelTask(id);
            long pa = (ti) - (System.currentTimeMillis() - past) / 1000;

            String newTime = parseTorchTime(String.valueOf(pa));
            System.out.println(newTime);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.playSound(player.getLocation(), Sound.CLICK, 5.0f, 1.0f);

            lo.set(lo.size() - 1, newTime);
            meta.setLore(lo);

            player.getInventory().getItem(e.getPreviousSlot()).setItemMeta(meta);
            //e.getPlayer().setItemInHand(e.getItem());
            MessageUtil.sendMessage(player, "Auto Off time used " + (System.currentTimeMillis() - past) / 1000 + " seconds");

            player.removeMetadata("HATREDTASKID", this);
        }

    }

    private Plugin getPlug()
    {

        return this;
    }

/*    private void setUpTorch()
    {

        ItemMeta torchMeta = Bukkit.getItemFactory().getItemMeta(Material.TORCH);
        //Enchantment unDropable = Enchantment.getByName("UNDROPABLE");
        torchMeta.setDisplayName(defaultTorchName());
        //AbstractEnchant abstractEnchant = (AbstractEnchant)unDropable;
        torchMeta.setLore(defaultTorchLore(Enchantment.getByName("UNDROPABLE").getName()));
        this.torch = new ItemStack(Material.TORCH);
        torchMeta.addEnchant(Enchantment.getByName("UNDROPABLE"), 1, true);
        this.torch.setItemMeta(torchMeta);

        getConfig().set("flashlight", this.torch);
        getConfig().set("settings.general.firstload", false);
        saveConfig();
        reloadConfig();

        MessageUtil.logInfoFormatted("setup is enabled");
    }*/

    private List<String> defaultTorchLore(String noDrop)
    {

        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.GREEN + "Keep this Torch in your hand to have constant visibility");
        lore.add(1, ChatColor.YELLOW + "Remove it from your hand and the lights go out");
        lore.add(2, ChatColor.GRAY + noDrop);
        return lore;
    }

    private String defaultTorchName()
    {

        return ChatColor.GOLD + "HoldAble Torch";
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

    public int getNextId()
    {

        ss.incrementId();

        getConfig().set("settings.craftable", ss);
        return ss.getId();
    }

    private String addAltColor(final String s)
    {

        final char COLOR_CHAR = '\u00A7';
        final char SAFE_CHAR = '&';

        char[] b = s.toCharArray();
        for(int i = 0 ; i < b.length - 1 ; i++)
        {
            if(b[i] == COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
            {
                b[i] = SAFE_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }

        }
        return new String(b);
    }
}
