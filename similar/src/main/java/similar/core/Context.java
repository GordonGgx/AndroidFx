package similar.core;

import java.io.File;
import java.net.URL;

public abstract class Context {

    private static String FXML_PATH="layout"+ File.pathSeparator;

     URL getFxmlLocation(String name){
       return ClassLoader.getSystemResource(/*FXML_PATH+*/name);
     }

}
