package com.github.hanzm_10.murico.app.scenes.auth;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.github.hanzm_10.murico.app.ui.ResizableImagePanel;
import net.miginfocom.swing.MigLayout;

public class AuthSceneMain extends JPanel {

    private static final long serialVersionUID = 1L;

    private ActionListener actionListener;

    /**
     * Create the panel.
     */
    public AuthSceneMain(ActionListener actionListener) {
        this.actionListener = actionListener;
        setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));

        var panel = new JPanel();
        add(panel, "flowx,cell 0 0");
        panel.setLayout(new MigLayout("", "[grow,right][64px,center][grow,left]",
                "[:320.00:520px,grow 50,bottom][72.00px,grow,top]"));

        var lblNewLabel = new ResizableImagePanel(
                new ImageIcon(getClass().getResource("/assets/images/auth-main_img.png"))
                        .getImage());
        lblNewLabel.setPreferredSize(new Dimension(1280, 720));
        panel.add(lblNewLabel, "cell 0 0 3 1");

        var btnNewButton = new JButton("Log In");
        btnNewButton.setActionCommand("login");
        btnNewButton.addActionListener(actionListener);
        btnNewButton.setPreferredSize(new Dimension(160, 48));
        panel.add(btnNewButton, "cell 0 1");

        var btnNewButton_1 = new JButton("Create an account");
        btnNewButton_1.setActionCommand("register");
        btnNewButton_1.addActionListener(actionListener);
        btnNewButton_1.setPreferredSize(new Dimension(160, 48));
        panel.add(btnNewButton_1, "cell 2 1");
    }

}
