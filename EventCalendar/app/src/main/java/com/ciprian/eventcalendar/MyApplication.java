package com.ciprian.eventcalendar;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class MyApplication extends Application {
    public static final String APPLICATION_ID = "9ED189A1-0EDC-4826-FFD6-5115227A2200";
    public static final String API_KEY = "1560A16B-B861-FFF3-FF04-377997025E00";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static String objectId;
    public static boolean editPressed= false;

    @Override
    public void onCreate() {
        super.onCreate();
        // do not forget to call Backendless.initApp in the app initialization code

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);

    }
}
