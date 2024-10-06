package com.cthulhu.controllers;

import com.cthulhu.events.BladeRunnerDataEvent;
import com.cthulhu.events.JoinEvent;
import com.cthulhu.events.RollResultEvent;
import com.cthulhu.listeners.BladeRunnerDataEventListener;
import com.cthulhu.listeners.JoinEventListener;
import com.cthulhu.listeners.RollResultEventListener;
import com.cthulhu.models.BladeRunner;
import com.cthulhu.services.CoCaListenerService;
import com.cthulhu.views.SessionView;

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

        JoinEventListener joinEventListener = (JoinEventListener) CoCaListenerService.getListener(JoinEventListener.class);
        BladeRunnerDataEventListener bladeRunnerDataEventListener = (BladeRunnerDataEventListener) CoCaListenerService.getListener(BladeRunnerDataEventListener.class);
        RollResultEventListener rollResultEventListener = (RollResultEventListener) CoCaListenerService.getListener(RollResultEventListener.class);
        view = new SessionView(isAdmin, bladeRunner, mainController.getQueue());

        if(joinEventListener != null) {
            joinEventListener.setHook(this::addToVBox);
        }

        if(bladeRunnerDataEventListener != null) {
            bladeRunnerDataEventListener.setHook(this::addToTreeView);
        }

        if(rollResultEventListener != null) {
            rollResultEventListener.setHook(this::addToVBoxRollResult);
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
        }
    }
}
