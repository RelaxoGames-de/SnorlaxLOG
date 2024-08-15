package de.relaxogames.snorlaxlog.shared.util;

import net.md_5.bungee.api.ChatColor;

public class CommandPrefix {

    public static final ChatColor SERVERCOLOR = ChatColor.BLUE;
    private static final String announcePrefix = ChatColor.GRAY + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.AQUA
            + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "»" +
            ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD + "Netzwerk"
            + ChatColor.RESET + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "«" +
            ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.AQUA + "---" + ChatColor.DARK_GRAY + "«"
            + ChatColor.GRAY + "---";

    private static final String NetworkPrefix = ChatColor.BLUE + "Netzwerk " + ChatColor.DARK_GRAY + "┃"
            + ChatColor.GRAY + " ";
    private static final String AbraxPrefix = ChatColor.BLUE + "Abrax " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY
            + " ";
    private static final String LOGPrefix = ChatColor.BLUE + "LOG " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY + " ";

    /**
     * Returns the network prefix used in the application.
     * 
     * @return the predefined Network Prefix
     */
    public static String getNetworkPrefix() {
        return NetworkPrefix;
    }

    /**
     * Returns the console prefix used in the application.
     * 
     * @return the predefined console prefix "LOG ┃ "
     */
    public static String getConsolePrefix() {
        return "LOG ┃ ";
    }

    /**
     * Returns the announce prefix used in the application.
     * 
     * @return the predefined announce prefix
     */
    public static String getAnnouncePrefix() {
        return announcePrefix;
    }

    /**
     * Returns the LOGPrefix used in the application
     * 
     * @return the LOGPrefix that was predefined
     */
    public static String getLOGPrefix() {
        return LOGPrefix;
    }

    /**
     * Returns the Abrax prefix used in the application.
     * 
     * @return the predefined Abrax Prefix
     */
    public static String getAbraxPrefix() {
        return AbraxPrefix;
    }
}
