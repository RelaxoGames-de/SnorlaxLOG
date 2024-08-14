package de.relaxogames.snorlaxlog.bungeecord.commands;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.shared.PermissionShortCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OnlineTimeCommand extends Command {

    public OnlineTimeCommand() {
        super("onlinetime", "", "ot", "ontime");
    }

    private final int cooldownTime = 5;
    public ArrayList<UUID> cooldown = new ArrayList<>();

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer player)) {
            return;
        }
        LOGPlayer logPlayer = new LOGGEDPlayer(player);
        CachedPlayer cachedPlayer = logPlayer.getCachedPlayer();

        if (cooldown.contains(player.getUniqueId())
                && !logPlayer.hasPermission(PermissionShortCut.ONLINE_TIME_BYPASS)) {
            player.sendMessage(
                    CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorCMDCooldown")
                            .replace("%COOLDOWN%", String.valueOf(cooldownTime)));
            return;
        }

        long gainedOT = cachedPlayer.getOnlineTime();
        Date date = new Date(gainedOT);
        if (args.length == 1 && logPlayer.hasPermission(PermissionShortCut.ONLINE_TIME_OTHER)) {
            player.sendMessage(
                    CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermission1"));
            return;
        }

        if (args.length != 0)
            return;

        player.sendMessage(
                CommandPrefix.getLOGPrefix() + LanguageManager.getMessage(logPlayer.language(), "OnlineTimeResponse")
                        .replace("%TIME%", String.valueOf(date.getTime())));
        cooldown.add(player.getUniqueId());

        @SuppressWarnings("unused")
        ScheduledTask task;
        task = SnorlaxLOG.getInstance().getProxy().getScheduler().schedule(SnorlaxLOG.getInstance(), (Runnable) () -> {
            cooldown.remove(player.getUniqueId());
        }, cooldownTime, TimeUnit.MINUTES);
    }
}
