package de.snorlaxlog.bungeecord.api;

import de.snorlaxlog.bungeecord.SnorlaxLOG;
import de.snorlaxlog.bungeecord.mysql.SQLManager;

public class APIBungeeManager {

    private static SnorlaxLOG snorlaxLOG = SnorlaxLOG.getInstance();
    private static SQLManager sqlManager = new SQLManager();

    public static SnorlaxLOG getSnorlaxLOG() {
        return snorlaxLOG;
    }

    public static SQLManager getBungeeSqlManager() {
        return sqlManager;
    }
}

