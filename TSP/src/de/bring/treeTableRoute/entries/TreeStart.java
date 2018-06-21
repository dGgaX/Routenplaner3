package de.bring.treeTableRoute.entries;

import de.bring.helfer.Uhrzeit;
import java.io.Serializable;

public class TreeStart extends TreeEntry implements Serializable {
    
    /**
     * Creates a new empty entity.
     */
    public TreeStart() {
        super();
        super.name = "Start";
    }
    
    /**
     * 
     * @param time 
     */
    public TreeStart(Uhrzeit time) {
        super();
        setTime(time);
        super.name = "Start";
    }
    
    /**
     * Clones the existing entity
     * @param master 
     */
    public TreeStart(TreeStart master) {
        super(master);
    }
    
    
    /**
     * will do NOTHING!!!
     * @param time 
     */
    @Override
    public void setEnd(Uhrzeit time) {
    }
    
    /**
     * will do NOTHING!!!
     * @param name 
     */
    @Override
    public void setName(String name) {
    }
}