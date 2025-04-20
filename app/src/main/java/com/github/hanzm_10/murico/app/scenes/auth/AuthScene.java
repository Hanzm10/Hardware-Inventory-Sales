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

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AuthScene extends JPanel implements ActionListener {

	private static final long serialVersionUID = -322983138401356081L;

	private CardLayout cardLayout;

	private AuthSceneLogin authSceneLogin;
	private AuthSceneRegister authSceneRegister;
	private AuthSceneMain authSceneMain;

	public AuthScene() {
		super();

		cardLayout = new CardLayout();
		setLayout(cardLayout);

		authSceneLogin = new AuthSceneLogin(this);
		authSceneRegister = new AuthSceneRegister(this);
		authSceneMain = new AuthSceneMain(this);

		add(authSceneLogin, "login");
		add(authSceneRegister, "register");
		add(authSceneMain, "main");

		showMainScene();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(() -> {
			switch (e.getActionCommand()) {
				case "login" :
					showLoginScene();
					break;
				case "register" :
					showRegisterScene();
					break;
				case "main" :
					showMainScene();
					break;
				default :
					break;
			}
		});
	}

	public void showLoginScene() {
		cardLayout.show(this, "login");
	}

	public void showMainScene() {
		cardLayout.show(this, "main");
	}

	public void showRegisterScene() {
		cardLayout.show(this, "register");
	}
}
