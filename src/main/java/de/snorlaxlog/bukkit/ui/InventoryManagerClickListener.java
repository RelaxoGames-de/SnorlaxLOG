package de.snorlaxlog.bukkit.ui;

import de.snorlaxlog.bukkit.commands.abrax.AbraxUIClickEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryManagerClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getLocalizedName() == null) return;
        if (event.getCurrentItem().getItemMeta().getLocalizedName().startsWith("snorlaxlog.ui.inventorymanager.warpui.")) AbraxUIClickEventHandler.handleClick(event);
    }
}