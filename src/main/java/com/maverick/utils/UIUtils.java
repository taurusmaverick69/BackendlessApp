package com.maverick.utils;

import java.awt.*;

public class UIUtils {

    public static GridBagConstraints getLabelGridBagConstraints(int x, int y) {
        return new GridBagConstraints(x, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(18, 10, 2, 2), 0, 0);
    }

    public static GridBagConstraints getTextFieldGridBagConstraints(int x, int y) {
        return new GridBagConstraints(x, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0);
    }

    public static GridBagConstraints getPanelGridBagConstraints(int y) {
        return new GridBagConstraints(0, y, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0);
    }
}