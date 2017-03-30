package com.maverick;

import com.backendless.Backendless;
import com.maverick.ui.frame.LoginFrame;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;

import static com.maverick.utils.BackendlessUtils.*;

public class Runner {

    public static void main(String[] args) throws ParseException, UnsupportedLookAndFeelException, IOException {
        Backendless.initApp(APP_ID, SECRET_KEY, VERSION);
        UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        new LoginFrame();
    }
}