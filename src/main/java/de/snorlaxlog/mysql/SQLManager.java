package de.snorlaxlog.mysql;

import de.snorlaxlog.Main;
import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.files.interfaces.PlayerEntryData;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class SQLManager {

    private static String userDataTable = FileManager.getUsersProfileTable();
    private static String database_path = FileManager.getDatabase();

    private static Connection con = Main.getInstance().mySQL.openConnection();

    /**
     * Will be triggered by the Server startup logic.
     * It initializes the MySQL Database.
     */
    public static void initializeDatabase(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = Main.getInstance().mySQL.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            String createUserCacheTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
            try (PreparedStatement statement = Main.getInstance().mySQL.getConnection().prepareStatement(createUserCacheTable)) {
                statement.execute();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public boolean isInDatabase(ProxiedPlayer player){

        this.checkCon();

        String sql = SQLQuery.SELECT_EVERYTHING_WHERE_UUID_A_N.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = Main.getInstance().mySQL.getConnection().prepareStatement(sql)){

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            ResultSet rs = statement.executeQuery();

            return rs.next();

        }catch (SQLException e){
            Main.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }
    }

    public void addEntry(ProxiedPlayer player){

        this.checkCon();

        String sql = SQLQuery.ADD_ENTRY_TO_DATABASE.getSql().replace("%DATABASE_PATH%", database_path).replace("%TABLE_NAME_STANDARD%", userDataTable);
        try (PreparedStatement statement = Main.getInstance().mySQL.getConnection().prepareStatement(sql)){

            Date today = new Date();
            Timestamp firstSeen = new Timestamp(today.getTime());

            statement.setString(1, player.getUUID());
            statement.setString(2, player.getName());
            statement.setTimestamp(3, firstSeen);
            statement.setNull(4, Types.TIMESTAMP);
            statement.setNull(5, Types.VARCHAR);
            statement.setNull(6, Types.VARCHAR);
            statement.setTimestamp(7, new Timestamp(0, 0, 0, 0, 0, 0, 0));
            statement.setString(8, "de_DE");
            statement.setString(9, player.getAddress().getHostName());

            statement.execute();

        }catch (SQLException e){
            Main.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

    }

    public UUID getUUIDThroughName(String name){

        this.checkCon();

        UUID uuid;

        String sql = SQLQuery.SELECT_USER_UUID_WITH_NAME.getSql();
        try (PreparedStatement statement = Main.getInstance().mySQL.getConnection().prepareStatement(sql)){
            statement.setString(1, name.toString());

            ResultSet rs = statement.executeQuery();
            uuid = UUID.fromString(rs.getString(PlayerEntryData.USER_UUID.getTableColumnName()));
        }catch (SQLException e){
            Main.logMessage(Level.OFF, CommandPrefix.getConsolePrefix() + "Methode getUUIDThroughName() is not supported?!");
            throw new RuntimeException(e);
        }

        return uuid;
    }


    private void checkCon(){

        try {
            if (!ConnectionUtil.isConnectionValid(con) || con == null || con.isClosed()) {
                con = Main.getInstance().mySQL.openConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
