package de.snorlaxlog.bukkit.commands.abrax;

import de.snorlaxlog.bukkit.ui.InventoryManager;
import de.snorlaxlog.bukkit.ui.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.round;

public class AbraxWarpUI {
    public static void handleResponse(String data) {
        final Player player = Bukkit.getPlayer(data.split(":")[1]);
        List<String> serverNames = new ArrayList<>(Arrays.asList((data.split(":")[2].split(";"))));

        int inventorySize = serverNames.size();

        if (inventorySize % 9 != 0) inventorySize = (round(inventorySize / 9) * 9) + 9;

        InventoryManager inventoryManager = new InventoryManager(player, inventorySize, "§l§3Choose a Server!");

        int iter = 0;
        for (String serverName : serverNames) {
            inventoryManager.setItem(iter, new ItemBuilder(Material.ENDER_PEARL).setDisplayname(serverName).setLocalizedName("snorlaxlog.ui.inventorymanager.warpui." + serverName));
            iter++;
        }

        inventoryManager.spawnToPlayer();
    }
}