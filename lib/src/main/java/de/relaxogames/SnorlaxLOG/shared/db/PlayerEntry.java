package de.relaxogames.SnorlaxLOG.shared.db;

/**
 * This enum is used to define the columns of the player table in the database.
 * They are defined here to make the interactions with the database
 * more readable and to have a consistent programming experience.
 * 
 * @version 1.0
 * @since 2.0
 * @see DatabaseType
 */
public enum PlayerEntry {

    /**
     * The id of the player in the database.
     * 
     * @since 2.0
     */
    USER_ID("user_id", 1, DatabaseType.INT),
    
    /**
     * The minecraft account uuid of the player.
     * This is used to identify the player definitly.
     * 
     * @since 2.0
     */
    USER_UUID("user_uuid", 2, DatabaseType.TEXT),
    
    /**
     * The minecraft account name of the player.
     * This is used to identify the player in the database.
     * 
     * @since 2.0
     */
    USER_NAME("user_name", 3, DatabaseType.TEXT),
    
    /**
     * The timestamp of the first join of the player.
     * 
     * @since 2.0
     */
    USER_FIRST_JOIN("user_first_join", 4, DatabaseType.TIMESTAMP),
    
    /**
     * The timestamp of the last join of the player.
     * 
     * @since 2.0
     */
    USER_LAST_JOIN("user_last_join", 5, DatabaseType.TIMESTAMP),
    
    /**
     * The discord id of the player.
     * This is used to identify the player definitly.
     * 
     * @since 2.0
     */
    USER_DISCORD_ID("user_discord_id", 6, DatabaseType.TEXT),
    
    /**
     * The online time of the player in seconds.
     * 
     * @since 2.0
     */
    USER_ONLINE_TIME("user_online_time", 7, DatabaseType.INT),
    
    /**
     * The cached ip of the player.
     * 
     * @since 2.0
     */
    USER_CACHED_IP("user_cached_ip", 8, DatabaseType.TEXT);

    /**
     * The name of the column in the database.
     * 
     * @since 2.0
     */
    private final String tableColumnName;

    /**
     * The position of the column in the database.
     * 
     * @since 2.0
     */
    private final int columnPosition;

    /**
     * The type of the column in the database.
     * 
     * @since 2.0
     * @see DatabaseType
     */
    private final DatabaseType columnType;

    /**
     * The constructor of the PlayerEntry.
     * 
     * @param tableColumnName The name of the column in the database.
     * @param columnPosition The position of the column in the database.
     * @param columnType The type of the column in the database.
     * @since 2.0
     * @see DatabaseType
     */
    PlayerEntry(String tableColumnName, int columnPosition, DatabaseType columnType) {
        this.tableColumnName = tableColumnName;
        this.columnPosition = columnPosition;
        this.columnType = columnType;
    }

    /**
     * Returns the name of the column in the database.
     * 
     * @return The name of the column in the database.
     * @since 2.0
     */
    public String getTableColumnName() {
        return tableColumnName;
    }

    /**
     * Returns the position of the column in the database.
     * 
     * @return The position of the column in the database.
     * @since 2.0
     */
    public int getColumnPosition() {
        return columnPosition;
    }

    /**
     * Returns the type of the column in the database.
     * 
     * @return The type of the column in the database.
     * @since 2.0
     * @see DatabaseType
     */
    public DatabaseType getColumnType() {
        return columnType;
    }

    /**
     * Returns the PlayerEntry from the given string.
     * 
     * @param columnName The string to convert to a PlayerEntry.
     * @return The PlayerEntry from the given string.
     * @since 2.0
     */
    public static PlayerEntry fromString(String columnName) {
        for (PlayerEntry entry : PlayerEntry.values()) {
            if (entry.getTableColumnName().equals(columnName)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns the PlayerEntry from the given position.
     * 
     * @param position The position to convert to a PlayerEntry.
     * @return The PlayerEntry from the given position.
     * @since 2.0
     */
    public static PlayerEntry fromPosition(int position) {
        for (PlayerEntry entry : PlayerEntry.values()) {
            if (entry.getColumnPosition() == position) {
                return entry;
            }
        }
        return null;
    }
}
