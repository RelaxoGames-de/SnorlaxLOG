package de.snorlaxlog.files.interfaces;

import java.sql.Timestamp;
import java.util.HashMap;

public class OnlineTimeManager {

    private HashMap<LOGPlayer, Long> inStampTime = new HashMap<>();
    private HashMap<LOGPlayer, Long> outStampTime = new HashMap<>();
    private HashMap<LOGPlayer, Timestamp> onlineList = new HashMap<>();



    public Timestamp getOnlineTime(LOGPlayer player){
        return onlineList.get(player);
    }

    public void stampIn(LOGPlayer player){

        long time = System.currentTimeMillis();
        inStampTime.put(player, time);

    }


}
