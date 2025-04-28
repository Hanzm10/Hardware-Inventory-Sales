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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestSceneManager {

	public static void main(String[] args) {
		var frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Test Scene Manager");
		SceneManager sceneManager = new SceneManager();

		sceneManager.registerScene("TestDummyScene/1", _ -> new TestDummyScene(0, _ -> {
			SwingUtilities.invokeLater(() -> {
				sceneManager.navigateTo("TestDummyScene/2");
			});
		}));

		sceneManager.registerScene("TestDummyScene/2", _ -> new TestDummyScene(1, _ -> {
			SwingUtilities.invokeLater(() -> {
				sceneManager.navigateTo("TestDummyScene/3");
			});
		}));

		sceneManager.registerScene("TestDummyScene/3", _ -> new TestDummyScene(2, _ -> {
			SwingUtilities.invokeLater(() -> {
				sceneManager.navigateTo("TestDummyScene/1");
			});
		}));

		var rootContainer = sceneManager.getRootContainer();

		rootContainer.setPreferredSize(new Dimension(1280, 720));

		frame.add(rootContainer);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		SwingUtilities.invokeLater(() -> {
			sceneManager.navigateTo("TestDummyScene/1");
		});
	}
}
