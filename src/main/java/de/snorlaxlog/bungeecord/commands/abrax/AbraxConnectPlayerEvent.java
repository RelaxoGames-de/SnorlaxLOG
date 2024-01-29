package de.snorlaxlog.bungeecord.commands.abrax;

import de.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class AbraxConnectPlayerEvent extends Event implements Cancellable {

    private ProxiedPlayer connectedPlayer;
    private LOGPlayer connectedLogPlayer;
    private String serverName;
    private ServerInfo originServer;
    private ServerInfo relocatedServer;
    private String connectMessage;
    private String abortMessage;
    private boolean canceled = false;

    public AbraxConnectPlayerEvent(ProxiedPlayer connectedPlayer, LOGPlayer connectedLogPlayer, String serverName, ServerInfo originServer, ServerInfo relocatedServer, String connectMessage, String abortMessage) {
        this.connectedPlayer = connectedPlayer;
        this.connectedLogPlayer = connectedLogPlayer;
        this.serverName = serverName;
        this.originServer = originServer;
        this.relocatedServer = relocatedServer;
        this.connectMessage = connectMessage;
        this.abortMessage = abortMessage;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        canceled = b;
    }

    public ProxiedPlayer getConnectedPlayer() {
        return connectedPlayer;
    }

    public LOGPlayer getConnectedLogPlayer() {
        return connectedLogPlayer;
    }

    public String getServerName() {
        return serverName;
    }

    public ServerInfo getOriginServer() {
        return originServer;
    }

    public ServerInfo getRelocatedServer() {
        return relocatedServer;
    }

    public String getConnectMessage() {
        return connectMessage;
    }

    public String getAbortMessage() {
        return abortMessage;
    }

    public void setConnectMessage(String connectMessage) {
        this.connectMessage = connectMessage;
    }

    public void setAbortMessage(String abortMessage) {
        this.abortMessage = abortMessage;
    }
}
