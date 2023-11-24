package de.snorlaxlog.files;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandPrefix {

    public static final ChatColor SERVERCOLOR = ChatColor.BLUE;
    private static String announcePrefix = ChatColor.GRAY + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.AQUA + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "»" +
            ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD + "Netzwerk" + ChatColor.RESET + ChatColor.DARK_GRAY + "]"  + ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "«" +
            ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.AQUA + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.GRAY + "---";


    private static String NetworkPrefix = ChatColor.BLUE + "Netzwerk " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY + " ";
    private static String consolePrefix = "LOG ┃ ";
    private static String LOGPrefix = ChatColor.BLUE + "LOG " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY + " ";
    public static String getNetworkPrefix() {
        return NetworkPrefix;
    }
    public static String getConsolePrefix() {
        return consolePrefix;
    }

    public static String getAnnouncePrefix() {
        return announcePrefix;
    }

    public static String getLOGPrefix() {
        return LOGPrefix;
    }
}
