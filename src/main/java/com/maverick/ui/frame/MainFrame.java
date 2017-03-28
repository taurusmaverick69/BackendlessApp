package com.maverick.ui.frame;

import com.maverick.ui.BackendlessTree;
import com.maverick.ui.ProfilePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(LoginFrame loginFrame) {

        setTitle("Main");
        setLayout(new GridBagLayout());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profile", new ProfilePanel(this, loginFrame));
        tabbedPane.addTab("Files", new BackendlessTree(this));

        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}