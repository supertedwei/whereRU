package com.supergigi.whereru.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tedwei on 28/02/2017.
 */

public class TimeUtil {

    public static final String toString(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat();
        return format.format(date);
    }
}
