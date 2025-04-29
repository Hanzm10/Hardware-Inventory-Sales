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
package com.github.hanzm_10.murico.swingapp.lib.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestMuricoLogger {
    private static final Logger LOGGER = MuricoLogger.getLogger(TestMuricoLogger.class);

    public static void main(String[] args) {
        // 🔥 Now test different types of logs
        LOGGER.info("Hello, this is an info log!");
        LOGGER.warning("Warning: something looks odd...");
        LOGGER.severe("Severe Error happened!");

        // 🔥 Multi-line log message
        LOGGER.info("This is a multi-line\nlog message\nto see how it behaves.");

        // 🔥 Log with parameters
        LOGGER.log(Level.INFO, "User {0} logged in from {1}", new Object[] { "Alice", "192.168.1.5" });

        // 🔥 Log with exception
        try {
            throw new RuntimeException("Test Exception!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Caught an exception:", e);
        }

        try {
            // Simulate a situation where an exception throws another exception with
            // suppressed ones
            throw new RuntimeException("Outer Exception", new Throwable("Inner Exception"));
        } catch (RuntimeException outerException) {
            // Suppress the exception and create the log record
            outerException.addSuppressed(new Exception("Suppressed Exception 1"));
            outerException.addSuppressed(new Exception("Suppressed Exception 2"));

            LOGGER.log(Level.SEVERE, "Caught an outer exception:", outerException);
        }

        try {
            throw new RuntimeException("Outer Exception", new Throwable("Cause of the problem"));
        } catch (RuntimeException outerException) {
            LOGGER.log(Level.SEVERE, "Caught an outer exception:", outerException);
        }
    }
}
