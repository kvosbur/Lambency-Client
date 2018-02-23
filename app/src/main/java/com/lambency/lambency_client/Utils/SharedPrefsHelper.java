package com.lambency.lambency_client.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lshan on 2/20/2018.
 */

public class SharedPrefsHelper {
    public static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences("com.lambency.lambency_client", Context.MODE_PRIVATE);
    }
}
