package de.snorlaxlog.listener;

import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(LoginEvent e){
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.getConnection().getUniqueId());
        LOGPlayer logPlayer = new LOGGEDPlayer(player);


    }

}
