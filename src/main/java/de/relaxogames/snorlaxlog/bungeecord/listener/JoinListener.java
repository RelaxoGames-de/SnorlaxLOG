package de.relaxogames.snorlaxlog.bungeecord.listener;

import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();
        if (player == null) return;
        LOGPlayer logPlayer = new LOGGEDPlayer(player);

        if (!logPlayer.isInDatabase()) logPlayer.addPlayerEntry();

        logPlayer.updatePlayerProfile();

        if (logPlayer.getLastJoinTime() == System.currentTimeMillis() || logPlayer.getLastJoinTime() == new Date().getTime()){
            //ERROR #607
            player.disconnect(LanguageManager.getMessage(logPlayer.language(), "kickScreen").replace("%REASON%", "Caching Onlintime! Error #607"));
            return;
        }

        player.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "LanguageSelected"));
    }
}