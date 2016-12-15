package edu.fich.pfcii.snoredetection.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class Helper
{
    private final Locale ARGENTINA_LOCALE = new Locale("es", "AR");
    private final TimeZone GMT_AR = TimeZone.getTimeZone("GMT-3");

    public long getTimestamp() {
        return (long)System.currentTimeMillis() / 1000L;
    }

    public String getDateAndTime(long timestamp) {
        Date date = new Date(timestamp * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", ARGENTINA_LOCALE); // the format of your date
        sdf.setTimeZone(GMT_AR); // give a timezone reference for formating
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public String getDate(long timestamp) {
        Date date = new Date(timestamp * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", ARGENTINA_LOCALE); // the format of your date
        sdf.setTimeZone(GMT_AR); // give a timezone reference for formating
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public String getTime(long timestamp) {
        Date date = new Date(timestamp * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", ARGENTINA_LOCALE); // the format of your date
        sdf.setTimeZone(GMT_AR); // give a timezone reference for formating
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public String getTotalTime(long timestamp_ini, long timestamp_fin) {
        long diff = (timestamp_fin - timestamp_ini);
        long diffSeconds = diff % 60;
        long diffMinutes = diff/60 % 60;
        long diffHours   = diff/(60*60) % 60;

        return diffHours + "h " +  diffMinutes + "' " + diffSeconds + "''";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Auxiliary functions to convert Double arrays to String
     * and vice versa
     *
     * @return
     */
    private static final String SEPARATOR = "|";

    public ArrayList<Double> getDoubleFromString(String str) {
        ArrayList<Double> list = new ArrayList<>();
        StringTokenizer listString = new StringTokenizer(str, SEPARATOR);

        while (listString.hasMoreElements()) {
            list.add(Double.parseDouble((String) listString.nextElement()));
        }

        return list;
    }

    public ArrayList<Integer> getIntegerFromString(String str) {
        ArrayList<Integer> list = new ArrayList<>();
        StringTokenizer listString = new StringTokenizer(str, SEPARATOR);

        while (listString.hasMoreElements()) {
            list.add(Integer.parseInt((String) listString.nextElement()));
        }

        return list;
    }

    public String getStringFromDouble(ArrayList<Double> list) {
        StringBuilder str = new StringBuilder();

        for (int i=0, n=list.size(); i<n-1; i++) {
            str.append(list.get(i) + SEPARATOR);
        }
        // Add last element to StringBuilder outside this for to avoid insert "|" at the end
        str.append(list.get(list.size()-1));

        return str.toString();
    }

    public String getStringFromInteger(ArrayList<Integer> list) {
        StringBuilder str = new StringBuilder();

        for (int i=0, n=list.size(); i<n-1; i++) {
            str.append(list.get(i) + SEPARATOR);
        }
        // Add last element to StringBuilder outside this for to avoid insert "|" at the end
        str.append(list.get(list.size()-1));

        return str.toString();
    }
}
