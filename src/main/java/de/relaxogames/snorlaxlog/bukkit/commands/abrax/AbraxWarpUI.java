package de.relaxogames.snorlaxlog.bukkit.commands.abrax;

import de.relaxogames.snorlaxlog.bukkit.ui.InventoryManager;
import de.relaxogames.snorlaxlog.bukkit.ui.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbraxWarpUI {
    public static void handleResponse(String data) {
        final Player player = Bukkit.getPlayer(data.split(":")[1]);
        List<String> serverNames = new ArrayList<>(Arrays.asList((data.split(":")[2].split(";"))));

        int iter = 0;

        InventoryManager inventoryManager = new InventoryManager(player, 9 * 6, "Choose a Server!");
        for (String serverName : serverNames) {
            inventoryManager.setItem(iter, new ItemBuilder(Material.ENDER_PEARL).setDisplayname(serverName)
                    .setLocalizedName("snorlaxlog.ui.inventorymanager.warpui." + serverName));
            iter++;
        }

        inventoryManager.spawnToPlayer();
    }
}