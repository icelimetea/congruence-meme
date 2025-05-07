package kek;

import kek.ui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public final class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        new MainWindow().setVisible(true);
    }
}
