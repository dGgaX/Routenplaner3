/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karima
 */
public class Uhrzeit implements java.io.Serializable {
    private String isoString = "HH:mm";
    private SimpleDateFormat isoFormat = new SimpleDateFormat();
    private Calendar Zeit = null;
    
    
    public Uhrzeit() {
        updateFormat();
        setZeitInMilli(0);
    }
    public Uhrzeit(int zeit) {
        updateFormat();
        setZeitInMilli(zeit);
    }
    public Uhrzeit(String zeit) {
        setZeit(zeit);
    }
    public Uhrzeit(Calendar zeit) {
        setZeit(zeit);
    }
    public Uhrzeit(Uhrzeit zeit) {
        setZeit(zeit);
    }
    private void updateFormat() {
        isoFormat.applyPattern(this.isoString);
    }
    public void updateFormat(String isoString) {
        this.isoString = isoString;
        isoFormat.applyPattern(isoString);
    }
    public void setZeitInMilli(int milli) {
        updateFormat();
        this.Zeit = new GregorianCalendar();
        this.Zeit.setTimeInMillis((milli * 1000) - (3600 * 1000));
    }
    public void setZeitInMin(int min) {
        updateFormat();
        this.Zeit = new GregorianCalendar();
        this.Zeit.setTimeInMillis((min * 1000 * 60) - (3600 * 1000));
    }
    public void setZeit(Uhrzeit uhrzeit) {
        updateFormat();
        this.Zeit = new GregorianCalendar();
        this.Zeit.setTimeInMillis(uhrzeit.getZeit().getTimeInMillis());
    }
    public void setZeit(String Suche) {
        updateFormat();
        this.Zeit = new GregorianCalendar();
        String[] suchePart = Suche.split(" ");
        String Text = suchePart[0];
        if (suchePart[0].length() > 5)
            Text = suchePart[0].substring(0, 5);
        if (Text.length() == 5 && Text.contains(":")) {
            try {
                this.Zeit.setTime(isoFormat.parse(Text));
            } catch (ParseException ex) {
                Logger.getLogger(Uhrzeit.class.getName()).log(Level.SEVERE, null, ex);
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
                        setZeitInMin(i);
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
                        setZeitInMin(i);
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        } else if (Text.length() <= 3) {
            try {
                int i = Integer.parseInt(Text);
                if (i < 5)
                    i *= 60;
                setZeitInMin(i);
            } catch (NumberFormatException ex) {
            }
        }
    }
    public void setZeit(Calendar Zeit) {
        updateFormat();
        this.Zeit = Zeit;
    }
    
    public void addier(Uhrzeit addCal) {
        Zeit.add(Calendar.MILLISECOND, ((int)addCal.getZeit().getTimeInMillis())+(3600*1000));
    }
    public Calendar getZeit() {
        return Zeit;
    }
    public String getDauerString() {
        String SplitString[] =  isoFormat.format(Zeit.getTime()).split(":");
        if (SplitString[0].equals("00"))
            return SplitString[1] + " Min.";
        if (SplitString[1].equals("00"))
            return SplitString[0] + " Std.";
        return SplitString[0] + " Std. " + SplitString[1] + " Min.";
    }
    public String getUhrzeitString() {
        if (this.Zeit == null)
            return "??:?? Uhr";
        return isoFormat.format(Zeit.getTime()) + " Uhr";
    }
    public long getMillis() {
        if (this.Zeit == null)
            return 0x0L;
        return Zeit.getTimeInMillis();
    }
    public String toString() {
        if (this.Zeit == null)
            return isoFormat.format(new Date(0));
        return isoFormat.format(Zeit.getTime());
    }
    
    public boolean isZero() {
        if (this.Zeit == null)
            return true;
        return (this.Zeit.getTimeInMillis() == ( - (3600 * 1000)));
    }
    public boolean isValid() {
        return this.Zeit != null && this.Zeit.getTimeInMillis() >= -3600000 && this.Zeit.getTimeInMillis() < 82800000;
    }
}
