package de.relaxogames.snorlaxlog.bukkit.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryPresets {
    private static final ItemBuilder emptySlot = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ")
            .setLore(" ");
    private static final ItemBuilder yesSlot = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayname("§aJa")
            .setLore("§7Klicke hier, um §aJa §7zu wählen.");
    private static final ItemBuilder noSlot = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§cNein")
            .setLore("§7Klicke hier, um §cNein §7zu wählen.");

    public static InventoryManager getYesNoInventory(Player player) {
        InventoryManager inventoryManager = new InventoryManager(player, 27, "Yes/No");
        for (int i = 0; i <= 8; i++)
            inventoryManager.setItem(i, emptySlot);
        inventoryManager.setItem(9, emptySlot);
        inventoryManager.setItem(10, emptySlot);
        inventoryManager.setItem(11, yesSlot);
        inventoryManager.setItem(12, emptySlot);
        inventoryManager.setItem(13, emptySlot);
        inventoryManager.setItem(14, emptySlot);
        inventoryManager.setItem(15, emptySlot);
        inventoryManager.setItem(16, noSlot);
        inventoryManager.setItem(17, emptySlot);
        for (int i = 18; i <= 26; i++)
            inventoryManager.setItem(i, emptySlot);

        return inventoryManager;
    }
}
