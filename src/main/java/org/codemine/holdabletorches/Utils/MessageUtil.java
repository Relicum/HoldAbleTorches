package org.codemine.holdabletorches.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Name: MessageUtil.java Created: 12 March 2014
 * <p/>
 * Message Utility class for send preformatted messages and writing to the log
 *
 * @author Relicum
 * @version 0.0.1
 */
public class MessageUtil implements consolColors {

    private static final char COLOR_CHAR = '\u00A7';
    private static final char SAFE_CHAR = '&';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

    private static final String prefix = "&5[&bHoldable_Torches&5] ";
    private static final String infoColor = "&a";
    private static final String errorColor = "&c";
    private static final String adminColor = "&5";

    private static final String logPrefix = WHITE + "[" + BOLDGREEN + "Holdable_Torches" + WHITE + "] ";
    private static final String logInfoColor = BOLDYELLOW;
    private static final String logSevereColor = BOLDRED;
    private static final String logWarningColor = BOLDMAGENTA;

    /**
     * Send command raw message.
     *
     * @param sender  the @{@link org.bukkit.command.CommandSender} sender
     * @param message the {@link java.lang.String} message
     */
    public static void sendRawMessage(CommandSender sender, String message)
    {

        sender.sendMessage(message);
    }

    public static void sendRawMessage(CommandSender sender, String[] messages)
    {

        sender.sendMessage(messages);
    }

    public static void sendMessage(CommandSender sender, String[] messages)
    {

        for(String s : messages)
        {
            sender.sendMessage(format(s));
        }

    }

    /**
     * Send command message.
     *
     * @param sender  the @{@link org.bukkit.command.CommandSender} sender
     * @param message the {@link java.lang.String} message
     */
    public static void sendMessage(CommandSender sender, String message)
    {

        sender.sendMessage(format(message));
    }

    /**
     * Send command error message.
     *
     * @param sender  the @{@link org.bukkit.command.CommandSender} sender
     * @param message the {@link java.lang.String} message
     */
    public static void sendErrorMessage(CommandSender sender, String message)
    {

        sender.sendMessage(errorFormat(message));
    }

    /**
     * Send command admin message.
     *
     * @param sender  the @{@link org.bukkit.command.CommandSender} sender
     * @param message the {@link java.lang.String} message
     */
    public static void sendAdminMessage(CommandSender sender, String message)
    {

        sender.sendMessage(adminFormat(message));
    }

    /**
     * Format string.
     *
     * @param message the message
     * @return the string formatted with prefix, message color and converts any other color codes in message
     */
    public static String format(String message)
    {

        return addColor(prefix + infoColor + message);
    }

    /**
     * Format string.
     *
     * @param name    the name to use in prefix
     * @param message the message
     * @return the string formatted with prefix with custom name, message color and converts any other color codes in message
     */
    public static String format(String name, String message)
    {

        return addColor("&5[&b" + name + "&5] " + message);
    }

    /**
     * Error format.
     *
     * @param message the message
     * @return the string
     */
    public static String errorFormat(String message)
    {

        return addColor(prefix + errorColor + message);
    }

    /**
     * Admin format.
     *
     * @param message the message
     * @return the string
     */
    public static String adminFormat(String message)
    {

        return addColor(prefix + adminColor + message);
    }

    /**
     * Log to console
     *
     * @param message the {@link java.lang.Object} message
     */
    public static void log(Object message)
    {

        System.out.println(message);
    }

    /**
     * Log to Minecraft Logger
     *
     * @param level   the {@link java.util.logging.Level}
     * @param message the {@link java.lang.Object} message
     */
    public static void log(Level level, Object message)
    {

        Logger.getLogger("Minecraft").log(level, message.toString());
    }

    /**
     * Log - Level INFO with color formatted.
     *
     * @param message the {@link java.lang.Object} message
     */
    public static void logInfoFormatted(Object message)
    {

        System.out.println(logPrefix + logInfoColor + removeColor(message.toString()) + RESET);
    }

    /**
     * Log - Level SEVERE with color formatted.
     *
     * @param message the {@link java.lang.Object} message
     */
    public static void logServereFormatted(Object message)
    {

        System.out.println(logPrefix + logSevereColor + removeColor(message.toString()) + RESET);
    }

    /**
     * Log - Level WARNING with color formatted.
     *
     * @param message the {@link java.lang.Object} message
     */
    public static void logWarningFormatted(Object message)
    {

        System.out.println(logPrefix + logWarningColor + removeColor(message.toString()) + RESET);
    }

    /**
     * Remove color.
     *
     * @param message the message
     * @return the {@link java.lang.String} that has color removed
     */
    public static String removeColor(String message)
    {

        return ChatColor.stripColor(message);
    }

    /**
     * Convert color.
     * Shade from Bukkit Api
     *
     * @param s {@link String} {@link String} the text to convert color chars to correct format
     * @return the {@link String} line of text which has color formatted
     */
    public static String addColor(String s)
    {

        char[] b = s.toCharArray();
        for(int i = 0 ; i < b.length - 1 ; i++)
        {
            if(b[i] == SAFE_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
            {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }

        }
        return new String(b);
    }

    /**
     * Convert color.
     * Shade from Bukkit Api
     *
     * @param s {@link String} {@link String} the text to convert color chars to correct format
     * @return the {@link String} line of text which has color formatted
     */
    public static String addAltColor(String s)
    {

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
