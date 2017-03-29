package com.maverick.ui.dialog;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import javax.swing.*;
import java.awt.*;

import static com.maverick.utils.Messages.*;
import static com.maverick.utils.UIUtils.*;
import static javax.swing.JOptionPane.*;

public class ChangePasswordDialog extends JDialog {

    public ChangePasswordDialog(Frame owner) {

        super(owner, true);
        setTitle("Change password");
        setLayout(new GridBagLayout());
        setResizable(false);

        JLabel[] labels = {
                new JLabel("New password"),
                new JLabel("Confirm password")};

        for (int i = 0; i < labels.length; i++)
            add(labels[i], getLabelGridBagConstraints(0, i));


        JTextField newPasswordField = new JPasswordField();
        JTextField confirmPasswordField = new JPasswordField();

        JTextField[] fields = {
                newPasswordField,
                confirmPasswordField};

        for (int i = 0; i < fields.length; i++) {
            fields[i].setPreferredSize(new Dimension(200, 30));
            add(fields[i], getTextFieldGridBagConstraints(1, i));
        }

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, getPanelGridBagConstraints(3));

        UserService userService = Backendless.UserService;
        BackendlessUser user = userService.CurrentUser();

        okButton.addActionListener(e -> {
            if (!newPasswordField.getText().equals(confirmPasswordField.getText())){
                showMessageDialog(owner, PASSWORDS_DONT_MATCH_WARNING, WARNING_TITLE, WARNING_MESSAGE);
                return;
            }
            user.setPassword(newPasswordField.getText());
            userService.update(user, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    showMessageDialog(owner, PASSWORD_HAS_BEEN_CHANGED, SUCCESS_TITLE, INFORMATION_MESSAGE);
                    userService.logout();
                    userService.login(user.getProperty("name").toString(), newPasswordField.getText());
                    dispose();
                    setVisible(false);
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(owner, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
                }
            });
        });

        cancelButton.addActionListener(e -> {
            dispose();
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}