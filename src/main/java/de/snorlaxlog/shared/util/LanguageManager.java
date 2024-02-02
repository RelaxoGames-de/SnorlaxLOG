package de.snorlaxlog.shared.util;

import de.snorlaxlog.bungeecord.files.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {


    /** This Class returns messages using the Message list from the FileManager.java
     */

    private static Map<String, Map<String, String>> messages = FileManager.getMessageList();

    /**
     * Gets a Message using a specified local to return the specified message in a language
     * @param locale locale that specifies in which language the message will be returned
     * @param fileKey The File key, how the Message is called in the language Files
     * @return returns the converted message as a String
     */
    @Deprecated
    public static String getMessage(String locale, String fileKey){

        return messages.getOrDefault(locale, messages.get(Language.system_default.getInitials())).getOrDefault(fileKey, "KEY NOT SET!");
    }

    /**
     * Gets a Message using a specified local to return the specified message in a language
     * @param language locale that specifies in which language the message will be returned
     * @param fileKey The File key, how the Message is called in the language Files
     * @return returns the converted message as a String
     */
    public static String getMessage(Language language, String fileKey){
        return messages.getOrDefault(language.getInitials(), messages.get(Language.system_default.getInitials())).getOrDefault(fileKey, "KEY NOT SET!");
    }

    public static void loadMessage(){
        if (de.snorlaxlog.bukkit.util.FileManager.getLangFolder().listFiles().length > 0) {
            for (File file : de.snorlaxlog.bukkit.util.FileManager.getLangFolder().listFiles()) {
                Map<String, String> localeMessages = new HashMap<>();

                FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
                for (String key : lang.getKeys(false)) {
                    for (String messName : lang.getConfigurationSection(key).getKeys(false )) {
                        String message = ChatColor.translateAlternateColorCodes('&', lang.getString(key + "." + messName));
                        localeMessages.put(messName, message);
                    }
                }
                String fileName = file.getName().split(".yml")[0];
                messages.put(fileName, localeMessages);
                System.out.println(file.getName() + " loaded!");
            }
        }
    }

}
