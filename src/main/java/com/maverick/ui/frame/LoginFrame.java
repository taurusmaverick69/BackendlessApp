package com.maverick.ui.frame;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.logging.Logger;
import com.maverick.ui.dialog.ForgotPasswordDialog;
import com.maverick.ui.dialog.RegistrationDialog;

import javax.swing.*;
import java.awt.*;

import static com.backendless.Backendless.UserService;
import static com.maverick.utils.Messages.ERROR_TITLE;
import static com.maverick.utils.UIUtils.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LoginFrame extends JFrame {

    private JTextField loginField = new JTextField();
    private JTextField passwordField = new JPasswordField();

    private Logger logger = Backendless.Logging.getLogger("AuthenticationFailedLogger");
    private static String AUTHENTICATION_FAILED = "Authentication failed with login [%s] and password [%s]";

    public LoginFrame() {

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
                new MainFrame(LoginFrame.this);
                dispose();
                setVisible(false);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                if ("3003".equals(backendlessFault.getCode())) {
                    logger.error(String.format(AUTHENTICATION_FAILED, loginField.getText(), passwordField.getText()));
                }
                showMessageDialog(LoginFrame.this, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
            }
        }));

        registrationButton.addActionListener(e -> new RegistrationDialog(LoginFrame.this));

        forgotPasswordButton.addActionListener(e -> new ForgotPasswordDialog(LoginFrame.this));
    }

    public JTextField getLoginField() {
        return loginField;
    }

    public JTextField getPasswordField() {
        return passwordField;
    }
}