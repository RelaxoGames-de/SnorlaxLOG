package de.snorlaxlog.bungeecord.commands.abrax;

import de.snorlaxlog.bukkit.ui.InventoryManager;
import de.snorlaxlog.bukkit.ui.ItemBuilder;
import de.snorlaxlog.bungeecord.SnorlaxLOG;
import de.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.snorlaxlog.shared.PermissionShotCut;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.Language;
import de.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.bukkit.Material;

public class WarpUICommand extends Command {
    
    public WarpUICommand(){
        super("warpui", "", "switchui", "connectui");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;
        LOGGEDPlayer loggedPlayer = new LOGGEDPlayer(p);

        if (!loggedPlayer.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER)){
            p.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(loggedPlayer.language(), "NoPermission1"));
            return;
        }

        InventoryManager inventoryManager = InventoryManager.getInventoryManagerFromLoggedPlayer(loggedPlayer, "Choose a Server!", 52);
        
        int iter = 0;        
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            inventoryManager.setItem(iter, new ItemBuilder(Material.ENDER_PEARL).setDisplayname(server.getName()).setLocalizedName("snorlaxlog.ui.inventorymanager.warpui."+server.getName()));
            iter++;
        }
    }
    
    
    public static void handleWarpUIMessage(String data) {
        LOGGEDPlayer loggedPlayer = new LOGGEDPlayer(ProxyServer.getInstance().getPlayer(data.split(":")[0]));
        
        ServerInfo relocatedServer = ProxyServer.getInstance().getServerInfo(data.split(":")[1]);
        String serverName = data.split(":")[1].replace("-", ".");
        
        if (!loggedPlayer.hasPermission(PermissionShotCut.ABRAX_JOIN_SERVER_PRE.getPermission() + serverName)){
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(loggedPlayer.language(), "NoPermForDir").replace("{SERVER}", relocatedServer.getName()));
            return;
        }
        
        if (relocatedServer.equals(loggedPlayer.getPlayer().getServer().getInfo())){
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + LanguageManager.getMessage(loggedPlayer.language(), "ErrorDirEqualsOrigin"));
            return;
        }

        String msg = LanguageManager.getMessage(loggedPlayer.language(), "ConnectionSuccess").replace("{SERVER}", relocatedServer.getName());
        String abort = LanguageManager.getMessage(loggedPlayer.language(), "ErrorUnknownError");

        AbraxConnectPlayerEvent connectPlayerEvent = new AbraxConnectPlayerEvent(loggedPlayer.getPlayer(), loggedPlayer, data.split(":")[1], loggedPlayer.getPlayer().getServer().getInfo(), relocatedServer, msg, abort);
        if (!connectPlayerEvent.isCancelled()) {
            loggedPlayer.getPlayer().connect(ProxyServer.getInstance().getServerInfo(data.split(":")[1]));
            loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getConnectMessage());
            SnorlaxLOG.getInstance().getProxy().getPluginManager().callEvent(connectPlayerEvent);
            return;
        }
        
        loggedPlayer.sendMessage(CommandPrefix.getAbraxPrefix() + connectPlayerEvent.getAbortMessage());
    }
}