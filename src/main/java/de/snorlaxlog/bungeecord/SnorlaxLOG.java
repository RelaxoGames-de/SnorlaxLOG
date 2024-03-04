package de.snorlaxlog.bungeecord;

import de.snorlaxlog.bungeecord.channels.BungeePluginMessageListener;
import de.snorlaxlog.bungeecord.commands.abrax.PermissionCommand;
import de.snorlaxlog.bungeecord.commands.abrax.WarpCommand;
import de.snorlaxlog.bungeecord.commands.abrax.WarpUICommand;
import de.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.snorlaxlog.bungeecord.listener.JoinListener;
import de.snorlaxlog.bungeecord.listener.KickEvent;
import de.snorlaxlog.bungeecord.listener.QuitListener;
import de.snorlaxlog.bungeecord.commands.*;
import de.snorlaxlog.bungeecord.mysql.SQLManager;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.bungeecord.files.FileManager;
import de.snorlaxlog.shared.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Level;

public final class SnorlaxLOG extends Plugin {
    private static SnorlaxLOG instance;
    private static String version;
    public MySQL mySQL;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        version = getInstance().getDescription().getVersion();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        if (version == null) {
            logMessage(Level.WARNING, CommandPrefix.getConsolePrefix() + "Plugin version is null! Disabling SnorlaxLOG");
            this.onDisable();
        }

        de.snorlaxlog.bungeecord.files.FileManager.loadFiles();
        this.loadMySQL();
        this.registerListener();
        this.registerCommands();

        getProxy().registerChannel("BungeeCord");
        getProxy().getPluginManager().registerListener(this, new BungeePluginMessageListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQL.close();
    }

    private void loadMySQL() {
        String host = FileManager.getHost();
        String user = FileManager.getUser();
        Integer port = FileManager.getPort();
        String password = FileManager.getPassword();
        String database = FileManager.getDatabase();

        if (host == null || user == null || port == null || password == null || database == null) {
            logMessage(Level.WARNING, CommandPrefix.getConsolePrefix() + "Could not open Connection to the MySQL Database! Disabling SnorlaxLOG!");
            onDisable();
            return;
        }

        mySQL = new MySQL(host, port, database, user, password);
        SQLManager.initializeDatabase();
    }

    private void registerListener() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new QuitListener());
        pm.registerListener(this, new KickEvent());
    }

    private void registerCommands() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new OnlineTimeCommand());
        pm.registerCommand(this, new SnorlaxLOGCommand());
        pm.registerCommand(this, new WarpCommand());
        pm.registerCommand(this, new PermissionCommand());
    }

    public static void logMessage(Level level, String message) {
        ProxyServer.getInstance().getLogger().log(level, message);
        for (LOGPlayer logPlayer : SnorlaxLOGCommand.getLogPlayers().keySet()) {
            Level logLVL = logPlayer.getNotifyLevel();
            if (logLVL.equals(level)) {
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
