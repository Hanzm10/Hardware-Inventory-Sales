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
package com.github.hanzm_10.murico.app.view.scenes.auth;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.app.ui.factory.ButtonFactory;
import com.github.hanzm_10.murico.app.ui.panel.ResizableImagePanel;

import net.miginfocom.swing.MigLayout;

public class AuthSceneMain extends JPanel {

	private static final long serialVersionUID = 1L;

	private MigLayout layout;

	private JPanel container;
	private JButton loginButton;
	private JButton registerButton;
	private ResizableImagePanel imagePanel;

	/** Create the panel. */
	public AuthSceneMain(ActionListener actionListener) {
		layout = new MigLayout("", "[grow,center]", "[grow,center]");

		setLayout(layout);

		container = new JPanel();
		container.setLayout(new MigLayout("", "[grow,right][64px,center][grow,left]",
				"[:320.00:520px,grow 50,bottom][16px,center][grow,top]"));

		imagePanel = new ResizableImagePanel(new ImageIcon(AuthSceneMain.class.getResource("main_img.png")).getImage());

		loginButton = ButtonFactory.createButton("Log In", "login");
		loginButton.addActionListener(actionListener);

		registerButton = ButtonFactory.createButton("Create an account", "register");
		registerButton.addActionListener(actionListener);

		container.add(imagePanel, "cell 0 0 3 1,grow");
		container.add(loginButton, "cell 0 2");
		container.add(registerButton, "cell 2 2");

		add(container, "flowx,cell 0 0");
	}
}
