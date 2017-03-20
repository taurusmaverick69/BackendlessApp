package com.maverick;

import com.backendless.Backendless;
import com.maverick.ui.LoginFrame;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class Runner {

    private static final String APP_ID = "D814F351-7654-24CB-FFFF-290BB7E26800";
    private static final String SECRET_KEY = "557CF7CD-AEE8-1F78-FF1C-DBCC19336600";
    private static final String VERSION = "v1";

    public static void main(String[] args) throws ParseException, UnsupportedLookAndFeelException, IOException {
        Backendless.initApp(APP_ID, SECRET_KEY, VERSION);


        HttpGet httpGet = new HttpGet("https://api.backendless.com/D814F351-7654-24CB-FFFF-290BB7E26800/v1/files/123123/shared%2Bwith%2Bme/readme.txt");

//        HttpClientBuilder.create().build().execute(httpGet, (ResponseHandler<Object>) httpResponse -> {
//            HttpEntity entity = httpResponse.getEntity();
//            System.out.println(entity);
//            return entity.getContent().toString();
//        });


//        InputStream content = HttpClientBuilder.create().build().execute(httpGet).getEntity().getContent();
//        FileUtils.copyInputStreamToFile(content, new File("readme"));

        UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        LoginFrame.getInstance();
    }
}