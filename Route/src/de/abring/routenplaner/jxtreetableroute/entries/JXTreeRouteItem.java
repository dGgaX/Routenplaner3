/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

/**
 *
 * @author Bring
 */
public class JXTreeRouteItem extends JXTreeRouteEntry implements java.io.Serializable {
    
    /**
     * Init das Item mit dem Namen!
     * @param name 
     */
    public JXTreeRouteItem(String name) {
        setName(name);
    }
    
    /**
     * Überschreibt die toString anweisung mit dem Namen!
     * @return 
     */
    @Override
    public String toString() {
        return getName();
    }
}
