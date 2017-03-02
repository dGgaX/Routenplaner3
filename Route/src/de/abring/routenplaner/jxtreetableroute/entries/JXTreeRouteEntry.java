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
public class JXTreeRouteEntry implements java.io.Serializable {
    private String name;
    private TimeOfDay start;
    private TimeOfDay duration;
    private JXTreeRouteEntry parent;
    
    public JXTreeRouteEntry() {
        this.name = new String();
        this.start = new TimeOfDay();
        this.duration = new TimeOfDay();
        this.parent = null;
    }
    
    public JXTreeRouteEntry(String name, TimeOfDay start, TimeOfDay duration) {
        this.name = name;
        this.start = start;
        this.duration = duration;
        this.parent = null;
    }

    public JXTreeRouteEntry(String name, TimeOfDay start, TimeOfDay duration, JXTreeRouteEntry parent) {
        this.name = name;
        this.start = start;
        this.duration = duration;
        this.parent = parent;
    }


    public JXTreeRouteEntry(JXTreeRouteEntry master) {
        this.name = master.getName();
        this.start = new TimeOfDay(master.getStart());
        this.duration = new TimeOfDay(master.getDuration());
        this.parent = master.getParent();
    }
    
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the start
     */
    public final TimeOfDay getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public final void setStart(TimeOfDay start) {
        this.start = start;
    }
    
    /**
     * @return the end
     */
    public TimeOfDay getEnd() {
        TimeOfDay end = new TimeOfDay(start);
        end.addier(duration);
        return end;
    }

    /**
     * @return the duration
     */
    public TimeOfDay getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(TimeOfDay duration) {
        this.duration = duration;
    }

    /**
     * @return the parent
     */
    public JXTreeRouteEntry getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(JXTreeRouteEntry parent) {
        this.parent = parent;
    }
}
