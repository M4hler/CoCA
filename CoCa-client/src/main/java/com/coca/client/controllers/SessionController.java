package com.coca.client.controllers;

import com.coca.client.events.*;
import com.coca.client.listeners.*;
import com.coca.client.models.BladeRunner;
import com.coca.client.models.MessageCode;
import com.coca.client.services.CoCaListenerService;
import com.coca.client.views.SessionView;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

public class SessionController extends AbstractController<SessionView> {
    private final MainController mainController;
    private final BladeRunner bladeRunner;
    private final JmsTemplate jmsTemplate;

    public SessionController(MainController mainController, boolean isAdmin, BladeRunner bladeRunner) {
        this.mainController = mainController;
        this.bladeRunner = bladeRunner;

        var joinEventListener = (JoinEventListener) CoCaListenerService.getListener(JoinEventListener.class);
        var bladeRunnerDataEventListener = (BladeRunnerDataEventListener) CoCaListenerService.getListener(BladeRunnerDataEventListener.class);
        var rollResultEventListener = (RollResultEventListener) CoCaListenerService.getListener(RollResultEventListener.class);
        var shiftChangeResultEventListener = (ShiftChangeResultEventListener) CoCaListenerService.getListener(ShiftChangeResultEventListener.class);
        var mainframeAllDataEventListener = (MainframeAllDataEventListener) CoCaListenerService.getListener(MainframeAllDataEventListener.class);

        jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(mainController.getConnectionFactory());
        var converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        jmsTemplate.setMessageConverter(converter);
        view = new SessionView(isAdmin, bladeRunner, mainController.getQueue(), jmsTemplate);

        if (joinEventListener != null) {
            joinEventListener.setHook(this::addToVBox);
        }

        if (bladeRunnerDataEventListener != null) {
            bladeRunnerDataEventListener.setHook(this::addToTreeView);
        }

        if (rollResultEventListener != null) {
            rollResultEventListener.setHook(this::addToVBoxRollResult);
        }

        if (shiftChangeResultEventListener != null) {
            shiftChangeResultEventListener.setHook(this::changeShift);
        }

        if (mainframeAllDataEventListener != null) {
            mainframeAllDataEventListener.setHook(this::getMainframeData);
        }

        requestMainframeData();
    }

    private void addToVBox(JoinEvent event) {
        view.addToChatBox(event.getName());
    }

    private void addToTreeView(BladeRunnerDataEvent event) {
        view.addToTreeView(event.getBladeRunner().getName());
    }

    private void addToVBoxRollResult(RollResultEvent event) {
        view.addToVBoxRollResult(event.getBladeRunnerId(), event.getAttribute(), event.getSkill(), event.getAttributeValue(),
                event.getSkillValue(), event.getDiceRolls(), event.getRollTypes(), event.getSuccesses());

        if (bladeRunner != null && bladeRunner.getId() == event.getBladeRunnerId()) {
            view.setPushButtonVisible(event.isCanPush());
            view.setAcceptButtonVisible(event.isCanPush());
        }
    }

    private void changeShift(ShiftChangeResultEvent event) {
        view.changeShift(event.getShift());
    }

    private void requestMainframeData() {
        var event = new MainframeAllDataRequestEvent();
        event.setMessageCode(MessageCode.getMessageCode(MainframeAllDataRequestEvent.class));
        jmsTemplate.convertAndSend(mainController.getQueue(), event);
    }

    private void getMainframeData(MainframeAllDataEvent event) {
        for (var data : event.getMainframeList()) {
            System.out.println("Data: " + data.getAuthor() + " " + data.getDescription());
        }
    }
}
