package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;

import static com.backendless.Backendless.UserService;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LoginFrame extends JFrame {

    private static LoginFrame instance = new LoginFrame();

    public static LoginFrame getInstance() {
        return instance;
    }

    private JTextField loginField = new JTextField();
    private JTextField passwordField = new JPasswordField();

    private LoginFrame() {

        JLabel[] labels = new JLabel[2];
        labels[0] = new JLabel("Login");
        labels[1] = new JLabel("Password");

        setTitle("Login");
        setLayout(new GridBagLayout());
        setResizable(false);

        for (int i = 0; i < labels.length; i++)
            add(labels[i], new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(20, 10, 2, 2), 0, 0));


        loginField.setPreferredSize(new Dimension(200, 30));
        add(loginField, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        passwordField.setPreferredSize(new Dimension(200, 30));
        add(passwordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton registrationButton = new JButton("Registration");
        JButton forgotPasswordButton = new JButton("Forgot password?");
        panel.add(okButton);
        panel.add(registrationButton);
        panel.add(forgotPasswordButton);

        add(panel, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        okButton.addActionListener(e -> UserService.login(loginField.getText(), passwordField.getText(), new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                new MainFrame();
                dispose();
                setVisible(false);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                showMessageDialog(LoginFrame.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
            }
        }));

        registrationButton.addActionListener(e -> new RegistrationDialog(LoginFrame.this));

        forgotPasswordButton.addActionListener(e -> new ForgotPasswordDialog(LoginFrame.this));
    }
}