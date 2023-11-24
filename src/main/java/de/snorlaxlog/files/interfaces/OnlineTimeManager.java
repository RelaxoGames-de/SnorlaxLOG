package de.snorlaxlog.files.interfaces;
import java.util.HashMap;

public class OnlineTimeManager {

    private static HashMap<LOGPlayer, Long> inStampTime = new HashMap<>();
    public void stampIn(LOGPlayer player){
        if (inStampTime.containsKey(player))return;
        long time = System.currentTimeMillis();
        inStampTime.put(player, time);
    }

    public static HashMap<LOGPlayer, Long> getInStampTime() {
        return inStampTime;
    }

    public boolean isCaptured(LOGPlayer logPlayer){
        return inStampTime.containsKey(logPlayer);
    }
}
