package de.snorlaxlog.bukkit.api;

import de.snorlaxlog.bukkit.LOGLaxAPI;
import de.snorlaxlog.bukkit.mysql.SQLManager;
import de.snorlaxlog.bukkit.util.FileManager;

public class APIBukkitManager {
    private static LOGLaxAPI logLaxAPI = LOGLaxAPI.getInstance();
    private static FileManager fileManager = new FileManager();

    public static SQLManager getSqlManager() {
        return new SQLManager();
    }

    public static LOGLaxAPI getLogLaxAPI() {
        return logLaxAPI;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }
}
