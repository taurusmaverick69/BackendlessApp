package com.maverick.ui.frame;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.FileInfo;
import com.maverick.ui.BackendlessTree;
import com.maverick.utils.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static javax.swing.JOptionPane.*;

public class MainFrame extends JFrame {

    private static MainFrame instance = new MainFrame();

    public static MainFrame getInstance() {
        return instance;
    }

    private JTextField makeDirField = new JTextField();
    private JComboBox<String> fileComboBox = new JComboBox<>(Backendless.Files.listing(Backendless.UserService.CurrentUser().getProperty("name").toString()).getData().stream().filter(fileInfo -> !fileInfo.getName().equals("shared+with+me")).map(FileInfo::getName).toArray(String[]::new));
    private JButton makeDirButton = new JButton("Make dir");
    private JButton deleteFileOrDirectoryButton = new JButton("Delete file or directory");

    private MainFrame() {

        setTitle("Main");
        setLayout(new GridBagLayout());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        makeDirField.setPreferredSize(new Dimension(200, 30));
//        add(makeDirField, getTextFieldGridBagConstraints(0, 0));
//
//        add(makeDirButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
//                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                new Insets(10, 10, 2, 2), 0, 0));
//
//        fileComboBox.setPreferredSize(new Dimension(200, 30));
//        add(fileComboBox, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
//                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                new Insets(10, 10, 2, 2), 0, 0));
//
//        deleteFileOrDirectoryButton.setPreferredSize(new Dimension(200, 30));
//        add(deleteFileOrDirectoryButton, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
//                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                new Insets(10, 10, 2, 2), 0, 0));


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Profile", new JPanel());
        tabbedPane.addTab("Files", getScrollPaneWithFileTree());
        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        makeDirButton.addActionListener(e -> {
            try {
                Backendless.Files.upload(new File("readmeFile.txt"), Backendless.UserService.CurrentUser().getProperty("name") + "/" + makeDirField.getText(), new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(BackendlessFile backendlessFile) {
                        showMessageDialog(MainFrame.this, "CREATE", Messages.SUCCESS_TITLE, INFORMATION_MESSAGE);
                        updateFileComboBox();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        showMessageDialog(MainFrame.this, backendlessFault.getMessage(), Messages.ERROR_TITLE, ERROR_MESSAGE);
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
                    showMessageDialog(MainFrame.this, "Directory removed", Messages.SUCCESS_TITLE, INFORMATION_MESSAGE);
                    updateFileComboBox();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    showMessageDialog(MainFrame.this, backendlessFault.getMessage(), Messages.ERROR_TITLE, ERROR_MESSAGE);
                }
            });
        });
    }

    private void updateFileComboBox() {
        String name = Backendless.UserService.CurrentUser().getProperty("name").toString();
        fileComboBox.removeAllItems();
        Backendless.Files.listing(name).getData()
                .stream()
                .filter(fileInfo -> !fileInfo.getName().equals("shared+with+me"))
                .map(FileInfo::getName)
                .forEach(directoryName -> fileComboBox.addItem(directoryName));
    }

    private JScrollPane getScrollPaneWithFileTree() {
        JScrollPane scrollPane = new JScrollPane(BackendlessTree.getInstance());
        scrollPane.setPreferredSize(new Dimension(700, 300));
        return scrollPane;
    }
}