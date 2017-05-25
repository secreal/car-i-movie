package com.secreal.cari_movie.extra;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import static android.support.multidex.MultiDex.install;

/**
 * Created by secreal on 5/25/2017.
 * saya ulong :)
 */

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build()
            );
//        }
    }
}
