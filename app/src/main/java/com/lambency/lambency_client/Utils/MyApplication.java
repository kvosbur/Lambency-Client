package com.lambency.lambency_client.Utils;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Evan on 4/3/2018.
 */

// Don't forget to add it to your manifest by doing
// <application android:name="your.package.MyApplication" ...
    //        android:name="android.support.multidex.MultiDexApplication"

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MyLifecycleHandler
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }
}
