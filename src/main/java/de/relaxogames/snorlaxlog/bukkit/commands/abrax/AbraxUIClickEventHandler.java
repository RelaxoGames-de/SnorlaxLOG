package de.relaxogames.snorlaxlog.bukkit.commands.abrax;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AbraxUIClickEventHandler {
    public static void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            Player player = Bukkit.getPlayer(event.getWhoClicked().getName());

            String serverName = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getLocalizedName().replace("snorlaxlog.ui.inventorymanager.warpui.", "");

            dataOutputStream.writeUTF("warpui");
            dataOutputStream.writeUTF((player != null ? player.getName() : null) + ":" + serverName);

            player.sendPluginMessage(LOGLaxAPI.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());

            event.getWhoClicked().closeInventory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}