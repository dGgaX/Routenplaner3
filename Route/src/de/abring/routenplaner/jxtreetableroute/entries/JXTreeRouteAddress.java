/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.abring.helfer.maproute.LookupAddress;
import de.abring.helfer.maproute.MapAddress;
import de.abring.helfer.maproute.MapRoute;
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Bring
 */
public class JXTreeRouteAddress extends JXTreeRouteEntry implements java.io.Serializable {
    private JXTreeRouteRoute route;
    private String extras;
    private MapMarkerDotWithNumber dot;
    private Appointment appointment;
    private int ID;
            
    public JXTreeRouteAddress(MapAddress address) {
        super();
        this.route = new JXTreeRouteRoute(new MapRoute(address, null));
        this.ID = -1;
    }
    
    public JXTreeRouteAddress(JXTreeRouteAddress master) {
        super();
        this.route = new JXTreeRouteRoute(master.getRoute());
        this.ID = -1;
    }

    /**
     * @return the address
     */
    public MapAddress getAddress() {
        return this.route.getRoute().getStartAddress();
    }

    /**
     * @param address the address to set
     */
    public void setAddress(MapAddress address) {
        this.route.getRoute().setStartAddress(address);
    }

    /**
     * @return the extras
     */
    public String getExtras() {
        return extras;
    }

    /**
     * @param extras the extras to set
     */
    public void setExtras(String extras) {
        this.extras = extras;
    }

    /**
     * @return the dot
     */
    public MapMarkerDotWithNumber getDot() {
        return dot;
    }

    /**
     * @param dot the dot to set
     */
    public void setDot(MapMarkerDotWithNumber dot) {
        this.dot = dot;
    }

    /**
     * @return the appointment
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @param appointment the appointment to set
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public void updateDot() {
        if (this.route.getRoute().getStartAddress().isValid()) {
            this.dot = new MapMarkerDotWithNumber(this.route.getRoute().getStartAddress().getLat(), this.route.getRoute().getStartAddress().getLon());
        }
    }
    
    public String getAddressName() {
        return this.route.getRoute().getStartAddress().toString();
    }
    
    public void findAddress() {
        LookupAddress finde = new LookupAddress(null, true, this.route.getRoute().getStartAddress());
        finde.setVisible(true);
        this.route.getRoute().setStartAddress(finde.getMapAddress());
        updateDot();
    }
    
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the route
     */
    public JXTreeRouteRoute getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(JXTreeRouteRoute route) {
        this.route = route;
    }
}
