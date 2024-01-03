package de.snorlaxlog.commands;

import de.snorlaxlog.SnorlaxLOG;
import api.shared.util.CommandPrefix;
import api.shared.util.LanguageManager;
import de.snorlaxlog.files.PermissionShotCut;
import de.snorlaxlog.files.interfaces.CachedPlayer;
import de.snorlaxlog.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.files.interfaces.LOGPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OnlineTimeCommand extends Command {

    public OnlineTimeCommand(){
        super("onlinetime", "", "ot", "ontime");
    }

    private int cooldownTime = 5;
    public ArrayList<UUID> cooldown = new ArrayList<>();

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer)){
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        LOGPlayer logPlayer = new LOGGEDPlayer(player);
        CachedPlayer cachedPlayer = logPlayer.getCachedPlayer();

        if (cooldown.contains(player.getUniqueId()) && !logPlayer.hasPermission(PermissionShotCut.ONLINE_TIME_BYPASS)){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorCMDCooldown").replace("%COOLDOWN%", String.valueOf(cooldownTime)));
            return;
        }

        long gainedOT = cachedPlayer.getOnlineTime();
        Date date = new Date(gainedOT);
        if (args.length == 0){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "OnlineTimeResponse").replace("%TIME%", String.valueOf(date.getTime())));
            cooldown.add(player.getUniqueId());

            ScheduledTask task;
            task = SnorlaxLOG.getInstance().getProxy().getScheduler().schedule(SnorlaxLOG.getInstance(), (Runnable) () -> {
                cooldown.remove(player.getUniqueId());
            }, cooldownTime, TimeUnit.MINUTES);

            return;
        }
        if (args.length == 1){
            if (!logPlayer.hasPermission(PermissionShotCut.ONLINE_TIME_OTHER)){
                player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermission1"));
                return;
            }
        }
    }
}
