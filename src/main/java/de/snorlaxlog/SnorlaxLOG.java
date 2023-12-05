package de.snorlaxlog;

import de.snorlaxlog.commands.OnlineTimeCommand;
import de.snorlaxlog.files.APIManager;
import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.listener.JoinListener;
import de.snorlaxlog.listener.QuitListener;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class SnorlaxLOG extends Plugin {

    private static SnorlaxLOG instance;
    public MySQL mySQL;
    private APIManager apiManager = new APIManager();
    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        apiManager = new APIManager();
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

    private void loadMySQL(){

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

    private void registerListener(){
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new QuitListener());
    }

    private void registerCommands(){
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new OnlineTimeCommand());
    }
    public static void logMessage(Level level, String message){
        ProxyServer.getInstance().getLogger().log(level, message);
    }

    public static SnorlaxLOG getInstance() {
        return instance;
    }

    public APIManager getApiManager() {
        return apiManager;
    }
}
