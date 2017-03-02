package org.openstreetmap.gui.jmapviewer.interfaces;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.Graphics;
import java.awt.Point;
import org.json.JSONArray;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

/**
 * Interface to be implemented by all elements that can be displayed on the map.
 * 
 * @author Jan Peter Stotz
 * @see JMapViewer#addMapMarker(MapMarker)
 * @see JMapViewer#getMapMarkerList()
 */
public interface MapLines {

        public JSONArray getCoordinates();
	
        /**
	 * @return Latitude of the map marker position
	 */
	public double getStartLat();

	/**
	 * @return Longitude of the map marker position
	 */
	public double getStartLon();
        /**
	 * @return Latitude of the map marker position
	 */
	public double getStopLat();

	/**
	 * @return Longitude of the map marker position
	 */
	public double getStopLon();

	/**
         * @param setVisible
         */
	public void setVisible(boolean setVisible);
        
        public boolean getVisible();
        
        /**
	 * Paints the map marker on the map. The <code>position</code> specifies the
	 * coordinates within <code>g</code>
	 * 
	 * @param g
	 * @param position
	 */
	public void paintLine(Graphics g, Point startposition, Point stopposition);
        public void paintRouteBack(Graphics g, Point startposition, Point stopposition);
        public void paintRouteFore(Graphics g, Point startposition, Point stopposition);
}
