package de.snorlaxlog.bukkit.interfaces;

import de.snorlaxlog.shared.util.PlayerEntryData;
import de.snorlaxlog.shared.util.Language;
import org.bukkit.entity.Player;
import de.snorlaxlog.bukkit.mysql.SQLManager;

import java.util.UUID;

public class LOGBukkitPlayer implements LOGPlayer{
    ///////////////////////////////////////////////////
    private SQLManager sqlManager = new SQLManager();
    ///////////////////////////////////////////////////
    Player player;
    UUID uuid;
    String name;
    Language language;
    ///////////////////////////////////////////////////

    public LOGBukkitPlayer(Player player){
        this.uuid = player.getUniqueId();
        this.player = player;
    }
    ///////////////////////////////////////////////////
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public UUID getUUIDFromDatabase() {
        if (uuid == null){
            uuid = sqlManager.getUUIDThroughName(player.getName());
        }
        return uuid;
    }

    @Override
    public String getName() {
        if (name == null) {
            name = sqlManager.getCorrectNameFromLOWERCASE(name.toLowerCase());
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
        if (language == null){
            language = Language.convertLanguage(sqlManager.getPlayerInfos(this).getLanguage());
        }
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
}
