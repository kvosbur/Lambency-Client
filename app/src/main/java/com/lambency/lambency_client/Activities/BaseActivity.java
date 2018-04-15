package com.lambency.lambency_client.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Evan on 4/12/2018.
 */

public class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
    }
}
