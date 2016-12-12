package com.example.alex.realmdemo.data;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Alex on 12.12.2016.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                .name("realm_demo.realm")
                .schemaVersion(2).migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
