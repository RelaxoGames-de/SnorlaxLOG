package de.relaxogames.snorlaxlog.bukkit.mysql;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.shared.mysql.ConnectionUtil;
import de.relaxogames.snorlaxlog.shared.mysql.SQLQuery;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;

public class SQLManager {
    private static final String userDataTable = de.relaxogames.snorlaxlog.bukkit.util.FileManager
            .getUsersProfileTable();
    private static final String database_path = de.relaxogames.snorlaxlog.bukkit.util.FileManager.getDatabase();

    private static Connection con = LOGLaxAPI.getInstance().mysql.openConnection();

    /**
     * Will be triggered by the Server startup logic.
     * It initializes the MySQL Database.
     */
    @SuppressWarnings("deprecation")
    public static void initializeDatabase() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed())
                con = LOGLaxAPI.getInstance().mysql.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String createUserCacheTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql()
                    .replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
            try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection()
                    .prepareStatement(createUserCacheTable)) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the given player is present in the database.
     * 
     * @param player The player to check in the database.
     * @return true if the player is found in the database, false otherwise.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public boolean isInDatabase(Player player) {
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new entry for the specified player to the database.
     * 
     * @param player The player for whom the entry is being added.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void addEntry(Player player) {
        this.checkCon();
        String sql = SQLQuery.ADD_ENTRY_TO_DATABASE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            Date today = new Date();
            Timestamp firstSeen = new Timestamp(today.getTime());

            statement.setNull(1, Types.INTEGER);
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, player.getName());
            statement.setTimestamp(4, firstSeen);
            statement.setTimestamp(5, firstSeen);
            statement.setNull(6, Types.VARCHAR);
            statement.setNull(7, Types.VARCHAR);
            statement.setLong(8, 0);
            statement.setString(9, Language.system_default.getInitials());
            statement.setString(10, Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());

            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the UUID of a player based on their name from the database.
     * 
     * @param name The name of the player to retrieve the UUID for.
     * @return The UUID of the player if found, otherwise null.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public UUID getUUIDThroughName(String name) {
        this.checkCon();

        if (name == null)
            return null;

        UUID uuid = null;
        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);

        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }

    /**
     * Retrieves the correct user name from the database based on the provided index
     * in lowercase.
     * 
     * @param index The index to search for in lowercase.
     * @return The correct user name if found, otherwise null.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public String getCorrectNameFromLOWERCASE(String index) {
        checkCon();
        String sql = SQLQuery.SELECT_USER_WITH_L_NAME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, index.toLowerCase());
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString("user_name");
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
        return null;
    }
    public String getNameThroughUUID(UUID uuid){
        checkCon();

        if (uuid == null)return null;
        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, uuid.toString());
            try (ResultSet set = statement.executeQuery()){
                if (set.next()){
                    return getCorrectNameFromLOWERCASE(set.getString("user_name"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Retrieves the saved online time for a specific player from the database.
     * 
     * @param player The player for whom the online time is being retrieved.
     * @return The total online time in milliseconds for the player, or 0 if no
     *         record is found.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public long getSavedOnlineTime(Player player) {
        this.checkCon();

        String sql = SQLQuery.SELECT_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            ResultSet rs = statement.executeQuery();
            if (!rs.next())
                return 0;
            return rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
        } catch (SQLException e) {
            // ERROR #502
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the timestamp of the last time a player was seen based on their
     * name.
     * 
     * @param name The name of the player to retrieve the last seen timestamp for.
     * @return The timestamp of the last seen time for the player, or null if the
     *         player is not found.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public Timestamp getLastSeen(String name) {
        this.checkCon();
        UUID uuid = getUUIDThroughName(getCorrectNameFromLOWERCASE(name.toLowerCase()));
        if (uuid == null)
            return null;
        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            while (rs.next())
                return rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Retrieves player information from the database based on the player's name.
     * 
     * @param name The name of the player to retrieve information for.
     * @return A CachedPlayer object containing the player's details if found,
     *         otherwise null.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer getPlayerInfos(String name) {
        this.checkCon();

        UUID uuid = getUUIDThroughName(name);
        if (uuid == null)
            return null;

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            UUID uuid1 = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
            String name1 = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
            Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
            Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
            String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
            long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
            String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
            String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

            return new de.relaxogames.snorlaxlog.bukkit.interfaces.CachePlayer(name1, uuid1, firstJoin, lastJoin,
                    discordID, forumID, onlineTime, language, ip);
        } catch (SQLException e) {
            // ERROR #502
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves player information from the database based on the provided
     * LOGPlayer object.
     * 
     * @param logPlayer The LOGPlayer object for which information is being
     *                  retrieved.
     * @return A CachedPlayer object containing the player's details if found,
     *         otherwise null.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer getPlayerInfos(
            de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer) {
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, logPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            UUID uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
            String name = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
            Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
            Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
            String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
            long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
            String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
            String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

            return new de.relaxogames.snorlaxlog.bukkit.interfaces.CachePlayer(name, uuid, firstJoin, lastJoin,
                    discordID, forumID, onlineTime, language, ip);
        } catch (SQLException e) {
            // ERROR #502
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the profile name of a player in the database.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose profile
     *                  name is being updated.
     * @param newName   The new name to set for the player's profile.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void updatePlayerProfileName(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer,
            String newName) {
        de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (newName.length() < 3 || cachedPlayer.getName().equals(newName))
            return;

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_NAME.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the last seen timestamp of a player's profile in the database.
     * Retrieves the player information using the provided LOGPlayer object,
     * compares the last online timestamp with the current timestamp,
     * and updates the database if they are different.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose last seen
     *                  timestamp is being updated.
     */
    public void updatePlayerProfileLastSeen(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer) {
        de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        if (cachedPlayer.getLastOnline().equals(timestamp) && cachedPlayer.getLastOnline() != null)
            return;

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, timestamp);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a specific player setting in the database.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose setting
     *                  is being updated.
     * @param index     The type of setting to update.
     * @param newValue  The new value to set for the player's setting.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void updatePlayerSetting(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer,
            PlayerEntryData index, String newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a specific player setting in the database.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose setting
     *                  is being updated.
     * @param index     The type of setting to update.
     * @param newValue  The new value to set for the player's setting.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void updatePlayerSetting(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer,
            PlayerEntryData index, Integer newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setInt(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a specific player setting in the database.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose setting
     *                  is being updated.
     * @param index     The type of setting to update.
     * @param newValue  The new value to set for the player's setting.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void updatePlayerSetting(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer,
            PlayerEntryData index, Timestamp newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the player's profile IP in the database.
     * Retrieves the cached player information based on the provided LOGPlayer
     * object,
     * compares the UUIDs, and updates the IP if they are different.
     * 
     * @param logPlayer The LOGPlayer object representing the player whose profile
     *                  IP is being updated.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    public void updatePlayerProfileIP(de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer) {
        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (cachedPlayer.getUUID().equals(logPlayer.getUUIDFromDatabase()))
            return;

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_CACHED_IP.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)) {
            statement.setString(1, logPlayer.getUUIDFromDatabase().toString());
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            LOGLaxAPI.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all cached players from the database.
     * 
     * @return A list of CachedPlayer objects representing all cached players.
     * @throws RuntimeException if an SQL exception occurs during the database
     *                          query.
     */
    @SuppressWarnings("deprecation")
    public static List<CachedPlayer> getAllCachedPlayers() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed())
                con = LOGLaxAPI.getInstance().mysql.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = SQLQuery.SELECT_ALL_ENTRIES.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            return ResultSetConverter.convertResultSetToList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the validity of the current database connection.
     * If the connection is not valid, closed, or null, it reopens the connection
     * using LOGLaxAPI.
     * 
     * @throws RuntimeException if an SQL exception occurs during the connection
     *                          check or reopening.
     */
    @SuppressWarnings("deprecation")
    private void checkCon() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed())
                con = LOGLaxAPI.getInstance().mysql.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}