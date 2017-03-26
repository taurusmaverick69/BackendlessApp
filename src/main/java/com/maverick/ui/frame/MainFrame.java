package com.maverick.ui.frame;

import com.maverick.ui.BackendlessFiles;
import sun.swing.icon.SortArrowIcon;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static MainFrame instance = new MainFrame();

    public static MainFrame getInstance() {
        return instance;
    }

    private MainFrame() {

        setTitle("Main");
        setLayout(new GridBagLayout());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profile", new JPanel());
        tabbedPane.addTab("Files", BackendlessFiles.getInstance());

        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));


        JLabel jLabel = new JLabel("TEST", new SortArrowIcon(true, "test"), JComponent.UNDEFINED_CONDITION);


        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        add(jLabel, new GridBagConstraints(0, 1, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}