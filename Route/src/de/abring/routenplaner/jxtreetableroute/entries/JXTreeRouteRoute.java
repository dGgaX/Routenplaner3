/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.maproute.LookupRoute;
import org.openstreetmap.gui.jmapviewer.MapLinesDot;
import de.abring.helfer.maproute.MapRoute;

/**
 *
 * @author Bring
 */
public class JXTreeRouteRoute extends JXTreeRouteEntry implements java.io.Serializable {
    private MapRoute route;
    transient private MapLinesDot mapLinesDot;
    
    public JXTreeRouteRoute(MapRoute route) {
        super();
        this.route = route;
        setName("");
    }
    
    public JXTreeRouteRoute(JXTreeRouteRoute master) {
        super();
        setName(master.getName());
        setStart(new TimeOfDay(master.getStart()));
        setDuration(new TimeOfDay(master.getDuration()));
        this.route = master.getRoute();
        this.mapLinesDot = new MapLinesDot(master.getMapLinesDot());
    }

    /**
     * @return the route
     */
    public MapRoute getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(MapRoute route) {
        this.route = route;
    }

    /**
     * @return the mapLinesDot
     */
    public MapLinesDot getMapLinesDot() {
        if (this.mapLinesDot == null)
            updateLine();
        return mapLinesDot;
    }

    /**
     * @param mapLinesDot the mapLinesDot to set
     */
    public void setMapLinesDot(MapLinesDot mapLinesDot) {
        this.mapLinesDot = mapLinesDot;
    }
 
    /**
     * @param duration the duration to set
     */
    @Override
    public final void setDuration(TimeOfDay duration) {
        super.setDuration(duration);
    }

    public void updateLine() {
        if (this.route.getStartAddress() != null && this.route.getStartAddress().isValid() && this.route.getEndAddress() != null  && this.route.getEndAddress().isValid()) {
            this.mapLinesDot = new MapLinesDot(this.route.getStartAddress().getLat(), this.route.getStartAddress().getLon(), this.route.getEndAddress().getLat(), this.route.getEndAddress().getLon());
            this.mapLinesDot.setCoordinates(this.route.getCoordinates());
        }
    }
    
    public MapLinesDot getLine() {
        if (this.mapLinesDot == null)
            updateLine();
        return this.mapLinesDot;
    }
    
    
    
    public void calcRoute() {
        LookupRoute berechne = new LookupRoute(null, true, this.route);
        berechne.setVisible(true);
        this.route = berechne.getMapRoute();
        updateLine();
    }
}
