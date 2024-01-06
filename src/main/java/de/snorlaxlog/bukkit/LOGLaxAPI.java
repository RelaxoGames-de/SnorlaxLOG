package de.snorlaxlog.bukkit;

import de.snorlaxlog.bukkit.interfaces.CachedPlayer;
import de.snorlaxlog.bukkit.mysql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import de.snorlaxlog.bukkit.mysql.MySQL;
import de.snorlaxlog.bukkit.util.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LOGLaxAPI extends JavaPlugin {

    private static LOGLaxAPI instance;
    public de.snorlaxlog.bukkit.mysql.MySQL mysql;
    private List<CachedPlayer> allCachedPlayers = new ArrayList<>();
    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.CommandRegistration();
        this.ListenerRegistration();
        this.onStartUp();
    }

    @Override
    public void onDisable() {
        mysql.close();
        FileManager.deleteFiles();
    }

    public void CommandRegistration(){

    }

    public void ListenerRegistration(){

    }

    public void onStartUp(){
        FileManager.createFiles();
        this.loadMySQL();
        SQLManager.initializeDatabase();
        new BukkitRunnable(){
            @Override
            public void run() {
                allCachedPlayers.addAll(SQLManager.getAllCachedPlayers());
            }
        }.runTaskAsynchronously(this);
    }

    public static void logMessage(Level level, String message){
        Bukkit.getLogger().log(level, message);
    }

    public void loadMySQL(){

        String host = FileManager.getHost();
        int port = FileManager.getPort();
        String database = FileManager.getDatabase();
        String user = FileManager.getUser();
        String password = FileManager.getPassword();

        mysql = new MySQL(host, port, database, user, password);
        if (mysql.getConnection() == null || !mysql.hasConnection()){
            logMessage(Level.OFF, "Disabling SnorlaxLOG-API cause the MySQL-Connection has been lost!");
            this.onDisable();
        }
    }

    public List<CachedPlayer> getAllCachedPlayers() {
        return allCachedPlayers;
    }

    public static LOGLaxAPI getInstance() {
        return instance;
    }
}
