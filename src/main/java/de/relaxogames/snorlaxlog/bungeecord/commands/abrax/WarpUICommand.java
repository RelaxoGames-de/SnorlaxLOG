package de.relaxogames.snorlaxlog.bungeecord.commands.abrax;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.relaxogames.snorlaxlog.shared.PermissionShortCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class WarpUICommand {
    public static void handleWarpUIMessage(String data) {
        LOGGEDPlayer loggedPlayer = new LOGGEDPlayer(ProxyServer.getInstance().getPlayer(data.split(":")[0]));

        ServerInfo relocatedServer = ProxyServer.getInstance().getServerInfo(data.split(":")[1]);
        String serverName = data.split(":")[1].replace("-", ".");

        if (!loggedPlayer.hasPermission(PermissionShortCut.ABRAX_JOIN_SERVER_PRE.getPermission() + serverName)) {
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(loggedPlayer.language(), "NoPermForDir").replace("{SERVER}", relocatedServer.getName()));
            return;
        }

        if (relocatedServer.equals(loggedPlayer.getPlayer().getServer().getInfo())) {
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(loggedPlayer.language(), "ErrorDirEqualsOrigin"));
            return;
        }

        String msg = LanguageManager.getMessage(loggedPlayer.language(), "ConnectionSuccess").replace("{SERVER}", relocatedServer.getName());
        String abort = LanguageManager.getMessage(loggedPlayer.language(), "ErrorUnknownError");

        AbraxConnectPlayerEvent connectPlayerEvent = new AbraxConnectPlayerEvent(loggedPlayer.getPlayer(), loggedPlayer, data.split(":")[1], loggedPlayer.getPlayer().getServer().getInfo(), relocatedServer, msg, abort);
        if (!connectPlayerEvent.isCancelled()) {
            loggedPlayer.getPlayer().connect(ProxyServer.getInstance().getServerInfo(data.split(":")[1]));
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getConnectMessage());
            SnorlaxLOG.getInstance().getProxy().getPluginManager().callEvent(connectPlayerEvent);
            return;
        }

        loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getAbortMessage());
    }

    public static void handleGetServerRequest(String data) {
        final String playerName = data.split(":")[1];
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        final Map<String, ServerInfo> servers = ProxyServer.getInstance().getServersCopy();
        StringBuilder encodedResponse = new StringBuilder("getServerResponse" + ":" + playerName + ":");

        for (ServerInfo server : servers.values()) encodedResponse.append(server.getName()).append(";");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("warpui");
            dataOutputStream.writeUTF(encodedResponse.toString());

            player.getServer().getInfo().sendData("BungeeCord", byteArrayOutputStream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}