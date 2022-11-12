package com.prateek.newsbreeze;

import android.app.Application;
import android.content.Context;


public class MyApp extends Application {
    private static Context context;

    public static Context getAppContext(){
        return context;
    }

    @Override
    public void onCreate() {
        if(context == null) {
            context=getApplicationContext();
        }
        super.onCreate();
    }

}
