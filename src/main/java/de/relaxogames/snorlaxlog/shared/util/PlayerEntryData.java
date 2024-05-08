package de.relaxogames.snorlaxlog.shared.util;

public enum PlayerEntryData {
    USER_ID("user_id", 1, DatabaseData.INTEGER),
    USER_UUID("user_uuid", 2, DatabaseData.VARCHAR),
    USER_NAME("user_name", 3, DatabaseData.VARCHAR),
    USER_FIRST_JOINED("user_first_seen", 4, DatabaseData.TIMESTAMP),
    USER_LAST_JOINED("user_last_seen", 5, DatabaseData.TIMESTAMP),
    USER_LINKS_DISCORD("user_discord_id", 6, DatabaseData.VARCHAR),
    USER_LINKS_FORUM("user_forum_id", 7, DatabaseData.VARCHAR),
    USER_ONLINE_TIME("user_total_onlinetime", 8, DatabaseData.BIGINT),
    USER_LANGUAGE("user_language_selected", 9, DatabaseData.VARCHAR),
    USER_CACHED_IP("user_last_cached_IP", 10, DatabaseData.VARCHAR);

    final String tableColumnName;
    final int columnPlace;
    final DatabaseData datatype;

    PlayerEntryData(String tableColumnName, int columnPlace, DatabaseData datatype) {
        this.tableColumnName = tableColumnName;
        this.columnPlace = columnPlace;
        this.datatype = datatype;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public int getColumnPlace() {
        return columnPlace;
    }

    public DatabaseData getDatatype() {
        return datatype;
    }
}
