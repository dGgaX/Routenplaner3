/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute.entries;

import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.gui.ImageIconFromColor;
import de.abring.routenplaner.jxtreetableroute.JXTreeRouteCopy;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Bring
 */
public class JXTreeRouteTour extends JXTreeRouteEntry implements java.io.Serializable {

    private List<JXTreeRouteEntry> entryList = new ArrayList<>();
    private String driver;
    private String coDriver;
    private String car;
    private Color color;
    private final ImageIconFromColor icon;
    
    public JXTreeRouteTour(String tourName) {
        super();
        setName(tourName);
        this.entryList = new ArrayList<>();
        this.driver = "";
        this.coDriver = "";
        this.car = "";
        this.color = Color.WHITE;
        this.icon = new ImageIconFromColor(20, this.color, Color.DARK_GRAY);
    }
    
    public JXTreeRouteTour(JXTreeRouteTour master) {
        super();
        setName(master.getName());
        this.entryList = JXTreeRouteCopy.copy(master.getEntryList());
        this.driver = master.getDriver();
        this.coDriver = master.getCoDriver();
        this.car = master.getCar();
        this.color = master.getColor();
        this.icon = new ImageIconFromColor(20, master.getColor(), Color.DARK_GRAY);
    }
    
    public JXTreeRouteTour(String tourName, String driver, String coDriver, String car) {
        super();
        setName(tourName);
        this.entryList = new ArrayList<>();
        this.driver = driver;
        this.coDriver = coDriver;
        this.car = car;
        this.color = Color.WHITE;
        this.icon = new ImageIconFromColor(20, this.color, Color.DARK_GRAY);
    }

    /**
     * @return the entryList
     */
    public List<JXTreeRouteEntry> getEntryList() {
        return entryList;
    }

    /**
     * @param entryList the entryList to set
     */
    public void setEntryList(List<JXTreeRouteEntry> entryList) {
        this.entryList = entryList;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the coDriver
     */
    public String getCoDriver() {
        return coDriver;
    }

    /**
     * @param coDriver the coDriver to set
     */
    public void setCoDriver(String coDriver) {
        this.coDriver = coDriver;
    }

    /**
     * @return the car
     */
    public String getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(String car) {
        this.car = car;
    }
    
    /**
     * @return the end
     */
    @Override
    public TimeOfDay getEnd() {
        super.getDuration();
        return super.getEnd();
    }

    /**
     * @return the duration
     */
    @Override
    public TimeOfDay getDuration() {
        super.setDuration(new TimeOfDay());
        for (JXTreeRouteEntry entry : entryList) {
            super.getDuration().addier(entry.getDuration());
        }
        return super.getDuration();
    }

    /**
     * @param duration the duration to set
     */
    @Override
    public void setDuration(TimeOfDay duration) {
    }

    /**
     * @return the colorIcon:
     * round and darkggrey
     */
    public final ImageIcon getIcon() {
        return this.icon.getImageIcon();
    }

    /**
     * @return the color
     */
    public final Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public final void setColor(Color color) {
        this.color = color;
        this.icon.updateColor(this.color);
    }
}
