package de.snorlaxlog.files;

import de.snorlaxlog.SnorlaxLOG;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;
import org.checkerframework.checker.units.qual.C;

import java.sql.Connection;

public class APIManager {

    private static Connection databaseConnection = SnorlaxLOG.getInstance().mySQL.getConnection();
    private static SQLManager sqlManager = new SQLManager();
    private static LanguageManager languageManager = new LanguageManager();

    public static Connection getMySQL() {
        return databaseConnection;
    }

    public static SQLManager getSqlManager() {
        return sqlManager;
    }

    public static LanguageManager getLanguageManager() {
        return languageManager;
    }
}
