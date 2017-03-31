package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.maverick.ui.frame.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.maverick.utils.Messages.*;
import static javax.swing.JOptionPane.*;

public class FeedbackPanel extends JPanel {


    public FeedbackPanel(MainFrame mainFrame) {

        setLayout(new GridBagLayout());

        JLabel[] labels = {
                new JLabel("To:"),
                new JLabel("Type:"),
                new JLabel("Subject:"),};

        for (int i = 0; i < labels.length; i++) {
            add(labels[i], new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL,
                    new Insets(15, 10, 0, 10), 0, 0));
        }

        JTextField toField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Error", "Advice"});
        JTextField subjectField = new JTextField();

        JComponent[] components = {
                toField, typeComboBox, subjectField
        };

        for (int i = 0; i < components.length; i++) {
            add(components[i], new GridBagConstraints(1, i, 1, 1, 10.0, 1.0,
                    GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL,
                    new Insets(10, 10, 0, 10), 1, 1));
        }

        JTextArea messageArea = new JTextArea();
        messageArea.setPreferredSize(new Dimension(700, 200));
        messageArea.setBorder(new LineBorder(Color.BLACK));
        add(messageArea, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));

        JButton sendButton = new JButton("Send");
        add(sendButton, new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 10), 0, 0));

        toField.setText("taurusmaverick69@gmail.com");

        String messageTitle = "<font color=\"%s\">This is %s!</font>";
        String messageBody = "<p>%s<p>";

        sendButton.addActionListener(e -> {

            String title = "";
            switch (typeComboBox.getSelectedItem().toString()) {
                case "Error":
                    title = String.format(messageTitle, "red", typeComboBox.getSelectedItem().toString());

                    break;
                case "Advice":
                    title = String.format(messageTitle, "green", typeComboBox.getSelectedItem().toString());
                    break;
            }
            String body = title + String.format(messageBody, messageArea.getText());
            Backendless.Messaging.sendHTMLEmail(subjectField.getText(), body, toField.getText(), new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void aVoid) {
                    showMessageDialog(mainFrame, MESSAGE_HAS_BEEN_SENT, SUCCESS_TITLE, INFORMATION_MESSAGE);
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(mainFrame, backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
                }
            });
        });
    }
}
