package de.relaxogames.snorlaxlog.bungeecord.api;

import de.relaxogames.snorlaxlog.bungeecord.SnorlaxLOG;
import de.relaxogames.snorlaxlog.bungeecord.mysql.SQLManager;

public class APIBungeeManager {
    private static final SnorlaxLOG snorlaxLOG = SnorlaxLOG.getInstance();
    private static final SQLManager sqlManager = new SQLManager();

    /**
     * Returns the instance of SnorlaxLOG.
     * 
     * @return the given SnorlaxLOG instance
     */
    public static SnorlaxLOG getSnorlaxLOG() {
        return snorlaxLOG;
    }

    /**
     * Returns the SQLManager instance used for BungeeCord operations.
     * 
     * @return the SQLManager instance for BungeeCord
     */
    public static SQLManager getBungeeSqlManager() {
        return sqlManager;
    }
}