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
package com.github.hanzm_10.murico.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.UIManager;
import com.github.hanzm_10.murico.app.managers.scenes.RootSceneManager;
import com.github.hanzm_10.murico.core.constants.AppConfig;

public class MuricoAppWindow extends JFrame {

    private static final long serialVersionUID = 2596513398187183073L;

    RootSceneManager rootSceneManager;
    String title;

    public MuricoAppWindow() {
        super();

        title = AppConfig.getInstance().getAppTitle() + " "
                + AppConfig.getInstance().getAppVersion();
        rootSceneManager = new RootSceneManager();

        var size = sizeFromTitle(title);

        setTitle(title);

        if (size.width < rootSceneManager.getPreferredSize().width) {
            size.width = rootSceneManager.getPreferredSize().width;
        }

        if (size.height < rootSceneManager.getPreferredSize().height) {
            size.height = rootSceneManager.getPreferredSize().height;
        }

        setPreferredSize(size);
        add(rootSceneManager, BorderLayout.CENTER);
        pack();

        // TODO: prompt user on exit when there are unsaved changes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Dimension sizeFromTitle(String title) {
        var font = UIManager.getFont("defaultFont");
        var metrics = getFontMetrics(font);
        var width = metrics.stringWidth(title) + 146;
        var height = metrics.getHeight() + 50;
        return new Dimension(width, height);
    }

}
