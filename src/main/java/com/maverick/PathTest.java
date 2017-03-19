import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class PathTest {

    public PathTest() {

        DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("ROOT"));
        JTree tree = new JTree(model);

        List<String> paths = Arrays.asList(
                "Node 1/Node 2/Node 3/Node 4/",
                "Node 1/Node 2/Node 3/Node 5",
                "Node 1/Node 2/Node 3/Node 6",
                "Node 1/Node 2/Node 4/Node 5",
                "Node 1/Node 1/Node 3/Node 5");

        buildTreeFromPaths(model, paths);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.setRootVisible(false);

        // UI
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(tree);
//        JButton button = new JButton("CLICK ME");
//        f.add(button);
        f.setSize(300, 300);
        f.setLocation(200, 200);
        f.setVisible(true);

//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//
//                TreePath selectionPath = tree.getSelectionPath();
//                System.out.println(selectionPath.getParentPath());
//                Object[] path = selectionPath.getPath();
//                System.out.println(Arrays.toString(path));
//
//
////                for (Object o : path) {
////                    System.out.println(o);
////                }
//
//
//            }
//        });
    }


    private void buildTreeFromPaths(DefaultTreeModel model, List<String> paths) {

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

    public static void main(String[] args) {
        new PathTest();
    }
}