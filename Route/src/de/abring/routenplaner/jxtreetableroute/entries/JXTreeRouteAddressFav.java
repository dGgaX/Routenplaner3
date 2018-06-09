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
import de.abring.helfer.maproute.MapAddress;
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Bring
 */
public class JXTreeRouteAddressFav extends JXTreeRouteAddress implements java.io.Serializable {
    
    public JXTreeRouteAddressFav(MapAddress address) {
        super(address);
    }
    
    public JXTreeRouteAddressFav(JXTreeRouteAddressFav master) {
        super(master.getAddress());
        super.setName(master.getName());
        super.setStart(new TimeOfDay(master.getStart()));
        super.setDuration(new TimeOfDay(master.getDuration()));
        super.setExtras(master.getExtras());
        super.setDot(new MapMarkerDotWithNumber(master.getDot().getLat(), master.getDot().getLon()));
        super.setAppointment(new Appointment(master.getAppointment()));
    }
}
