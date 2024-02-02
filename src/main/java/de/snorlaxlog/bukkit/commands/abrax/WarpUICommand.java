package de.snorlaxlog.bukkit.commands.abrax;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.snorlaxlog.bukkit.interfaces.LOGBukkitPlayer;
import de.snorlaxlog.bukkit.interfaces.LOGPlayer;
import de.snorlaxlog.bukkit.ui.InventoryManager;
import de.snorlaxlog.bukkit.ui.ItemBuilder;
import de.snorlaxlog.shared.PermissionShotCut;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.Language;
import de.snorlaxlog.shared.util.LanguageManager;

public class WarpUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return false;
        }
        
        LOGPlayer logPlayer = new LOGBukkitPlayer(player);

        if (!player.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER.getPermission())){
            player.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(Language.system_default, "NoPermission1"));
            return false;
        }

        InventoryManager inventoryManager = new InventoryManager(player, 9*6, "Choose a Server!");
        
        int iter = 0;
        /*
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            inventoryManager.setItem(iter, new ItemBuilder(Material.ENDER_PEARL).setDisplayname(server.getName()).setLocalizedName("snorlaxlog.ui.inventorymanager.warpui."+server.getName()));
            iter++;
        }
        */
        inventoryManager.setItem(0, new ItemBuilder(Material.ENDER_PEARL).setDisplayname("micro-1").setLocalizedName("snorlaxlog.ui.inventorymanager.warpui.micro-1"));
        
        inventoryManager.spawnToPlayer();
        
        return true;
    }
}