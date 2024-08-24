package de.relaxogames.snorlaxlog.bukkit.api;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import de.relaxogames.snorlaxlog.bukkit.mysql.SQLManager;
import de.relaxogames.snorlaxlog.bukkit.util.FileManager;

/**
 * APIBukkitManager class responsible for managing the LOGLax API, SQLManager,
 * and FileManager instances.
 * Provides static methods to access SQLManager, LOGLaxAPI, and FileManager
 * objects.
 */
public class APIBukkitManager {
    private static final LOGLaxAPI logLaxAPI = LOGLaxAPI.getInstance();
    private static final FileManager fileManager = new FileManager();

    /**
     * Returns a new instance of SQLManager.
     * 
     * @return a fresh instance of the SQL Manager Class
     */
    public static SQLManager getSqlManager() {
        return new SQLManager();
    }

    /**
     * Returns the LOGLax API instance.
     * 
     * @return the current LogLaxAPI Instance
     */
    public static LOGLaxAPI getLogLaxAPI() {
        return logLaxAPI;
    }

    /**
     * Returns the instance of FileManager used for managing files within the Bukkit
     * API.
     * 
     * @return the current FileManager Instance
     */
    public static FileManager getFileManager() {
        return fileManager;
    }
}