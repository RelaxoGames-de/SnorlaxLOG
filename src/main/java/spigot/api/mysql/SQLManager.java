package spigot.api.mysql;

import api.shared.mysql.ConnectionUtil;
import api.shared.mysql.SQLQuery;
import api.shared.util.CommandPrefix;
import api.shared.util.PlayerEntryData;
import de.snorlaxlog.files.FileManager;
import api.shared.util.Language;
import org.bukkit.entity.Player;
import spigot.api.LOGLaxAPI;
import spigot.api.interfaces.CachedPlayer;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class SQLManager {

    private static String userDataTable = FileManager.getUsersProfileTable();
    private static String database_path = FileManager.getDatabase();

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

        UUID uuid = null;

        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){
            statement.setString(1, name.toString());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getColumnPlace()));
            }
        }catch (SQLException e){
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }
    public String getCorrectNameFromLOWERCASE(String index){

        this.checkCon();
        String sql = SQLQuery.SELECT_USER_WITH_L_NAME.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){
            statement.setString(1, index.toLowerCase());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
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

    public spigot.api.interfaces.CachedPlayer getPlayerInfos(spigot.api.interfaces.LOGPlayer logPlayer){
        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = LOGLaxAPI.getInstance().mysql.getConnection().prepareStatement(sql)){

            statement.setString(1, logPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(PlayerEntryData.USER_ID.getTableColumnName());
                UUID uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
                String name = rs.getString(PlayerEntryData.USER_NAME.getTableColumnName());
                Timestamp firstJoin = rs.getTimestamp(PlayerEntryData.USER_FIRST_JOINED.getTableColumnName());
                Timestamp lastJoin = rs.getTimestamp(PlayerEntryData.USER_LAST_JOINED.getTableColumnName());
                String discordID = rs.getString(PlayerEntryData.USER_LINKS_DISCORD.getTableColumnName());
                String forumID = rs.getString(PlayerEntryData.USER_LINKS_FORUM.getTableColumnName());
                long onlineTime = rs.getLong(PlayerEntryData.USER_ONLINE_TIME.getTableColumnName());
                String language = rs.getString(PlayerEntryData.USER_LANGUAGE.getTableColumnName());
                String ip = rs.getString(PlayerEntryData.USER_CACHED_IP.getTableColumnName());

                return new spigot.api.interfaces.CachePlayer(id, name, uuid, firstJoin, lastJoin, discordID, forumID, onlineTime, language, ip);
            }
            return null;
        }catch (SQLException e){
            //ERROR #502
            LOGLaxAPI.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "SQL query 'getSavedOnlineTime' is not working! ERROR #502");
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerProfileName(spigot.api.interfaces.LOGPlayer logPlayer, String newName){

        spigot.api.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

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

    public void updatePlayerProfileLastSeen(spigot.api.interfaces.LOGPlayer logPlayer){

        spigot.api.interfaces.CachedPlayer cachedPlayer = getPlayerInfos(logPlayer);

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

    public void updatePlayerSetting(spigot.api.interfaces.LOGPlayer logPlayer, PlayerEntryData index, String newValue){
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

    public void updatePlayerSetting(spigot.api.interfaces.LOGPlayer logPlayer, PlayerEntryData index, Integer newValue){
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

    public void updatePlayerSetting(spigot.api.interfaces.LOGPlayer logPlayer, PlayerEntryData index, Timestamp newValue){
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
    public void updatePlayerProfileIP(spigot.api.interfaces.LOGPlayer logPlayer){

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
