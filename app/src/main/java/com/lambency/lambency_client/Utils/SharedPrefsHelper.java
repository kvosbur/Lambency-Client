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

    public static String getOauth(Context context){
        return getSharedPrefs(context).getString("myauth", "auth token not found!");
    }


    public static long getStartTime(Context context){
        return getSharedPrefs(context).getLong("startTime", 0L);
    }

    public static void setStartTime(Context context, long time){
        getSharedPrefs(context).edit().putLong("startTime", time).apply();
    }

    public static void setCheckedIn(Context context, Boolean flag){
        getSharedPrefs(context).edit().putBoolean("checkedIn", flag).apply();
    }

    public static boolean isCheckedIn(Context context){
        return getSharedPrefs(context).getBoolean("checkedIn", false);
    }

}
