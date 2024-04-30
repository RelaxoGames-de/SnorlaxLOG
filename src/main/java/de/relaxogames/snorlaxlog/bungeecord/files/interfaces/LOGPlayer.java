package de.relaxogames.snorlaxlog.bungeecord.files.interfaces;

import de.relaxogames.snorlaxlog.shared.PermissionShotCut;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
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
    boolean hasPermission(String permission);
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
