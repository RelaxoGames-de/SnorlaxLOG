package de.relaxogames.snorlaxlog.bungeecord.api;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.mysql.SQLManager;

public class APIBungeeManager {
    private static final SnorlaxLOG snorlaxLOG = SnorlaxLOG.getInstance();
    private static final SQLManager sqlManager = new SQLManager();

    public static SnorlaxLOG getSnorlaxLOG() {
        return snorlaxLOG;
    }

    public static SQLManager getBungeeSqlManager() {
        return sqlManager;
    }
}