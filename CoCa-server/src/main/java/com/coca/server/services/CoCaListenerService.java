package com.coca.server.services;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.Event;
import com.coca.server.listeners.CustomListener;
import lombok.Getter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CoCaListenerService {
    @Getter
    private static final Map<Class<? extends Event>, CustomListener<? extends Event>> cocaListeners = new HashMap<>();

    public CoCaListenerService(ApplicationContext context) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(CoCaListener.class));
        var candidates = provider.findCandidateComponents("com.coca.server");

        for(var candidate : candidates) {
            if(candidate instanceof AnnotatedBeanDefinition abd) {
                var attributes = abd.getMetadata().getAnnotationAttributes(CoCaListener.class.getCanonicalName());
                if(attributes != null) {
                    var eventName = attributes.get("handles");
                    var eventClass = Class.forName(eventName.toString().substring(6)); //strips 'class' prefix
                    var eventInstance = eventClass.getDeclaredConstructor().newInstance();

                    var listenerClass = Class.forName(candidate.getBeanClassName());
                    var constructors = listenerClass.getConstructors();
                    var constructor = constructors[0];
                    for(var c : constructors) {
                        if(c.getParameterCount() > constructor.getParameterCount()) {
                            constructor = c;
                        }
                    }
                    var parameters = constructor.getParameterTypes();
                    Object[] beans = new Object[parameters.length];
                    for(int i = 0; i < parameters.length; i++) {
                        beans[i] = context.getBean(parameters[i]);
                    }
                    var listenerInstance = constructor.newInstance(beans);

                    if(listenerInstance instanceof CustomListener<?> listener && eventInstance instanceof Event event) {
                        cocaListeners.put(event.getClass(), listener);
                    }
                }
            }
        }
    }
}
