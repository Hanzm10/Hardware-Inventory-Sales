package com.github.hanzm_10.murico.app.scenes.auth;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AuthSceneRegister extends JPanel {

    private static final long serialVersionUID = 1L;

    private ActionListener actionListener;

    /**
     * Create the panel.
     */
    public AuthSceneRegister(ActionListener actionListener) {
        setLayout(new BorderLayout());

        this.actionListener = actionListener;
    }

}
