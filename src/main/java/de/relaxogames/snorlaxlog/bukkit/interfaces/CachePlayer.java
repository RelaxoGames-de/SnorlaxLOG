package de.relaxogames.snorlaxlog.bukkit.interfaces;

import java.sql.Timestamp;
import java.util.UUID;

public class CachePlayer implements CachedPlayer {
    String name;
    UUID uuid;
    Timestamp firstJoin;
    Timestamp lastJoin;
    String discordID;
    String forumID;
    long onlineTime;
    String language;
    String ip;

    public CachePlayer(String name, UUID uuid, Timestamp firstJoin, Timestamp lastJoin, String discordID,
            String forumID, long onlineTime, String language, String ip) {
        this.name = name;
        this.uuid = uuid;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.discordID = discordID;
        this.forumID = forumID;
        this.onlineTime = onlineTime;
        this.language = language;
        this.ip = ip;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Timestamp getFirstJoin() {
        return firstJoin;
    }

    @Override
    public Timestamp getLastOnline() {
        return lastJoin;
    }

    @Override
    public String getDiscordID() {
        return discordID;
    }

    @Override
    public String getForumID() {
        return forumID;
    }

    @Override
    public long getOnlineTime() {
        return onlineTime;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getIP() {
        return ip;
    }
}