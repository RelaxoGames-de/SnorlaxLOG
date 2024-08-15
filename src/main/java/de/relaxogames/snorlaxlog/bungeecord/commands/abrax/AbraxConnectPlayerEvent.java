package de.relaxogames.snorlaxlog.bungeecord.commands.abrax;

import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class AbraxConnectPlayerEvent extends Event implements Cancellable {
    private final ProxiedPlayer connectedPlayer;
    private final LOGPlayer connectedLogPlayer;
    private final String serverName;
    private final ServerInfo originServer;
    private final ServerInfo relocatedServer;
    private String connectMessage;
    private String abortMessage;
    private boolean canceled = false;

    public AbraxConnectPlayerEvent(ProxiedPlayer connectedPlayer, LOGPlayer connectedLogPlayer, String serverName,
            ServerInfo originServer, ServerInfo relocatedServer, String connectMessage, String abortMessage) {
        this.connectedPlayer = connectedPlayer;
        this.connectedLogPlayer = connectedLogPlayer;
        this.serverName = serverName;
        this.originServer = originServer;
        this.relocatedServer = relocatedServer;
        this.connectMessage = connectMessage;
        this.abortMessage = abortMessage;
    }

    /**
     * Indicates whether the event is canceled.
     *
     * @return true if the event is canceled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets the cancellation status of the event.
     *
     * @param b the boolean value to set for the cancellation status
     */
    @Override
    public void setCancelled(boolean b) {
        canceled = b;
    }

    /**
     * Retrieves the connected player associated with this event.
     *
     * @return the ProxiedPlayer object representing the connected player
     */
    public ProxiedPlayer getConnectedPlayer() {
        return connectedPlayer;
    }

    /**
     * Retrieves the connected LOGPlayer associated with this event.
     *
     * @return the LOGPlayer object representing the connected LOGPlayer
     */
    public LOGPlayer getConnectedLogPlayer() {
        return connectedLogPlayer;
    }

    /**
     * Retrieves the name of the server associated with this event.
     *
     * @return the name of the server as a String
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Retrieves the origin server associated with this event.
     *
     * @return the ServerInfo object representing the origin server
     */
    public ServerInfo getOriginServer() {
        return originServer;
    }

    /**
     * Retrieves the relocated server associated with this event.
     *
     * @return the ServerInfo object representing the relocated server
     */
    public ServerInfo getRelocatedServer() {
        return relocatedServer;
    }

    /**
     * Retrieves the connect message associated with this event.
     *
     * @return the connect message as a String
     */
    public String getConnectMessage() {
        return connectMessage;
    }

    /**
     * Retrieves the abort message associated with this event.
     *
     * @return the abort message as a String
     */
    public String getAbortMessage() {
        return abortMessage;
    }

    /**
     * Sets the connect message for this event.
     *
     * @param connectMessage the message to set as the connect message
     */
    public void setConnectMessage(String connectMessage) {
        this.connectMessage = connectMessage;
    }

    /**
     * Sets the abort message for this event.
     *
     * @param abortMessage the message to set as the abort message
     */
    public void setAbortMessage(String abortMessage) {
        this.abortMessage = abortMessage;
    }
}