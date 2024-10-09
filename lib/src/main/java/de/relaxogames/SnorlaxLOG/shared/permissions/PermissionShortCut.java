package de.relaxogames.SnorlaxLOG.shared.permissions;

/**
 * This enum is used to define the permissions of the plugin.
 * 
 * @version 1.0
 * @since 2.0
 */
public enum PermissionShortCut {

    /**
     * The permission for the owner of the server.
     * 
     * @since 2.0
     */
    OWNER("snorlaxlog.owner"),
    
    /**
     * The permission for the admin of the server.
     * 
     * @since 2.0
     */
    ADMIN("snorlaxlog.admin"),
    
    /**
     * The permission for the moderator of the server.
     * 
     * @since 2.0
     */
    MOD("snorlaxlog.mod"),
    
    /**
     * The permission for the helper of the server.
     * 
     * @since 2.0
     */
    HELPER("snorlaxlog.helper"),
    
    /**
     * The permission for the member of the server.
     * 
     * @since 2.0
     */
    MEMBER("snorlaxlog.member");

    /**
     * The permission name of the shortcut.
     * 
     * @since 2.0
     */
    private final String permission;

    /**
     * Creates a new PermissionShortCut with the given permission.
     * 
     * @param permission The permission of the shortcut.
     * @since 2.0
     */
    PermissionShortCut(String permission) {
        this.permission = permission;
    }

    /**
     * Returns the permission of the shortcut.
     * 
     * @return The permission of the shortcut.
     * @since 2.0
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Returns the PermissionShortCut from the given string.
     * 
     * @param permission The string to convert to a PermissionShortCut.
     * @return The PermissionShortCut from the given string.
     * @since 2.0
     */
    public static PermissionShortCut fromString(String permission) {
        for (PermissionShortCut value : values()) {
            if (value.getPermission().equalsIgnoreCase(permission)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns if the given string is a permission.
     * 
     * @param permission The string to check.
     * @return If the given string is a permission.
     * @since 2.0
     */
    public static boolean isPermission(String permission) {
        return fromString(permission) != null;
    }
}
