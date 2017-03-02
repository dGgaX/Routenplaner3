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
public class MapMarkerDotWithNumber extends MapMarkerDot implements java.io.Serializable {

    private int ID = 0;
    public MapMarkerDotWithNumber(double lat, double lon) {
        super(lat, lon);
    }
    
    public MapMarkerDotWithNumber(Color color, double lat, double lon) {
        super(color, lat, lon);
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
            g.setColor(Color.WHITE);
            g.fillRect(position.x, position.y - 18, 23, 18);
            g.setColor(Color.BLACK);
            g.drawRect(position.x, position.y - 18, 23, 18);
            
            g.drawString(String.valueOf(ID) + ".", position.x + 5, position.y - 5);
        }
    }
}
