package com.maverick.ui.frame;

import com.maverick.ui.BackendlessTree;
import com.maverick.ui.ProfilePanel;

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
        tabbedPane.addTab("Profile", ProfilePanel.getInstance());
        tabbedPane.addTab("Files", BackendlessTree.getInstance());

        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}