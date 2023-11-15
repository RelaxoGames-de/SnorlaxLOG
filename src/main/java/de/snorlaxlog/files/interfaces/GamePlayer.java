package de.snorlaxlog.files.interfaces;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface GamePlayer {

    ProxiedPlayer getPlayer();
    boolean hasPermission(String permission);
    String language();
    void sendMessage(String fileKey);
    void sendMessage(TextComponent message);

}
