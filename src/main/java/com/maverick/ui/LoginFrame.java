package com.maverick.ui;

import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;

import static com.backendless.Backendless.UserService;
import static com.maverick.utils.UIUtils.*;
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

        setTitle("Login");
        setLayout(new GridBagLayout());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel[] labels = {new JLabel("Login"), new JLabel("Password")};

        for (int i = 0; i < labels.length; i++)
            add(labels[i], getLabelGridBagConstraints(0, i));

        loginField.setPreferredSize(new Dimension(200, 30));
        add(loginField, getTextFieldGridBagConstraints(1, 0));

        passwordField.setPreferredSize(new Dimension(200, 30));
        add(passwordField, getTextFieldGridBagConstraints(1, 1));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton registrationButton = new JButton("Registration");
        JButton forgotPasswordButton = new JButton("Forgot password?");
        panel.add(okButton);
        panel.add(registrationButton);
        panel.add(forgotPasswordButton);

        add(panel, getPanelGridBagConstraints(2));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        okButton.addActionListener(e -> UserService.login(loginField.getText(), passwordField.getText(), new AsyncCallback<BackendlessUser>() {

            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                MainFrame.getInstance();
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