package com.lambency.lambency_client.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lshan on 2/21/2018.
 */

public class TimeHelper {

    public static String dateFromTimestamp(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static String hourFromTimestamp(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String result = Integer.toString(hour);
        if(hour > 12){
            result+="PM";
        }else{
            result+="AM";
        }

        return result;
    }


}
