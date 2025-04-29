/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.navigation;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public class TestDummyScene implements Scene {
    JPanel view;
    JLabel label;
    JButton button;

    int count;

    ActionListener actionListener;

    public TestDummyScene(int count, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.count = count;
    }

    @Override
    public String getSceneName() {
        return "test" + count;
    }

    @Override
    public JPanel getSceneView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        label = new JLabel("Test Dummy Scene " + count);
        label.setBounds(0, 0, 200, 50);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        button = new JButton("Go to next scene");
        button.setBounds(0, 50, 200, 50);
        button.addActionListener(actionListener);

        view.add(label);
        view.add(button);
    }

    @Override
    public void onShow() {
    }

    @Override
    public boolean onDestroy() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    }
}
