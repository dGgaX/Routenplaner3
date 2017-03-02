/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Karima
 */
public class ImageIconFromColor implements java.io.Serializable {
    int imgSize;
    Color imgColor;
    Color borderColor;
    ImageIcon imgicon;
    transient BufferedImage img;    
        
    public ImageIconFromColor(int imgSize, Color imgColor) {
        this.imgSize = imgSize;
        this.imgColor = imgColor;
        this.borderColor = Color.BLACK;
        this.imgicon = null;
        createIcon();
    }
    
    public ImageIconFromColor(int imgSize, Color imgColor, Color borderColor) {
        this.imgSize = imgSize;
        this.imgColor = imgColor;
        this.borderColor = borderColor;
        this.imgicon = null;
        createIcon();
    }
    
    private void createIcon() {
        if (this.img == null)
            this.img = new BufferedImage(this.imgSize,this.imgSize,BufferedImage.TYPE_INT_ARGB);
        
        for (int x = 0; x < this.imgSize; x++)
            for (int y = 0; y < this.imgSize; y++)
                if ((int) Math.round(Math.sqrt(Math.pow(x - this.imgSize / 2, 2) + Math.pow(y - this.imgSize / 2, 2))) == this.imgSize / 2 - 1)
                    this.img.setRGB(x, y, this.borderColor.getRGB());
                else if (Math.sqrt(Math.pow(x - this.imgSize / 2, 2) + Math.pow(y - this.imgSize / 2, 2)) < this.imgSize / 2 - 1)
                    this.img.setRGB(x, y, this.imgColor.getRGB());
                else
                    this.img.setRGB(x, y, 0);
        this.imgicon = new ImageIcon(img);
    }
    
    public final void updateColor(Color color) {
        this.imgColor = color;
        createIcon();
    }
    
    public final void updateColor(Color color, Color bgColor) {
        this.imgColor = color;
        this.borderColor = bgColor;
        createIcon();
    }
    
    public ImageIcon getImageIcon() {
        if (this.imgicon == null)
            createIcon();
        return this.imgicon;
    }
}
