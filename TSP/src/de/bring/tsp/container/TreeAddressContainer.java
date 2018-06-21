/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.tsp.container;

import de.bring.treeTableRoute.entries.TreeAddress;
import de.bring.treeTableRoute.entries.TreeRoute;
import java.util.ArrayList;
import java.util.List;

/**
 * AContainer for a TreeAddress-Tree
 * @author Karima
 */
public class TreeAddressContainer {
    private TreeAddress me;
    private List<TreeAddressContainer> children = new ArrayList<>();
    private List<TreeRouteContainer> childRouten = new ArrayList<>();
    public boolean isConnected = false;
    public int anzahlKanten = 0;
    
    /**
     * 
     * @param me 
     */
    public TreeAddressContainer(TreeAddress me) {
        this.me = me;
    }
    
    /**
     * 
     * @return this Knot
     */
    public TreeAddress getMe() {
        return this.me;
    }
    
    /**
     * 
     * @return ChildList
     */
    public List<TreeAddressContainer> getChildern() {
        return children;
    }
    
    /**
     * 
     * @return ChildList
     */
    public List<TreeRouteContainer> getChildRouten() {
        return childRouten;
    }
    
    /**
     * Adds a Child to the List
     * @param child 
     */
    public void addChild(TreeAddressContainer child) {
        children.add(child);
    }
    
    /**
     * Removes a Child from the List
     * @param child 
     */
    public void removeChild(TreeAddressContainer child) {
        children.remove(child);
    }
    
    /**
     * Adds a Child to the List
     * @param childRoute
     */
    public void addChildRoute(TreeRouteContainer childRoute) {
        childRouten.add(childRoute);
    }
    
    /**
     * Removes a Child from the List
     * @param childRoute
     */
    public void removeChildRoute(TreeRouteContainer childRoute) {
        childRouten.remove(childRoute);
    }
    
    public List<TreeRouteContainer> getMaxRouten() {
        List<TreeRouteContainer> routen = new ArrayList<>();
        for(TreeAddressContainer child : children) {
            routen.add(new TreeRouteContainer(new TreeRoute(this.me.getAddress(), child.me.getAddress()), this, child));
            this.anzahlKanten++;
            child.anzahlKanten++;
            routen.addAll(child.getMaxRouten());
            routen.add(new TreeRouteContainer(new TreeRoute(child.me.getAddress(), this.me.getAddress()), child, this));
            child.anzahlKanten++;
            this.anzahlKanten++;
        }
        return routen;
    }
}
