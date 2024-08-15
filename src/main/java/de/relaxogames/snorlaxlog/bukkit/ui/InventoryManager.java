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

    /**
 * Sets the item in the inventory at the specified slot using the provided ItemBuilder.
 *
 * @param slot The slot in the inventory where the item will be set
 * @param itemBuilder The ItemBuilder used to build the item to be set
 */
    public Inventory getInventory() {
        return inventory;
    }

    /**
 * Sets the item in the inventory at the specified slot using the provided ItemBuilder.
 *
 * @param slot The slot in the inventory where the item will be set
 * @param itemBuilder The ItemBuilder used to build the item to be set
 */
    public void setItem(int slot, ItemBuilder itemBuilder) {
        inventory.setItem(slot, itemBuilder.build());
    }

    /**
 * Opens the inventory associated with this InventoryManager to the player.
 */
    public void spawnToPlayer() {
        this.player.openInventory(this.inventory);
    }

    /**
 * Retrieves an InventoryManager object associated with the provided LOGGEDPlayer, title, and size.
 *
 * @param loggedPlayer the LOGGEDPlayer object to retrieve the InventoryManager from
 * @param title the title of the inventory
 * @param size the size of the inventory
 * @return the InventoryManager object created with the player, size, and title
 */
    public static InventoryManager getInventoryManagerFromLoggedPlayer(LOGGEDPlayer loggedPlayer, String title,
            int size) {
        return new InventoryManager(Bukkit.getPlayer(loggedPlayer.getUUIDFromDatabase()), size, title);
    }
}