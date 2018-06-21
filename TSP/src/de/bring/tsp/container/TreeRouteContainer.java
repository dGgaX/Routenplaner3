/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.tsp.container;

import de.bring.treeTableRoute.entries.TreeRoute;

/**
 *
 * @author Karima
 */
public class TreeRouteContainer {
    public TreeRoute route;
    public TreeAddressContainer start;
    public TreeAddressContainer ende;
    
    public TreeRouteContainer (TreeRoute route, TreeAddressContainer start, TreeAddressContainer ende) {
        this.route = route;
        this.start = start;
        this.ende = ende;
    }
}
