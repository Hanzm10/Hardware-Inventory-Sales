/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

    /** Create the panel. */
    public AuthSceneMain(ActionListener actionListener) {
        this.actionListener = actionListener;
        setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));

        var panel = new JPanel();
        add(panel, "flowx,cell 0 0");
        panel.setLayout(new MigLayout("", "[grow,right][64px,center][grow,left]",
                "[:320.00:520px,grow 50,bottom][72.00px,grow,top]"));

        var resource = AuthSceneMain.class.getResource("/assets/images/auth-main_img.png");
        var lblNewLabel = new ResizableImagePanel(new ImageIcon(resource).getImage());
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
