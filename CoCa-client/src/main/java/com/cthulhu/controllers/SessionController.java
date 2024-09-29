package com.cthulhu.controllers;

import com.cthulhu.listeners.BladeRunnerDataEventListener;
import com.cthulhu.listeners.JoinEventListener;
import com.cthulhu.models.BladeRunner;
import com.cthulhu.services.CoCaListenerService;
import com.cthulhu.views.SessionView;

public class SessionController extends AbstractController<SessionView> {
    private final MainController mainController;
    private final JoinEventListener joinEventListener;
    private final BladeRunnerDataEventListener bladeRunnerDataEventListener;

    public SessionController(MainController mainController, boolean isAdmin, BladeRunner bladeRunner) {
        this.mainController = mainController;
        this.joinEventListener = (JoinEventListener) CoCaListenerService.getListener(JoinEventListener.class);
        this.bladeRunnerDataEventListener = (BladeRunnerDataEventListener) CoCaListenerService.getListener(BladeRunnerDataEventListener.class);
        view = new SessionView(isAdmin, bladeRunner, mainController.getQueue());

        if(joinEventListener != null) {
            joinEventListener.setHook(this::addToVBox);
        }

        if(bladeRunnerDataEventListener != null) {
            bladeRunnerDataEventListener.setHook(this::addToTreeView);
        }
    }

    public void addToVBox(String message) {
        view.addToVBox(message);
    }

    public void addToTreeView(String name) {
        view.addToTreeView(name);
    }
}
