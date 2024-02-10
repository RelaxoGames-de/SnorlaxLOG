package de.snorlaxlog.bukkit.commands;

import de.snorlaxlog.bukkit.interfaces.LOGBukkitPlayer;
import de.snorlaxlog.bukkit.interfaces.LOGPlayer;
import de.snorlaxlog.bukkit.listener.AntiOPListener;
import de.snorlaxlog.shared.PermissionShotCut;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfirmCommand implements CommandExecutor {

    AntiOPListener antiOPListener = new AntiOPListener();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(commandSender instanceof Player executer)){
            return false;
        }

        LOGPlayer logExecuter = new LOGBukkitPlayer(executer);

        if (!executer.hasPermission(PermissionShotCut.OP_COMMAND_BYPASS.getPermission())){
            executer.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logExecuter.language(), "NoPermission1"));
            return false;
        }

        if (!antiOPListener.getConfirm().contains(executer.getUniqueId())){
            executer.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logExecuter.language(), "ErrorNothingToConfirm"));
            return false;
        }



        return false;
    }
}
