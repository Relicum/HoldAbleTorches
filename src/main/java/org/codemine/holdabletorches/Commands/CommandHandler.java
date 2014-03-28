package org.codemine.holdabletorches.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemine.holdabletorches.Utils.MessageUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    public String PREFIX = "[OLD]";
    public String USAGE_MESSAGE = "\u00A7cUsage: \u00A7f";
    public String NOPERMS_MESSAGE = "§cYou do not have permission to perform that command";
    public String PLAYER_ONLY = "\u00A7cYou can only run this command in game";
    private JavaPlugin plugin;
    private List<SimpleCommand> cmdList = new ArrayList<>();

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        list.add(this);
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            scmp = (SimpleCommandMap) f.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerCommand(SimpleCommand command) {


        scmp.register("", command);
        MessageUtil.logInfoFormatted("Command registered: " + command.getName());
        cmdList.add(command);
    }

    public void registerCommands(List<SimpleCommand> commands) {
        for (SimpleCommand command : commands) {
            registerCommand(command);
        }
    }

    public void registerCommands(SimpleCommand[] commands) {
        registerCommands(Arrays.asList(commands));
    }

    public SimpleCommandMap getCommand() {
        return scmp;
    }

    public List<SimpleCommand> getCommands() {
        return cmdList;
    }

    public static List<CommandHandler> getCommandManagers() {
        return list;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
