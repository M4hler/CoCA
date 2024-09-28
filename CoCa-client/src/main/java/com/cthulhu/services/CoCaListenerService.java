package com.cthulhu.services;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.Event;
import com.cthulhu.listeners.CustomListener;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CoCaListenerService {
    @Getter
    private static final Map<Event, CustomListener<? extends Event>> cocaListeners = new HashMap<>();
    private static final String PACKAGE_NAME = "com.cthulhu.listeners";
    public CoCaListenerService() {
        var inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(PACKAGE_NAME.replaceAll("[.]", "/"));
        if(inputStream == null) {
            System.out.println("InputStream is null");
            return;
        }

        var bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        var set = bufferReader.lines()
                .filter(x -> x.endsWith(".class"))
                .map(this::getClass)
                .collect(Collectors.toSet());
        for(var s : set) {
            var annotation = s.getAnnotation(CoCaListener.class);
            if(annotation == null) {
                continue;
            }

            var eventName = annotation.handles();
            try {
                var eventClass = Class.forName(eventName.getName());
                var eventInstance = eventClass.getDeclaredConstructor().newInstance();
                var listenerClass = Class.forName(s.getName());
                var listenerInstance = listenerClass.getDeclaredConstructor().newInstance();

                if(listenerInstance instanceof CustomListener<?> listener && eventInstance instanceof Event event) {
                    cocaListeners.put(event, listener);
                }
            }
            catch(ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(PACKAGE_NAME + "."
                    + className.substring(0, className.lastIndexOf('.')));
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
}
