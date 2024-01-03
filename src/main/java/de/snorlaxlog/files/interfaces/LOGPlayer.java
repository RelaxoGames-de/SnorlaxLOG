package de.snorlaxlog.files.interfaces;

import api.shared.util.Language;
import api.shared.util.PlayerEntryData;
import de.snorlaxlog.files.PermissionShotCut;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.logging.Level;
public interface LOGPlayer {

    ProxiedPlayer getPlayer();
    void addPlayerEntry();
    UUID getUUIDFromDatabase();
    String getNameFromDatabase();
    boolean isInDatabase();
    String getUserIP();
    void changePlayerEntry(PlayerEntryData setting, String newValue);

    void changePlayerEntry(PlayerEntryData setting, Integer newValue);

    String getPlayerEntry(PlayerEntryData setting);
    boolean hasPermission(PermissionShotCut permission);
    Language language();
    void sendMessage(String fileKey);
    void sendMessage(TextComponent message);
    void logEntry(Level level, String loggingMessage);
    Long getOnlineTime();
    void updateOnlineTime();
    void updatePlayerProfile();
    CachedPlayer getCachedPlayer();
    long getLastJoinTime();
    boolean notifyIsActive();
    @Deprecated
    void activateNotify(Level level);
    @Deprecated
    void disableNotify();
    void toggleNotify(Level level);
    Level getNotifyLevel();
}
