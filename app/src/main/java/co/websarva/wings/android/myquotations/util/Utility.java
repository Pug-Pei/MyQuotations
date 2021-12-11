package co.websarva.wings.android.myquotations.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    private static final String TAG = "Utility";

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
