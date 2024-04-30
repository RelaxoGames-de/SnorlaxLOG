package de.relaxogames.snorlaxlog.bukkit.channels;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.relaxogames.snorlaxlog.bukkit.commands.abrax.AbraxWarpUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class PluginChannelMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("warpui")) {
            String data = in.readUTF();

            if (data.startsWith("getServerResponse")) {
                AbraxWarpUI.handleResponse(data);
                return;
            }
        }
    }
}