package de.relaxogames.snorlaxlog.bukkit.interfaces;

import de.relaxogames.snorlaxlog.shared.util.Language;
import de.relaxogames.snorlaxlog.shared.util.PlayerEntryData;
import eu.cloudnetservice.modules.bridge.player.executor.PlayerExecutor;
import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface LOGPlayer {
    Player getPlayer();

    UUID getUUIDFromDatabase();

    String getName();

    boolean isInDatabase();

    void changePlayerEntry(PlayerEntryData setting, String newValue);

    void changePlayerEntry(PlayerEntryData setting, Integer newValue);

    String getPlayerEntry(PlayerEntryData setting);

    Language language();

    Long getOnlineTime();

    CachedPlayer getCachedPlayer();

    PlayerExecutor getCloudNetPlayer();

    void connectToTask(String taskName, ServerSelectorType selectorType);

    void connectToGroup(String groupName, ServerSelectorType selectorType);

    void connectToFallback();
}