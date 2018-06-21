
package de.bring.treeTableRoute.entries;

import de.bring.helfer.Uhrzeit;
import java.io.Serializable;

/**
 *
 * @author Bring
 */
public class TreeEntry implements Serializable {
    
    protected int ID;
    protected Uhrzeit time;
    protected String name;
    
    /**
     * Creates a new empty entity.
     */
    public TreeEntry() {
        this.ID = 0;
        setTime(new Uhrzeit("00:00"));
        this.name = "";
    }
    
    /**
     * Clones the existing entity
     * @param master 
     */
    public TreeEntry(TreeEntry master) {
        this.ID = master.ID;
        setTime(master.time);
        this.name = master.name;
    }
    
    
    /**
     * gets the ID of this entity
     * @return ID (Integer)
     */
    public int getID() {
        return this.ID;
    }
    
    /**
     * gets the start-time of this entity
     * @return time (Uhrzeit)
     */
    public Uhrzeit getStart() {
        return this.time;
    }
    
    /**
     * gets the end-time of this entity
     * @return time (Uhrzeit)
     */
    public Uhrzeit getEnd() {
        return this.time;
    }
    
    /**
     * gets the name of this entity
     * @return name (String)
     */
    public String getName() {
        return this.name;
    }
    
    
    /**
     * sets the ID
     * @param ID 
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    
    /**
     * sets the start-time
     * @param time 
     */
    protected final void setTime(Uhrzeit time) {
        if (time != null && time.isValid())
            this.time = time;
    }
    
    /**
     * sets the start-time
     * @param time 
     */
    public void setStart(Uhrzeit time) {
        if (time != null && time.isValid())
            this.time = time;
    }
    
    /**
     * sets the end-time
     * @param time 
     */
    public void setEnd(Uhrzeit time) {
        if (time != null && time.isValid())
            this.time = time;
    }
    
    /**
     * sets the name of this entity
     * @param name 
     */
    public void setName(String name) {
        if (!name.isEmpty())
            this.name = name;
    }
}
