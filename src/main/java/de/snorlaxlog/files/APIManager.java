package de.snorlaxlog.files;

import de.snorlaxlog.SnorlaxLOG;
import de.snorlaxlog.mysql.MySQL;
import de.snorlaxlog.mysql.SQLManager;

public class APIManager {

    private MySQL mySQL = SnorlaxLOG.getInstance().mySQL;
    private SQLManager sqlManager = new SQLManager();
    private LanguageManager languageManager = new LanguageManager();

    public MySQL getMySQL() {
        return mySQL;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
