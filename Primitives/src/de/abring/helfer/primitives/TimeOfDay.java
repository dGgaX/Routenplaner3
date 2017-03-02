/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.primitives;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karima
 */
public class TimeOfDay implements java.io.Serializable {
    private String isoString = "HH:mm";
    private SimpleDateFormat isoFormat = new SimpleDateFormat();
    private Calendar time = new GregorianCalendar();
    
    
    public TimeOfDay() {
        updateFormat();
        setTimeInMilli(0);
    }
    public TimeOfDay(int time) {
        updateFormat();
        this.time.setTimeInMillis((time * 1000) - (3600 * 1000));
    }
    public TimeOfDay(String time) {
        setTime(time);
    }
    public TimeOfDay(Calendar time) {
        setTime(time);
    }
    public TimeOfDay(TimeOfDay time) {
        setTime(time);
    }
    private void updateFormat() {
        isoFormat.applyPattern(this.isoString);
    }
    public void updateFormat(String isoString) {
        this.isoString = isoString;
        isoFormat.applyPattern(isoString);
    }
    public void setTimeInMilli(int milli) {
        updateFormat();
        this.time.setTimeInMillis((milli * 1000) - (3600 * 1000));
    }
    public void setTimeInMin(int min) {
        updateFormat();
        this.time.setTimeInMillis((min * 1000 * 60) - (3600 * 1000));
    }
    public void setTime(TimeOfDay timeOfDay) {
        updateFormat();
        this.time.setTimeInMillis(timeOfDay.getTime().getTimeInMillis());
    }
    public void setTime(String Suche) {
        updateFormat();
        String[] suchePart = Suche.split(" ");
        String Text = suchePart[0];
        if (suchePart[0].length() > 5)
            Text = suchePart[0].substring(0, 5);
        if (Text.length() == 5 && Text.contains(":")) {
            try {
                this.time.setTime(isoFormat.parse(Text));
            } catch (ParseException ex) {
                Logger.getLogger(TimeOfDay.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (Text.length() <= 4 && (Text.contains(".") || Text.contains(","))) {
            if (Text.contains(".")) {
                String[] parts = Text.split(".");
                if (parts.length == 2) {
                    if (parts[0].isEmpty())
                        parts[0] = "0";
                    if (parts[1].isEmpty())
                        parts[1] = "0";
                    try {
                        int i = 60 * Integer.parseInt(parts[0]) + (int)Math.round(60 * (Integer.parseInt(parts[1]) / Math.pow(10, parts[1].length())));
                        setTimeInMin(i);
                    } catch (NumberFormatException ex) {
                    }
                }
            } else if (Text.contains(",")) {
                String[] parts = Text.split(",");
                if (parts.length == 2) {
                    if (parts[0].isEmpty())
                        parts[0] = "0";
                    if (parts[1].isEmpty())
                        parts[1] = "0";
                    try {
                        int i = 60 * Integer.parseInt(parts[0]) + (int)Math.round(60 * (Integer.parseInt(parts[1]) / Math.pow(10, parts[1].length())));
                        setTimeInMin(i);
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        } else if (Text.length() <= 3) {
            try {
                int i = Integer.parseInt(Text);
                if (i < 5)
                    i *= 60;
                setTimeInMin(i);
            } catch (NumberFormatException ex) {
            }
        }
    }
    public void setTime(Calendar time) {
        updateFormat();
        this.time = time;
    }
    
    public void addier(TimeOfDay addCal) {
        time.add(Calendar.MILLISECOND, ((int)addCal.getTime().getTimeInMillis())+(3600*1000));
    }
    public Calendar getTime() {
        return time;
    }
    public String getDurationString() {
        String SplitString[] =  isoFormat.format(time.getTime()).split(":");
        if (SplitString[0].equals("00"))
            return SplitString[1] + " Min.";
        if (SplitString[1].equals("00"))
            return SplitString[0] + " Std.";
        return SplitString[0] + " Std. " + SplitString[1] + " Min.";
    }
    public String getTimeString() {
        return isoFormat.format(time.getTime()) + " Uhr";
    }
    public long getMillis() {
        return time.getTimeInMillis();
    }
    
    @Override
    public String toString() {
        return isoFormat.format(time.getTime());
    }
    
    public boolean isZero() {
        return (this.time.getTimeInMillis() == ( - (3600 * 1000)));
    }
    
    public boolean equals(TimeOfDay obj) {
        return (this.getMillis() == obj.getMillis());
    }

}
