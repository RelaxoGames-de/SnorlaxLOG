package de.snorlaxlog.bukkit.commands.abrax;

import de.snorlaxlog.bukkit.interfaces.LOGBukkitPlayer;
import de.snorlaxlog.bukkit.interfaces.LOGPlayer;
import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        LOGPlayer logPlayer = new LOGBukkitPlayer(p);
        logPlayer.connectToTask(strings[0], ServerSelectorType.RANDOM);
        return false;
    }
}
