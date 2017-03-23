package com.maverick.ui.dialog;

import com.backendless.Backendless;
import com.maverick.ui.BackendlessTree;
import com.maverick.utils.FileUtils;
import com.maverick.utils.Messages;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static com.maverick.utils.BackendlessUtils.getBackendlessPathFromTreePath;
import static com.maverick.utils.Messages.*;
import static com.maverick.utils.Messages.DIRECTORY_CREATED;
import static com.maverick.utils.UIUtils.*;
import static javax.swing.JOptionPane.*;

public class CreateDirectoryDialog extends JDialog {

    private JTextField directoryNameField = new JTextField();

    public CreateDirectoryDialog(Frame owner) {

        super(owner, true);
        setTitle("Create directory");
        setLayout(new GridBagLayout());
        setResizable(false);

        add(new JLabel("Directory name"), getLabelGridBagConstraints(0, 0));

        directoryNameField.setPreferredSize(new Dimension(200, 30));
        add(directoryNameField, getTextFieldGridBagConstraints(1, 0));

        JPanel panel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.setEnabled(false);
        JButton cancelButton = new JButton("Cancel");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, getPanelGridBagConstraints(2));

        directoryNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                okButton.setEnabled(!directoryNameField.getText().isEmpty());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                okButton.setEnabled(!directoryNameField.getText().isEmpty());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                okButton.setEnabled(!directoryNameField.getText().isEmpty());
            }
        });

        okButton.addActionListener(e -> {
            String backendlessPath = getBackendlessPathFromTreePath(BackendlessTree.getInstance().getSelectionPath(), directoryNameField.getText());
            try {
                Backendless.Files.upload(FileUtils.readme, backendlessPath);
            } catch (Exception ex) {
                showMessageDialog(CreateDirectoryDialog.this, ex.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
                return;
            }
            showMessageDialog(CreateDirectoryDialog.this, DIRECTORY_CREATED, SUCCESS_TITLE, INFORMATION_MESSAGE);
            dispose();
            setVisible(false);
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