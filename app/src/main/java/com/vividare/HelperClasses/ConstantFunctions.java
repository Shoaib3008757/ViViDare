package com.vividare.HelperClasses;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantFunctions {

    public static String getratingRandomId()
    {
        String assignmentId = "";
        int max = 999999999;
        int min = 1;
        int random = (int )(Math.random() * max + min);
        String timeStemp = String.valueOf(System.currentTimeMillis());
        return assignmentId = timeStemp+"_"+String.valueOf(random);
        //return assignmentId = String.valueOf(random);
    }
    public static String getratingRandom2Digit()
    {
        String assignmentId = "";
        int max = 99;
        int min = 1;
        int random = (int )(Math.random() * max + min);
        return assignmentId = String.valueOf(random);
        //return assignmentId = String.valueOf(random);
    }

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    public static String verficationRandom()
    {
        String rnadom = "";
        int max = 9999;
        int min = 1;
        int random = (int )(Math.random() * max + min);
        return rnadom = String.valueOf(random);
    }

    public static long daysBetween(String startDate, String endDate)
    {

        final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        final Date date1;
        final Date date2;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            date1 = format.parse(startDate);
            calendar1.setTime(date1);
            calendar1.add(Calendar.DAY_OF_YEAR, 1);
            date2 = format.parse(endDate);
            calendar2.setTime(date2);
            calendar2.add(Calendar.DAY_OF_YEAR, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long end = calendar1.getTimeInMillis();
        long start = calendar2.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }


    public static String currentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String mCurrentDate = sdf.format(currentDate);
        return mCurrentDate;
    }

    public static String currentTime()
    {
        long timeStemp = System.currentTimeMillis();
        String currentTime = android.text.format.DateFormat.format("hh:mm a", new Date(timeStemp)).toString();
        return currentTime;

    }
    public static String currenttimeStemp()
    {
        long timeStemp = System.currentTimeMillis();
        return String.valueOf(timeStemp);

    }
    public static String dateTimeToTimeStemp(String sDate, String sTime)
    {
        String resultTimeStemp = "";
        Date date = null;
        try {
            String dateAndTime = sDate+" "+sTime;
            java.text.DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            date = (Date)formatter.parse(dateAndTime);
            Log.e("TAg", "the time Stemp is here  " + date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(date.getTime());
    }

    public static String formatCurrency(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.0");
        return formatter.format(Double.parseDouble(amount));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}


