/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.tsp.container;

import de.bring.helfer.*;
import de.bring.helfer.gui.LookupRoute;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Karima
 */
public class AddressPermute{
    List<Route> savedRoutes = new ArrayList<>();
    List<Address> shortestTrack = null;
    Uhrzeit shortestDuration = new Uhrzeit("23:59:59");
    Address startEntry, endEntry;
    Frame parent;
    int vergleich = 0;
    
    public AddressPermute() {
        this.parent = null;
    }
    public AddressPermute(Frame parent) {
        this.parent = parent;
    }
    
    public void permuteRoute(List<Address> arr, int k){
        if (k == 0) {
            startEntry = arr.get(0);
            endEntry = arr.get(arr.size() - 1);
        }
        for(int i = k; i < arr.size(); i++){
            Collections.swap(arr, i, k);
            permuteRoute(arr, k+1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            if (startEntry.equals(arr.get(0)) && endEntry.equals(arr.get(arr.size() - 1))) {
                vergleich++;
                Uhrzeit duration = new Uhrzeit();
                for (int it = 1; it < arr.size(); it++) {
                    Address parentNode = arr.get(it - 1);
                    Address thisNode = arr.get(it);
                    Route currentRoute = isInSavedRoutes(parentNode, thisNode);
                    if (currentRoute == null) {
                        LookupRoute berechne = new LookupRoute(parent, true, new Route(parentNode, thisNode));
                        berechne.setVisible(true);
                        currentRoute = berechne.getMapRoute();
                        savedRoutes.add(currentRoute);
                    }
                    duration.addier(currentRoute.getDauer());
                }
                if (shortestDuration.getMillis()>duration.getMillis()) {
                    shortestDuration = duration;
                    shortestTrack = new ArrayList<>();
                    shortestTrack.addAll(arr);
                }
                
            }
        }
    }
    
    public List<Address> getShortestTrack() {
        return shortestTrack;
    }
    
    public Route isInSavedRoutes(Address start, Address end) {
        for (Route compareRoute : savedRoutes)
            if (compareRoute.getStartAddress().equals(start) && compareRoute.getEndAddress().equals(end))
                return compareRoute;
        return null;
    }
    
//    public static void main(String[] args){
//        AddressPermute.permute(java.util.Arrays.asList(3,4,6,2,1), 0);
//    }
}