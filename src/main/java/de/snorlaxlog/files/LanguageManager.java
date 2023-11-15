package de.snorlaxlog.files;

import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {


    /** This Class returns messages using the Message list from the FileManager.class
     */

    private static Map<String, Map<String, String>> messages = FileManager.getMessageList();

    /** In this list are the Player Locals saved. If the Player is not in the List, the MySQL Database will be selected. The Answer of the MySQL-Database
     * will be saved in this HashMap.
     */
    private static HashMap<ProxiedPlayer, String> playerlocals = new HashMap<>();


    /**
     * Gets a Message using the Player to get his locale to return the specified message in the player's language
     * @param player The Player that will receive the message
     * @param message The File key, how the Message is called in the language Files
     * @return returns the converted message as a String
     *
     */
    public static String getMessage(ProxiedPlayer player, String message){
        LOGPlayer LOGPlayer = new LOGGEDPlayer(player);
        if (!playerlocals.containsKey(player)){
            playerlocals.put(player, LOGPlayer.language());
        }
        String locale = playerlocals.getOrDefault(player, "de_DE");
        return messages.getOrDefault(locale, messages.get("de_DE")).getOrDefault(message, "KEY NOT SET!");
    }

    /**
     * Gets a Message using a specified local to return the specified message in a language
     * @param locale locale that specifies in which language the message will be returned
     * @param fileKey The File key, how the Message is called in the language Files
     * @return returns the converted message as a String
     */
    public static String getMessage(String locale, String fileKey){

        return null;
    }


}
