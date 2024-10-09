package de.relaxogames.SnorlaxLOG.shared.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to connect to the database.
 * It is used to manage the DB Connection and to
 * make better use of the database.
 * 
 * @version 1.0
 * @since 2.0
 */
public class DBConnector {

    /**
     * The connection to the database.
     * 
     * @since 2.0
     */
    private Connection connection;
    
    /**
     * The host of the database.
     * 
     * @since 2.0
     */
    private final String HOST;
    
    /**
     * The port of the database.
     * 
     * @since 2.0
     */
    private final String PORT;
    
    /**
     * The path of the database.
     * 
     * @since 2.0
     */
    private final String DATABASE_PATH;
    
    /**
     * The user of the database.
     * 
     * @since 2.0
     */
    private final String USER;
    
    /**
     * The password of the database.
     * 
     * @since 2.0
     */
    private final String PASSWORD;

    /**
     * Creates a new DBConnector with the given parameters.
     * 
     * @param host The host of the database.
     * @param port The port of the database.
     * @param databasePath The path of the database.
     * @param user The user of the database.
     * @param password The password of the database.
     * @since 2.0
     */
    public DBConnector(String host, String port, String databasePath, String user, String password) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE_PATH = databasePath;
        this.USER = user;
        this.PASSWORD = password;
    }

    /**
     * Connects to the database.
     * 
     * @throws SQLException If the connection could not be established.
     * @since 2.0
     */
    public void connect() throws SQLException {
        if (this.isConnected()) return;
        String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE_PATH;
        connection = DriverManager.getConnection(url, USER, PASSWORD);
    }

    /**
     * Disconnects from the database.
     * 
     * @throws SQLException If the connection could not be closed.
     * @since 2.0
     */
    public void disconnect() throws SQLException {
        if (!this.isConnected()) return;
        connection.close();
    }

    /**
     * Returns if the connection is still active and usable.
     * 
     * @return If the connection is active.
     * @since 2.0
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Executes the given query.
     * 
     * @param query The query to execute.
     * @throws SQLException If the query could not be executed.
     * @since 2.0
     */
    public void execute(String query) throws SQLException {
        connection.createStatement().execute(query);
    }

    /**
     * Returns the connection to the database.
     * 
     * @return The connection to the database.
     * @since 2.0
     */
    public Connection getConnection() {
        return connection;
    }
}
