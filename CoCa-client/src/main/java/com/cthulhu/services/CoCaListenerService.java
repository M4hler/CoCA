package com.cthulhu.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CoCaListenerService {
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
            System.out.println("S: " + s.getName());
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
