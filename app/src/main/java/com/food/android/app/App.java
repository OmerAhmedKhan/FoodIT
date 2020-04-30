package com.food.android.app;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by Aleesha Kanwal on 13/05/2018.
 */

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();

//        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/Montserrat-Regular.ttf");
//        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Bold.ttf");
//        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/Montserrat-SemiBold.ttf");
//        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/Montserrat-Medium.ttf");

        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
