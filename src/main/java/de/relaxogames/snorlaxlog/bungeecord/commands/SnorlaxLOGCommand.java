package de.relaxogames.snorlaxlog.bungeecord.commands;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.shared.PermissionShortCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SnorlaxLOGCommand extends Command implements TabExecutor {
    private static final HashMap<LOGPlayer, Level> logPlayers = new HashMap<>();


    public SnorlaxLOGCommand(){
        super("log", PermissionShortCut.SL_LOG_COMMAND_USE.getPermission(), "sl", "snorlaxlog");
    }

    /**
     * This Command implements the /log Command to get some Information about the Plugin an to Toggle the LOG-Notifications
     * @param sender The Player that sent the command
     * @param args The Arguments passed into the command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        LOGPlayer logPla = new LOGGEDPlayer(player);

        if (!logPla.hasPermission(PermissionShortCut.SL_LOG_COMMAND_USE)){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "NoPermission1"));
            SnorlaxLOG.logMessage(Level.INFO, player.getName() + " issued the Command " + this.getName() + " without any permission for that!");
            return;
        }

        if (args.length == 0){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "LogCMDUsage"));
            SnorlaxLOG.logMessage(Level.INFO, player.getName() + " issued the Command " + this.getName() + " and got the Usage message!");
            return;
        }

        switch (args[0]){

            case "credits":{
                player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "credits"));
                break;
            }

            case "version":{
                player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "PluginVersion").replace("%VERSION%", SnorlaxLOG.getVersion()));
                break;
            }

            case "toggle":{
                if (logPla.notifyIsActive()){
                    logPla.disableNotify();
                    return;
                }

                if (args.length != 2 && !logPla.notifyIsActive()){
                    player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "ErrorNoChannel"));
                    return;
                }
                Level loggingLvl = Level.parse(args[1]);

                if (loggingLvl.equals(Level.INFO) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_INFO) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.OFF) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_OFF) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.WARNING) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_WARNING) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.ALL) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_ALL) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.SEVERE) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_SERVERE) && !logPla.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }

                if (loggingLvl == null){
                    player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "ErrorNoChannel"));
                    return;
                }
                logPla.activateNotify(loggingLvl);
                break;
            }

            default:{
                player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPla.language(), "LogCMDUsage"));
                SnorlaxLOG.logMessage(Level.INFO, player.getName() + " issued the Command " + this.getName() + " and got the Usage message!");
                break;
            }
        }
    }

    private void noPermissionForChannel(LOGPlayer player){
        player.getPlayer().sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(player.language(), "NoPermForChannel"));
    }

    public static HashMap<LOGPlayer, Level> getLogPlayers() {
        return logPlayers;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        LOGPlayer logPlayer = new LOGGEDPlayer((ProxiedPlayer) sender);
        ArrayList<String> tab = new ArrayList<>();
        if (args.length == 0){
            if (logPlayer.hasPermission(PermissionShortCut.SL_LOG_NOTIFY_READ)) tab.add("toggle");
            tab.add("credits");
            tab.add("version");
            return tab;
        }

        Map<String, PermissionShortCut> logLevels = Map.of(
                "info", PermissionShortCut.SL_LOG_LEVEL_INFO,
                "off", PermissionShortCut.SL_LOG_LEVEL_OFF,
                "warning", PermissionShortCut.SL_LOG_LEVEL_WARNING,
                "severe", PermissionShortCut.SL_LOG_LEVEL_SERVERE,
                "all", PermissionShortCut.SL_LOG_LEVEL_ALL
        );

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle ") && !logPlayer.notifyIsActive()) {
            for (Map.Entry<String, PermissionShortCut> entry : logLevels.entrySet()) {
                if (logPlayer.hasPermission(entry.getValue()) || logPlayer.hasPermission(PermissionShortCut.SL_LOG_LEVEL_EVERY))
                    tab.add(entry.getKey());
            }
            return tab;
        }
        return tab;
    }
}
