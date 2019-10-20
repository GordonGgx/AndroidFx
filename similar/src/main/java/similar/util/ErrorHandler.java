package similar.util;

import org.controlsfx.dialog.ExceptionDialog;

public class ErrorHandler {

    private static final ErrorHandler handler=new ErrorHandler();

    private ErrorHandler(){

    }

    public static ErrorHandler get(){
        return handler;
    }
    public void show(Throwable throwable){
        ExceptionDialog dialog=new ExceptionDialog(throwable);
        dialog.show();
    }
}
