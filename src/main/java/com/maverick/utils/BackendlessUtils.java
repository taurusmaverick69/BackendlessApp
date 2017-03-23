package com.maverick.utils;

import javax.swing.tree.TreePath;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class BackendlessUtils {

    public static final String APP_ID = "D814F351-7654-24CB-FFFF-290BB7E26800";
    public static final String SECRET_KEY = "557CF7CD-AEE8-1F78-FF1C-DBCC19336600";
    public static final String VERSION = "v1";

    private static final String DELIMITER = "/";

    public static String getBackendlessPathFromTreePath(TreePath treePath, String directoryName) {
        List<String> paths = Arrays.stream(treePath.getPath()).map(Object::toString).collect(toList());
        paths.add(directoryName);
        return paths.stream().collect(joining(DELIMITER));
    }

    public static String getBackendlessPathFromTreePath(TreePath treePath) {
        return Arrays.stream(treePath.getPath()).map(Object::toString).collect(joining(DELIMITER));
    }
}