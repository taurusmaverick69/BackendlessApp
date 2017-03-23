package com.maverick.ui;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.servercode.IBackendlessService;
import com.maverick.ui.dialog.CreateDirectoryDialog;
import com.maverick.ui.frame.MainFrame;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

import static com.backendless.Backendless.*;
import static com.maverick.utils.BackendlessUtils.getBackendlessPathFromTreePath;
import static com.maverick.utils.Messages.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.swing.JOptionPane.*;

public class BackendlessTree extends JTree {

    private static BackendlessTree instance = new BackendlessTree();

    public static BackendlessTree getInstance() {
        return instance;
    }

    private static String DESKTOP_PATH = System.getProperty("user.home") + "/Desktop";

    private String userName = Backendless.UserService.CurrentUser().getProperty("name").toString();

    private BackendlessTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(userName);
        DefaultTreeModel model = new DefaultTreeModel(root);
        buildTreeFromPaths(model, Backendless.Files.listing(userName, "*", true).getData().stream().map(fileInfo -> fileInfo.getURL().replace(userName + "/", "")).collect(toList()));
        setModel(model);
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
            new CreateDirectoryDialog(MainFrame.getInstance());
            updateModel();
        });

        removeDirectoryItem.addActionListener(e -> {
            int choose = showConfirmDialog(MainFrame.getInstance(), CONFIRM_REMOVE_DIRECTORY, REMOVE_TITLE, YES_NO_OPTION, QUESTION_MESSAGE);
            if (choose == YES_OPTION) {
                Backendless.Files.removeDirectory(getBackendlessPathFromTreePath(getSelectionPath()), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        updateModel();
                        showMessageDialog(MainFrame.getInstance(), DIRECTORY_REMOVED, SUCCESS_TITLE, INFORMATION_MESSAGE);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        showMessageDialog(MainFrame.getInstance(), backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
                    }
                });
            }
        });

        uploadFileItem.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser(DESKTOP_PATH);
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showDialog(MainFrame.getInstance(), "Upload");
            if (result == JFileChooser.APPROVE_OPTION) {
                Arrays.stream(chooser.getSelectedFiles()).forEach(file -> {
                    String path = getBackendlessPathFromTreePath(getSelectionPath());
                    try {
                        Backendless.Files.upload(file, path);
                    } catch (Exception ex) {
                        showMessageDialog(MainFrame.getInstance(), String.format(UPLOAD_FAILED, file.getName()), ERROR_TITLE, ERROR_MESSAGE);
                    }
                });
                updateModel();
                showMessageDialog(MainFrame.getInstance(), UPLOAD_COMPLETE, SUCCESS_TITLE, INFORMATION_MESSAGE);
            }
        });

        downloadFileItem.addActionListener(e -> {
            Stream<String> downloadStream = Stream.of(getUrl(), getApplicationId(), getVersion(), "files");
            Stream<String> pathStream = Arrays.stream(getSelectionPath().getPath()).map(Object::toString);
            String uri = Stream.concat(downloadStream, pathStream).collect(joining("/"));
            HttpGet httpGet = new HttpGet(uri);
            try (InputStream content = HttpClientBuilder.create().build().execute(httpGet).getEntity().getContent()) {
                FileUtils.copyInputStreamToFile(content, new File(DESKTOP_PATH, FilenameUtils.getName(uri)));
                showMessageDialog(MainFrame.getInstance(), FILE_DOWNLOADED, SUCCESS_TITLE, INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showMessageDialog(MainFrame.getInstance(), ex.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
            }
        });

        removeFileItem.addActionListener(e -> {

            int option = showConfirmDialog(MainFrame.getInstance(), CONFIRM_REMOVE_FILE, REMOVE_TITLE, YES_NO_OPTION, QUESTION_MESSAGE);
            if (option == YES_OPTION) {
                Backendless.Files.remove(getBackendlessPathFromTreePath(getSelectionPath()), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        updateModel();
                        showMessageDialog(MainFrame.getInstance(), FILE_REMOVED, SUCCESS_TITLE, INFORMATION_MESSAGE);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        showMessageDialog(MainFrame.getInstance(), backendlessFault.getMessage(), ERROR_TITLE, ERROR_MESSAGE);
                    }
                });
            }
        });
    }

    private void buildTreeFromPaths(DefaultTreeModel model, List<String> paths) {

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

    private void updateModel() {
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        ((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
        model.reload();
        buildTreeFromPaths(model, Backendless.Files.listing(userName, "*", true).getData().stream().map(fileInfo -> fileInfo.getURL().replace(userName + "/", "")).collect(toList()));
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }
}