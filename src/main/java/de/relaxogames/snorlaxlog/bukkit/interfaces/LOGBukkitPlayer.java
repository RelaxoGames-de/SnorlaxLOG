package de.relaxogames.snorlaxlog.bukkit.interfaces;

import de.relaxogames.snorlaxlog.bukkit.mysql.SQLManager;
import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.bridge.player.executor.PlayerExecutor;
import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LOGBukkitPlayer implements LOGPlayer{
    ///////////////////////////////////////////////////
    private SQLManager sqlManager = new SQLManager();
    ///////////////////////////////////////////////////
    Player player;
    PlayerExecutor cloudNetPlayer;
    UUID uuid;
    String name;
    Language language;
    ///////////////////////////////////////////////////

    public LOGBukkitPlayer(Player player){
        PlayerManager playerManager = ServiceRegistry.first(PlayerManager.class);
        this.uuid = player.getUniqueId();
        this.player = player;
        this.cloudNetPlayer = playerManager.playerExecutor(player.getUniqueId());
    }
    ///////////////////////////////////////////////////
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public UUID getUUIDFromDatabase() {
        if (uuid == null) uuid = sqlManager.getUUIDThroughName(player.getName());
        return uuid;
    }

    @Override
    public String getName () {
        if (name == null) {
            String lowercaseName = player.getName().toLowerCase();
            name = sqlManager.getCorrectNameFromLOWERCASE(lowercaseName);
        }
        return name;
    }

    @Override
    public boolean isInDatabase() {
        return getUUIDFromDatabase() != null;
    }

    @Override
    public void changePlayerEntry(PlayerEntryData setting, String newValue) {
        return;
    }

    @Override
    public void changePlayerEntry(PlayerEntryData setting, Integer newValue) {
        return;
    }

    @Override
    public String getPlayerEntry(PlayerEntryData setting) {
        return null;
    }

    @Override
    public Language language() {
        if (language == null) language = Language.convertLanguage(sqlManager.getPlayerInfos(this).getLanguage());
        return language;
    }

    @Override
    public Long getOnlineTime() {
        return null;
    }

    @Override
    public CachedPlayer getCachedPlayer() {
        return sqlManager.getPlayerInfos(this);
    }

    @Override
    public PlayerExecutor getCloudNetPlayer() {
        return cloudNetPlayer;
    }

    @Override
    public void connectToTask(String taskName, ServerSelectorType selectorType) {
        cloudNetPlayer.connectToTask(taskName, selectorType);
    }

    @Override
    public void connectToGroup(String groupName, ServerSelectorType selectorType) {
        cloudNetPlayer.connectToGroup(groupName, selectorType);
    }

    @Override
    public void connectToFallback() {
        cloudNetPlayer.connectToFallback();
    }
}
