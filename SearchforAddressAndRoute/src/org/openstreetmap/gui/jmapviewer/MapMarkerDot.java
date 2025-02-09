package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 * 
 * @author Jan Peter Stotz
 * 
 */
public class MapMarkerDot implements MapMarker, java.io.Serializable {

	private double lat;
	private double lon;
	private Color color;
        private boolean setVisible = true;
        
	public MapMarkerDot(double lat, double lon) {
		this(Color.YELLOW, lat, lon);
	}

	public MapMarkerDot(Color color, double lat, double lon) {
		super();
		this.color = color;
		this.lat = lat;
		this.lon = lon;
	}
        
        public void setColor(Color color) {
            this.color = color;
        }
        
        @Override
        public void setVisible(boolean SetVisible_obj){
            this.setVisible = SetVisible_obj;
        }
        @Override
	public boolean getVisible(){
            return this.setVisible;
        }
	
        @Override
        public double getLat() {
            return lat;
	}

        @Override
	public double getLon() {
		return lon;
	}

        @Override
	public void paint(Graphics g, Point position) {
		int size_h = 5;
		int size = size_h * 2;
		g.setColor(color);
		g.fillOval(position.x - size_h, position.y - size_h, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(position.x - size_h, position.y - size_h, size, size);
	}

	@Override
	public String toString() {
		return "MapMarker at " + lat + " " + lon + " " + color.toString();
	}

   

}
