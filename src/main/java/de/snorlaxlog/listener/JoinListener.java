package de.snorlaxlog.listener;

import de.snorlaxlog.files.LanguageManager;
import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.files.interfaces.OnlineTimeManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    private OnlineTimeManager onlineTimeManager = new OnlineTimeManager();
    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();
        if (player == null)return;
        LOGPlayer logPlayer = new LOGGEDPlayer(player);

        if (!logPlayer.isInDatabase()){
            logPlayer.addPlayerEntry();
        }

        onlineTimeManager.stampIn(logPlayer);
        logPlayer.updatePlayerProfile();

        if (!onlineTimeManager.isCaptured(logPlayer)){
            //ERROR #607
            player.disconnect(LanguageManager.getMessage(logPlayer.language(), "kickScreen").replace("%REASON%", "Caching Onlintime! Error #607"));
            return;
        }
    }

}
