package de.snorlaxlog.files.interfaces;

public enum PlayerEntryData {

    USER_UUID("user_uuid", 1),
    USER_NAME("user_name", 2),
    USER_FIRST_JOINED("user_first_seen", 3),
    USER_LAST_JOINED("user_last_seen", 4),
    USER_LINKS_DISCORD("user_discord_id", 5),
    USER_LINKS_FORUM("user_forum_id", 6),
    USER_ONLINE_TIME("user_total_onlinetime", 7),
    USER_CACHED_IP("user_last_cached_IP", 8);

    String tableColumnName;
    int columnPlace;

    PlayerEntryData(String tableColumnName, int columnPlace) {
        this.tableColumnName = tableColumnName;
        this.columnPlace = columnPlace;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public int getColumnPlace() {
        return columnPlace;
    }
}
