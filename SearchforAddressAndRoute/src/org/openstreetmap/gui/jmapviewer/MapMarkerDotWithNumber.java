/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.gui.jmapviewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Bring
 */
public class MapMarkerDotWithNumber extends MapMarkerDot implements java.io.Serializable {

    private int ID = 0;
    private String Name = "";
    
    private final int margin = 3;
    private final int fontHeight = 12;
    private final Font gridFont = new Font(Font.DIALOG, Font.PLAIN, fontHeight);
    
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
    
    private String getLabel() {
        String ret = "";
        if (ID > 0) {
            ret += String.valueOf(ID) + ".";
        }
        if (ID > 0 && !Name.isEmpty())
            ret += " ";
        if (!Name.isEmpty())
            ret += Name;
        return ret;
    }
    
    @Override
    public void paint(Graphics g, Point position) {
        super.paint(g ,position);
        String label = getLabel();
        
        if (!label.isEmpty()) {
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(gridFont);
            FontMetrics fm = g2.getFontMetrics();
            
            g.setColor(Color.WHITE);
            g.fillRect(position.x + 7, position.y - 7 - fontHeight - (2 * margin), fm.stringWidth(label) + (2 * margin), fontHeight + (2 * margin));
            g.setColor(Color.BLACK);
            g.drawRect(position.x + 7, position.y - 7 - fontHeight - (2 * margin), fm.stringWidth(label) + (2 * margin), fontHeight + (2 * margin));
            
            g2.drawString(label, position.x + 7 + margin, position.y - 9 - margin);
        }
    }

    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }
}
