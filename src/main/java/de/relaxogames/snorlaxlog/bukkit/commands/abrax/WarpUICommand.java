package de.relaxogames.snorlaxlog.bukkit.commands.abrax;

import de.relaxogames.snorlaxlog.bukkit.LOGLaxAPI;
import de.relaxogames.snorlaxlog.bukkit.interfaces.LOGBukkitPlayer;
import de.relaxogames.snorlaxlog.bukkit.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.shared.PermissionShortCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WarpUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return false;
        }

        LOGPlayer logPlayer = new LOGBukkitPlayer(player);

        if (!player.hasPermission(PermissionShortCut.ABRAX_JOIN_SERVER.getPermission())) {
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