
package de.bring.helfer;


/**
 *
 * @author AndyB
 */
public class Address implements java.io.Serializable {
    private double lat, lon;
    private String SuchString, Straße, HsNr, PLZ, Stadt, Land;
    
    public Address() {
        this.SuchString = "";
        this.Straße = "";
        this.HsNr = "";
        this.PLZ = "";
        this.Stadt = "";
        this.Land = "";
        this.lat = 91;
        this.lon = 181;
    }
    public Address(String SuchString) {
        this.SuchString = SuchString;
        this.Straße = "";
        this.HsNr = "";
        this.PLZ = "";
        this.Stadt = "";
        this.Land = "";
        this.lat = 91;
        this.lon = 181;
    }
    public Address(double lat, double lon) {
        this.SuchString = "";
        this.Straße = "";
        this.HsNr = "";
        this.PLZ = "";
        this.Stadt = "";
        this.Land = "";
        this.lat = lat;
        this.lon = lon;
    }
    public Address(String Straße, String HsNr, String PLZ, String Stadt, String Land, double lat, double lon) {
        setAddress(Straße, HsNr, PLZ, Stadt, Land, lat, lon);
    }
    public Address(Address master) {
        this.Straße = master.Straße;
        this.HsNr = master.HsNr;
        this.PLZ = master.PLZ;
        this.Stadt = master.Stadt;
        this.Land = master.Land;
        this.lat = master.lat;
        this.lon = master.lon;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getStraße() {
        return Straße;
    }
    public String getHsNr() {
        return HsNr;
    }
    public String getPLZ() {
        return PLZ;
    }
    public String getStadt() {
        return Stadt;
    }
    public String getSuchString() {
        return SuchString;
    }
    public String getLand() {
        return Land;
    }
    
    public boolean isValid() {
        int minlat = -85;
        int maxlat = 85;
        int minlon = -180;
        int maxlon = 180;
        return (minlat <= this.lat && this.lat <= maxlat && minlon <= this.lon && this.lon <= maxlon);
    }
    
    public final void setAddress(String Straße, String HsNr, String PLZ, String Stadt, String Land, double lat, double lon) {
        this.Straße = Straße;
        this.HsNr = HsNr;
        this.PLZ = PLZ;
        this.Stadt = Stadt;
        this.Land = Land;
        this.lat = lat;
        this.lon = lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public void setStraße(String Straße) {
        this.Straße = Straße;
    }
    public void setHsNr(String HsNr) {
        this.HsNr = HsNr;
    }
    public void setPLZ(String PLZ) {
        this.PLZ = PLZ;
    }
    public void setStadt(String Stadt) {
        this.Stadt = Stadt;
    }
    public void setSuchString(String SuchString) {
        this.SuchString = SuchString;
    }
    public void setLand() {
        this.Land  = Land;
    }
    @Override
    public String toString() {
        String ersteZeile = this.Straße;
        if (!this.Straße.isEmpty() && !this.HsNr.isEmpty())
            ersteZeile += " ";
        ersteZeile += this.HsNr;
        String zweiteZeile = this.PLZ;
        if (!this.PLZ.isEmpty() && !this.Stadt.isEmpty())
            zweiteZeile += " ";
        zweiteZeile += this.Stadt;
        String dritteZeile = this.Land;
        String Adresse = ersteZeile;
        if (!ersteZeile.isEmpty() && !zweiteZeile.isEmpty())
            Adresse += ", \n";
        Adresse += zweiteZeile;
        if (!zweiteZeile.isEmpty() && !dritteZeile.isEmpty())
            Adresse += ", \n";
        Adresse += dritteZeile;
        if (Adresse.isEmpty())
            Adresse += this.SuchString;
        return Adresse;
    }
    @Override
    public Address clone() {
        return new Address(this);
    }
}
