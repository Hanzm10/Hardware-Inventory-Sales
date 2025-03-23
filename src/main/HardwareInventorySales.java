package main;

import javax.swing.*;

public class HardwareInventorySales extends JFrame {
    private MainScreen mainScreen;

    public HardwareInventorySales() {
        this.initClasses();

        this.add(this.mainScreen);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initClasses() {
        this.mainScreen = new MainScreen(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HardwareInventorySales hardwareInventorySales = new HardwareInventorySales();
        });
    }
}
