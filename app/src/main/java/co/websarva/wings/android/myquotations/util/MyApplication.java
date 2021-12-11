package co.websarva.wings.android.myquotations.util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance= null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static MyApplication getInstance(){
        return instance;
    }
}
