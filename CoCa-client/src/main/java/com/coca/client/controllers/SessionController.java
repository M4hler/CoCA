package com.coca.client.controllers;

import com.coca.client.events.BladeRunnerDataEvent;
import com.coca.client.events.RollResultEvent;
import com.coca.client.listeners.BladeRunnerDataEventListener;
import com.coca.client.listeners.JoinEventListener;
import com.coca.client.listeners.RollResultEventListener;
import com.coca.client.models.BladeRunner;
import com.coca.client.services.CoCaListenerService;
import com.coca.client.views.SessionView;
import com.coca.client.events.JoinEvent;
import com.coca.client.events.ShiftChangeResultEvent;
import com.coca.client.listeners.ShiftChangeResultEventListener;

public class SessionController extends AbstractController<SessionView> {
    private final MainController mainController;
    private final String bladeRunnerName;

    public SessionController(MainController mainController, boolean isAdmin, BladeRunner bladeRunner) {
        this.mainController = mainController;
        if(bladeRunner != null) {
            bladeRunnerName = bladeRunner.getName();
        }
        else {
            bladeRunnerName = "";
        }

        var joinEventListener = (JoinEventListener) CoCaListenerService.getListener(JoinEventListener.class);
        var bladeRunnerDataEventListener = (BladeRunnerDataEventListener) CoCaListenerService.getListener(BladeRunnerDataEventListener.class);
        var rollResultEventListener = (RollResultEventListener) CoCaListenerService.getListener(RollResultEventListener.class);
        var shiftChangeResultEventListener = (ShiftChangeResultEventListener) CoCaListenerService.getListener(ShiftChangeResultEventListener.class);

        view = new SessionView(isAdmin, bladeRunner, mainController.getQueue(), mainController.getConnectionFactory());

        if(joinEventListener != null) {
            joinEventListener.setHook(this::addToVBox);
        }

        if(bladeRunnerDataEventListener != null) {
            bladeRunnerDataEventListener.setHook(this::addToTreeView);
        }

        if(rollResultEventListener != null) {
            rollResultEventListener.setHook(this::addToVBoxRollResult);
        }

        if(shiftChangeResultEventListener != null) {
            shiftChangeResultEventListener.setHook(this::changeShift);
        }
    }

    public void addToVBox(JoinEvent event) {
        view.addToVBox(event.getName());
    }

    public void addToTreeView(BladeRunnerDataEvent event) {
        view.addToTreeView(event.getBladeRunner().getName());
    }

    public void addToVBoxRollResult(RollResultEvent event) {
        view.addToVBoxRollResult(event.getBladeRunnerName(), event.getAttribute(), event.getSkill(), event.getAttributeValue(),
                event.getSkillValue(), event.getDiceRolls(), event.getRollTypes(), event.getSuccesses());

        if(bladeRunnerName.equals(event.getBladeRunnerName())) {
            view.setPushButtonVisible(event.isCanPush());
            view.setAcceptButtonVisible(event.isCanPush());
        }
    }

    public void changeShift(ShiftChangeResultEvent event) {
        view.changeShift(event.getShift());
    }
}
