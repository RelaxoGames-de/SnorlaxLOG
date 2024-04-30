package de.relaxogames.snorlaxlog.bungeecord.commands.abrax;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.api.APIBungeeManager;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.CachedPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGGEDPlayer;
import de.relaxogames.snorlaxlog.bungeecord.files.interfaces.LOGPlayer;
import de.relaxogames.snorlaxlog.shared.PermissionShortCut;
import de.relaxogames.snorlaxlog.shared.util.CommandPrefix;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.LanguageManager;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class PermissionCommand extends Command implements TabExecutor {

    public PermissionCommand(){
        super("permission", "", "perm");
    }

    // /permission <grant | revoke> <player> <permission>
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(CommandPrefix.getConsolePrefix() + LanguageManager.getMessage(Language.system_default, "OnlyPlayer"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;
        LOGPlayer logPlayer = new LOGGEDPlayer(p);

        if (!logPlayer.hasPermission(PermissionShortCut.PERMISSION_GRANT_COMMAND)){
            p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "NoPermission1"));
            return;
        }

        if (args.length <= 2){
            p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorPermissionNEArgs"));
            return;
        }

        if (!PermissionShortCut.isPermission(args[2])){
            p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorNotPSCAvailable"));
            return;
        }
        CachedPlayer target = APIBungeeManager.getBungeeSqlManager().getPlayerInfos(args[1]);
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        User targetUser = userManager.getUser(target.getName());

        String perm = args[2];
        PermissionShortCut pmSc = PermissionShortCut.getPermissionSC(perm);

        if (args[0].equalsIgnoreCase("grant")) {
            if (targetUser != null && targetUser.getCachedData().getPermissionData().checkPermission(pmSc.getPermission()).asBoolean()){
                p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorHasPermission"));
                return;
            }

            Objects.requireNonNull(targetUser).data().add(Node.builder(pmSc.getPermission()).build());
            userManager.saveUser(targetUser);
            p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "PermissionGranted").replace("{PERMISSION}", pmSc.getPermission()).replace("{PLAYER}", target.getName()));
        } else if (args[0].equalsIgnoreCase("revoke")) {
            if (!(targetUser != null && targetUser.getCachedData().getPermissionData().checkPermission(pmSc.getPermission()).asBoolean())){
                p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "ErrorHasNotPermission"));
                return;
            }

            targetUser.data().remove(Node.builder(pmSc.getPermission()).build());
            userManager.saveUser(targetUser);
            p.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logPlayer.language(), "PermissionRevoked").replace("{PERMISSION}", pmSc.getPermission()).replace("{PLAYER}", target.getName()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return Arrays.asList("revoke", "grant");
        else if(args.length == 2) return SnorlaxLOG.getInstance().getProxy().getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList());
        else if (args.length == 3) return Arrays.stream(PermissionShortCut.values()).map(PermissionShortCut::getPermission).collect(Collectors.toList());
        return null;
    }
}
