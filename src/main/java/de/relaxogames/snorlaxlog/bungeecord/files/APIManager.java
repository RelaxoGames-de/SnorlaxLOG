package de.relaxogames.snorlaxlog.bungeecord.files;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.mysql.SQLManager;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;

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