package com.cthulhu.controllers;

import com.cthulhu.views.SessionView;

public class SessionController extends AbstractController<SessionView> {
    public SessionController(boolean isAdmin) {
        view = new SessionView(isAdmin);
    }
}
