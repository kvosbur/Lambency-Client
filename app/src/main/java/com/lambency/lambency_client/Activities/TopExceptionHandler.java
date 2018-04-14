package com.lambency.lambency_client.Activities;

import android.app.Activity;

import com.lambency.lambency_client.Models.UserModel;

/**
 * Created by Evan on 4/12/2018.
 */

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
    public TopExceptionHandler(Activity app) {
        Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        UserModel.myUserModel.setActiveForModelAndDatabase(false);
        System.exit(0);
    }
}
