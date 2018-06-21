package de.bring.treeTableRoute.entries;

import de.bring.helfer.*;
import java.io.Serializable;

public class TreeEnd extends TreeEntry implements Serializable {

    /**
     * Creates a new empty entity.
     */
    public TreeEnd() {
        super();
        super.setName("Ende");
    }
    
    /**
     * Clones the existing entity
     * @param master 
     */
    public TreeEnd(TreeEnd master) {
        super(master);
    }
    
    
    /**
     * will do NOTHING!!!
     * @param time (Uhrzeit)
     */
    @Override
    public void setStart(Uhrzeit time) {
    }
    
    /**
     * will do NOTHING!!!
     * @param name (String)
     */
    @Override
    public void setName(String name) {
    }
}