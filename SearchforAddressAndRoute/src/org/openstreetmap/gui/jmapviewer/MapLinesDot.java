package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import org.json.JSONArray;

import org.openstreetmap.gui.jmapviewer.interfaces.MapLines;

/**
 * A simple implementation of the {@link MapLines} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 * 
 * @author Jan Peter Stotz
 * 
 */
public class MapLinesDot implements MapLines {

    private JSONArray coordinates = new JSONArray();
    private double startlat;
    private double startlon;
    private double stoplat;
    private double stoplon;
    private Color color;
    private boolean setVisible = true;

    /**
     * 
     * @param startlat
     * @param startlon
     * @param stoplat
     * @param stoplon 
     */
    public MapLinesDot(double startlat, double startlon, double stoplat, double stoplon) {
        this(Color.YELLOW, startlat, startlon, stoplat, stoplon);
    }

    /**
     * 
     * @param color
     * @param startlat
     * @param startlon
     * @param stoplat
     * @param stoplon 
     */
    public MapLinesDot(Color color, double startlat, double startlon, double stoplat, double stoplon) {
        super();
        this.color = color;
        this.startlat = startlat;
        this.startlon = startlon;
        this.stoplat = stoplat;
        this.stoplon = stoplon;
    }
        
    /**
     *
     * @param master
     */
    public MapLinesDot(MapLinesDot master) {
        this.startlat = master.getStartLat();
        this.startlon = master.getStartLon();
        this.stoplat = master.getStopLat();
        this.stoplon = master.getStopLon();
        this.color = master.getColor();
        this.setVisible = master.getVisible();
        this.coordinates = master.getCoordinates();
    }
    
    /**
     * 
     * @return die Coordinates
     */
    @Override
    public JSONArray getCoordinates() {
        return coordinates;
    }
    
    /**
     * 
     * @param coordinates_obj 
     */
    public void setCoordinates(JSONArray coordinates_obj) {
        this.coordinates = coordinates_obj;
    }

    /**
     * 
     * @param color 
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * 
     * @param SetVisible_obj 
     */
    @Override
    public void setVisible(boolean SetVisible_obj){
        this.setVisible = SetVisible_obj;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean getVisible(){
        return setVisible;
    }


    @Override
    public double getStartLat() {
        return startlat;
    }

    @Override
    public double getStartLon() {
        return startlon;
    }
    @Override
    public double getStopLat() {
        return stoplat;
    }

    @Override
    public double getStopLon() {
        return stoplon;
    }

    @Override
    public void paintLine(Graphics g, Point startposition, Point stopposition) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(startposition.x, startposition.y, stopposition.x, stopposition.y);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(startposition.x, startposition.y, stopposition.x, stopposition.y);
        g2d.setColor(getColor());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(startposition.x, startposition.y, stopposition.x, stopposition.y);

    }

    /**
     *
     * @param g
     * @param startposition
     * @param stopposition
     */
    @Override
        public void paintRouteFore(Graphics g, Point startposition, Point stopposition) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(getColor());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(startposition.x, startposition.y, stopposition.x, stopposition.y);
                
	}

    /**
     *
     * @param g
     * @param startposition
     * @param stopposition
     */
    @Override
	public void paintRouteBack(Graphics g, Point startposition, Point stopposition) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(startposition.x, startposition.y, stopposition.x, stopposition.y);
                
	}

    /**
     *
     * @return
     */
    @Override
	public String toString() {
		return "MapLine from " + startlat + " " + startlon + " to  " + stoplat + " " + stoplon + " " + getColor().toString();
	}

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

   

}
