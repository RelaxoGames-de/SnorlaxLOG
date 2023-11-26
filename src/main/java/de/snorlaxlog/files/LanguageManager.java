package de.snorlaxlog.files;

import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.Language;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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


}
