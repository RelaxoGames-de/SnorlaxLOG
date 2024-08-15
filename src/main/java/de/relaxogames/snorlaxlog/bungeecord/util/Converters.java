package de.relaxogames.snorlaxlog.bungeecord.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    /**
     * Retrieves a list of user names from the provided ResultSet.
     *
     * @param resultSet the ResultSet containing user data
     * @return a list of user names extracted from the ResultSet
     */
    public static List<String> getNamesFromResultSet(ResultSet resultSet) {
        List<String> names = new ArrayList<>();
        try {
            while (resultSet.next())
                names.add(resultSet.getString("user_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
}
