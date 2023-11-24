package de.snorlaxlog.files.interfaces;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
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
    String getPlayerEntry(PlayerEntryData setting);
    boolean hasPermission(String permission);
    String language();
    void sendMessage(String fileKey);
    void sendMessage(TextComponent message);
    void logEntry(Level level, String loggingMessage);
    Long getOnlineTime();
    void updateOnlineTime();
    void updatePlayerProfile();
}
