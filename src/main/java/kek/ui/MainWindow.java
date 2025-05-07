package kek.ui;

import javax.swing.*;

public final class MainWindow extends JFrame {
    public MainWindow() {
        super("Modular arithmetic algorithms");

        setSize(640, 480);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setFont(Fonts.TAB_FONT);

        tabbedPane.addTab("Калькулятор", new CalculatorTab());
        tabbedPane.addTab("Сравнения", new CongruencesTab());

        add(tabbedPane);
    }
}
