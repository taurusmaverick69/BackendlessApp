package com.maverick.ui.dialog;

import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.FileUtils;
import com.maverick.utils.Messages;
import com.maverick.utils.Sex;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

import static com.backendless.Backendless.Files;
import static com.backendless.Backendless.UserService;
import static com.maverick.utils.Messages.*;
import static com.maverick.utils.UIUtils.getLabelGridBagConstraints;
import static com.maverick.utils.UIUtils.getTextFieldGridBagConstraints;
import static javax.swing.JOptionPane.*;

public class RegistrationDialog extends JDialog {

    private static final String SHARED_WITH_ME_DIRECTORY = "/shared with me";

    private JTextField emailField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField passwordField = new JPasswordField();
    private JTextField confirmPasswordField = new JPasswordField();
    private JComboBox<Integer> ageComboBox = new JComboBox<>(IntStream.rangeClosed(5, 80).boxed().toArray(Integer[]::new));
    private JComboBox<Sex> sexComboBox = new JComboBox<>(Sex.values());
    private JComboBox<String> countryComboBox = new JComboBox<>(Arrays.stream(Locale.getISOCountries()).map(country -> new Locale("", country).getDisplayCountry(Locale.ENGLISH)).toArray(String[]::new));

    public RegistrationDialog(Frame owner) {
        super(owner, true);

        setTitle("Registration");
        setLayout(new GridBagLayout());
        setResizable(false);

        JLabel[] labels = {new JLabel("Email"), new JLabel("Login"), new JLabel("Password"), new JLabel("Confirm password"), new JLabel("Age"), new JLabel("Sex"), new JLabel("Country")};
        for (int i = 0; i < labels.length; i++)
            add(labels[i], getLabelGridBagConstraints(0, i));

        emailField.setPreferredSize(new Dimension(200, 30));
        add(emailField, getTextFieldGridBagConstraints(1, 0));
        emailField.setText("taurusmaverick69@gmail.com");

        nameField.setPreferredSize(new Dimension(200, 30));
        add(nameField, getTextFieldGridBagConstraints(1, 1));

        passwordField.setPreferredSize(new Dimension(200, 30));
        add(passwordField, getTextFieldGridBagConstraints(1, 2));

        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        add(confirmPasswordField, getTextFieldGridBagConstraints(1, 3));

        ageComboBox.setPreferredSize(new Dimension(200, 30));
        add(ageComboBox, getTextFieldGridBagConstraints(1, 4));

        sexComboBox.setPreferredSize(new Dimension(200, 30));
        add(sexComboBox, getTextFieldGridBagConstraints(1, 5));

        countryComboBox.setPreferredSize(new Dimension(200, 30));
        add(countryComboBox, getTextFieldGridBagConstraints(1, 6));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, new GridBagConstraints(0, 7, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        okButton.addActionListener(e -> {
            if (passwordField.getText().isEmpty()) {
                showMessageDialog(RegistrationDialog.this, EMPTY_PASSWORD, WARNING, WARNING_MESSAGE);
                return;
            }
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                showMessageDialog(RegistrationDialog.this, PASSWORDS_DONT_MATCH_WARNING, WARNING, WARNING_MESSAGE);
                return;
            }
            BackendlessUser user = new BackendlessUser();
            user.setEmail(emailField.getText());
            user.setPassword(passwordField.getText());
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", nameField.getText());
            properties.put("sex", sexComboBox.getSelectedItem());
            properties.put("country", countryComboBox.getSelectedItem());
            properties.put("age", ageComboBox.getSelectedItem());
            user.putProperties(properties);
            UserService.register(user, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    showMessageDialog(RegistrationDialog.this, USER_SUCCESSFULLY_CREATED_CONFIRM_BY_EMAIL, SUCCESS, INFORMATION_MESSAGE);
                    try {
                        Files.upload(FileUtils.readme, backendlessUser.getProperty("name").toString() + SHARED_WITH_ME_DIRECTORY);
                    } catch (Exception ex) {
                        showMessageDialog(RegistrationDialog.this, ex.getMessage(), Messages.ERROR, ERROR_MESSAGE);
                    }
                    dispose();
                    setVisible(false);
                }
                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(RegistrationDialog.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
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