package de.snorlaxlog.listener;

import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer p = e.getPlayer();
        if (p == null)return;
        LOGPlayer lp = new LOGGEDPlayer(p);

        lp.updatePlayerProfile();
        lp.updateOnlineTime();
    }
}
