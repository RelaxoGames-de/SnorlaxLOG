package de.snorlaxlog.shared.util;

import de.snorlaxlog.bungeecord.files.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageManager {

    /** This Class returns messages using the Message list from the FileManager.java
     */
    private static final Map<String, Map<String, String>> messages = new HashMap<>();

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
    public static String getMessage(Language language, String fileKey) {
        return messages.getOrDefault(language.getInitials(), messages.get(Language.system_default.getInitials())).getOrDefault(fileKey, "KEY NOT SET!");
    }

    public static void loadBukkitMessage(String fileName, Map<String, String> localeMessages){
        messages.put(fileName, localeMessages);
    }

    public static void loadBungeeMessage() {
        /* This Code Section gets all the Keys and Messages of the Language Files and put them in a HashMap.
         */
        for (File file : Objects.requireNonNull(FileManager.getLangFolder().listFiles())) {
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
            messages.put(fileName, localeMessages);
        }
    }
}
