package de.snorlaxlog;

import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.plugin.Plugin;

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


    public static Main getInstance() {
        return instance;
    }
}
