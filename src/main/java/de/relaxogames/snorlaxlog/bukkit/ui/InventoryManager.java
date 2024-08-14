package de.relaxogames.snorlaxlog.bukkit.ui;

import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryManager {
    private final Inventory inventory;
    private final Player player;
    @SuppressWarnings("unused")
    private final String title;

    public InventoryManager(Player player, int size, String title) {
        this.inventory = Bukkit.createInventory(player, size, title);
        this.title = title;
        this.player = player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setItem(int slot, ItemBuilder itemBuilder) {
        inventory.setItem(slot, itemBuilder.build());
    }

    public void spawnToPlayer() {
        this.player.openInventory(this.inventory);
    }

    public static InventoryManager getInventoryManagerFromLoggedPlayer(LOGGEDPlayer loggedPlayer, String title,
            int size) {
        return new InventoryManager(Bukkit.getPlayer(loggedPlayer.getUUIDFromDatabase()), size, title);
    }
}