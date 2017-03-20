package com.maverick.ui;

import com.backendless.Backendless;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class BackendlessTree extends JTree {

    public BackendlessTree() {

        String name = Backendless.UserService.CurrentUser().getProperty("name").toString();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(name);
        DefaultTreeModel model = new DefaultTreeModel(root);
        setModel(model);

        buildTreeFromPaths(model, Backendless.Files.listing(name, "*", true).getData().stream().map(fileInfo -> fileInfo.getURL().replace(name + "/", "")).collect(Collectors.toList()));
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }

        JPopupMenu menu = new JPopupMenu();
        JMenuItem createDirectoryItem = new JMenuItem("Create directory");
        JMenuItem removeDirectoryItem = new JMenuItem("Remove directory");
        JMenuItem uploadFileItem = new JMenuItem("Upload file");
        JMenuItem downloadFileItem = new JMenuItem("Download file");
        JMenuItem removeFileItem = new JMenuItem("Remove file");

        menu.add(createDirectoryItem);
        menu.add(removeDirectoryItem);
        menu.add(uploadFileItem);
        menu.add(downloadFileItem);
        menu.add(removeFileItem);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {

                    enableAllMenuItems(menu.getSubElements());
                    int row = getClosestRowForLocation(e.getX(), e.getY());
                    setSelectionRow(row);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();

                    if (node.isLeaf()) {
                        createDirectoryItem.setEnabled(false);
                        removeDirectoryItem.setEnabled(false);
                        uploadFileItem.setEnabled(false);
                    } else {
                        if (node.isRoot()) {
                            removeDirectoryItem.setEnabled(false);
                        }
                        downloadFileItem.setEnabled(false);
                        removeFileItem.setEnabled(false);
                    }
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        createDirectoryItem.addActionListener(e -> {

            System.out.println(getSelectionPath());
        });

        removeDirectoryItem.addActionListener(e -> {

        });

        uploadFileItem.addActionListener(e -> {

        });

        downloadFileItem.addActionListener(e -> {

        });

        removeFileItem.addActionListener(e -> {

        });
    }

    private void buildTreeFromPaths(DefaultTreeModel model, java.util.List<String> paths) {

        paths.forEach(path -> {

            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            String[] strings = path.split("/");

            DefaultMutableTreeNode node = root;

            for (String s : strings) {
                int index = childIndex(node, s);
                if (index < 0) {
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
                    node.insert(newChild, node.getChildCount());
                    node = newChild;
                } else {
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

    private static void enableAllMenuItems(MenuElement... elements) {
        Arrays.stream(elements).map(menuElement -> (JMenuItem) menuElement).forEach(menuItem -> menuItem.setEnabled(true));
    }
}