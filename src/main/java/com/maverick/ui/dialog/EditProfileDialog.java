package com.maverick.ui.dialog;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Sex;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

import static com.maverick.utils.Messages.*;
import static com.maverick.utils.UIUtils.getLabelGridBagConstraints;
import static com.maverick.utils.UIUtils.getTextFieldGridBagConstraints;
import static javax.swing.JOptionPane.*;

public class EditProfileDialog extends JDialog {

    private JTextField emailField = new JTextField();
    private JTextField nameField = new JTextField();
    private JComboBox<Integer> ageComboBox = new JComboBox<>(IntStream.rangeClosed(5, 80).boxed().toArray(Integer[]::new));
    private JComboBox<Sex> sexComboBox = new JComboBox<>(Sex.values());
    private JComboBox<String> countryComboBox = new JComboBox<>(Arrays.stream(Locale.getISOCountries()).map(country -> new Locale("", country).getDisplayCountry(Locale.ENGLISH)).toArray(String[]::new));

    public EditProfileDialog(Frame owner) {

        super(owner, true);

        setTitle("Edit Profile");
        setLayout(new GridBagLayout());
        setResizable(false);

        JLabel[] labels = {new JLabel("Email"), new JLabel("Login"), new JLabel("Age"), new JLabel("Sex"), new JLabel("Country")};
        for (int i = 0; i < labels.length; i++)
            add(labels[i], getLabelGridBagConstraints(0, i));

        BackendlessUser currentUser = Backendless.UserService.CurrentUser();

        emailField.setPreferredSize(new Dimension(200, 30));
        emailField.setText(currentUser.getEmail());
        add(emailField, getTextFieldGridBagConstraints(1, 0));

        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setText(currentUser.getProperty("name").toString());
        add(nameField, getTextFieldGridBagConstraints(1, 1));

        ageComboBox.setPreferredSize(new Dimension(200, 30));
        ageComboBox.setSelectedItem(currentUser.getProperty("age"));
        add(ageComboBox, getTextFieldGridBagConstraints(1, 2));

        sexComboBox.setPreferredSize(new Dimension(200, 30));
        sexComboBox.setSelectedItem(currentUser.getProperty("sex"));
        add(sexComboBox, getTextFieldGridBagConstraints(1, 3));

        countryComboBox.setPreferredSize(new Dimension(200, 30));
        countryComboBox.setSelectedItem(currentUser.getProperty("country"));
        add(countryComboBox, getTextFieldGridBagConstraints(1, 4));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        okButton.addActionListener(e -> {

            currentUser.setEmail(emailField.getText());
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", nameField.getText());
            properties.put("sex", sexComboBox.getSelectedItem());
            properties.put("country", countryComboBox.getSelectedItem());
            properties.put("age", ageComboBox.getSelectedItem());
            currentUser.putProperties(properties);

            Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    showMessageDialog(EditProfileDialog.this, EDIT_PROFILE_COMPLETE, SUCCESS_TITLE, INFORMATION_MESSAGE);
                    dispose();
                    setVisible(false);
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(EditProfileDialog.this, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
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