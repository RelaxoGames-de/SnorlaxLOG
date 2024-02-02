package de.snorlaxlog.shared;

public enum PermissionShotCut {

    SL_LOG_COMMAND_USE("snorlax.log.command.use"),
    SL_LOG_NOTIFY_READ("snorlax.log.notify"),
    SL_LOG_LEVEL_INFO("snorlax.log.info"),
    SL_LOG_LEVEL_WARNING("snorlax.log.warning"),

    SL_LOG_LEVEL_OFF("snorlax.log.off"),

    SL_LOG_LEVEL_ALL("snorlax.log.all"),

    SL_LOG_LEVEL_SERVERE("snorlax.log.servere"),
    SL_LOG_LEVEL_EVERY("snorlax.log.*"),

    ONLINE_TIME_BYPASS("snorlax.log.command.ot.bypass"),
    ONLINE_TIME_OTHER("snorlax.log.command.ot.other"),
    ABRAX_JOIN_SERVER("snorlax.abrax.warp.command"),
    ABRAX_JOIN_SERVER_PRE("snorlax.abrax.warp."),
    PERMISSION_GRANT_COMMAND("snorlax.permission.command");

    String permission;

    PermissionShotCut(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }


    public static PermissionShotCut getPermissionSC(String pmsc){
        for (PermissionShotCut permissionShotCut : PermissionShotCut.values()){
            if (permissionShotCut.getPermission().toLowerCase().equals(pmsc.toLowerCase())){
                return permissionShotCut;
            }
        }
        return null;
    }

    public static boolean isPermission(String pmsc){
        return getPermissionSC(pmsc) != null;
    }
}