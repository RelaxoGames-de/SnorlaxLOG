package de.relaxogames.snorlaxlog.bukkit.api;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import de.relaxogames.snorlaxlog.bukkit.mysql.SQLManager;
import de.relaxogames.snorlaxlog.bukkit.util.FileManager;

public class APIBukkitManager {
    private static final LOGLaxAPI logLaxAPI = LOGLaxAPI.getInstance();
    private static final FileManager fileManager = new FileManager();

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