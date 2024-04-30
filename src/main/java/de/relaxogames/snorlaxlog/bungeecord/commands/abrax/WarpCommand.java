package de.snorlaxlog.bungeecord.commands.abrax;

import de.snorlaxlog.bungeecord.SnorlaxLOG;
import de.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.snorlaxlog.shared.PermissionShotCut;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.Language;
import de.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarpCommand extends Command {

    public WarpCommand(){
        super("warp", "", "switch", "connect");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;
        LOGPlayer logPlayer = new LOGGEDPlayer(p);

        if (args.length == 0){
            p.sendMessage(CommandPrefix.getAbraxPrefix() + "§7Nutzung: /warp <server>");
            return;
        }

        if (!logPlayer.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER)){
             p.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermission1"));
             return;
        }

        String server = args[0];
        if (args.length < 1 || ProxyServer.getInstance().getServerInfo(server) == null){
            p.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoDirection"));
            return;
        }
        ServerInfo relocatedServer = ProxyServer.getInstance().getServerInfo(server);

        String serverName = server.replace("-", ".");

        if (!logPlayer.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER_PRE.getPermission() + serverName)){
            p.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermForDir").replace("{SERVER}", relocatedServer.getName()));
            return;
        }

        if (relocatedServer.equals(p.getServer().getInfo())){
            p.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorDirEqualsOrigin"));
            return;
        }

        String msg = LanguageManager.getMessage(logPlayer.language(), "ConnectionSuccess").replace("{SERVER}", relocatedServer.getName());
        String abort = LanguageManager.getMessage(logPlayer.language(), "ErrorUnknownError");

        AbraxConnectPlayerEvent connectPlayerEvent = new AbraxConnectPlayerEvent(p, logPlayer, server, p.getServer().getInfo(), relocatedServer, msg, abort);

        if (!connectPlayerEvent.isCancelled()) {
            p.connect(ProxyServer.getInstance().getServerInfo(server));
            p.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getConnectMessage());
            SnorlaxLOG.getInstance().getProxy().getPluginManager().callEvent(connectPlayerEvent);
            return;
        }

        p.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getAbortMessage());
    }
}
