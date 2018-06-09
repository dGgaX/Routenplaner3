/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.maproute;

import de.abring.helfer.maproute.MapAddress;
import de.abring.helfer.primitives.TimeOfDay;
import org.json.JSONArray;
import org.openstreetmap.gui.jmapviewer.Coordinates;

/**
 *
 * @author Karima
 */
public class MapRoute implements java.io.Serializable {
    private MapAddress StartAddress, EndAddress;
    private int Zeit;
    private double Länge;
    private Coordinates Coordinates;
    private String Beschreibung = "";
    private boolean calculated;
    
    public MapRoute() {
        this.StartAddress = new MapAddress();
        this.EndAddress = new MapAddress();
        this.Coordinates = new Coordinates(new JSONArray());
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public MapRoute(MapAddress StartAddress, MapAddress EndAddress) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(new JSONArray());
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public MapRoute(MapAddress StartAddress, MapAddress EndAddress, JSONArray Coordinates) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = 0;
        this.Zeit = 0;
        this.resetCoordinates();
        this.resetLänge();
    }
    public MapRoute(MapAddress StartAddress, MapAddress EndAddress, JSONArray Coordinates, double Länge, int Zeit) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = Länge;
        this.Zeit = Zeit;
        this.calculated = false;
        this.resetCoordinates();
        this.resetLänge();
    }
    public MapRoute(MapAddress StartAddress, MapAddress EndAddress, JSONArray Coordinates, double Länge, int Zeit, String Beschreibung) {
        this.StartAddress = StartAddress;
        this.EndAddress = EndAddress;
        this.Coordinates = new Coordinates(Coordinates);
        this.Länge = Länge;
        this.Zeit = Zeit;
        this.Beschreibung = Beschreibung;
        this.calculated = false;
    }
    public MapRoute(MapRoute master) {
        this.StartAddress = new MapAddress(master.StartAddress);
        this.EndAddress = new MapAddress(master.EndAddress);
        this.Coordinates = new Coordinates(master.Coordinates.getArrayList());
        this.Länge = master.Länge;
        this.Zeit = master.Zeit;
        this.Beschreibung = new String(master.Beschreibung);
        this.calculated = master.calculated;
    }
    
    public MapAddress getStartAddress() {
        return this.StartAddress;
    }
    public MapAddress getEndAddress() {
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
    public TimeOfDay getDauer() {
        return new TimeOfDay(this.Zeit);
    }
    public String getBeschreibung() {
        return this.Beschreibung;
    }
    public boolean isCalculated() {
        return this.calculated;
    }
    public void setStartAddress(MapAddress StartAddress) {
        if (StartAddress.isValid()) {
            this.StartAddress = StartAddress;
            this.resetCoordinates();
            this.resetLänge();
        }
    }
    public void setEndAddress(MapAddress EndAddress) {
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
    
    public MapRoute clone() {
        return new MapRoute(this);
    }
    
    private void resetCoordinates() {
        if (this.StartAddress != null && this.StartAddress.isValid() && this.EndAddress != null && this.EndAddress.isValid()) {
            this.Coordinates = new Coordinates();
            this.Coordinates.addEntry(this.StartAddress.getLat(), this.StartAddress.getLon());
            this.Coordinates.addEntry(this.EndAddress.getLat(), this.EndAddress.getLon());
        }
        this.calculated = false;
    }
    
    private void resetLänge() {
        this.Zeit = 0;
        this.calculated = false;
        this.Länge = 0;
        if (this.StartAddress != null && this.StartAddress.isValid() && this.EndAddress != null && this.EndAddress.isValid()) {
            double erdumfang = 6378.388;
            double multiSin = Math.sin(this.StartAddress.getLat()) * Math.sin(this.EndAddress.getLat());
            double multiCos = Math.cos(this.StartAddress.getLat()) * Math.cos(this.EndAddress.getLat());
            double subCos = Math.cos(this.EndAddress.getLon() - this.StartAddress.getLon());
            double temp = Math.acos((multiSin + multiCos) * subCos);
            this.Länge = Math.round(erdumfang * (Math.PI / 180) * temp * 1000) / 1000.0f;
            float kmh = 0.0f;
            if (this.Länge > 100)
                kmh = 160.0f;
            else if (this.Länge > 50)
                kmh = 120.0f;
            else if (this.Länge > 10)
                kmh = 80.0f;
            else if (this.Länge > 0)
                kmh = 50.0f;

            this.Zeit = (int)Math.round((this.Länge / kmh) * 3600) ;
        }
    }
    
    @Override
    public String toString() {
        String temp = "";
        temp += "Route ist ";
        if (!this.calculated) {
            temp += "un";
        }
        temp += "berechnet und beträgt ca. ";
        temp += String.valueOf(this.Länge);
        temp += " km";
        if (!this.calculated) {
            temp += " Luftlinie";
        }
        temp += ".";
        return temp;
    }
}
