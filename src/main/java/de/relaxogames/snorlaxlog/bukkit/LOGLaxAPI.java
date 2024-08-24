package de.relaxogames.snorlaxlog.bukkit;

import de.relaxogames.snorlaxlog.bukkit.channels.PluginChannelMessageListener;
import de.relaxogames.snorlaxlog.bukkit.commands.abrax.WarpUICommand;
import de.relaxogames.snorlaxlog.bukkit.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.bukkit.mysql.MySQL;
import de.relaxogames.snorlaxlog.bukkit.mysql.SQLManager;
import de.relaxogames.snorlaxlog.bukkit.ui.InventoryManagerClickListener;
import de.relaxogames.snorlaxlog.bukkit.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public class LOGLaxAPI extends JavaPlugin {

    private static LOGLaxAPI instance;
    public de.relaxogames.snorlaxlog.bukkit.mysql.MySQL mysql;
    private HashMap<String, CachedPlayer> allCachedPlayersByName;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.startEssentials();
        this.CommandRegistration();
        this.ListenerRegistration();
        this.onStartUp();
    }

    @Override
    public void onDisable() {
        mysql.close();
        FileManager.deleteFiles();
    }

    public void CommandRegistration() {
        getCommand("warpui").setExecutor(new WarpUICommand());
    }

    public void ListenerRegistration() {
        getServer().getPluginManager().registerEvents(new InventoryManagerClickListener(), this);
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord",
                new PluginChannelMessageListener());
    }

    public void startEssentials() {
        FileManager.createFiles();
        this.loadMySQL();
        SQLManager.initializeDatabase();
    }

    public void onStartUp() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    /**
     * Logs a message with the specified log level.
     * 
     * @param level   the log level of the message
     * @param message the message to be logged
     */
    public static void logMessage(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }

    public void loadMySQL() {

        String host = FileManager.getHost();
        int port = FileManager.getPort();
        String database = FileManager.getDatabase();
        String user = FileManager.getUser();
        String password = FileManager.getPassword();

        mysql = new MySQL(host, port, database, user, password);
        if (mysql.getConnection() == null || !mysql.hasConnection()) {
            logMessage(Level.OFF, "Disabling SnorlaxLOG-API cause the MySQL-Connection has been lost!");
            this.onDisable();
        }
    }

    /**
     * Returns a HashMap containing all CachedPlayer objects stored by their names.
     * 
     * @return HashMap<String, CachedPlayer> - a mapping of player names to
     *         CachedPlayer objects
     */
    public HashMap<String, CachedPlayer> getAllCachedPlayersByName() {
        return allCachedPlayersByName;
    }

    /**
     * Returns the instance of the LOGLaxAPI class.
     * 
     * @return LOGLaxAPI - the instance of the LOGLaxAPI class
     */
    public static LOGLaxAPI getInstance() {
        return instance;
    }

    public static SQLManager getBukkitLogSQL(){
        return new SQLManager();
    }
}
