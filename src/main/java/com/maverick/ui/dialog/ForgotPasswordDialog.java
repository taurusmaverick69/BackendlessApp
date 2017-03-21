package com.maverick.ui.dialog;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;

import static com.maverick.utils.Messages.PASSWORD_HAS_BEEN_SENT;
import static com.maverick.utils.Messages.SUCCESS;
import static com.maverick.utils.UIUtils.getLabelGridBagConstraints;
import static com.maverick.utils.UIUtils.getPanelGridBagConstraints;
import static com.maverick.utils.UIUtils.getTextFieldGridBagConstraints;
import static javax.swing.JOptionPane.*;

public class ForgotPasswordDialog extends JDialog {

    private JTextField loginField = new JTextField();

    public ForgotPasswordDialog(Frame owner) {

        super(owner, true);

        setTitle("Forgot password?");
        setLayout(new GridBagLayout());
        setResizable(false);

        add(new JLabel("Login"), getLabelGridBagConstraints(0, 0));

        loginField.setPreferredSize(new Dimension(200, 30));
        add(loginField, getTextFieldGridBagConstraints(1, 0));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, getPanelGridBagConstraints(2));

        okButton.addActionListener(e -> Backendless.UserService.restorePassword(loginField.getText(), new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                showMessageDialog(ForgotPasswordDialog.this, PASSWORD_HAS_BEEN_SENT, SUCCESS, INFORMATION_MESSAGE);
                dispose();
                setVisible(false);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                showMessageDialog(ForgotPasswordDialog.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
            }
        }));

        cancelButton.addActionListener(e -> {
            dispose();
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}