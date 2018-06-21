
package de.bring.treeTableRoute.entries;

import de.bring.helfer.gui.LookupRoute;
import de.bring.helfer.Route;
import de.bring.helfer.Address;
import de.bring.helfer.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.openstreetmap.gui.jmapviewer.*;

public class TreeRoute extends TreeEntry implements java.io.Serializable {

    private Uhrzeit start;
    private Route route;
    transient private MapLinesDot mapLinesDot;
    
    public TreeRoute(Address startaddress, Address endaddress) {
        this.start = new Uhrzeit();
        this.route = new Route(startaddress, endaddress);
        this.mapLinesDot = new MapLinesDot(this.route.getStartAddress().getLat(), this.route.getStartAddress().getLon(), this.route.getEndAddress().getLat(), this.route.getEndAddress().getLon());
        this.mapLinesDot.setCoordinates(this.route.getCoordinates());
    }
    public TreeRoute(Route route) {
        this.start = new Uhrzeit();
        this.route = route;
        if (this.route.isCalculated()) {
            updateLine();
        } else {
            this.mapLinesDot = new MapLinesDot(this.route.getStartAddress().getLat(), this.route.getStartAddress().getLon(), this.route.getEndAddress().getLat(), this.route.getEndAddress().getLon());
            this.mapLinesDot.setCoordinates(this.route.getCoordinates());
        }
    }
    public TreeRoute(TreeRoute master) {
        this.start = new Uhrzeit(master.start);
        this.route = new Route(master.route);
        this.mapLinesDot = new MapLinesDot(this.route.getStartAddress().getLat(), this.route.getStartAddress().getLon(), this.route.getEndAddress().getLat(), this.route.getEndAddress().getLon());
        this.mapLinesDot.setCoordinates(this.route.getCoordinates());
    }
    
    public void setStart(Uhrzeit start) {
        if (start.getZeit().getTimeInMillis() >= -3600000 && start.getZeit().getTimeInMillis() < 82800000)
            this.start = start;
    }
    public void setStartAddress(Address startaddress) {
        this.route.setStartAddress(startaddress);
        updateLine();
    }
    public void setEndAddress(Address endaddress) {
        this.route.setEndAddress(endaddress);
        updateLine();
    }

    public Address getStartAddress() {
        return this.route.getStartAddress();
    }
    public Address getEndAddress() {
        return this.route.getEndAddress();
    }
    public Uhrzeit getStart() {
        return this.start;
    }
    public Uhrzeit getDauer() {
        if (route == null)
            return new Uhrzeit();
        return route.getDauer();
    }
    public Uhrzeit getEnde() {
        Calendar sum = new GregorianCalendar();
        sum.setTimeInMillis(this.start.getZeit().getTimeInMillis() + (3600 * 1000));                ;
        sum.add(Calendar.MILLISECOND, ((int)this.getDauer().getZeit().getTimeInMillis()));
        return new Uhrzeit(sum);
    }
    public String getName() {
        return "Die Route beträgt " + route.getLänge() + " Kilometer.";
    }
    public Euro getPreis(){
        return new Euro();
    }
    public Route getRoute() {
        return this.route;
    }
    public MapLinesDot getLine() {
        if (this.mapLinesDot == null)
            updateLine();
        return this.mapLinesDot;
    }
    public final void updateLine() {
        if (this.route.getStartAddress().isValid() && this.route.getEndAddress().isValid()) {
            this.mapLinesDot = new MapLinesDot(this.route.getStartAddress().getLat(), this.route.getStartAddress().getLon(), this.route.getEndAddress().getLat(), this.route.getEndAddress().getLon());
            this.mapLinesDot.setCoordinates(this.route.getCoordinates());
        }
    }
    public void calcRoute() {
        LookupRoute berechne = new LookupRoute(null, true, this.route);
        berechne.setVisible(true);
        this.route = berechne.getMapRoute();
        updateLine();
    }
    
}