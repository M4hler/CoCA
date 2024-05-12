package com.cthulhu.services;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.Event;
import jakarta.jms.MessageListener;
import lombok.Getter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class CoCaListenerService {
    private static final Map<Event, MessageListener> cocaListeners = new HashMap<>();

    public CoCaListenerService() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(CoCaListener.class));
        var candidates = provider.findCandidateComponents("com.cthulhu");

        for(var candidate : candidates) {
            if(candidate instanceof AnnotatedBeanDefinition abd) {
                var attributes = abd.getMetadata().getAnnotationAttributes(CoCaListener.class.getCanonicalName());
                if(attributes != null) {
                    var eventName = attributes.get("handles");
                    var eventClass = Class.forName(eventName.toString().substring(6)); //strips 'class' prefix
                    var eventInstance = eventClass.getDeclaredConstructor().newInstance();
                    var listenerClass = Class.forName(candidate.getBeanClassName());
                    var listenerInstance = listenerClass.getDeclaredConstructor().newInstance();

                    if(listenerInstance instanceof MessageListener listener && eventInstance instanceof Event event) {
                        System.out.println("adding to map");
                        cocaListeners.put(event, listener);
                    }
                }
            }
        }
    }
}
