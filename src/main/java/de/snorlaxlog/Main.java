package de.snorlaxlog;

import de.snorlaxlog.commands.OnlineTimeCommand;
import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.listener.JoinListener;
import de.snorlaxlog.listener.QuitListener;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends Plugin {

    private static Main instance;
    public MySQL mySQL;
    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileManager.loadFiles();
        this.loadMySQL();
        this.registerListener();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadMySQL(){

        String host = FileManager.getHost();
        String user = FileManager.getUser();
        Integer port = FileManager.getPort();
        String password = FileManager.getPassword();
        String database = FileManager.getDatabase();

        if (host == null || user == null || port == null || password == null || database == null){
            return;
        }

        mySQL = new MySQL(host, port, database, user, password);
        SQLManager.initializeDatabase();
    }

    public void registerListener(){
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new QuitListener());
    }

    public void registerCommands(){
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new OnlineTimeCommand());
    }
    public static void logMessage(Level level, String message){
        ProxyServer.getInstance().getLogger().log(level, message);
    }

    public static Main getInstance() {
        return instance;
    }
}
