package de.snorlaxlog.bukkit.commands.abrax;

import de.snorlaxlog.bukkit.LOGLaxAPI;
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class WarpUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return false;
        }

        LOGPlayer logPlayer = new LOGBukkitPlayer(player);

        if (!player.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER.getPermission())) {
            player.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(Language.system_default, "NoPermission1"));
            return false;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {

            dataOutputStream.writeUTF("warpui");
            dataOutputStream.writeUTF("getServers" + ":" + player.getName());

            player.sendPluginMessage(LOGLaxAPI.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return true;
    }
}