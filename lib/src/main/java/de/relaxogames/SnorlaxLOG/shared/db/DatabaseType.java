package de.relaxogames.SnorlaxLOG.shared.db;

/**
 * This enum is used to define the types of the database columns.
 * They are defined here to make the interactions with the database
 * more readable and to have a consistent programming experience.
 * 
 * @version 1.0
 * @since 2.0
 */
public enum DatabaseType {
    /**
     * The type of the column is an integer.
     * 
     * @since 2.0
     */
    INT,

    /**
     * The type of the column is a text.
     * 
     * @since 2.0
     */
    TEXT,

    /**
     * The type of the column is a timestamp.
     * 
     * @since 2.0
     */
    TIMESTAMP;

    /**
     * Returns the DatabaseType from the given string.
     * 
     * @param type The string to convert to a DatabaseType.
     * @return The DatabaseType from the given string.
     * @since 2.0
     * @see DatabaseType
     */
    public static DatabaseType fromString(String type) {
        switch (type) {
            case "INT":
                return INT;
            case "TEXT":
                return TEXT;
            case "TIMESTAMP":
                return TIMESTAMP;
            default:
                return null;
        }
    }

    /**
     * Returns the string representation of the DatabaseType.
     * 
     * @return The string representation of the DatabaseType.
     * @since 2.0
     * @see DatabaseType
     */
    public String toString() {
        switch (this) {
            case INT:
                return "INT";
            case TEXT:
                return "TEXT";
            case TIMESTAMP:
                return "TIMESTAMP";
            default:
                return null;
        }
    }

    /**
     * Returns whether the given string is a valid DatabaseType.
     * 
     * @param type The string to check.
     * @return Whether the given string is a valid DatabaseType.
     * @since 2.0
     * @see DatabaseType
     */
    public static boolean isValid(String type) {
        return fromString(type) != null;
    }

    /**
     * Returns whether the given DatabaseType is valid.
     * 
     * @param type The DatabaseType to check.
     * @return Whether the given DatabaseType is valid.
     * @since 2.0
     * @see DatabaseType
     */
    public static boolean isValid(DatabaseType type) {
        return type != null;
    }
}
