package de.relaxogames.snorlaxlog.shared.util;

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

    /**
     * This function get's all the keys and messages of the LanguageFiles and puts them into a HashMap
     */
    public static void loadBungeeMessage() {
        for (File file : Objects.requireNonNull(FileManager.getLangFolder().listFiles())) {
            try {
                Configuration lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                Map<String, String> localeMessages = new HashMap<>();
                for (String key : lang.getKeys())
                    lang.getSection(key).getKeys().forEach(messName -> {
                        String message = ChatColor.translateAlternateColorCodes('&', lang.getString(key + "." + messName));
                        localeMessages.put(messName, message);
                    });
                messages.put(file.getName().split(".yml")[0], localeMessages);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
