package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.FileInfo;
import com.maverick.utils.Messages;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.util.Enumeration;
import java.util.stream.Collectors;

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

    private JScrollPane getScrollPaneWithFileTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);

        String name = Backendless.UserService.CurrentUser().getProperty("name").toString();
        buildTreeFromPaths(model, Backendless.Files.listing(name, "*", true).getData().stream().map(FileInfo::getURL).collect(Collectors.toList()));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.setRootVisible(false);
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        return scrollPane;
    }

    private void buildTreeFromPaths(DefaultTreeModel model, java.util.List<String> paths) {

        paths.forEach(path -> {
            // Fetch the root node
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

            // Split the string around the delimiter
            String[] strings = path.split("/");

            // Create a node object to use for traversing down the tree as it
            // is being created
            DefaultMutableTreeNode node = root;

            // Iterate of the string array
            for (String s : strings) {
                // Look for the index of a node at the current level that
                // has a value equal to the current string
                int index = childIndex(node, s);

                // Index less than 0, this is a new node not currently present on the tree
                if (index < 0) {
                    // Add the new node
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
                    node.insert(newChild, node.getChildCount());
                    node = newChild;
                }
                // Else, existing node, skip to the next string
                else {
                    node = (DefaultMutableTreeNode) node.getChildAt(index);
                }
            }
        });
    }

    private int childIndex(DefaultMutableTreeNode node, String childValue) {
        Enumeration<DefaultMutableTreeNode> children = node.children();
        int index = -1;

        while (children.hasMoreElements() && index < 0) {
            DefaultMutableTreeNode child = children.nextElement();

            if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
                index = node.getIndex(child);
            }
        }
        return index;
    }
}