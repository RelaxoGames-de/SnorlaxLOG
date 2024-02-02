package de.snorlaxlog.bukkit.ui;

import de.snorlaxlog.bukkit.LOGLaxAPI;
import de.snorlaxlog.bukkit.commands.abrax.AbraxUIClickEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InventoryManagerClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem().getItemMeta().getLocalizedName().startsWith("snorlaxlog.ui.inventorymanager.warpui.")) AbraxUIClickEventHandler.handleClick(event);
    }
}