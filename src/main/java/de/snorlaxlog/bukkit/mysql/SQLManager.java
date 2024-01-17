package de.snorlaxlog.bukkit.mysql;

import de.snorlaxlog.bukkit.interfaces.LOGPlayer;
import de.snorlaxlog.shared.mysql.ConnectionUtil;
import de.snorlaxlog.shared.mysql.SQLQuery;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.PlayerEntryData;
import de.snorlaxlog.shared.util.Language;
import org.bukkit.entity.Player;
import de.snorlaxlog.bukkit.LOGLaxAPI;
import de.snorlaxlog.bukkit.interfaces.CachedPlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SQLManager {

    private static String userDataTable = de.snorlaxlog.bukkit.util.FileManager.getUsersProfileTable();
    private static String database_path = de.snorlaxlog.bukkit.util.FileManager.getDatabase();

    private static Connection con = LOGLaxAPI.getInstance().mysql.openConnection();

    /**
     * Will be triggered by the Server startup logic.
     * It initializes the MySQL Database.
     */
    public static void initializeDatabase(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = LOGLaxAPI.getInstance().mysql.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            String createUserCacheTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
            try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(createUserCacheTable)) {
                statement.execute();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public boolean isInDatabase(Player player){

        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            return rs.next();

        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    public void addEntry(Player player){

        this.checkCon();

        String sql = SQLQuery.ADD_ENTRY_TO_DATABASE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

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
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

    }

    public UUID getUUIDThroughName(String name){

        this.checkCon();

        if (name == null)return null;

        UUID uuid = null;

        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
            }
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }
    public String getCorrectNameFromLOWERCASE(String index){

        checkCon();
        String sql = SQLQuery.SELECT_USER_WITH_L_NAME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1, index.toLowerCase());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("user_name");
            }
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
        return null;
    }

    public long getSavedOnlineTime(Player player){
        this.checkCon();

        String sql = SQLQuery.SELECT_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())return 0;

            return rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
        }catch (SQLException e){
            //ERROR #502
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public Timestamp getLastSeen(String name){
        this.checkCon();
        UUID uuid = getUUIDThroughName(getCorrectNameFromLOWERCASE(name.toLowerCase()));
        if (uuid == null){
            return null;
        }

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)){

            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                return rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public de.snorlaxlog.bukkit.interfaces.CachedPlayer getPlayerInfos(String name){
        this.checkCon();

        UUID uuid = getUUIDThroughName(name);
        if (uuid == null){
            return null;
        }

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)){

            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                UUID uuid1 = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
                String name1 = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
                Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
                Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
                String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
                String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
                long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
                String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
                String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

                return new de.snorlaxlog.bukkit.interfaces.CachePlayer(name1, uuid1, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
            }
            return null;
        }catch (SQLException e){
            //ERROR #502
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public de.snorlaxlog.bukkit.interfaces.CachedPlayer getPlayerInfos(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer){
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)){

            statement.setString(1, logPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
                String name = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
                Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
                Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
                String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
                String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
                long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
                String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
                String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

                return new de.snorlaxlog.bukkit.interfaces.CachePlayer(name, uuid, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
            }
            return null;
        }catch (SQLException e){
            //ERROR #502
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerProfileName(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer, String newName){

        de.snorlaxlog.bukkit.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (newName.length() < 3 || cachedPlayer.getName().equals(newName)){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_NAME.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, newName);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
         }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerProfileLastSeen(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer){

        de.snorlaxlog.bukkit.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        if (cachedPlayer.getLastOnline().equals(timestamp) && cachedPlayer.getLastOnline() != null){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setTimestamp(1, timestamp);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerSetting(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer, PlayerEntryData index, String newValue){
        if (index.equals(PlayerEntryData.USER_UUID))return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerSetting(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer, PlayerEntryData index, Integer newValue){
        if (index.equals(PlayerEntryData.USER_UUID))return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setInt(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerSetting(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer, PlayerEntryData index, Timestamp newValue){
        if (index.equals(PlayerEntryData.USER_UUID))return;
        String sql = SQLQuery.UPDATE_USER_ONLINE_TIME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", index.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setTimestamp(1, newValue);
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'updatePlayerSetting();'");
            throw new RuntimeException(e);
        }
    }
    public void updatePlayerProfileIP(de.snorlaxlog.bukkit.interfaces.LOGPlayer logPlayer){

        CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

        if (cachedPlayer.getUUID().equals(logPlayer.getUUIDFromDatabase())){
            return;
        }

        this.checkCon();

        String sql = SQLQuery.UPDATE_USER_PROFILE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable).replace("%ENTRY_DATA%", PlayerEntryData.USER_CACHED_IP.getTableColumnName());
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, logPlayer.getUUIDFromDatabase().toString());
            statement.setString(2, logPlayer.getPlayer().getUniqueId().toString());

            statement.execute();
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQLException in SnorlaxLOG in Method: 'setSavedOnlineTime();'");
            throw new RuntimeException(e);
        }
    }
    public static List<CachedPlayer> getAllCachedPlayers() {
        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = LOGLaxAPI.getInstance().mysql.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = SQLQuery.SELECT_ALL_ENTRIES.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            return ResultSetConverter.convertResultSetToList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void checkCon(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = LOGLaxAPI.getInstance().mysql.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
