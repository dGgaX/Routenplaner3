package de.bring.treeTableRoute.entries;

import de.bring.helfer.*;

public class TreeProduct implements java.io.Serializable {

    private int anzahl = 1;
    private String name;
    private Euro preis;
    private boolean altgeraet = false;
    private TreeOrder parent;
    
    public TreeProduct(TreeOrder parent) {
        this.name = "";
        this.preis = new Euro(0);
        this.parent = parent;
    }
    public TreeProduct(TreeOrder parent, String name, Euro preis) {
        this.name = name;
        this.preis = preis;
        this.parent = parent;
    }
    
    public int getAnzahl() {
        return this.anzahl;
    }
    public String getName() {
        return this.name;
    }
    public Euro getPreis(){
        return this.preis;
    }
    public Euro getGesamtPreis(){
        return this.preis.multi(this.anzahl);
    }
    public Boolean getAltgeraet(){
        return this.altgeraet;
    }
    
    public int getParentIndex() {
        return this.parent.getIndexAt(this);
    }
    
    public void setAnzahl(int anzahl) {
        if (anzahl > 0)
            this.anzahl = anzahl;
    }
    public void setName(String name) {
        if (name.length() >= 8 && name.length() < 100)
            this.name = name;
    }
    public void setPreis(Euro preis) {
        this.preis = preis;
    }
    public void setAltgeraet(Boolean altgeraet) {
        this.altgeraet = altgeraet;
    }
}