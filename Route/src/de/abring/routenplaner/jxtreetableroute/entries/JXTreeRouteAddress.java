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
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Bring
 */
public class JXTreeRouteAddress extends JXTreeRouteEntry implements java.io.Serializable {
    private MapAddress address;
    private String extras;
    private MapMarkerDotWithNumber dot;
    private Appointment appointment;
    private int ID;
            
    public JXTreeRouteAddress(MapAddress address) {
        super();
        this.address = address;
        this.ID = -1;
    }
    
    public JXTreeRouteAddress(JXTreeRouteAddress master) {
        super();
        this.address = master.getAddress();
        this.ID = -1;
    }

    /**
     * @return the address
     */
    public MapAddress getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(MapAddress address) {
        this.address = address;
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
        if (this.address.isValid()) {
            this.dot = new MapMarkerDotWithNumber(this.address.getLat(), this.address.getLon());
        }
    }
    
    public String getAddressName() {
        return this.address.toString();
    }
    
    public void findAddress() {
        LookupAddress finde = new LookupAddress(null, true, address);
        finde.setVisible(true);
        this.address = finde.getMapAddress();
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
}
