package com.cthulhu.models;

import com.cthulhu.events.*;

import java.util.HashMap;
import java.util.Map;

public class MessageCode {
    private static final Map<Class<? extends Event>, Integer> messageCodes = fillMap();

    public static int getMessageCode(Class<? extends Event> event) {
        return messageCodes.get(event);
    }

    public static Class<? extends Event> getEvent(int messageCode) {
        for(var entry : messageCodes.entrySet()) {
            if(entry.getValue() == messageCode) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static Map<Class<? extends Event>, Integer> fillMap() {
        var messageCodes = new HashMap<Class<? extends Event>, Integer>();
        messageCodes.put(JoinEvent.class, 1);
        messageCodes.put(BladeRunnerDataEvent.class, 2);
        messageCodes.put(RollEvent.class, 3);
        messageCodes.put(RollResultEvent.class, 4);
        messageCodes.put(PushEvent.class, 5);
        messageCodes.put(AcceptEvent.class, 6);
        messageCodes.put(ShiftChangeEvent.class, 7);
        messageCodes.put(ShiftChangeResultEvent.class, 8);
        messageCodes.put(NpcDataEvent.class, 9);

        return messageCodes;
    }
}
