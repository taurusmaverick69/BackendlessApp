package com.maverick;

import com.backendless.Backendless;
import com.maverick.ui.LoginFrame;

public class Runner {

    private static final String APP_ID = "D814F351-7654-24CB-FFFF-290BB7E26800";
    private static final String SECRET_KEY = "557CF7CD-AEE8-1F78-FF1C-DBCC19336600";
    private static final String VERSION = "v1";

    public static void main(String[] args) {
        Backendless.initApp(APP_ID, SECRET_KEY, VERSION);
        LoginFrame.getInstance();
    }
}