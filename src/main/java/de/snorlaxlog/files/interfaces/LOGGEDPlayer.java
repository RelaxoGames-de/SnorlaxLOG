package de.snorlaxlog.files.interfaces;

import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.LanguageManager;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.Level;

public class LOGGEDPlayer implements LOGPlayer {

    private SQLManager sqlManager = new SQLManager();

    private ProxiedPlayer player;
    private Long joinedMillis;

    public LOGGEDPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    /** Returns the actually ProxiedPlayer
     */
    @Override
    public ProxiedPlayer getPlayer() {
        return player;
    }

    @Override
    public void addPlayerEntry() {
        sqlManager.addEntry(player);
        ProxyServer.getInstance().getLogger().log(Level.INFO, CommandPrefix.getConsolePrefix() + "Registered a new Database entry [name:" + getPlayer().getName() + "] [uuid: " + getPlayer().getUniqueId() + "]");
        return;
    }

    @Override
    public UUID getUUIDFromDatabase() {
        return sqlManager.getUUIDThroughName(getPlayer().getName());
    }

    @Override
    public String getNameFromDatabase() {
        return null;
    }

    @Override
    public boolean isInDatabase() {
        return sqlManager.isInDatabase(player);
    }

    @Override
    public String getUserIP() {
        return player.getAddress().getHostName();
    }

    @Override
    public void changePlayerEntry(PlayerEntryData setting, String newValue) {

    }

    @Override
    public String getPlayerEntry(PlayerEntryData setting) {
        return null;
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
        getPlayer().sendMessage(new TextComponent(CommandPrefix.getLOGPrefix() + msg));
    }

    /**
     * This Code is used to send the Player a custom message
     * @param message is the KEY of the message wich are deposited in the language Files
     */
    @Override
    public void sendMessage(TextComponent message) {
        TextComponent msg = new TextComponent(CommandPrefix.getLOGPrefix());
        msg.addExtra(message);
        getPlayer().sendMessage(msg);
    }

    @Override
    public void logEntry(Level level, String loggingMessage) {

    }

    @Override
    public long getMillisOnJoin() {
        return joinedMillis;
    }

    @Override
    public void setMillisOnJoin(Long newValue) {
        joinedMillis = newValue;
    }

    @Override
    public Timestamp getOnlineTime() {
        return null;
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
