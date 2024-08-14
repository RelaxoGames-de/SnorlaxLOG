package de.relaxogames.snorlaxlog.bukkit.mysql;

import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private Connection con;
    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;

    public MySQL(String host, int port, String database, String user, String password) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    /**
     * Opens the Connection to the Database and
     * returns the current Connection
     * 
     * @return The opened Connection
     */
    public Connection openConnection() {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

        try {
            con = DriverManager.getConnection(url + "?autoReconnect=true", USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return con;
    }

    public void connect() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage("       MySQL - Datenbank wurde erfolgreich initialisiert!");
            Bukkit.getConsoleSender()
                    .sendMessage("       Gib /admin debug mysql ein um alle wichtigen Informationen zu sehen!");
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(" ");
        } catch (SQLException e) {
            printErr(e);
        }
    }

    public void close() {
        try {
            if (con == null)
                return;

            con.close();
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage("       Verbindung zur MySQL - Datenbank abgebrochen!");
            Bukkit.getConsoleSender().sendMessage("       Sollte dies ein Fehler sein, starte bitte das System neu!");
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
            Bukkit.getConsoleSender().sendMessage(" ");
            Bukkit.getConsoleSender().sendMessage(" ");
        } catch (SQLException e) {
            printErr(e);
        }
    }

    public boolean hasConnection() {
        return this.con != null;
    }

    public Connection getConnection() {
        return con;
    }

    private void printErr(SQLException e) {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("       Konnte keine Verbindung mit MySQL - Datenbank herstellen");
        Bukkit.getConsoleSender().sendMessage("       Sollte dies ein Fehler sein, starte bitte das System neu!");
        Bukkit.getConsoleSender().sendMessage("       " + ChatColor.RED + "Fehler: " + e.getMessage());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CommandPrefix.getAnnouncePrefix());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}