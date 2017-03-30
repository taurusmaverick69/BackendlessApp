package com.maverick;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.BodyParts;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.ObjectProperty;
import com.maverick.ui.frame.LoginFrame;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

import static com.maverick.utils.BackendlessUtils.*;

public class Runner {

    public static void main(String[] args) throws ParseException, UnsupportedLookAndFeelException, IOException {
        Backendless.initApp(APP_ID, SECRET_KEY, VERSION);


        Persistence data = Backendless.Data;


        Backendless.Messaging.sendHTMLEmail("Subject1", "This is Body1", "taurusmaverick69@gmail.com");
        Backendless.Messaging.sendTextEmail("Subject2", "<h1>This is Body2<h1>", "taurusmaverick69@gmail.com");

      //  HashMap entity = new HashMap();
      //  entity.put("qe", "rq");

     //   data.create(Product.class, new HashMap());

       // data.of(Product.class).save(new Product());


//        Product e = new Product();
//        e.setCost(1);
//        e.setName("Ivan");

//        data.of(BackendlessUser.class).find(new BackendlessDataQuery())
//
//       data.of(Product.class).find().getData().forEach(product -> {
//           System.out.println(product);
//       });


//        Map<String, String> e = new HashMap<>();
//        e.put("test", "yest");
//        myTable.save(e);

//        UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
//        new LoginFrame();
    }
}