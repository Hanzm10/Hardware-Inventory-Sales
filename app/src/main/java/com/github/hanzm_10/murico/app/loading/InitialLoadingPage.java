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
package com.github.hanzm_10.murico.app.loading;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import net.miginfocom.swing.MigLayout;

public class InitialLoadingPage extends JWindow {
    private static final long serialVersionUID = 1L;
    JProgressBar progressBar;

    public InitialLoadingPage() {
        var contentPane = getContentPane();
        var container = new JPanel();

        progressBar = new JProgressBar();

        contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
        container.setLayout(new MigLayout("", "[100px:n:200px,grow,shrink 50]", "[grow]"));

        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");

        contentPane.add(container, "grow");
        container.add(progressBar, "alignx center,aligny center");

        var size = new Dimension(1280, 720);
        setSize(size);
        setPreferredSize(size);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    public void setProgressLabel(String label) {
        progressBar.setString(label);
    }
}
