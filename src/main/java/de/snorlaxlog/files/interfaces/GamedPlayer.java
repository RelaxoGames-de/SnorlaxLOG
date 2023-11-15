package de.snorlaxlog.files.interfaces;

import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.LanguageManager;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GamedPlayer implements GamePlayer {

    private SQLManager sqlManager = new SQLManager();

    private ProxiedPlayer player;

    public GamedPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    /** Returns the actually ProxiedPlayer
     */
    @Override
    public ProxiedPlayer getPlayer() {
        return player;
    }

    /**
     * The hasPermission Method is used to check via LuckPerms if a Player has
     * a specified Permission. As result a boolean will be given back.
     * @param permission is the that will be checked
     * @return
     */
    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    /**
     * This Code is used to send the Player a message of a language File (prepared Messages)
     * if you want to send the player a custom message, please use the sendMessage() method below.
     * @param fileKey is the KEY of the message wich are deposited in the language Files
     */
    @Override
    public void sendMessage(String fileKey) {
        String msg = LanguageManager.getMessage(language(), fileKey);
        getPlayer().sendMessage(new TextComponent(CommandPrefix.getRelaxoBanPrefix() + msg));
    }

    /**
     * This Code is used to send the Player a custom message
     * @param message is the KEY of the message wich are deposited in the language Files
     */
    @Override
    public void sendMessage(TextComponent message) {
        TextComponent msg = new TextComponent(CommandPrefix.getRelaxoBanPrefix());
        msg.addExtra(message);
        getPlayer().sendMessage(msg);
    }

    /**
     * This Method uses the MySQL Database to get the Local/Language settings of the Player
     * @return the Local of a Player
     */
    @Override
    public String language() {
        return "de_DE";
    }
}
