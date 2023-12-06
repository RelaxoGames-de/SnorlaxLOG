package de.snorlaxlog;

import de.snorlaxlog.commands.OnlineTimeCommand;
import de.snorlaxlog.commands.SnorlaxLOGCommand;
import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.FileManager;
import de.snorlaxlog.files.PermissionShotCut;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.listener.JoinListener;
import de.snorlaxlog.listener.KickEvent;
import de.snorlaxlog.listener.QuitListener;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Level;

public final class SnorlaxLOG extends Plugin {
    private static SnorlaxLOG instance;
    private static String version = "inDev-1.7";
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
        mySQL.close();
    }

    private void loadMySQL(){

        String host = FileManager.getHost();
        String user = FileManager.getUser();
        Integer port = FileManager.getPort();
        String password = FileManager.getPassword();
        String database = FileManager.getDatabase();

        if (host == null || user == null || port == null || password == null || database == null){
            onDisable();
            return;
        }

        mySQL = new MySQL(host, port, database, user, password);
        SQLManager.initializeDatabase();
    }

    private void registerListener(){
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new QuitListener());
        pm.registerListener(this, new KickEvent());
    }

    private void registerCommands(){
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new OnlineTimeCommand());
        pm.registerCommand(this, new SnorlaxLOGCommand());
    }
    public static void logMessage(Level level, String message){
        ProxyServer.getInstance().getLogger().log(level, message);
        for (LOGPlayer logPlayer : SnorlaxLOGCommand.getLogPlayers().keySet()){
            Level logLVL = logPlayer.getNotifyLevel();
            if (logLVL.equals(level)){
                logPlayer.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + "§b§l[" + logLVL.getName() + "]§r " + message);
                return;
            }
        }
    }

    public static SnorlaxLOG getInstance() {
        return instance;
    }

    public static String getVersion() {
        return version;
    }
}
