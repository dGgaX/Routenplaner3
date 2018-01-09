/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.gui.snippingtool;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 *
 * @author Karima
 */
public class PercentDimension {
    private String xPe;
    private String yPe;
    private String wPe;
    private String hPe;
    
    public PercentDimension(String xPe, String yPe, String wPe, String hPe) {
        this.xPe = xPe;
        this.yPe = yPe;
        this.wPe = wPe;
        this.hPe = hPe;
    }
    
    public Rectangle getDimension(Rectangle allSpace) {
        Rectangle temp = new Rectangle(allSpace);

        if (getxPe() >= 0) {
            if (xPe.toUpperCase().startsWith("X")) {
                temp.x += Math.round((float) allSpace.width * getxPe());
            } else if (xPe.toUpperCase().startsWith("Y")) {
                temp.x += Math.round((float) allSpace.height * getxPe());
            } else {
                return null;
            }
        } else {
            if (xPe.toUpperCase().startsWith("X")) {
                temp.x = (allSpace.x + allSpace.width) + Math.round((float) allSpace.width * getxPe());
            } else if (xPe.toUpperCase().startsWith("Y")) {
                temp.x = (allSpace.x + allSpace.width) + Math.round((float) allSpace.height * getxPe());
            } else {
                return null;
            }
        }
        
        if (getyPe() >= 0) {
            if (yPe.toUpperCase().startsWith("X")) {
                temp.y += Math.round((float) allSpace.width * getyPe());
            } else if (yPe.toUpperCase().startsWith("Y")) {
                temp.y += Math.round((float) allSpace.height * getyPe());
            } else {
                return null;
            }
        } else {
            if (yPe.toUpperCase().startsWith("X")) {
                temp.y = (allSpace.y + allSpace.height) + Math.round((float) allSpace.width * getyPe());
            } else if (yPe.toUpperCase().startsWith("Y")) {
                temp.y = (allSpace.y + allSpace.height) + Math.round((float) allSpace.height * getyPe());
            } else {
                return null;
            }
        }
        
        if (getwPe() >= 0) {
            if (wPe.toUpperCase().startsWith("W")) {
                temp.width = Math.round((float) allSpace.width * getwPe());
            } else if (wPe.toUpperCase().startsWith("H")) {
                temp.width = Math.round((float) allSpace.height * getwPe());
            } else {
                return null;
            }
        } else {
            if (wPe.toUpperCase().startsWith("W")) {
                temp.width = allSpace.width - allSpace.x - temp.x + Math.round((float) allSpace.width * getwPe());
            } else if (wPe.toUpperCase().startsWith("H")) {
                temp.width = allSpace.width - allSpace.x - temp.x + Math.round((float) allSpace.height * getwPe());
            } else {
                return null;
            }
        }

        if (gethPe() >= 0) {
            if (hPe.toUpperCase().startsWith("W")) {
                temp.height = Math.round((float) allSpace.width * gethPe());
            } else if (hPe.toUpperCase().startsWith("H")) {
                temp.height = Math.round((float) allSpace.height * gethPe());
            } else {
                return null;
            }
        } else {
            if (hPe.toUpperCase().startsWith("W")) {
                temp.height = allSpace.height + allSpace.y - temp.y + Math.round((float) allSpace.width * gethPe());
            } else if (hPe.toUpperCase().startsWith("H")) {
                temp.height = allSpace.height + allSpace.y - temp.y + Math.round((float) allSpace.height * gethPe());
            } else {
                return null;
            }
        }
        return temp;
    }

    /**
     * @return the xPe
     */
    public float getxPe() {
        return Float.valueOf(xPe.substring(1));
    }

    /**
     * @param xPe the xPe to set
     */
    public void setxPe(String xPe) {
        this.xPe = xPe;
    }

    /**
     * @return the yPe
     */
    public float getyPe() {
        return Float.valueOf(yPe.substring(1));
    }

    /**
     * @param yPe the yPe to set
     */
    public void setyPe(String yPe) {
        this.yPe = yPe;
    }

    /**
     * @return the wPe
     */
    public float getwPe() {
        return Float.valueOf(wPe.substring(1));
    }

    /**
     * @param wPe the wPe to set
     */
    public void setwPe(String wPe) {
        this.wPe = wPe;
    }

    /**
     * @return the hPe
     */
    public float gethPe() {
        return Float.valueOf(hPe.substring(1));
    }

    /**
     * @param hPe the hPe to set
     */
    public void sethPe(String hPe) {
        this.hPe = hPe;
    }
    public String toString() {
        String ret = "";
        
        ret += String.valueOf(this.xPe);
        ret += ", ";
        ret += String.valueOf(this.yPe);
        ret += ", ";
        ret += String.valueOf(this.wPe);
        ret += ", ";
        ret += String.valueOf(this.hPe);
        
        return ret;
    }
    
}
