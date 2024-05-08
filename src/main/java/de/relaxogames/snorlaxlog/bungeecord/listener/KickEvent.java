package de.relaxogames.snorlaxlog.bungeecord.listener;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

public class KickEvent implements Listener {
    @EventHandler
    public void onKick(ServerKickEvent e){
        ProxiedPlayer p = e.getPlayer();
        SnorlaxLOG.logMessage(Level.INFO, p.getName() + " got kicked from " + e.getKickedFrom() + " with the reason: " + e.getKickReason());
    }
}