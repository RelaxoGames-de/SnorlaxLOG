package de.snorlaxlog.bukkit.listener;

import de.snorlaxlog.bukkit.LOGLaxAPI;
import de.snorlaxlog.bukkit.interfaces.LOGBukkitPlayer;
import de.snorlaxlog.bukkit.interfaces.LOGPlayer;
import de.snorlaxlog.shared.PermissionShotCut;
import de.snorlaxlog.shared.util.CommandPrefix;
import de.snorlaxlog.shared.util.LanguageManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class AntiOPListener implements Listener {

    ArrayList<UUID> confirm = new ArrayList<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        Player chatter = e.getPlayer();
        LOGPlayer logChatter = new LOGBukkitPlayer(chatter);
        if (!e.getMessage().startsWith("/op"))return;
        if (!chatter.hasPermission(PermissionShotCut.OP_COMMAND_BYPASS.getPermission())){
            chatter.sendMessage(CommandPrefix.getNetworkPrefix() + LanguageManager.getMessage(logChatter.language(), "NoPermission1"));
            LOGLaxAPI.logMessage(Level.WARNING, chatter.getName() + " has tried to get op via /op");
            return;
        }

        TextComponent continueButton = new TextComponent("§a§l[CONTINUE]");
        continueButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirm"));

        TextComponent refuseButton = new TextComponent("                              §c§l[REFUSE]");
        refuseButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/refuse"));

        continueButton.addExtra(refuseButton);

        chatter.sendMessage(CommandPrefix.getAnnouncePrefix());
        chatter.sendMessage(LanguageManager.getMessage(logChatter.language(), "AntiOP"));
        chatter.spigot().sendMessage(continueButton);
        chatter.sendMessage(CommandPrefix.getAnnouncePrefix());
        e.setCancelled(true);
    }

    public ArrayList<UUID> getConfirm() {
        return confirm;
    }
}
