package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.FileInfo;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static javax.swing.JOptionPane.*;

public class MainFrame extends JFrame {

    private JTextField makeDirField = new JTextField();

    private JComboBox<String> fileComboBox = new JComboBox<>(
            Backendless.Files.listing(Backendless.UserService.CurrentUser().getProperty("name").toString()).getData().stream().filter(fileInfo ->
                    !fileInfo.getName().equals("shared+with+me")).map(FileInfo::getName).toArray(String[]::new)
    );

    private JButton makeDirButton = new JButton("Make dir");
    private JButton deleteFileOrDirectoryButton = new JButton("Delete file or directory");

    public MainFrame() {

        setTitle("Main");
        setLayout(new GridBagLayout());
        setResizable(false);

        makeDirField.setPreferredSize(new Dimension(200, 30));
        add(makeDirField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        add(makeDirButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        fileComboBox.setPreferredSize(new Dimension(200, 30));
        add(fileComboBox, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        deleteFileOrDirectoryButton.setPreferredSize(new Dimension(200, 30));
        add(deleteFileOrDirectoryButton, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 2, 2), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        makeDirButton.addActionListener(e -> {
            try {
                Backendless.Files.upload(new File("readme.txt"), Backendless.UserService.CurrentUser().getProperty("name") + "/" + makeDirField.getText(), new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(BackendlessFile backendlessFile) {
                        showMessageDialog(MainFrame.this, "CREATE", Messages.SUCCESS, INFORMATION_MESSAGE);
                        updateFileCombobox();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        showMessageDialog(MainFrame.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
                    }
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        deleteFileOrDirectoryButton.addActionListener(e -> {

            String name = Backendless.UserService.CurrentUser().getProperty("name").toString();
            Backendless.Files.removeDirectory(name + "/" + fileComboBox.getSelectedItem().toString(), new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void aVoid) {
                    showMessageDialog(MainFrame.this, "Directory removed", Messages.SUCCESS, INFORMATION_MESSAGE);
                    updateFileCombobox();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(MainFrame.this, backendlessFault.getMessage(), Messages.ERROR, ERROR_MESSAGE);
                }
            });
        });
    }

    private void updateFileCombobox() {
        String name = Backendless.UserService.CurrentUser().getProperty("name").toString();
        fileComboBox.removeAllItems();
        Backendless.Files.listing(name).getData()
                .stream()
                .filter(fileInfo -> !fileInfo.getName().equals("shared+with+me"))
                .map(FileInfo::getName)
                .forEach(directoryName -> fileComboBox.addItem(directoryName));
    }
}