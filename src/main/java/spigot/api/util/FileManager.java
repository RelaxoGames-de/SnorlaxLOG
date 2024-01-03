package spigot.api.util;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import spigot.api.LOGLaxAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileManager {

        private static File datafolder;
        private static File mySQLConfig;
        private static File taskConfig;


        public static void createFiles() {
                datafolder = LOGLaxAPI.getInstance().getDataFolder();
                if (!datafolder.exists()) {
                        datafolder.mkdir();
                }

                mySQLConfig = new File(datafolder + "//database.yml");
                try {
                        if (!mySQLConfig.exists()) {
                                InputStream in = LOGLaxAPI.getInstance().getResource("database.yml");
                                Files.copy(in, mySQLConfig.toPath());
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        /**
         * Gets the IP of the MySQL Server out of the File
         *
         * @return HostIP as a String
         */
        public static String getHost() {
                File configFile = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(configFile);

                return fc.getString("MySQL.Host");

        }

        /**
         * Gets the Port of the MySQL Server out of the File
         *
         * @return The Port of the Server by default 3306
         */
        public static Integer getPort() {
                File configFile = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(configFile);

                return fc.getInt("MySQL.Port", 3306);

        }

        /**
         * Gets the Database Name of the MySQL Server out of the File
         *
         * @return Database as a String
         */
        public static String getDatabase() {
                File configFile = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(configFile);

                return fc.getString("MySQL.Database");

        }

        /**
         * Gets the Username of the MySQL User out of the File
         *
         * @return Username as a String
         */
        public static String getUser() {
                File configFile = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(configFile);

                return fc.getString("MySQL.User");

        }

        /**
         * Gets the Password of the MySQL Useraccount out of the File
         *
         * @return The Password of the MySQL User Account
         */
        public static String getPassword() {
                File configFile = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(configFile);

                return fc.getString("MySQL.Password");

        }
        public static String getUsersProfileTable(){
                File config = getMySQLConfig();
                FileConfiguration fc;
                fc = YamlConfiguration.loadConfiguration(config);

                return fc.getString("DatabaseTables.SnorlaxUserProfile", "snorlax_users_profile");
        }

        public static File getDatafolder() {
                return datafolder;
        }

        public static File getMySQLConfig() {
                return mySQLConfig;
        }

        public static File getTaskConfig() {
                return taskConfig;
        }

        public static void deleteFiles() {
                if (datafolder != null || datafolder.exists()) {
                        datafolder.delete();
                }
        }
}
