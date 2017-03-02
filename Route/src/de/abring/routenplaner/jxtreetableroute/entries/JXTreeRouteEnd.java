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
public class JXTreeRouteEnd extends JXTreeRouteEntry implements java.io.Serializable {

    public JXTreeRouteEnd() {
        super("End", new TimeOfDay("00:00"), new TimeOfDay("00:00"));
    }
    
    /**
     * @param duration dummy to nothing
     */
    @Override
    public void setDuration(TimeOfDay duration) {
    }
    
    /**
     * @param end the end to set
     */
    public void setEnd(TimeOfDay end) {
        super.setStart(end);
    }
    
}
