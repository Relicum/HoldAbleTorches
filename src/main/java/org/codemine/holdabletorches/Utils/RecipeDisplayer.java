package org.codemine.holdabletorches.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

@SuppressWarnings("MethodReturnOfConcreteClass")
public class RecipeDisplayer implements Listener {

    private final String name;

    private int size;

    private OptionClickEventHandler handler;

    private Plugin plugin;

    private String[] optionNames;

    private ItemStack[] optionIcons;

    public RecipeDisplayer(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin)
    {

        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public RecipeDisplayer(final String name, final OptionClickEventHandler handler, final Plugin plugin, final ItemStack[] matrix)
    {

        this.name = name;
        this.handler = handler;
        this.plugin = plugin;
        ItemStack[] matrix1 = new ItemStack[9];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public RecipeDisplayer setOption(final int position, final ItemStack icon, final String name, final String... info)
    {

        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public static RecipeDisplayer setMatrix(final String name, final ItemStack[] matrix)
    {

        return null;
    }

    public void opens(final HumanEntity player)
    {
        //Inventory inventory = Bukkit.createInventory(null, size, name);
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for(int i = 0 ; i < optionIcons.length ; i++)
        {
            if(optionIcons[i] != null)
            {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void open(final HumanEntity player, final Recipe recipe)
    {

        InventoryView inventoryView = player.openWorkbench(null, true);
        CraftingInventory inventory = (CraftingInventory) inventoryView.getTopInventory();

    }

    void destroy()
    {

        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    public void drag(final InventoryDragEvent event)
    {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(final InventoryClickEvent event)
    {

        if(event.getInventory().getTitle().equals(name))
        {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if(slot >= 0 && slot < size && optionNames[slot] != null)
            {
                Plugin plugin = this.plugin;

                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
                handler.onOptionClick(e);
                if(e.willClose())
                {
                    final HumanEntity p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                                                                   plugin, new Runnable() {

                          public void run()
                          {

                              p.closeInventory();
                          }
                      }, 1
                                                                 );
                }
                if(e.willDestroy())
                {
                    destroy();
                }
            }
        }
    }

    @SuppressWarnings("MethodParameterOfConcreteClass")
    public interface OptionClickEventHandler {

        public void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {

        private final Player player;

        private final int position;

        private final String name;

        private boolean close;

        private boolean destroy;

        public OptionClickEvent(final Player player, final int position, final String name)
        {

            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }

        public Player getPlayer()
        {

            return player;
        }

        public int getPosition()
        {

            return position;
        }

        public String getName()
        {

            return name;
        }

        public boolean willClose()
        {

            return close;
        }

        public boolean willDestroy()
        {

            return destroy;
        }

        public void setWillClose(final boolean close)
        {

            this.close = close;
        }

        public void setWillDestroy(final boolean destroy)
        {

            this.destroy = destroy;
        }
    }

    private ItemStack setItemNameAndLore(final ItemStack item, final String name, final String[] lore)
    {

        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

}
