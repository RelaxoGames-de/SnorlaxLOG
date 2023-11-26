package de.snorlaxlog.commands;

import de.snorlaxlog.Main;
import de.snorlaxlog.files.CommandPrefix;
import de.snorlaxlog.files.LanguageManager;
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
        super("onlinetime", "", "ot");
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

        if (cooldown.contains(player.getUniqueId())){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorCMDCooldown").replace("%COOLDOWN%", String.valueOf(cooldownTime)));
            return;
        }

        long gainedOT = cachedPlayer.getOnlineTime();
        Date date = new Date(gainedOT);
        if (args.length == 0){
            player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "OnlineTimeResponse").replace("%TIME%", String.valueOf(date.getHours())));
            cooldown.add(player.getUniqueId());

            ScheduledTask task;
            task = Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), (Runnable) () -> {
                cooldown.remove(player.getUniqueId());
            }, cooldownTime, TimeUnit.MINUTES);

            return;
        }
        if (args.length == 1){
            if (!logPlayer.hasPermission("slog.ot.other")){
                player.sendMessage(CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermission1"));
                return;
            }
        }
    }
}
