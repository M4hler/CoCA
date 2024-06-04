package com.cthulhu.controllers;

import com.cthulhu.models.BladeRunner;
import com.cthulhu.views.SessionView;

public class SessionController extends AbstractController<SessionView> {
    public SessionController(boolean isAdmin, BladeRunner bladeRunner) {
        view = new SessionView(isAdmin, bladeRunner);
    }

    public void test(String message) {
        view.addToVBox(message);
    }

    public void test2(String name) {
        view.addToTreeView(name);
    }
}
