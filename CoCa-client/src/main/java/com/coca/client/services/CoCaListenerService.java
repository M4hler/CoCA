package com.coca.client.services;

import com.coca.client.annotations.CoCaListener;
import com.coca.client.events.Event;
import com.coca.client.listeners.CustomListener;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CoCaListenerService {
    @Getter
    private static final Map<Class<? extends Event>, CustomListener<? extends Event>> cocaListeners = new HashMap<>();
    private static final String PACKAGE_NAME = "com.coca.client.listeners";

    public CoCaListenerService() {
        var reflections = new Reflections(PACKAGE_NAME);
        var listeners = reflections.getTypesAnnotatedWith(CoCaListener.class);
        for (var listener : listeners) {
            var annotation = listener.getAnnotation(CoCaListener.class);
            var eventName = annotation.handles();

            try {
                var eventClass = Class.forName(eventName.getName());
                var eventInstance = eventClass.getDeclaredConstructor().newInstance();
                var listenerClass = Class.forName(listener.getName());
                var listenerInstance = listenerClass.getDeclaredConstructor().newInstance();

                if (listenerInstance instanceof CustomListener<?> customListener && eventInstance instanceof Event event) {
                    cocaListeners.put(event.getClass(), customListener);
                }
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public static CustomListener<? extends Event> getListener(Class<?> clazz) {
        for (var entry : cocaListeners.values()) {
            if (clazz.isInstance(entry)) {
                return entry;
            }
        }
        return null;
    }
}
