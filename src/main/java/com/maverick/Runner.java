package com.maverick;

import com.backendless.Backendless;
import com.maverick.ui.frame.LoginFrame;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

import javax.swing.*;
import java.text.ParseException;

import static com.maverick.utils.BackendlessUtils.*;

public class Runner {

    public static void main(String[] args) throws ParseException, UnsupportedLookAndFeelException {

        Backendless.initApp(APP_ID, SECRET_KEY, VERSION);
        UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        LoginFrame.getInstance();
    }
}