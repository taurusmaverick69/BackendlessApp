package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.ui.dialog.ChangePasswordDialog;
import com.maverick.ui.dialog.EditProfileDialog;
import com.maverick.ui.frame.LoginFrame;
import com.maverick.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

import static com.maverick.utils.Messages.ERROR_TITLE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class ProfilePanel extends JPanel {

    private MainFrame mainFrame;
    private LoginFrame loginFrame;

    public ProfilePanel(MainFrame mainFrame, LoginFrame loginFrame) {

        this.mainFrame = mainFrame;
        this.loginFrame = loginFrame;

        setLayout(new GridBagLayout());
        add(getPhotoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));
        add(getInfoPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));
    }

    private JPanel getPhotoPanel() {

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

    private JPanel getInfoPanel() {

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
                new JLabel("Information", SwingConstants.CENTER),
                new JLabel("Name: " + backendlessUser.getProperty("name")),
                new JLabel("Email: " + backendlessUser.getProperty("email")),
                new JLabel("Age: " + backendlessUser.getProperty("age")),
                new JLabel("Sex: " + backendlessUser.getProperty("sex")),
                new JLabel("Country: " + backendlessUser.getProperty("country")),
                new JLabel("Last logged: " + backendlessUser.getProperty("lastLogin")),
                new JLabel("Created: " + backendlessUser.getProperty("created")),
                buttonsPanel
        };

        for (int i = 0; i < components.length; i++) {
            panel.add(components[i], new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(10, 10, 0, 10), 0, 0));
        }
        editButton.addActionListener(e -> new EditProfileDialog(mainFrame));
        changePasswordButton.addActionListener(e -> new ChangePasswordDialog(mainFrame));

        logoutButton.addActionListener(e -> Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
                loginFrame.getLoginField().setText("");
                loginFrame.getPasswordField().setText("");
                loginFrame.setVisible(true);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                showMessageDialog(mainFrame, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
            }
        }));
        return panel;
    }
}