package de.snorlaxlog.bungeecord.files.interfaces;

import java.sql.Timestamp;
import java.util.UUID;

public interface CachedPlayer {

    int getID();
    String getName();
    UUID getUUID();
    Timestamp getFirstJoin();
    Timestamp getLastOnline();
    String getDiscordID();
    String getForumID();
    long getOnlineTime();
    String getLanguage();
    String getIP();

}
