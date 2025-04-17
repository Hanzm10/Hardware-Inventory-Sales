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
package com.github.hanzm_10.murico.core.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.core.constants.Directories;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private static final Logger LOGGER = MuricoLogUtils.getLogger(GlobalUncaughtExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		try {
			var fileHandler = new FileHandler(Directories.LOGS_DIRECTORY + "/global_uncaught_exception_%u.log",
					1024 * 1024, 5, true);
			fileHandler.setFormatter(new SimpleFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.log(Level.SEVERE, "Uncaught exception in thread: " + t.getName(), e);

			fileHandler.close();
		} catch (Exception ex) {
			LOGGER.severe("Failed to save log of uncaught exception: " + ex.getMessage());
		}

		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		});
	}
}
