package org.codemine.holdabletorches;

import java.util.ArrayList;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemine.holdabletorches.Utils.MessageUtil;

/**
 * Name: Torches.java Created: 21 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Torches extends JavaPlugin implements Listener {

    public ArrayList<String> torch = new ArrayList<>();

    public void onEnable() {
        PluginDescriptionFile pdf = getDescription();
        MessageUtil.logInfoFormatted("Plugin made by: " + pdf.getAuthors());


    }

    public void onDisable() {


    }

}
