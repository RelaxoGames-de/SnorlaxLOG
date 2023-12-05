package de.snorlaxlog.mysql;

import de.snorlaxlog.SnorlaxLOG;
import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.files.interfaces.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class SQLManager {

    private static String userDataTable = FileManager.getUsersProfileTable();
    private static String database_path = FileManager.getDatabase();

    private static Connection con = SnorlaxLOG.getInstance().mySQL.openConnection();

    /**
     * Will be triggered by the Server startup logic.
     * It initializes the MySQL Database.
     */
    public static void initializeDatabase(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = SnorlaxLOG.getInstance().mySQL.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            String createUserCacheTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
            try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(createUserCacheTable)) {
                statement.execute();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public boolean isInDatabase(ProxiedPlayer player){

        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            return rs.next();

        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    public void addEntry(ProxiedPlayer player){

        this.checkCon();

        String sql = SQLQuery.ADD_ENTRY_TO_DATABASE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

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

        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

    }

    public UUID getUUIDThroughName(String name){

        this.checkCon();

        UUID uuid;

        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){
            statement.setString(1, name.toString());

            ResultSet rs = statement.executeQuery();
            uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }
    public long getSavedOnlineTime(ProxiedPlayer player){
        this.checkCon();

        String sql = SQLQuery.SELECT_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())return 0;

            return rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
        }catch (SQLException e){
            //ERROR #502
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public CachedPlayer getPlayerInfos(LOGPlayer logPlayer){
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, logPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
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
            }
            return null;
        }catch (SQLException e){
            //ERROR #502
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public void setSavedOnlineTime(LOGPlayer logPlayer, long newOnlineTime){
        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setLong(1, newOnlineTime);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());
            statement.setString(3, logPlayer.getPlayer().getName());

            statement.execute();

            if (getSavedOnlineTime(logPlayer.getPlayer()) == newOnlineTime) {
                SnorlaxLOG.logMessage(Level.INFO, CommandPrefix.getConsolePrefix() + "Successfully saved onlinetime of user: [name: " + logPlayer.getPlayer().getName() + "] [uuid: " + logPlayer.getPlayer().getUniqueId().toString() + "]");
                return;
            }
            SnorlaxLOG.logMessage(Level.WARNING, CommandPrefix.getConsolePrefix() + "Could not save onlinetime of user: [name: " + logPlayer.getPlayer().getName() + "] [uuid: " + logPlayer.getPlayer().getUniqueId().toString() + "]. New Onlinetime: " + newOnlineTime);
        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerProfileName(LOGPlayer logPlayer, String newName){

        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (newName.length() < 3 || cachedPlayer.getName().equals(newName)){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_NAME.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, newName);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
         }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerProfileLastSeen(LOGPlayer logPlayer){

        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        if (cachedPlayer.getLastOnline().equals(timestamp) && cachedPlayer.getLastOnline() != null){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setTimestamp(1, timestamp);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    //TODO: Implementierung für Updating der Discord und Forums IDs.

    public void updatePlayerProfileIP(LOGPlayer logPlayer){

        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (cachedPlayer.getIP().equals(logPlayer.getUserIP())){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_CACHED_IP.getTableColumnName());
        try (PreparedStatement statement = SnorlaxLOG.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, logPlayer.getUserIP());
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            SnorlaxLOG.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    private void checkCon(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = SnorlaxLOG.getInstance().mySQL.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
