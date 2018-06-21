
package de.bring.treeTableRoute.entries;

import de.bring.helfer.*;
import java.util.ArrayList;
import java.util.List;

public final class TreeOrder implements java.io.Serializable {

    private Uhrzeit dauer;
    private int anzahl = 1;
    private String name;
    private Euro preis;
    private List<TreeProduct> productList;
    private TreeAddress parent;
    
    public TreeOrder(TreeAddress parent) {
        this.name = "Lieferpaket Komfort";
        this.preis = new Euro(49);
        this.productList = new ArrayList<TreeProduct>();
        setDauer(new Uhrzeit("00:00"));
        this.parent = parent;
    }
    public TreeOrder(TreeAddress parent, Uhrzeit dauer, String name, Euro preis) {
        setDauer(dauer);
        setName(name);
        setPreis(preis);
        setProductList(new ArrayList<TreeProduct>());
        this.parent = parent;
    }
    public TreeOrder(TreeAddress parent, Uhrzeit dauer, String name, Euro preis, List<TreeProduct> productList) {
        setDauer(dauer);
        setName(name);
        setPreis(preis);
        setProductList(productList);
        this.parent = parent;
    }
    public TreeOrder(TreeAddress parent, TreeOrder master) {
        setDauer(new Uhrzeit(master.getDauer()));
        setAnzahl(master.getAnzahl());
        setName(master.getName());
        setPreis(new Euro(master.getPreis().getValue()));
        List<TreeProduct> productList = new ArrayList<TreeProduct>();
        for (TreeProduct produkt : master.getProductList()) {
            TreeProduct neuprodukt = new TreeProduct(this, produkt.getName(), produkt.getPreis());
            neuprodukt.setAnzahl(master.getAnzahl());
            productList.add(neuprodukt);
        }
        setProductList(productList);
        this.parent = parent;
    }

    public Uhrzeit getDauer() {
        return this.dauer;
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
    public List<TreeProduct> getProductList() {
        return productList;
    }
    public Boolean getAltgeraet(){
        for (TreeProduct product : productList)
            if (product.getAltgeraet())
                return true;
        return false;
    }
    
    
    public int getIndexAt(TreeProduct Product) {
        return productList.indexOf(Product);
    }
    public int getParentIndex() {
        return this.parent.getIndexAt(this);
    }
    
    public void setDauer(Uhrzeit dauer) {
        if (dauer.getZeit().getTimeInMillis() >= -3600000 && dauer.getZeit().getTimeInMillis() < 82800000)
            this.dauer = dauer;
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
    public void setProductList(List<TreeProduct> employeeList) {
        this.productList = employeeList;
    }
}