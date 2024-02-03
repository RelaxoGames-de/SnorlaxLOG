package de.snorlaxlog.bukkit.api;

import de.snorlaxlog.bukkit.LOGLaxAPI;
import de.snorlaxlog.bukkit.mysql.SQLManager;
import de.snorlaxlog.bukkit.util.FileManager;
import eu.cloudnetservice.modules.bridge.node.player.NodePlayerManager;

public class APIBukkitManager {
    private static LOGLaxAPI logLaxAPI = LOGLaxAPI.getInstance();
    private static FileManager fileManager = new FileManager();
    private static SQLManager sqlManager = new SQLManager();
    private static NodePlayerManager playerManager;

    public static NodePlayerManager getPlayerManager() {
        return playerManager;
    }

    public static void setPlayerManager(NodePlayerManager playerManager) {
        APIBukkitManager.playerManager = playerManager;
    }

    public static SQLManager getSqlManager() {
        return sqlManager;
    }

    public static LOGLaxAPI getLogLaxAPI() {
        return logLaxAPI;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }
}
