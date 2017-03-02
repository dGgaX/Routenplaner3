/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute;

import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karima
 */
public class JXTreeRouteCopy {
    
    /**
     * Copy one Entrie
     * @param master
     * @return 
     */
    public static final JXTreeRouteEntry copy(JXTreeRouteEntry master) {
        JXTreeRouteEntry copy = null;
        if (master instanceof JXTreeRouteAddressClient) {
            copy =  new JXTreeRouteAddressClient((JXTreeRouteAddressClient) master);
        } else if (master instanceof JXTreeRouteAddressFav) {
            copy =  new JXTreeRouteAddressFav((JXTreeRouteAddressFav) master);
        } else if (master instanceof JXTreeRouteRoute) {
            copy =  new JXTreeRouteRoute((JXTreeRouteRoute) master);
        } else if (master instanceof JXTreeRouteItem) {
            copy =  new JXTreeRouteItem(((JXTreeRouteItem) master).getName());
        } else if (master instanceof JXTreeRouteTour) {
            copy =  new JXTreeRouteTour((JXTreeRouteTour) master);
        }
        return copy;
    }
    
    /**
     * Copy a List of Entries
     * @param master
     * @return 
     */
    public static final List<JXTreeRouteEntry> copy(List<JXTreeRouteEntry> master) {
        List<JXTreeRouteEntry> copy = new ArrayList<>();
        master.forEach((entry) -> {
            copy.add(copy(entry));
        });
        return copy;
    }
}
