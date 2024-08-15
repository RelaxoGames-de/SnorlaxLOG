package de.relaxogames.snorlaxlog.bungeecord.files;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.mysql.SQLManager;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;

import java.sql.Connection;

public class APIManager {
    private static final Connection databaseConnection = SnorlaxLOG.getInstance().mySQL.getConnection();
    private static final SQLManager sqlManager = new SQLManager();
    private static final LanguageManager languageManager = new LanguageManager();

    /**
     * Returns the MySQL database connection.
     * 
     * @return The current DB Connection
     */
    public static Connection getMySQL() {
        return databaseConnection;
    }

    /**
     * Returns the SQLManager instance.
     * 
     * @return The SQLManager instance
     */
    public static SQLManager getSqlManager() {
        return sqlManager;
    }

    /**
     * Returns the LanguageManager instance.
     * 
     * @return The LanguageManager instance
     */
    public static LanguageManager getLanguageManager() {
        return languageManager;
    }
}