package de.snorlaxlog.files;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandPrefix {

    public static final ChatColor SERVERCOLOR = ChatColor.BLUE;

    private static TextComponent getAnnouncePrefix = new TextComponent(ChatColor.GRAY + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.RED + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.RED + "---" + ChatColor.DARK_GRAY + "»" +
       ChatColor.DARK_RED + "---" + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + ChatColor.BOLD + "SnorlaxBan" + ChatColor.RESET + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_RED + "---" + ChatColor.DARK_GRAY + "«" +
       ChatColor.RED + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.RED + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.GRAY + "---");

    private static TextComponent getBanAnnouncePrefix = new TextComponent(ChatColor.GRAY + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.AQUA + "---" + ChatColor.DARK_GRAY + "»" + ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "»" +
            ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD + "Netzwerk" + ChatColor.RESET + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + "---" + ChatColor.DARK_GRAY + "«" +
            ChatColor.DARK_AQUA + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.AQUA + "---" + ChatColor.DARK_GRAY + "«" + ChatColor.GRAY + "---");

    private static TextComponent NetworkPrefix = new TextComponent(ChatColor.BLUE + "Netzwerk " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY + " ");

    private static TextComponent RelaxoBanPrefix = new TextComponent(ChatColor.DARK_RED + "SnorlaxBan " + ChatColor.DARK_GRAY + "┃" + ChatColor.GRAY + " ");

    public static TextComponent getAnnouncePrefix() {
        return getAnnouncePrefix;
    }

    public static TextComponent getNetworkPrefix() {
        return NetworkPrefix;
    }

    public static TextComponent getRelaxoBanPrefix() {
        return RelaxoBanPrefix;
    }

    public static TextComponent getBanAnnouncePrefix() {
        return getBanAnnouncePrefix;
    }


}
