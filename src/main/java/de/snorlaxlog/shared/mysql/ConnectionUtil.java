package de.snorlaxlog.shared.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {

    /**
     * This Method checks if an open connection is valid for 5 seconds
     * @param connection is the open connection that should be checked
     * @return 'true' if the connection is open
     */
    public static boolean isConnectionValid(Connection connection) {
        try {
            return connection.isValid(5);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
