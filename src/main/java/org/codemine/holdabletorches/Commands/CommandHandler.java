package org.codemine.holdabletorches.Commands;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: CommandHandler.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class CommandHandler {

    public static SimpleCommandMap scmp;
    private static List<CommandHandler> list = new ArrayList<>();
    public String PREFIX ="";
    public String USAGE_MESSAGE="";
    public String NOPERMS_MESSAGE="";
    public String PLAYER_ONLY="";
    private JavaPlugin plugin;
    private List<SimpleCommand> cmdList = new ArrayList<>();

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
