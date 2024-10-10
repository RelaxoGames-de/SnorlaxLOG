package de.relaxogames.SnorlaxLOG.shared.db;

import de.relaxogames.SnorlaxLOG.shared.model.User;
import de.relaxogames.SnorlaxLOG.shared.model.Role;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final DBConnector dbConnector;

    public UserManager(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void createTable() throws SQLException {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS users (");
        for (PlayerEntry entry : PlayerEntry.values()) {
            query.append(entry.getTableColumnName())
                 .append(" ")
                 .append(entry.getColumnType().asSQLString())
                 .append(", ");
        }
        query.setLength(query.length() - 2);
        query.append(")");

        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            stmt.execute(query.toString());
        }
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (user_id, user_uuid, user_name, user_first_join, user_last_join, user_discord_id, user_online_time, user_cached_ip, user_roles) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnector.getConnection().prepareStatement(query)) {
            stmt.setInt(1, user.getID());
            stmt.setString(2, user.getUUID());
            stmt.setString(3, user.getNAME());
            stmt.setTimestamp(4, new java.sql.Timestamp(user.getFIRST_JOIN().getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(user.getLAST_JOIN().getTime()));
            stmt.setString(6, user.getDISCORD_ID());
            stmt.setInt(7, user.getONLINE_TIME());
            stmt.setString(8, user.getCACHED_IP());
            stmt.setString(9, Role.toString(user.getROLES()));
            stmt.executeUpdate();
        }
    }

    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnector.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt(PlayerEntry.USER_ID.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_UUID.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_NAME.getColumnPosition()),
                    rs.getTimestamp(PlayerEntry.USER_FIRST_JOIN.getColumnPosition()),
                    rs.getTimestamp(PlayerEntry.USER_LAST_JOIN.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_DISCORD_ID.getColumnPosition()),
                    rs.getInt(PlayerEntry.USER_ONLINE_TIME.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_CACHED_IP.getColumnPosition()),
                    Role.fromString(rs.getString(PlayerEntry.USER_ROLES.getColumnPosition()))
                );
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement stmt = dbConnector.getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt(PlayerEntry.USER_ID.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_UUID.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_NAME.getColumnPosition()),
                    rs.getTimestamp(PlayerEntry.USER_FIRST_JOIN.getColumnPosition()),
                    rs.getTimestamp(PlayerEntry.USER_LAST_JOIN.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_DISCORD_ID.getColumnPosition()),
                    rs.getInt(PlayerEntry.USER_ONLINE_TIME.getColumnPosition()),
                    rs.getString(PlayerEntry.USER_CACHED_IP.getColumnPosition()),
                    Role.fromString(rs.getString(PlayerEntry.USER_ROLES.getColumnPosition()))
                ));
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET user_uuid = ?, user_name = ?, user_first_join = ?, user_last_join = ?, user_discord_id = ?, user_online_time = ?, user_cached_ip = ?, user_roles = ? WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnector.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getUUID());
            stmt.setString(2, user.getNAME());
            stmt.setTimestamp(3, new java.sql.Timestamp(user.getFIRST_JOIN().getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(user.getLAST_JOIN().getTime()));
            stmt.setString(5, user.getDISCORD_ID());
            stmt.setInt(6, user.getONLINE_TIME());
            stmt.setString(7, user.getCACHED_IP());
            stmt.setString(8, Role.toString(user.getROLES()));
            stmt.setInt(9, user.getID());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnector.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}