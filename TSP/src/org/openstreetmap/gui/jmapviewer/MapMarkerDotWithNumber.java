/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.gui.jmapviewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Bring
 */
public class MapMarkerDotWithNumber extends MapMarkerDot {

    private int ID = 0;
    public MapMarkerDotWithNumber(double lat, double lon) {
        super(lat, lon);
    }
    
    public MapMarkerDotWithNumber(Color color, double lat, double lon) {
        super(color, lat, lon);
    }
    public MapMarkerDotWithNumber(Color color, double lat, double lon, int ID) {
        super(color, lat, lon);
        this.ID = ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }
    
    public int getID() {
        return this.ID;
    }
    
    @Override
    public void paint(Graphics g, Point position) {
        super.paint(g ,position);
        if (this.ID > 0) {
            g.setColor(super.color);
            g.fillRect(position.x, position.y - 18, 23, 18);
            g.setColor(Color.BLACK);
            g.drawRect(position.x, position.y - 18, 23, 18);
            if (isColorDark(super.color))
                g.setColor(Color.WHITE);
            g.drawString(String.valueOf(ID) + ".", position.x + 5, position.y - 5);
        }
    }
    
    public boolean isColorDark(Color color){
        double darkness = 1-(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue())/255;
        return darkness >= 0.5;
    }
}
