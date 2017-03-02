/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

import de.abring.helfer.primitives.TimeOfDay;

/**
 *
 * @author Bring
 */
public class JXTreeRouteStart extends JXTreeRouteEntry implements java.io.Serializable {

    public JXTreeRouteStart(TimeOfDay start) {
        super("Start", start, new TimeOfDay("00:00"));
    }
    
    /**
     * @param duration dummy to nothing
     */
    @Override
    public void setDuration(TimeOfDay duration) {
    
    }
    
}
