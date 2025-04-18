package com.murico.app;

import java.io.File;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import com.murico.app.config.ExternalSettings;
import com.murico.app.dal.User.UserSessionDAL;
import com.murico.app.exceptions.handlers.GlobalUncaughtExceptionHandler;
import com.murico.app.managers.UserSessionManager;
import com.murico.app.utils.io.FileLoader;
import com.murico.app.view.AppWindow;
import com.murico.app.view.loading_indicators.splash_screen.AppInitializationSplashScreen;
import muricolaf.MuricoLaf;

public class Murico {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(new MuricoLaf());
    } catch (Exception e) {
      e.printStackTrace();
    }

    Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
    SwingUtilities.invokeLater(Murico::initialize);
  }

  private static void initialize() {
    new InitializationWorker(new AppInitializationSplashScreen()).execute();
  }

  private static void initializeApp() {
    new AppWindow();
  }

  private static class InitializationWorker extends SwingWorker<Void, String> {
    private AppInitializationSplashScreen splashScreen;

    public InitializationWorker(AppInitializationSplashScreen splashScreen) {
      this.splashScreen = splashScreen;
    }

    private static void initializeFileSystem() {
      var configDir = new File(FileLoader.getConfigurationDirectory());

      if (!configDir.exists()) {
        configDir.mkdirs();
        System.out.println("Configuration directory created: " + configDir.getAbsolutePath());
      }

      var logDir = new File(FileLoader.getLogsDirectory());

      if (!logDir.exists()) {
        logDir.mkdirs();
        System.out.println("Log directory created: " + logDir.getAbsolutePath());
      }
    }

    private static void initializeUserSession() {
      var sessionUid =
          ExternalSettings.getInstance().getProperty(ExternalSettings.SESSION_ID_KEY);
      var userSession = UserSessionDAL.selectSessionById(sessionUid);

      if (userSession.isExpired()) {
        ExternalSettings.getInstance().removeProperty(ExternalSettings.SESSION_ID_KEY);
        ExternalSettings.getInstance().save();
      } else if (userSession != null) {
        UserSessionManager.getInstance().setUserSession(userSession);
      }
    }

    @Override
    protected Void doInBackground() throws Exception {
      publish("Initializing file system...");
      initializeFileSystem();
      publish("Initializing session...");
      initializeUserSession();

      return null;
    }

    @Override
    protected void process(List<String> progressMessages) {
      var latestMessage = progressMessages.get(progressMessages.size() - 1);

      splashScreen.setProgressLabel(latestMessage);
    }

    @Override
    protected void done() {
      // TODO Auto-generated method stub
      super.done();
      splashScreen.dispose();
      initializeApp();
    }
  }
}
