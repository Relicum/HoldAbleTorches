package org.codemine.holdabletorches.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.codemine.holdabletorches.Utils.MessageUtil;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Name: SimpleCommand.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public abstract class SimpleCommand extends Command implements CmdExecutor,PluginIdentifiableCommand,CmdInfo {

    
    protected CommandHandler commandHandler;
    protected Plugin plugin;
    protected boolean playerOnly;
    protected int minArgs;
    protected int maxArgs;

    protected SimpleCommand(CommandHandler commandHandler,String name) {
        super(name);
        
        this.commandHandler = commandHandler;

        if (commandHandler == null) {
            try {
                throw new IllegalArgumentException("Unable to register command " + name);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            }
        }

        plugin = this.commandHandler.getPlugin();


        if (!getClass().isAnnotationPresent(CmdInfo.class)) {

            try {
                throw new Exception("Missing Annotations in " + getClass().getSimpleName());
            } catch (Exception e) {
                e.printStackTrace();

                return;
            }
        }


        setAliases(Arrays.asList(aliases()));
        setDescription(description());
        setUsage(commandHandler.PREFIX + commandHandler.USAGE_MESSAGE + usage());
        setPermission(permission());
        setPermissionMessage(commandHandler.PREFIX + commandHandler.NOPERMS_MESSAGE);
        playerOnly = playerOnly();
        setMinArgs(minArgs());
        setMaxArgs(maxArgs());


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {

        boolean success = false;

        if (!plugin.isEnabled()) {
            try {
                throw new CommandException(command + " in plugin " +
                        plugin.getDescription().getFullName() + ":plugin is not enabled");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (!testPermission(sender)) {
            return true;
        }

        if ((playerOnly()) && (!(sender instanceof Player))) {
            MessageUtil.sendRawMessage(sender, commandHandler.PREFIX + commandHandler.PLAYER_ONLY);
            return true;
        }


        if (minArgs() > minArgs()) {
            MessageUtil.sendErrorMessage(sender, "Maximum number of arguments can't be more than the minimum number of arguments");
            return true;
        }

        if (!(args.length >= minArgs()) || !(args.length <= maxArgs())) {
            MessageUtil.sendErrorMessage(sender, "Incorrect number of arguments supplied");
            return true;
        }


        try {
            success = onCommand(sender, command, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled command exception in command " + command + " in plugin " +
                    plugin.getDescription().getFullName());
        }

        if ((!success) && (usageMessage.length() > 0)) {
            for (String line : usageMessage.replace("<command>", command).split("\\n")) {
                MessageUtil.sendMessage(sender, line);
            }
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets Annotation info.
     *
     * @return the Annotation info
     */
    public CmdInfo getCmdInfo() {
        return getClass().getAnnotation(CmdInfo.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description() {
        return getCmdInfo().description();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String usage() {
        return getCmdInfo().usage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String permission() {
        return getCmdInfo().permission();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerOnly() {
        return getCmdInfo().playerOnly();
    }

    /**
     * Sets min args.
     *
     * @param minArgs the min args
     */
    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }


    /**
     * Sets max args.
     *
     * @param maxArgs the max args
     */
    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }


    /**
     * Get min arguments required
     *
     * @return int the minimum args required
     */
    @Override
    public int minArgs() {
        return getCmdInfo().minArgs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int maxArgs() {
        return getCmdInfo().maxArgs();
    }

    @Override
    public String[] aliases() {
        return getCmdInfo().aliases();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean onCommand(CommandSender sender, String command, String[] args);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<String> tabComplete(CommandSender sender, String s, String[] args);

    /**
     * Returns the annotation type of this annotation.
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
