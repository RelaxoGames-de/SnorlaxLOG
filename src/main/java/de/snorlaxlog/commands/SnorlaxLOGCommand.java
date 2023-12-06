package de.snorlaxlog.commands;

import de.snorlaxlog.SnorlaxLOG;
import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.LanguageManager;
import de.snorlaxlog.files.PermissionShotCut;
import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import de.snorlaxlog.files.interfaces.Language;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class SnorlaxLOGCommand extends Command implements TabExecutor {

    private static HashMap<LOGPlayer, Level> logPlayers = new HashMap<>();


    public SnorlaxLOGCommand(){
        super("log", PermissionShotCut.SL_LOG_COMMAND_USE.getPermission(), "sl", "snorlaxlog");
    }

    /**
     * This Command implements the /log Command to get some Information about the Plugin an to Toggle the LOG-Notifications
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        LOGPlayer logPla = new LOGGEDPlayer(player);

        if (!logPla.hasPermission(PermissionShotCut.SL_LOG_COMMAND_USE)){
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

                if (loggingLvl.equals(Level.INFO) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_INFO) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.OFF) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_OFF) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.WARNING) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_WARNING) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.ALL) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_ALL) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)){
                    noPermissionForChannel(logPla);
                    return;
                }
                if (loggingLvl.equals(Level.SEVERE) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_SERVERE) && !logPla.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)){
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
            if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_NOTIFY_READ)) tab.add("toggle");
            tab.add("credits");
            tab.add("version");
            return tab;
        }

        if (args.length == 1){
            if (args[0].equalsIgnoreCase("toggle ")){
                if (!logPlayer.notifyIsActive()){
                    if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_INFO) || logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)) tab.add("info");
                    if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_OFF) || logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)) tab.add("off");
                    if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_WARNING) || logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)) tab.add("warning");
                    if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_SERVERE) || logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)) tab.add("servere");
                    if (logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_ALL) || logPlayer.hasPermission(PermissionShotCut.SL_LOG_LEVEL_EVERY)) tab.add("all");
                    return tab;
                }
            }
        }

        return tab;
    }
}
