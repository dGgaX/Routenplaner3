/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer;

/**
 *
 * @author Karima
 */
public class RainbowColor {
    public static final int[] wav2RGB(double w) {
        double R,G,B,SSS;
    
        if ((w >= 380) && (w < 440)) {
            R = -(w - 440) / (440 - 350);
            G = 0;
            B = 1;
        } else if ((w >= 440) && (w < 490)) {
            R = 0;
            G = (w - 440) / (490 - 440);
            B = 1;
        } else if ((w >= 490) && (w < 510)) {
            R = 0;
            G = 1;
            B = -(w - 510) / (510 - 490);
        } else if ((w >= 510) && (w < 580)) {
            R = (w - 510) / (580 - 510);
            G = 1;
            B = 0;
        } else if ((w >= 580) && (w < 645)) {
            R = 1;
            G = -(w - 645) / (645 - 580);
            B = 0;
        } else if ((w >= 645) && (w <= 780)) {
            R = 1;
            G = 0;
            B = 0;
        } else {
            R = 0;
            G = 0;
            B = 0;
        }
            
            
        if ((w >= 380) && (w < 420)) {
            SSS = 0.3 + 0.7*(w - 350) / (420 - 350);
        } else if ((w >= 420) && (w <= 700)) {
            SSS = 1.0;
        } else if ((w > 700) && (w <= 780)) {
            SSS = 0.3 + 0.7*(780 - w) / (780 - 700);
        } else {
            SSS = 0;
        }
        SSS *= 255;
        R *= SSS;
        G *= SSS;
        B *= SSS;
        int[] res = {(int) R, (int) G, (int) B};
        return res;
    }
}
