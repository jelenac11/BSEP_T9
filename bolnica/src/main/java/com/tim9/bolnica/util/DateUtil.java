package com.tim9.bolnica.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date parse(String strDate) throws ParseException {
    	Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
        return date;
    }

    private DateUtil() {
    	
    }
}
