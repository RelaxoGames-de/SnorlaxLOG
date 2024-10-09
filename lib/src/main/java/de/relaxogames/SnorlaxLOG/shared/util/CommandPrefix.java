package de.relaxogames.SnorlaxLOG.shared.util;

import net.md_5.bungee.api.ChatColor;

/**
 * This class is used to define the prefixes for the commands and Logs.
 * It is used to make the code more readable and to have a consistent design.
 * 
 * @version 1.0
 * @since 2.0
 * @see ChatColor
 */
public class CommandPrefix {

    /**
     * The color of the server prefix.
     * 
     * @since 2.0
     * @see ChatColor
     */
    public static final ChatColor SERVER_COLOR = ChatColor.BLUE;
    
    /**
     * The color of the error prefix.
     * 
     * @since 2.0
     * @see ChatColor
     */
    public static final ChatColor ERRROR_COLOR = ChatColor.RED;

    /**
     * The color of the success prefix for the ServerConsole or Chat.
     * 
     * @since 2.0
     * @see ChatColor
     */
    private static final String NetworkPrefix = SERVER_COLOR + "RelaxoGames " + ChatColor.RESET + ChatColor.BOLD + ChatColor.DARK_GRAY + "» " + ChatColor.RESET;

    /**
     * The color of the error prefix for the ServerConsole.
     * 
     * @since 2.0
     * @see ChatColor
     */
    private static final String ErrorPrefix = ERRROR_COLOR + "Network Error: " + ChatColor.RESET + ChatColor.BOLD + ChatColor.DARK_GRAY + "» " + ChatColor.RESET;

    /**
     * Returns the network prefix.
     * 
     * @return The network prefix.
     * @since 2.0
     */
    public static String getNetworkPrefix() {
        return NetworkPrefix;
    }

    /**
     * Returns the error prefix.
     * 
     * @return The error prefix.
     * @since 2.0
     */
    public static String getErrorPrefix() {
        return ErrorPrefix;
    }
}
