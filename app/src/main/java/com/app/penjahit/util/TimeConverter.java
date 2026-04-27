package com.app.penjahit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeConverter {
    private static final String YYYY_MM_DD;
    private static final String DD;

    private static final String MM;

    private static final String HH;

    private static final String YYYY;

    private static final String MMM_YYYY;

    private static final String YYYY_MM_DD_HH_MM_SS;

    private static final String DD_MMMM_YYYY;
    private static final String DD_MMMM;

    private static final String DD_MMM_YYYY;

    private static final String DD__MM__YYYY;

    private static final String EEE_DD__MMM__YYYY;

    private static final String DD_MM_YYYY;
    private static final String DD_MM_YY;

    private static final String EEEE_DD_MMMM_YYYY_HH_MM;

    private static final String EEEE_DD_MMMM_YYYY_DIVIDER_HH_MM;

    private static final String DD_MM_AT_HH_MM;

    private static final String HH_MM;

    private static final String EEEE_DD_MMMM_YYYY;

    private static final String EEEE_D_MMM_YYYY_HH_MM_SS_Z;

    private static final String DD_MM_YY_HH_MM;

    private static final String DD_MMMM_YY_HH_MM;

    private static final String DD_MM_YY_HH_MM_SS;

    private static final String MM_DD_YYYY_at_HH_MM;

    private static final String MM_DD_YYYY;

    private static final String DD_MM_YY_HH_MM_SLASH;

    public static final TimeConverter INSTANCE;


    public final String getYYYY_MM_DD() {
        return YYYY_MM_DD;
    }


    public final String getDD() {
        return DD;
    }


    public final String getMM() {
        return MM;
    }


    public final String getHH() {
        return HH;
    }


    public final String getYYYY() {
        return YYYY;
    }


    public final String getMMM_YYYY() {
        return MMM_YYYY;
    }


    public final String getYYYY_MM_DD_HH_MM_SS() {
        return YYYY_MM_DD_HH_MM_SS;
    }


    public final String getDD_MMMM_YYYY() {
        return DD_MMMM_YYYY;
    }

    public static String getDD_MMMM() {
        return DD_MMMM;
    }

    public final String getDD_MMM_YYYY() {
        return DD_MMM_YYYY;
    }


    public final String getDD__MM__YYYY() {
        return DD__MM__YYYY;
    }


    public final String getEEE_DD__MMM__YYYY() {
        return EEE_DD__MMM__YYYY;
    }


    public final String getDD_MM_YYYY() {
        return DD_MM_YYYY;
    }

    public final String getDD_MM_YY() {
        return DD_MM_YY;
    }


    public final String getEEEE_DD_MMMM_YYYY_HH_MM() {
        return EEEE_DD_MMMM_YYYY_HH_MM;
    }


    public final String getEEEE_DD_MMMM_YYYY_DIVIDER_HH_MM() {
        return EEEE_DD_MMMM_YYYY_DIVIDER_HH_MM;
    }


    public final String getDD_MM_AT_HH_MM() {
        return DD_MM_AT_HH_MM;
    }


    public final String getHH_MM() {
        return HH_MM;
    }


    public final String getEEEE_DD_MMMM_YYYY() {
        return EEEE_DD_MMMM_YYYY;
    }


    public final String getEEEE_D_MMM_YYYY_HH_MM_SS_Z() {
        return EEEE_D_MMM_YYYY_HH_MM_SS_Z;
    }


    public final String getDD_MM_YY_HH_MM() {
        return DD_MM_YY_HH_MM;
    }


    public final String getDD_MMMM_YY_HH_MM() {
        return DD_MMMM_YY_HH_MM;
    }


    public final String getDD_MM_YY_HH_MM_SS() {
        return DD_MM_YY_HH_MM_SS;
    }


    public final String getMM_DD_YYYY_at_HH_MM() {
        return MM_DD_YYYY_at_HH_MM;
    }


    public final String getMM_DD_YYYY() {
        return MM_DD_YYYY;
    }


    public final String getDD_MM_YY_HH_MM_SLASH() {
        return DD_MM_YY_HH_MM_SLASH;
    }


    public final String convertDate(String time, String fromTimeFormat, String toTimeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromTimeFormat, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(toTimeFormat, Locale.getDefault());
        simpleDateFormat2.setTimeZone(TimeZone.getDefault());
        String timeConverted = null;
        try {
            timeConverted = simpleDateFormat2.format(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeConverted;
    }


    public final String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String var10000 = formatter.format(calendar.getTime());
        return var10000;
    }
    public final String getDatePlusTwoDays(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        calendar.add(Calendar.DAY_OF_YEAR, 2); // Tambahkan 2 hari
        return formatter.format(calendar.getTime());
    }



    static {
        TimeConverter var0 = new TimeConverter();
        INSTANCE = var0;
        YYYY_MM_DD = "yyyy-MM-dd";
        DD = "dd";
        MM = "MM";
        HH = "HH";
        YYYY = "yyyy";
        MMM_YYYY = "MMM yyyy";
        YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        DD_MMMM_YYYY = "dd MMMM";
        DD_MMMM = "dd MMMM";
        DD_MMM_YYYY = "dd MMM yyyy";
        DD__MM__YYYY = "dd/MM/yyyy";
        EEE_DD__MMM__YYYY = "EEE, dd MMM yyyy";
        DD_MM_YYYY = "dd-MM-yyyy";
        DD_MM_YY = "dd-MM-yyyy";
        EEEE_DD_MMMM_YYYY_HH_MM = "EEEE, dd MMMM yyyy, HH:mm";
        EEEE_DD_MMMM_YYYY_DIVIDER_HH_MM = "EEEE, dd MMMM yyyy | HH:mm";
        DD_MM_AT_HH_MM = "dd MMM, HH:mm";
        HH_MM = "HH:mm";
        EEEE_DD_MMMM_YYYY = "EEEE, dd MMMM yyyy";
        EEEE_D_MMM_YYYY_HH_MM_SS_Z = "EEEE, d MMM yyyy HH:mm:ss z";
        DD_MM_YY_HH_MM = "dd-MM-yy, HH:mm";
        DD_MMMM_YY_HH_MM = "dd MMMM yyyy\n HH:mm";
        DD_MM_YY_HH_MM_SS = "dd-MM-yy HH:mm:ss";
        MM_DD_YYYY_at_HH_MM = "MMM dd, yyyy 'at' HH:mm";
        MM_DD_YYYY = "MMMM dd, yyyy";
        DD_MM_YY_HH_MM_SLASH = "dd/MMM/yy HH:mm";
    }
}
