package com.maverick.ui;

import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.utils.Messages;
import com.maverick.utils.Sex;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

import static com.backendless.Backendless.Files;
import static com.backendless.Backendless.UserService;
import static com.maverick.utils.Messages.*;
import static javax.swing.JOptionPane.*;

public class RegistrationDialog extends JDialog {

    private JTextField emailField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField passwordField = new JPasswordField();
    private JTextField confirmPasswordField = new JPasswordField();
    private JComboBox<Integer> ageComboBox = new JComboBox<>(IntStream.range(5, 81).boxed().toArray(Integer[]::new));
    private JComboBox<Sex> sexComboBox = new JComboBox<>(Sex.values());
    private JComboBox<String> countryComboBox = new JComboBox<>(Arrays.stream(Locale.getISOCountries()).map(country -> new Locale("", country).getDisplayCountry(Locale.ENGLISH)).toArray(String[]::new));

    public RegistrationDialog(Frame owner) {
        super(owner, true);

        JLabel[] labels = new JLabel[7];
        labels[0] = new JLabel("Email");
        labels[1] = new JLabel("Login");
        labels[2] = new JLabel("Password");
        labels[3] = new JLabel("Confirm password");
        labels[4] = new JLabel("Age");
        labels[5] = new JLabel("Sex");
        labels[6] = new JLabel("Country");

        setTitle("Registration");
        setLayout(new GridBagLayout());
        setResizable(false);

        for (int i = 0; i < labels.length; i++)
            add(labels[i], new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(15, 10, 2, 2), 0, 0));

        emailField.setPreferredSize(new Dimension(200, 30));
        add(emailField, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));
        emailField.setText("taurusmaverick69@gmail.com");

        nameField.setPreferredSize(new Dimension(200, 30));
        add(nameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        passwordField.setPreferredSize(new Dimension(200, 30));
        add(passwordField, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        add(confirmPasswordField, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        ageComboBox.setPreferredSize(new Dimension(200, 30));
        add(ageComboBox, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        sexComboBox.setPreferredSize(new Dimension(200, 30));
        add(sexComboBox, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        countryComboBox.setPreferredSize(new Dimension(200, 30));
        add(countryComboBox, new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

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
                        Files.upload(new File("readme.txt"), backendlessUser.getProperty("name").toString() + "/shared with me");
                    } catch (Exception e1) {
                        e1.printStackTrace();
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