package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.maverick.ui.dialog.EditProfileDialog;
import com.maverick.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private static ProfilePanel instance = new ProfilePanel();

    public static ProfilePanel getInstance() {
        return instance;
    }

    private ProfilePanel() {
        setLayout(new GridBagLayout());
        add(getPhotoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));
        add(getInfoPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));
    }

    private static JPanel getPhotoPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        JLabel photoLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("dmcwwHdgrPk.jpg").getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
        photoLabel.setIcon(imageIcon);
        panel.add(photoLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(10, 10, 0, 10), 0, 0));

        JButton changePhotoButton = new JButton("Change photo");
        panel.add(changePhotoButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 10, 10), 0, 0));

        return panel;
    }

    private static JPanel getInfoPanel() {

        BackendlessUser backendlessUser = Backendless.UserService.CurrentUser();
        JPanel panel = new JPanel(new GridBagLayout());
        JButton editButton = new JButton("Edit Profile");
        JButton changePasswordButton = new JButton("Change password");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(editButton);
        buttonsPanel.add(changePasswordButton);
        buttonsPanel.add(logoutButton);

        JComponent[] components = {
                new JLabel("Information:"),
                new JLabel("Name: " + backendlessUser.getProperty("name")),
                new JLabel("Email: " + backendlessUser.getProperty("email")),
                new JLabel("Age: " + backendlessUser.getProperty("age")),
                new JLabel("Sex: " + backendlessUser.getProperty("sex")),
                new JLabel("Last logged: " + backendlessUser.getProperty("lastLogin")),
                new JLabel("Created: " + backendlessUser.getProperty("created")),
                buttonsPanel
        };

        for (int i = 0; i < components.length; i++) {
            panel.add(components[i], new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(10, 10, 0, 10), 0, 0));
        }
        editButton.addActionListener(e -> new EditProfileDialog(MainFrame.getInstance()));
        return panel;
    }
}