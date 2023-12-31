package de.snorlaxlog.files;

import de.snorlaxlog.SnorlaxLOG;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileManager {


    /** Creates a HashMap. In this HashMap all messages are saved. This following
     * pattern is used for this
     * @impl language = is a variable that changes from Player to Player
     * @impl key = is the name of the messages
     * @impl message = is the Message themselves with converted '&' ColorCodes
     */
    private static Map<String, Map<String, String>> messageList = new HashMap<>();

    /**
     * Is the File where all the informations about the MySQL-Connection is stored.
     */
    private static File mySQLConfig;

    /** This Method is triggered at the start of the Minecraft Server.
     * It creates the different Types of files that are used to operate.
     */
    public static void loadFiles() {

        /* This part of the Code checks if the DataFolder exists.
         * If it doesn't, it creates a new folder
         */
        File datafolder = SnorlaxLOG.getInstance().getDataFolder();
        if (!datafolder.exists()) {
            datafolder.mkdirs();
        }

        /* Creates the database.yml wich is used for the login to the MySQL Database
         */
        mySQLConfig = new File(SnorlaxLOG.getInstance().getDataFolder(), "database.yml");
        if (!mySQLConfig.exists()) {
            try (InputStream is = SnorlaxLOG.getInstance().getResourceAsStream("database.yml")) {
                Files.copy(is, mySQLConfig.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                SnorlaxLOG.logMessage(Level.WARNING,"Could not create database.yml in the BungeeCord Plugins folder!");
                throw new RuntimeException(e);
            }
        }

        /* Creates the different Types of the messages.yml and a Folder called 'languages'. In these Files every message is defined.
         */

        File langFolder = new File(SnorlaxLOG.getInstance().getDataFolder().getPath() + "//languages");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        //GERMAN FILE
        File germanFile = new File(langFolder, "de_DE.yml");
        if (!germanFile.exists()) {
            try (InputStream is = SnorlaxLOG.getInstance().getResourceAsStream("de_DE.yml")) {
                Files.copy(is, germanFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                SnorlaxLOG.logMessage(Level.WARNING,"Could not create de_DE.yml!");
                throw new RuntimeException(e);
            }
        }

        //ENGLISH FILE
        File english = new File(langFolder, "en_US.yml");
        if (!english.exists()) {
            try (InputStream is = SnorlaxLOG.getInstance().getResourceAsStream("en_US.yml")) {
                Files.copy(is, english.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                SnorlaxLOG.logMessage(Level.WARNING,"Could not create en_US.yml!");
                throw new RuntimeException(e);
            }
        }

        /* This Code Section gets all the Keys and Messages of the Language Files and put them in a HashMap.
         */
        for (File file : langFolder.listFiles()) {
            Map<String, String> localeMessages = new HashMap<>();

            Configuration lang = null;
            try {
                lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (String key : lang.getKeys()) {
                for (String messName : lang.getSection(key).getKeys()) {
                    String message = ChatColor.translateAlternateColorCodes('&', lang.getString(key + "." + messName));
                    localeMessages.put(messName, message);
                }
            }
            String fileName = file.getName().split(".yml")[0];
            messageList.put(fileName, localeMessages);
        }
    }

    /**
     * Gets the MessagesList
     * @return the MessagesList itself.
     */
    public static Map<String, Map<String, String>> getMessageList() {
        return messageList;
    }


    /**
     * Gets the IP of the MySQL Server out of the File
     * @return HostIP as a String
     */
    public static String getHost() {
        File configFile = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fc.getString("MySQL.Host");

    }

    /**
     * Gets the Port of the MySQL Server out of the File
     * @return The Port of the Server by default 3306
     */
    public static Integer getPort(){
        File configFile = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fc.getInt("MySQL.Port", 3306);

    }
    /**
     * Gets the Database Name of the MySQL Server out of the File
     * @return Database as a String
     */
    public static String getDatabase(){
        File configFile = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fc.getString("MySQL.Database");

    }
    /**
     * Gets the Username of the MySQL User out of the File
     * @return Username as a String
     */
    public static String getUser(){
        File configFile = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fc.getString("MySQL.User");

    }
    /**
     * Gets the Password of the MySQL Useraccount out of the File
     * @return The Password of the MySQL User Account
     */
    public static String getPassword(){
        File configFile = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fc.getString("MySQL.Password");

    }

    /**
     * @return The name of the Users Profile for the MySQL Database
     */
    public static String getUsersProfileTable(){
        File config = getMySQLConfig();
        Configuration fc;
        try {
            fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return fc.getString("DatabaseTables.SnorlaxUserProfile", "snorlax_users_profile");
    }

    /**
     *
     * @return The MySQL - Config File
     */
    public static File getMySQLConfig() {
        return mySQLConfig;
    }

}