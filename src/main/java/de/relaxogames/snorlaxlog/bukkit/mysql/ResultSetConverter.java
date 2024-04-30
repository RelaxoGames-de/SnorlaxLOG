package de.relaxogames.snorlaxlog.bukkit.mysql;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResultSetConverter {

    public static List<CachedPlayer> convertResultSetToList(ResultSet resultSet) throws SQLException {
        List<CachedPlayer> playerList = new ArrayList<>();
        while (resultSet.next()) {
            UUID uuid1 = UUID.fromString(resultSet.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
            String name1 = resultSet.getString(PlayerEntryData.USER_NAME.getTableColumnName());
            Timestamp firstJoin = resultSet.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
            Timestamp lastJoin = resultSet.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            String discordID = resultSet.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
            String forumID = resultSet.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
            long onlineTime = resultSet.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
            String language = resultSet.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
            String ip = resultSet.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

            CachedPlayer cached = new de.relaxogames.snorlaxlog.bukkit.interfaces.CachePlayer(name1, uuid1, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
            playerList.add(cached);
            LOGLaxAPI.getInstance().getAllCachedPlayersByName().put(name1, cached);
        }
        return playerList;
    }

}
