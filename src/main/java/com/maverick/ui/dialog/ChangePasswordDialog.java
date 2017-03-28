package com.maverick.ui.dialog;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.maverick.utils.Messages.EDIT_PROFILE_COMPLETE;
import static com.maverick.utils.Messages.ERROR_TITLE;
import static com.maverick.utils.Messages.SUCCESS_TITLE;
import static com.maverick.utils.UIUtils.getLabelGridBagConstraints;
import static com.maverick.utils.UIUtils.getPanelGridBagConstraints;
import static com.maverick.utils.UIUtils.getTextFieldGridBagConstraints;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class ChangePasswordDialog extends JDialog {

    public ChangePasswordDialog(Frame owner) {

        super(owner, true);
        setTitle("Change password");
        setLayout(new GridBagLayout());
        setResizable(false);

        JLabel[] labels = {
                new JLabel("Current password"),
                new JLabel("New password"),
                new JLabel("Confirm password")};

        for (int i = 0; i < labels.length; i++)
            add(labels[i], getLabelGridBagConstraints(0, i));

        JTextField currentPasswordField = new JPasswordField();
        JTextField newPasswordField = new JPasswordField();
        JTextField confirmPasswordField = new JPasswordField();

        JTextField[] fields = {
                currentPasswordField,
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


        okButton.addActionListener(e -> {

//            currentUser.setEmail(emailField.getText());
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("name", nameField.getText());
//            properties.put("sex", sexComboBox.getSelectedItem());
//            properties.put("country", countryComboBox.getSelectedItem());
//            properties.put("age", ageComboBox.getSelectedItem());
//            currentUser.putProperties(properties);
//
//            Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
//                @Override
//                public void handleResponse(BackendlessUser backendlessUser) {
//                    showMessageDialog(EditProfileDialog.this, EDIT_PROFILE_COMPLETE, SUCCESS_TITLE, INFORMATION_MESSAGE);
//                    dispose();
//                    setVisible(false);
//                }
//
//                @Override
//                public void handleFault(BackendlessFault backendlessFault) {
//                    showMessageDialog(EditProfileDialog.this, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
//                }
//            });
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