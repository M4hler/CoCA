package com.cthulhu.controllers;

import com.cthulhu.listeners.JoinEventListener;
import com.cthulhu.models.BladeRunner;
import com.cthulhu.services.CoCaListenerService;
import com.cthulhu.views.SessionView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionController extends AbstractController<SessionView> {
    private final MainController mainController;
    private final JoinEventListener joinEventListener;
    private SessionView sessionView;

    public SessionController(MainController mainController, boolean isAdmin, BladeRunner bladeRunner) {
        this.mainController = mainController;
        this.joinEventListener = (JoinEventListener) CoCaListenerService.getListener(JoinEventListener.class);
        view = new SessionView(isAdmin, bladeRunner, mainController.getQueue());

        if(joinEventListener != null) {
            joinEventListener.setHook(this::addToVBox);
        }
    }

    public void addToVBox(String message) {
        view.addToVBox(message);
    }

    public void test2(String name) {
        view.addToTreeView(name);
    }
}
