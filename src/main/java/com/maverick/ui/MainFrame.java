package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.FileInfo;
import com.maverick.utils.Messages;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        tabbedPane.addTab("Files", getFileScrollPane());
        add(tabbedPane, new GridBagConstraints(0, 0, 10, 10, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));


        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String name = Backendless.UserService.CurrentUser().getProperty("name").toString();
                Backendless.Files.listing(name, "*", true).getData().forEach(fileInfo -> {

                    System.out.println(fileInfo.getURL());

                });
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        makeDirButton.addActionListener(e -> {
            try {
                Backendless.Files.upload(new File("readmeFile.txt"), Backendless.UserService.CurrentUser().getProperty("name") + "/" + makeDirField.getText(), new AsyncCallback<BackendlessFile>() {
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

    private JScrollPane getFileScrollPane() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        DefaultMutableTreeNode tEst = new DefaultMutableTreeNode("TEst");
        tEst.add(new DefaultMutableTreeNode("tset"));
        root.add(tEst);
        JTree tree = new JTree(root);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        return scrollPane;
    }
}