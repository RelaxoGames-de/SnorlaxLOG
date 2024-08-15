package de.relaxogames.snorlaxlog.bungeecord.mysql;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.files.FileManager;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.CachePlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.bungeecord.util.Converters;
import de.relaxogames.snorlaxlog.shared.mysql.ConnectionUtil;
import de.relaxogames.snorlaxlog.shared.mysql.SQLQuery;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SQLManager {
    private static final String userDataTable = FileManager.getUsersProfileTable();
    private static final String database_path = FileManager.getDatabase();

    private static Connection con = SnorlaxLOG.getInstance().mySQL.openConnection();

    /**
     * Will be triggered by the Server startup logic.
     * It initializes the MySQL Database.
     */
    @SuppressWarnings("deprecation")
    public static void initializeDatabase() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed())
                con = SnorlaxLOG.getInstance().mySQL.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String createUserCacheTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql()
                    .replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
            try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection()
                    .prepareStatement(createUserCacheTable)) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Checks if the specified ProxiedPlayer is present in the database.
     *
     * @param player the ProxiedPlayer to check in the database
     * @return true if the player is found in the database, false otherwise
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public boolean isInDatabase(ProxiedPlayer player) {
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new entry for the specified ProxiedPlayer to the database.
     *
     * @param player the ProxiedPlayer for which the entry is added
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    @SuppressWarnings("deprecation")
    public void addEntry(ProxiedPlayer player) {
        this.checkCon();

        String sql = SQLQuery.ADD_ENTRY_TO_DATABASE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
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
            statement.setString(10, player.getAddress().getAddress().getHostAddress());

            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the UUID of a player based on the player's name from the database.
     *
     * @param name the name of the player
     * @return the UUID of the player if found, otherwise null
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public UUID getUUIDThroughName(String name) {
        this.checkCon();

        UUID uuid = null;

        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, name.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getColumnPlace()));
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }

    /**
     * Retrieves the correct username from the database based on the provided index
     * in lowercase.
     *
     * @param index the index to search for in the database
     * @return the correct username corresponding to the index in lowercase, or null
     *         if not found
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public String getCorrectNameFromLOWERCASE(String index) {
        this.checkCon();
        String sql = SQLQuery.SELECT_USER_WITH_L_NAME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, index.toLowerCase());

            ResultSet rs = statement.executeQuery();
            while (rs.next())
                return rs.getString("user_name");
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Retrieves the saved online time of a player from the database.
     *
     * @param player the ProxiedPlayer object for which the online time is retrieved
     * @return the saved online time of the player, or 0 if not found
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public long getSavedOnlineTime(ProxiedPlayer player) {
        this.checkCon();

        String sql = SQLQuery.SELECT_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            ResultSet rs = statement.executeQuery();
            if (!rs.next())
                return 0;
            return rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
        } catch (SQLException e) {
            // ERROR #502
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves player information from the database based on the provided
     * LOGPlayer object.
     *
     * @param logPlayer the LOGPlayer object representing the player
     * @return a CachedPlayer object containing the player's information if found,
     *         otherwise null
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public CachedPlayer getPlayerInfos(LOGPlayer logPlayer) {
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, logPlayer.getPlayer().getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (!rs.next())
                return null;
            String uuid = rs.getString(PlayerEntryData.USER_UUID.getTableColumnName());
            String name = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
            Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
            Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
            String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
            long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
            String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
            String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

            return new CachePlayer(uuid, name, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
        } catch (SQLException e) {
            // ERROR #502
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves player information from the database based on the provided index.
     *
     * @param index the index to search for in the database
     * @return a CachedPlayer object containing the player's information if found,
     *         otherwise null
     * @throws RuntimeException if an SQL exception occurs during the process
     */
    public CachedPlayer getPlayerInfos(String index) {
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_NAME_A_N.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, index);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return null;
            String uuid = rs.getString(PlayerEntryData.USER_UUID.getTableColumnName());
            String name = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
            Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
            Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
            String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
            long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
            String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
            String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

            return new CachePlayer(uuid, name, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
        } catch (SQLException e) {
            // ERROR #502
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the saved online time of a player in the database.
     *
     * @param logPlayer     the LOGPlayer object representing the player
     * @param newOnlineTime the new online time to be set for the player
     */
    public void setSavedOnlineTime(LOGPlayer logPlayer, long newOnlineTime) {
        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setLong(1, newOnlineTime);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.setString(3, logPlayer.getPlayer().getName());
            statement.execute();
            if (getSavedOnlineTime(logPlayer.getPlayer()) == newOnlineTime) {
                SnorlaxLOG.logMessage(Level.INFO,
                        CommandPrefix.getConsolePrefix() + "Successfully saved onlinetime of user: [name: "
                                + logPlayer.getPlayer().getName() + "] [uuid: "
                                + logPlayer.getPlayer().getUniqueId().toString() + "]");
                return;
            }
            SnorlaxLOG.logMessage(Level.WARNING,
                    CommandPrefix.getConsolePrefix() + "Could not save onlinetime of user: [name: "
                            + logPlayer.getPlayer().getName() + "] [uuid: "
                            + logPlayer.getPlayer().getUniqueId().toString() + "]. New Onlinetime: " + newOnlineTime);
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the player's profile name in the database if the new name is valid
     * and different from the current name.
     *
     * @param logPlayer the LOGPlayer object representing the player
     * @param newName   the new name to be set for the player's profile
     */
    public void updatePlayerProfileName(LOGPlayer logPlayer, String newName) {
        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (newName.length() < 3 || cachedPlayer.getName().equals(newName))
            return;

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_NAME.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the last seen timestamp of a player's profile in the database.
     *
     * @param logPlayer the LOGPlayer object representing the player
     */
    public void updatePlayerProfileLastSeen(LOGPlayer logPlayer) {
        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        if (cachedPlayer.getLastOnline().equals(timestamp) && cachedPlayer.getLastOnline() != null)
            return;
        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, timestamp);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the User setting of the player in the database with a String value.
     *
     * @param logPlayer the LOGPlayer object representing the player
     * @param index     the PlayerEntryData representing the setting to be updated
     * @param newValue  the new String value to be set for the setting
     */
    public void updatePlayerSetting(LOGPlayer logPlayer, PlayerEntryData index, String newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the User setting of the player in the database with an Integer value.
     *
     * @param logPlayer the LOGPlayer object representing the player
     * @param index     the PlayerEntryData representing the setting to be updated
     * @param newValue  the new Integer value to be set for the setting
     */
    public void updatePlayerSetting(LOGPlayer logPlayer, PlayerEntryData index, Integer newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setInt(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the User settings of the player in the database.
     *
     * @param logPlayer the LOGPlayer object representing the player
     * @param index     the PlayerEntryData representing the setting to be updated
     * @param newValue  the new value to be set for the setting
     */
    public void updatePlayerSetting(LOGPlayer logPlayer, PlayerEntryData index, Timestamp newValue) {
        if (index.equals(PlayerEntryData.USER_UUID))
            return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix()
                    + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the User save of the player in the database.
     *
     * @param logPlayer the LOGPlayer object representing the player
     */
    public void updatePlayerProfileIP(LOGPlayer logPlayer) {
        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (cachedPlayer.getIP().equals(logPlayer.getUserIP()))
            return;

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable)
                .replace("%ENTRY_DATA%", PlayerEntryData.USER_CACHED_IP.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            statement.setString(1, logPlayer.getUserIP());
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the validity of the current database connection.
     * If the connection is not valid, closed, or null, opens a new connection using
     * SnorlaxLOG instance.
     * Throws a RuntimeException if a SQLException occurs during the process.
     */
    @SuppressWarnings("deprecation")
    private void checkCon() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed())
                con = SnorlaxLOG.getInstance().mySQL.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of all the player names from the database.
     *
     * @return a List of player names
     */
    public List<String> getPlayerNames() {
        this.checkCon();
        String sql = SQLQuery.SELECT_ALL_ENTRIES.getSql().replace("%DATABASE_PATH%", database_path)
                .replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            return Converters.getNamesFromResultSet(rs);
        } catch (SQLException e) {
            SnorlaxLOG.logMessage(Level.OFF,
                    CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'getPlayerNames();'");
            throw new RuntimeException(e);
        }
    }
}