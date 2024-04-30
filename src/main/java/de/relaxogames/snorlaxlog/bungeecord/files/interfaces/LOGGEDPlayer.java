package de.relaxogames.snorlaxlog.bungeecord.files.interfaces;

import de.relaxogames.snorlaxlog.bungeecord.commands.SnorlaxLOGCommand;
import de.relaxogames.snorlaxlog.bungeecord.mysql.SQLManager;
import de.relaxogames.snorlaxlog.shared.PermissionShotCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.logging.Level;

public class LOGGEDPlayer implements LOGPlayer {
    private SQLManager sqlManager = new SQLManager();
    private ProxiedPlayer player;
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
        if (isInDatabase()) return;

        sqlManager.addEntry(player);
        ProxyServer.getInstance().getLogger().log(Level.INFO, CommandPrefix.getConsolePrefix() + "Registered a new Database entry [name:" + getPlayer().getName() + "] [uuid: " + getPlayer().getUniqueId() + "]");
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
        return player.getAddress().getHostString();
    }

    @Override
    public void changePlayerEntry(PlayerEntryData setting, String newValue) {
        sqlManager.updatePlayerSetting(this, setting, newValue);
    }

    @Override
    public void changePlayerEntry(PlayerEntryData setting, Integer newValue) {
        sqlManager.updatePlayerSetting(this, setting, newValue);
    }
    @Override
    @Deprecated
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
    public boolean hasPermission(PermissionShotCut permission) {
        UserManager um = LuckPermsProvider.get().getUserManager();
        User user = um.getUser(getPlayer().getUniqueId());

        return user.getCachedData().getPermissionData().checkPermission(permission.getPermission()).asBoolean();
    }

    /**
     * The hasPermission Method is used to check via LuckPerms if a Player has
     * a specified Permission. As result a boolean will be given back.
     * @param permission is the that will be checked
     * @return
     */
    @Override
    public boolean hasPermission(String permission) {
        UserManager um = LuckPermsProvider.get().getUserManager();
        User user = um.getUser(getPlayer().getUniqueId());

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
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
    public Long getOnlineTime() {
        return sqlManager.getSavedOnlineTime(player);
    }

    @Override
    public void updateOnlineTime() {
        long joinOn = getLastJoinTime();
        long leftOn = System.currentTimeMillis();
        long savedOnlineTime = getOnlineTime();

        long cal = (leftOn - joinOn) + savedOnlineTime;
        sqlManager.setSavedOnlineTime(this, cal);
    }


    @Override
    public void updatePlayerProfile() {
        String userName = player.getName();

        sqlManager.updatePlayerProfileIP(this);
        sqlManager.updatePlayerProfileName(this, userName);
        sqlManager.updatePlayerProfileLastSeen(this);
    }

    /**
     * This Method uses the MySQL Database to get the Local/Language settings of the Player
     * @return the Local of a Player
     */
    @Override
    public Language language() {
        return Language.convertLanguage(sqlManager.getPlayerInfos(this).getLanguage());
    }
    @Override
    public CachedPlayer getCachedPlayer(){
        return sqlManager.getPlayerInfos(this);
    }

    @Override
    public long getLastJoinTime(){
        return sqlManager.getPlayerInfos(this).getLastOnline().getTime();
    }

    @Override
    public boolean notifyIsActive() {
        return SnorlaxLOGCommand.getLogPlayers().containsKey(this);
    }

    @Override
    @Deprecated
    public void activateNotify(Level level) {
        if (notifyIsActive()) return;
        SnorlaxLOGCommand.getLogPlayers().put(this, level);
        this.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(getCachedPlayer().getLanguage(), "LogToggleNotify")
                .replace("%STATE%", "§aenabled")
                .replace("%CHANNEL%", "§r§7§o" + level.getName()));
    }

    @Override
    @Deprecated
    public void disableNotify() {
        if (!notifyIsActive()) return;
        SnorlaxLOGCommand.getLogPlayers().remove(this);
        this.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(getCachedPlayer().getLanguage(), "LogToggleNotify")
                .replace("%STATE%", "§cdisabled")
                .replace("%CHANNEL%", ""));
    }

    @Override
    public void toggleNotify(Level level) {
        if (notifyIsActive()){
            SnorlaxLOGCommand.getLogPlayers().remove(this);
            this.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(getCachedPlayer().getLanguage(), "LogToggleNotify")
                    .replace("%STATE%", "§cdisabled")
                    .replace("%CHANNEL%", "§r§7§o" + level.getName()));
            return;
        }
        SnorlaxLOGCommand.getLogPlayers().put(this, level);
        this.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(getCachedPlayer().getLanguage(), "LogToggleNotify").replace("%STATE%", "§aenabled"));
    }

    @Override
    public Level getNotifyLevel() {
        return SnorlaxLOGCommand.getLogPlayers().get(this);
    }
}
