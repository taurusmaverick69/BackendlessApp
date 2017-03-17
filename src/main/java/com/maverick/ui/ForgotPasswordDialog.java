package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;

import static com.maverick.utils.Messages.SUCCESS;
import static com.maverick.utils.Messages.PASSWORD_HAS_BEEN_SENT;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class ForgotPasswordDialog extends JDialog {

    private JLabel loginLabel = new JLabel("Login");
    private JTextField loginField = new JTextField();

    public ForgotPasswordDialog(Frame owner) {

        super(owner, true);

        setTitle("Forgot password");
        setLayout(new GridBagLayout());
        setResizable(false);

        add(loginLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 10, 2, 2), 0, 0));

        loginField.setPreferredSize(new Dimension(200, 30));
        add(loginField, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        okButton.addActionListener(e -> {

            Backendless.UserService.restorePassword(loginField.getText(), new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void aVoid) {

                    System.out.println(aVoid);

                    showMessageDialog(ForgotPasswordDialog.this, PASSWORD_HAS_BEEN_SENT, SUCCESS, INFORMATION_MESSAGE);
                    dispose();
                    setVisible(false);
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(ForgotPasswordDialog.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
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
