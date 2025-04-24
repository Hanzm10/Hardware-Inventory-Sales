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
package com.github.hanzm_10.murico.swingapp.ui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.github.hanzm_10.murico.swingapp.constants.Metadata;
import com.github.hanzm_10.murico.swingapp.scenes.SceneManager;
import com.github.hanzm_10.murico.swingapp.scenes.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.scenes.auth.AuthScene;

public class MainFrame extends JFrame {

    private class MainFrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // TODO: If a user is performing a task, ask if they want to save their progress
            // before closing the application
            dispose();
        }
    }

    public MainFrame() {
        addWindowListener(new MainFrameWindowListener());

        setTitle(Metadata.APP_TITLE + " " + Metadata.APP_VERSION);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        var sceneManager = new SceneManager();

        sceneManager.register("auth", _ -> new AuthScene(), AuthScene.authSceneGuard);

        SceneNavigator.setSceneManager(sceneManager);

        var rootContainer = sceneManager.getRootContainer();

        rootContainer.setPreferredSize(new Dimension(1280, 768));

        add(rootContainer);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        sceneManager.navigateTo("auth");
    }
}
