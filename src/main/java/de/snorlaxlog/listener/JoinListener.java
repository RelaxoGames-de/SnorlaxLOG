package de.snorlaxlog.listener;

import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();
        LOGPlayer logPlayer = new LOGGEDPlayer(player);

        if (!logPlayer.isInDatabase()){
            logPlayer.addPlayerEntry();
        }
    }

}
