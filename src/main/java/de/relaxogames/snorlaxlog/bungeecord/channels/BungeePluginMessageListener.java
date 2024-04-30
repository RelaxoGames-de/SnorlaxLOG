package de.relaxogames.snorlaxlog.bungeecord.channels;

import de.relaxogames.snorlaxlog.bungeecord.commands.abrax.WarpUICommand;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeePluginMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("BungeeCord")) return;
    
        event.setCancelled(true);
    
        // Converting the byte array data into a DataInputStream
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
    
        try {
            String subChannel = in.readUTF();
    
            if (subChannel.equals("warpui")) {
                String data = in.readUTF();

                if (data.startsWith("getServers")) {
                    WarpUICommand.handleGetServerRequest(data);
                    return;
                }

                WarpUICommand.handleWarpUIMessage(data);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}