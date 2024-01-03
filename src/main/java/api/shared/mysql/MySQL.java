package api.shared.mysql;

import api.shared.util.CommandPrefix;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection con;
    private String HOST;
    private int PORT;
    private String DATABASE_PATH;
    private String USER;
    private String PASSWORD;

    public MySQL(String host, int port, String database, String user, String password) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE_PATH = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    /**
     * Opens the Connection to the Database and
     * returns the current Connection
     * @return The opened Connection
     */
    public Connection openConnection(){
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_PATH;
        try {
            con = DriverManager.getConnection(url + "?autoReconnect=true", USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    /** Using the connect() method the Server conncets to a defined MySQL server. */
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_PATH + "?autoReconnect=true", USER, PASSWORD);
            ProxyServer.getInstance().getConsole().sendMessage(" ");
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\tMySQL - Datenbank wurde erfolgreich initialisiert!"));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\tDas System ist jetzt einsatzbereit!\n"));
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(" ");
        } catch (SQLException e) {
            e.printStackTrace();
            ProxyServer.getInstance().getConsole().sendMessage(" ");
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\tKonnte keine Verbindung mit MySQL - Datenbank herstellen"));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\tSollte dies ein Fehler sein, starte bitte das System neu!"));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\t" + ChatColor.RED + "Fehler: " + e.getMessage() + "\n"));
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(" ");
        }
    }

    /** This Method closes the MySQL-Database connection */
    public void close() {
        try {
            if (con != null) {
                con.close();
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\n"));
                ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\tVerbindung zur MySQL - Datenbank abgebrochen!"));
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\tSollte dies ein Fehler sein, starte bitte das System neu!!\n"));
                ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\n"));
            }
        } catch (SQLException e) {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\n"));
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\tKonnte keine Verbindung mit MySQL - Datenbank herstellen"));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\tSollte dies ein Fehler sein, starte bitte das System neu!"));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\t" + ChatColor.RED + "Fehler: " + e.getMessage() + "\n"));
            ProxyServer.getInstance().getConsole().sendMessage(CommandPrefix.getAnnouncePrefix());
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("\n\n"));
        }
    }

    /**
     * This method checks the MySQL-Database connection
     * @return if the connection is opend or not.
     */
    public boolean hasConnection() {
        if (this.con != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets the Connection
     * @return the connection itself.
     */
    public Connection getConnection() {
        return con;
    }
}