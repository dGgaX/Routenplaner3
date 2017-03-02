/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import java.util.ArrayList;
import java.util.List;
import de.abring.helfer.maproute.MapAddress;
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Bring
 */
public class JXTreeRouteAddressClient extends JXTreeRouteAddress implements java.io.Serializable {
    
    private final JXTreeRouteAddressFav favorite;
    private List<JXTreeRouteEntry> items;
    
    private int p;
    private int k;
    private int s;
    
    /**
     * 
     * @param address
     * @param favorite 
     */
    public JXTreeRouteAddressClient(MapAddress address, JXTreeRouteAddressFav favorite) {
        super(address);
        this.favorite = favorite;
        this.items = new ArrayList<>();

        this.p = 0;
        this.k = 0;
        this.s = 0;
    }
    
    /**
     * 
     * @param address
     * @param favorite
     * @param items 
     */
    public JXTreeRouteAddressClient(MapAddress address, JXTreeRouteAddressFav favorite, List<JXTreeRouteEntry> items) {
        super(address);
        this.favorite = favorite;
        this.items = items;
        
        this.p = 0;
        this.k = 0;
        this.s = 0;
    }
    
    /**
     * 
     * @param master 
     */
    public JXTreeRouteAddressClient(JXTreeRouteAddressClient master) {
        super(master.getAddress());
        setName(master.getName());
        setStart(new TimeOfDay(master.getStart()));
        setDuration(new TimeOfDay(master.getDuration()));
        
        
        setExtras(master.getExtras());
        setDot(new MapMarkerDotWithNumber(master.getDot().getLat(), master.getDot().getLon()));
        setAppointment(new Appointment(master.getAppointment()));
        
        this.favorite = master.getFavorite();
        this.items = master.items;
        
        this.p = master.getP();
        this.k = master.getK();
        this.s = master.getS();
    }

    /**
     * 
     * @return 
     */
    public JXTreeRouteAddressFav getFavorite() {
        return favorite;
    }
    
    /**
     * 
     */
    public void clearItemList() {
        this.items.clear();
    }
    
    /**
     * 
     * @param item 
     */
    public void addItem(JXTreeRouteItem item) {
        this.items.add(item);
    }
    
    /**
     * 
     * @param i
     * @param item 
     */
    public void addItem(int i,JXTreeRouteItem item) {
        this.items.add(i, item);
    }
    
    /**
     * 
     * @param item 
     */
    public void removeItem(JXTreeRouteItem item) {
        this.items.remove(item);
    }
    
    /**
     * 
     * @param i 
     */
    public void removeItem(int i) {
        this.items.remove(i);
    }
    
    /**
     * 
     * @return 
     */
    public boolean itemListIsEmpty() {
        return this.items.isEmpty();
    }
    
    /**
     * 
     * @param item
     * @return 
     */
    public boolean itemListContains(JXTreeRouteItem item) {
        return this.items.contains(item);
    }
    
    /**
     * 
     * @return 
     */
    public String itemListToString() {
        return this.items.stream().map((item) -> item + ", ").reduce("", String::concat);
    }

    /**
     * @return the p
     */
    public int getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(int p) {
        this.p = p;
    }

    /**
     * @return the k
     */
    public int getK() {
        return k;
    }

    /**
     * @param k the k to set
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * @return the s
     */
    public int getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(int s) {
        this.s = s;
    }
    
    public String getPKSString() {
        String text = "";
        if (p > 0 || k > 0 || s > 0) {
            text += "(";
            if (p > 0)
                text += String.valueOf(p) + " x P ";
            if (k > 0)
                text += String.valueOf(k) + " x K ";
            if (s > 0)
                text += String.valueOf(s) + " x S ";
            text = text.trim();
            text += ")";
        }
        return text;
    }

    /**
     * @return the items
     */
    public List<JXTreeRouteEntry> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<JXTreeRouteEntry> items) {
        this.items = items;
    }
    
}
