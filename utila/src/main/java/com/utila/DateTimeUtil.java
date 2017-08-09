package com.utila;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    private static Calendar calendar;
    public static String fullDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    public static String defaultDateTimeFormat = "yyyy-MM-dd'T'HH:mm";
    public static String dateTimeFormat = "yyyy-MM-dd HH:mm";
    public static String dateFormatOnly = "yyyy-MM-dd";
    public static String displayFormat = "EEE, d MMMM";
    public static String timeOnlyFormat = "HH:mm a";
    public static final HashMap<Integer, String> MONTH = new HashMap<Integer, String>() {{
        put(1, "Jan");
        put(2, "Feb");
        put(3, "Mar");
        put(4, "Apr");
        put(5, "May");
        put(6, "Jun");
        put(7, "Jul");
        put(8, "Aug");
        put(9, "Sep");
        put(10, "Oct");
        put(11, "Nov");
        put(12, "Dec");
    }};
    public static final HashMap<Integer, String> MONTH_FULL = new HashMap<Integer, String>() {{
        put(1, "January");
        put(2, "February");
        put(3, "March");
        put(4, "April");
        put(5, "May");
        put(6, "June");
        put(7, "July");
        put(8, "August");
        put(9, "September");
        put(10, "October");
        put(11, "November");
        put(12, "December");
    }};

    public static HashMap<String, Integer> getDate(int dayOffSet) {
        HashMap<String, Integer> dateMap = new HashMap<>();
        calendar = Calendar.getInstance();
        if (dayOffSet > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, dayOffSet);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateMap.put("year", year);
        dateMap.put("month", month + 1);
        dateMap.put("day", day);

        return dateMap;
    }

    public static String getStringDate(int dayOffSet) {
        HashMap<String, Integer> dateMap = new HashMap<>();
        calendar = Calendar.getInstance();
        if (dayOffSet > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, dayOffSet);
        }
        SimpleDateFormat inFormat = new SimpleDateFormat(dateFormatOnly, Locale.ENGLISH);
        return inFormat.format(calendar.getTime());
    }

    public static String getMonthName(Integer month) {
        return MONTH.get(month);
    }

    public static String getDayOfTheWeek(String date) {
        SimpleDateFormat inFormat = new SimpleDateFormat(dateFormatOnly, Locale.ENGLISH);
        String goal = "";
        try {
            Date date2 = inFormat.parse(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
            goal = outFormat.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return goal;
    }

    public static String getFullDayOfTheWeek(String date) {
        SimpleDateFormat inFormat = new SimpleDateFormat(dateFormatOnly, Locale.ENGLISH);
        String goal = "";
        try {
            Date date2 = inFormat.parse(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            goal = outFormat.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return goal;
    }

    public static String getCurrent24HourTime(int additionalMinute) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:45"));
        if (additionalMinute > 0) {
            calendar.add(Calendar.MINUTE, additionalMinute);
        }
        Date currentTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:45"));
        /*-------------------------------------------------------*/
        String time = date.format(currentTime);

        String[] a = time.split(":");
        //hour = Integer.parseInt(a[0]);
        String[] b = a[1].split("\\s+");
        int minute = Integer.parseInt(b[0]);

        return time;
    }

    public static String getCurrent12HourTime(int additionalMinute) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:45"));
        if (additionalMinute > 0) {
            calendar.add(Calendar.MINUTE, additionalMinute);
        }
        Date currentTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:45"));
        /*-------------------------------------------------------*/
        String time = date.format(currentTime);

        String[] a = time.split(":");
        //hour = Integer.parseInt(a[0]);
        String[] b = a[1].split("\\s+");
        int minute = Integer.parseInt(b[0]);

        return time;
    }

    public static String convert24to12(String time24Hour) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date date = parseFormat.parse(time24Hour);
            String time = displayFormat.format(date);
            int x = Integer.parseInt(time.substring(0, 1));
            if (x == 0) {
                time = time.substring(1);
            }
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convert12to24(String time12Hour) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        try {
            Date date = parseFormat.parse(time12Hour);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDayOfMonthSuffix(int n) {
        String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st"};
        return suffixes[n];

    }

    public static HashMap<String, Object> offSetDay(String fullDate, int offSet) {
        String date[] = TextUtils.split(fullDate, "-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));

        /*int newOffSet;
        if (offSet > 0) {
            newOffSet = offSet - 1;
        } else {
            newOffSet = offSet;
        }*/

        calendar.add(Calendar.DAY_OF_MONTH, offSet);

        int year, month, day;
        String newFullDate;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        newFullDate = year + "-" + month + "-" + day;

        HashMap<String, Object> dateMap = new HashMap<>();
        dateMap.put("year", year);
        dateMap.put("month", month);
        dateMap.put("day", day);
        dateMap.put("dayOfWeek", getDayOfTheWeek(newFullDate));
        dateMap.put("fullDate", newFullDate);
        return dateMap;
    }

    public static String getFullDate(String date) {
        String[] dateArray = date.split("-");
        String month, day;
        if (dateArray[1].length() == 1) {
            month = 0 + dateArray[1];
        } else {
            month = dateArray[1];
        }
        if (dateArray[2].length() == 1) {
            day = 0 + dateArray[2];
        } else {
            day = dateArray[2];
        }
        return dateArray[0] + "-" + month + "-" + day;
    }

    public static Integer getDateDifference(String firstDate, String secondDate, boolean positive) {
        HashMap<String, Integer> dateMap = getDate(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (firstDate.isEmpty()) {
            firstDate = dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day");
        }
        if (secondDate.isEmpty()) {
            secondDate = dateMap.get("year") + "-" + dateMap.get("month") + "-" + dateMap.get("day");
        }
        try {
            Date date1 = dateFormat.parse(firstDate);
            Date date2 = dateFormat.parse(secondDate);

            Long difference;
            if (positive) {
                difference = Math.abs(date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
            } else {
                difference = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
            }
            return Integer.parseInt(difference.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Long getDurationInMilli(String primary, String secondary) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(primary);
            Date date2 = simpleDateFormat.parse(secondary);
            return date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static boolean isBefore(String primary, String secondary) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(primary);
            Date date2 = simpleDateFormat.parse(secondary);
            LogUtil.log("IS BEFORE :: ", date1.before(date2));
            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getDuration(String primary, String secondary) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(primary);
            Date date2 = simpleDateFormat.parse(secondary);

            long different = date2.getTime() - date1.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            String day = "", hour = "";

            if (elapsedDays > 0) {
                day = elapsedDays + "d ";
            }

            if (elapsedHours > 0) {
                hour = elapsedHours + "h ";
            }

            return day + hour + elapsedMinutes + "m";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getGreaterDate(String from, String to) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(from);
            Date date2 = simpleDateFormat.parse(to);

            if (date1.after(date2)) {
                return from;
            } else {
                return to;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSmallerDate(String from, String to) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(from);
            Date date2 = simpleDateFormat.parse(to);

            if (date1.before(date2)) {
                return from;
            } else {
                return to;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String changeStringDateFormatToString(String date, String fromFormat, String toFormat) {
        SimpleDateFormat convertFrom = new SimpleDateFormat(fromFormat, Locale.US);
        SimpleDateFormat convertTo = new SimpleDateFormat(toFormat, Locale.US);

        try {
            LogUtil.log("DATE TO FORMAT :: ", date);
            Date dateToFormat = convertFrom.parse(date);
            String formattedDate = convertTo.format(dateToFormat);
            LogUtil.log("FORMATTED DATE :: ", formattedDate);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFullStringDateTime(int dayOffSet, int minuteOffset) {
        return DateTimeUtil.getStringDate(dayOffSet) + " " + DateTimeUtil.getCurrent24HourTime(minuteOffset);

    }

    public static Calendar getTimeCalender(String time, String dateFormat) {
        try {
            Date time1 = new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time1);

            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFullReadableDateTime(String dateTime) {
        String[] dSplit = dateTime.split("T");
        String[] dSplit1 = dSplit[0].split("-");
        return DateTimeUtil.getFullDayOfTheWeek(dSplit[0])
                + ", "
                + dSplit1[2]
                + " "
                + MONTH_FULL.get(Integer.parseInt(dSplit1[1]))
                + " "
                + dSplit1[0]
                + " at "
                + DateTimeUtil.convert24to12(dSplit[1]);
    }

    public static String getFullReadableDate(String date) {
        String[] dSplit1 = date.split("-");
        return DateTimeUtil.getFullDayOfTheWeek(date)
                + ", "
                + dSplit1[2]
                + " "
                + MONTH_FULL.get(Integer.parseInt(dSplit1[1]))
                + " "
                + dSplit1[0];
    }

    public static boolean earlierThan(String date, String referenceDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1 = dateFormat.parse(date);
            Date date2 = dateFormat.parse(referenceDate);

            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean laterThan(String date, String referenceDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1 = dateFormat.parse(date);
            Date date2 = dateFormat.parse(referenceDate);

            return date1.after(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
