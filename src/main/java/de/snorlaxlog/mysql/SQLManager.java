package de.snorlaxlog.mysql;

import de.snorlaxlog.Main;
import de.snorlaxlog.files.FileManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

            String createPunishTable = SQLQuery.CREATE_MYSQL_USER_CACHE.getSql().replace("%TABLE_PUNISH_NAME%", punishmentTable).replace("%PUNISHMENT_DATABASE%", database_path);
            try (PreparedStatement statement = Main.getInstance().mySQL.getConnection().prepareStatement(createPunishTable)) {
                statement.execute();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

}
