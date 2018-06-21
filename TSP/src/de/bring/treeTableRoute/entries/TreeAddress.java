package de.bring.treeTableRoute.entries;

import de.bring.helfer.Address;
import de.bring.helfer.*;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;
import org.openstreetmap.gui.jmapviewer.*;

public class TreeAddress extends TreeStart implements Serializable {

    private Object parent;
    private int quantity;
    private Uhrzeit duration;
    private Address address;
    transient private MapMarkerDotWithNumber dot;
    private Euro price;
    private List<TreeOrder> orderList;
    private Termin date;
    private Boolean ecCash;
    private String extras;
    
    public TreeAddress() {
        super();
        this.parent = null;
        this.quantity = 1;
        this.duration = new Uhrzeit();
        this.address = new Address();
        this.price = new Euro();
        this.orderList = new ArrayList<>();
        this.dot = null;
        this.date = new Termin();
        this.ecCash = Boolean.FALSE;
        this.extras = "";
    }
    public TreeAddress(Object parent, Uhrzeit start, Uhrzeit dauer, String name, Address address, Euro preis) {
        super();
        this.parent = parent;
        setTime(start);
        this.name = name;
        this.quantity = 1;
        setDuration(dauer);
        setAddress(address);
        this.price = preis;
        this.orderList = new ArrayList<>();
        this.date = new Termin();
        this.ecCash = Boolean.FALSE;
        this.extras = "";
        this.orderList = new ArrayList<>();
    }
    public TreeAddress(Object parent, Uhrzeit start, Uhrzeit dauer, String name, Address address, Euro preis, List<TreeOrder> orderList) {
        super();
        this.parent = parent;
        setTime(start);
        this.name = name;
        this.quantity = 1;
        setDuration(dauer);
        setAddress(address);
        this.price = preis;
        this.orderList = new ArrayList<>();
        this.date = new Termin();
        this.ecCash = Boolean.FALSE;
        this.extras = "";
        this.orderList = orderList;
    }
    public TreeAddress(TreeAddress master) {
        this.ID = 0;
        this.time = new Uhrzeit(master.time);
        this.name = master.name;
        this.parent = master.parent;
        this.quantity = master.quantity;
        this.duration = new Uhrzeit(master.duration);
        this.address = new Address(master.address);
        this.dot = new MapMarkerDotWithNumber(Color.WHITE, master.dot.getLat(), master.dot.getLon(), master.dot.getID());
        this.date = new Termin(master.date);
        this.price = new Euro(master.price.getValue());
        this.orderList = new ArrayList<>();
        this.date = new Termin();
        this.ecCash = Boolean.FALSE;
        this.extras = "";
        this.orderList = new ArrayList<>();
        master.getOrderList().stream().forEach((order) -> {
            this.orderList.add(new TreeOrder(this, order));
        });
    }
    
    public int getAnzahl() {
        return this.quantity;
    }
    public Uhrzeit getDauer() {
        return this.duration;
    }
    public Uhrzeit getEnde() {
        Uhrzeit sum = new Uhrzeit(this.time);
        sum.addier(duration);
        for (TreeOrder order: orderList)
            sum.addier(order.getDauer());
        return sum;
    }
    public Address getAddress() {
        return this.address;
    }
    public Euro getPreis(){
        return this.price;
    }
    public Euro getGesamtPreis(){
        Euro Gesamt = new Euro();
        for (TreeOrder order: orderList) {
            for (TreeProduct product: order.getProductList())
                Gesamt.addier(product.getGesamtPreis());
            Gesamt.addier(order.getGesamtPreis());
        }
        Gesamt.subtrahier(this.price.multi(this.quantity));
        return Gesamt;
    }
    public List<TreeOrder> getOrderList() {
        return orderList;
    }
    public MapMarkerDot getDot() {
        if (this.dot == null)
            updateDot();
        return this.dot;
    }
    public Termin getTermin() {
        return this.date;
    }
    public Boolean getEC_Cash() {
        return this.ecCash;
    }
    public String getExtras() {
        return this.extras;
    }
    public Boolean getAltgeraet(){
        for (TreeOrder order : orderList)
            if (order.getAltgeraet())
                return true;
        return false;
    }
    
    public void updateDot() {
        if (this.address.isValid()) {
            if (this.parent instanceof JMapViewer)
                ((JMapViewer)this.parent).deleteMapMarker(this.dot);
            this.dot = new MapMarkerDotWithNumber(this.address.getLat(), this.address.getLon());
            if (this.parent instanceof JMapViewer)
                ((JMapViewer)this.parent).addMapMarker(this.dot);
        }
    }
    
    public int getIndexAt(TreeOrder Order) {
        return orderList.indexOf(Order);
    }
    public Object getParent() {
        return this.parent;
    }
    @Override
    public void setID(int ID) {
        super.setID(ID);
        if (this.dot != null)
            this.dot.setID(ID);
    }
    public void setAnzahl(int quantity) {
        if (quantity >= 1)
            this.quantity = quantity;
    }
    public final void setDuration(Uhrzeit duration) {
        if (duration.getZeit() != null && duration.getZeit().getTimeInMillis() >= -3600000 && duration.getZeit().getTimeInMillis() < 82800000)
            this.duration = duration;
    }
    @Override
    public void setName(String name) {
        if (name.length() > 100)
            name = name.substring(0, 100);
        this.name = name;
    }
    public final void setAddress(Address address) {
        this.address = address;
        updateDot();
    }
    public void setPreis(Euro preis){
        if (preis.getValue() >= 0)
            this.price = preis;
    }
    public void setOrderList(List<TreeOrder> employeeList) {
        this.orderList = employeeList;
    }
    public void setTermin(Termin termin) {
        this.date = termin;
    }
    public void setEC_Cash(Boolean ecCash) {
        this.ecCash = ecCash;
    }
    public void setExtras(String extras) {
        this.extras = extras;
    }
    public void setParent(Object parent) {
        this.parent = parent;
    }
    
}