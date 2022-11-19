package com.example.instagramclone;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Application;
import android.util.Log;

public class StarterActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable local DataStorage
        Parse.enableLocalDatastore(this);
        // Add you initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myappID")
                // if defined
                .clientKey("oIxXEf0OpnNZ")
                .server("http://13.230.127.223/parse/")
                .build()
        );


        ParseUser.enableAutomaticUser();
        ParseACL defaultACl = new ParseACL();
        defaultACl.setPublicReadAccess(true);
        defaultACl.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACl, true);
    }
}