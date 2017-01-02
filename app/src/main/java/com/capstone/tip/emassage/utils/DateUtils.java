package com.capstone.tip.emassage.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class DateUtils {

    public static String getLongDateTimeString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date);
    }

}
