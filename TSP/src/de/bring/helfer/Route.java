/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer;

import de.bring.helfer.Uhrzeit;
import org.json.JSONArray;

/**
 *
 * @author Karima
 */
public class Route implements java.io.Serializable {
    private Address StartAddress, EndAddress;
    private int Zeit;
    private double Länge;
    private Coordinates Coordinates;
    private String Beschreibung = "";
    private boolean calculated;
    
    public Route() {
        this.StartAddress = new Address();
        this.EndAddress = new Address();
        this.Coordinates = new Coordinates(new JSONArray());
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public Route(Address StartAddress, Address EndAddress) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(new JSONArray());
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public Route(Address StartAddress, Address EndAddress, JSONArray Coordinates) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public Route(Address StartAddress, Address EndAddress, JSONArray Coordinates, double Länge, int Zeit) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = Länge;
        this.Zeit = Zeit;
        this.calculated = false;
        this.resetCoordinates();
        this.resetLänge();
    }
    public Route(Address StartAddress, Address EndAddress, JSONArray Coordinates, double Länge, int Zeit, String Beschreibung) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = Länge;
        this.Zeit = Zeit;
        this.Beschreibung = Beschreibung;
        this.calculated = false;
    }
    public Route(Route master) {
        this.StartAddress = new Address(master.StartAddress);
        this.EndAddress = new Address(master.EndAddress);
        this.Coordinates = new Coordinates(master.Coordinates.getArrayList());
        this.Länge = master.Länge;
        this.Zeit = master.Zeit;
        this.Beschreibung = master.Beschreibung;
        this.calculated = master.calculated;
    }
    
    public Address getStartAddress() {
        return this.StartAddress;
    }
    public Address getEndAddress() {
        return this.EndAddress;
    }
    public JSONArray getCoordinates() {
        return this.Coordinates.getJSONList();
    }
    public Double getLänge() {
        return this.Länge;
    }
    public int getZeit() {
        return this.Zeit;
    }
    public Uhrzeit getDauer() {
        return new Uhrzeit(this.Zeit);
    }
    public String getBeschreibung() {
        return this.Beschreibung;
    }
    public boolean isCalculated() {
        return this.calculated;
    }
    public void setStartAddress(Address StartAddress) {
        if (StartAddress.isValid()) {
            this.StartAddress = StartAddress;
            this.resetCoordinates();
            this.resetLänge();
        }
    }
    public void setEndAddress(Address EndAddress) {
        if (EndAddress.isValid()) {
            this.EndAddress = EndAddress;
            this.calculated = false;
            this.resetCoordinates();
            this.resetLänge();
        }
    }
    public void setCoordinates(JSONArray Coordinates) {
        this.Coordinates = new Coordinates(Coordinates);
    }
    public void setLänge(Double Länge) {
        this.Länge = Länge;
    }
    public void setZeit(int Zeit) {
        this.Zeit = Zeit;
    }
    public void setBeschreibung(String Beschreibung) {
        this.Beschreibung = Beschreibung;
    }
    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
        if (!this.calculated) {
            this.resetCoordinates();
            this.resetLänge();
        }
    }
    @Override
    public Route clone() {
        return new Route(this);
    }
    private void resetCoordinates() {
        if (this.StartAddress.isValid() && this.EndAddress.isValid()) {
            this.Coordinates = new Coordinates();
            this.Coordinates.addEntry(this.StartAddress.getLat(), this.StartAddress.getLon());
            this.Coordinates.addEntry(this.EndAddress.getLat(), this.EndAddress.getLon());
        }
        this.calculated = false;
    }
    private void resetLänge() {
        this.Zeit = 0;
        double erdumfang = 6378.388;
        double multiSin = Math.sin(this.StartAddress.getLat()) * Math.sin(this.EndAddress.getLat());
        double multiCos = Math.cos(this.StartAddress.getLat()) * Math.cos(this.EndAddress.getLat());
        double subCos = Math.cos(this.EndAddress.getLon() - this.StartAddress.getLon());
        double temp = Math.acos((multiSin + multiCos) * subCos);
        this.Länge = Math.round(erdumfang * (Math.PI / 180) * temp*1000)/1000.0;
        int kmh = 0;
        if (this.Länge > 100)
            kmh = 160;
        else if (this.Länge > 50)
            kmh = 120;
        else if (this.Länge > 10)
            kmh = 80;
        else if (this.Länge > 0)
            kmh = 50;
        
        this.Zeit = (int)Math.round((this.Länge / 40) * 3600) ;
        this.calculated = false;
    }
}
