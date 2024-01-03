package spigot.api.interfaces;

import api.shared.util.PlayerEntryData;
import api.shared.util.Language;
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

}
