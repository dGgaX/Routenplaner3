/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.PathFinder;
import de.abring.routenplaner.jxtreetableroute.*;
import de.abring.routenplaner.Routenplaner;
import de.abring.routenplaner.gui.dialogues.*;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.Color;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.*;
import javax.swing.*;
import static javax.swing.TransferHandler.COPY;
import de.abring.helfer.maproute.LookupAddress;
import de.abring.pdferkennung.gui.dialogues.JscanPDF;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Karima
 */
public class Main extends javax.swing.JFrame {

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    
    private final Routenplaner parent;
    private File FavoritenFile;
    private String workingDir;
    private String programDir;
    private int tourCounter;
    private double wavelength = 0;
    PageFormat format;
    
    /**
     * Creates new form Main
     * @param parent
     */
    public Main(Routenplaner parent) {
        LOGGER.info("Starte das Hauptfenster ...");
        
        this.parent = parent;
        
        this.tourCounter = 0;
        this.format = PrinterJob.getPrinterJob().defaultPage();
        this.format.setOrientation(PageFormat.LANDSCAPE);
        
        double rand = 20.0d; // in mm
        
        Paper paper = this.format.getPaper();
        double margin = (72.0d * rand) / 25.4d;
        paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
        this.format.setPaper(paper);
        
        initComponents();
        initOtherComponents();
    }

//-- getter/setter - Methodes
    
    /**
     * @return the FavoriteTable
     */
    public de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute getFavoriteTable() {
        return FavoriteTable;
    }

    /**
     * @return the Karte
     */
    public org.openstreetmap.gui.jmapviewer.JMapViewer getKarte() {
        return Karte;
    }

    /**
     * @return the TourPane
     */
    public org.jdesktop.swingx.JXTaskPane getTourPane() {
        return TourPane;
    }

    /**
     * @return the Desktop_Pane
     */
    public javax.swing.JDesktopPane getDesktop_Pane() {
        return Desktop_Pane;
    }
    
    /**
     *
     * @param visible
     */
    public void setjPopupMenuRoutejMenuItemRouteMapVisibleSelected(boolean visible) {
        this.jPopupMenuRoutejMenuItemRouteMapVisible.setSelected(visible);
    }
    
//-- Tour - Methodes

    /**
     * creates a new Tour
     */
    public void createTour() {
        LOGGER.debug("create new Tour!");
        Route tour = new Route("neue Tour " + String.valueOf(++this.tourCounter), this);
        tour.setTourColor(wavelength);
        wavelength+=2.2;
        tour.getTablePane().setComponentPopupMenu(jPopupMenuRoute);
        
        
        this.Desktop_Pane.add(tour);
        try {
            tour.setMaximum(true);
        } catch (PropertyVetoException e) {
            // Vetoed by internalFrame
            // ... possibly add some handling for this case
        }
        tour.show();
        Karte.setDisplayToFitMapMarkers();
    }
    
    /**
     * loads a tour from File
     * @param tourFilename
     * @return 
     */
    public boolean loadTour(File tourFilename) {
        LOGGER.info("Öffne Routendatei...");
        Object input = null;
        try {
            FileInputStream fileIn = new FileInputStream(tourFilename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            input = in.readObject();
            in.close();
            fileIn.close();
            if (!(input instanceof JXTreeRouteTour)) {
                throw new IllegalArgumentException( "Falsche Daten-Datei!!!" );
            }
        } catch (IOException | ClassNotFoundException | IllegalArgumentException ex) {
            LOGGER.warn(ex);
            return false;
        }
        JXTreeRouteTour tourInput = (JXTreeRouteTour) input;
        
        Route tour = new Route(tourInput, this);
        tour.setTourColor(wavelength);
        wavelength+=2.2;
        tour.getTablePane().setComponentPopupMenu(jPopupMenuRoute);
        
        
        this.Desktop_Pane.add(tour);
        try {
            tour.setMaximum(true);
        } catch (PropertyVetoException e) {
            // Vetoed by internalFrame
            // ... possibly add some handling for this case
        }
        tour.show();
        Karte.setDisplayToFitMapMarkers();
        return true;
    }
    
    /**
     * 
     * @param file
     * @return success
     */
    public boolean saveThisTour(File file) {
        return saveThisTour(file, true);
    }
    
    /**
     * 
     * @param file
     * @param overwrite
     * @return success
     */
    public boolean saveThisTour(File file, boolean overwrite) {
        if (this.Desktop_Pane.getSelectedFrame() instanceof Route)
            return saveTour(((Route) this.Desktop_Pane.getSelectedFrame()).getTour(), file, overwrite);
        return false;
    }
    
    /**
     * 
     * @param tour
     * @param file
     * @param overwrite
     * @return success
     */
    public boolean saveTour(JXTreeRouteTour tour, File file, boolean overwrite) {
        LOGGER.debug("Speichere Routedatei...");
        try {
            if (file == null)
                return false;
            if (file.exists() && !overwrite)
                if(!MSG.msgOverwriteFile(this, file.getName()))
                    return false;
            FileOutputStream fileOut = new FileOutputStream(file);
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(tour);
            }
            
        } catch(IOException ex) {
            LOGGER.warn(ex);
            return false;
        }
        return true;
    }
        
    
        
    
        
//-- Print - Methodes
    
    /**
     * opens the Option-Dialog
     */
    private void printOption() {
        LOGGER.debug("printOption...");
        PrinterJob pj = PrinterJob.getPrinterJob();
        this.format = pj.pageDialog(this.format);
    }
    
    /**
     * prints the Tour from the actual Route
     * @return printOkay
     */
    private boolean printTour() {
        return printTour(((Route) this.Desktop_Pane.getSelectedFrame()).getTour());
    }
    
    /**
     * prints the Tour from the actual Route
     * @return printOkay
     */
    private boolean printPreview() {
        LOGGER.debug("printPreview...");
        Print print = new Print(this, true, this.programDir, ((Route) this.Desktop_Pane.getSelectedFrame()).getTour(), this.format, true);
        print.setVisible(true);
        return print.isPrintDone();
    }
    
    /**
     * prints the Tour from the List
     * @param route List JXTreeRouteTable
     * @return printOkay
     */
    private boolean printTour(JXTreeRouteTour tour) {
        LOGGER.debug("printTour...");
        Print print = new Print(this, true, this.programDir, tour, this.format, false);
        print.setVisible(true);
        return print.isPrintDone();
    }
    
    /**
     * prints the Tour from all Routes
     * @return printOkay
     */
    private boolean printAllTours() {
        for(JInternalFrame frame : this.Desktop_Pane.getAllFrames()) {
            if (frame instanceof Route) {
                if (!printTour(((Route) frame).getTour())) {
                    return false;
                }
            }
        }
        return true;
    }
    
//-- Favorites - Methodes
    
    /**
     * creates the default Favorites
     */
    private void createFavorite() {
        LOGGER.debug("createFavorites...");
        LookupAddress address;
        JXTreeRouteAddressFav fav;
        
        address = new LookupAddress(this, true, "Schloss-Schönau-Straße 6, 52072 Aachen");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:20"));
        fav.setName("Löschmann & Claßen GbR");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("09:00"),new TimeOfDay("18:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        
        address = new LookupAddress(this, true, "Franzstraße 6, 52064 Aachen");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:40"));
        fav.setName("Media Markt Aachen");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Voccartstraße 66, 52134 Herzogenrath");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:40"));
        fav.setName("Media Markt Herzogenrath");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Auerbachstraße 30, 52249 Eschweiler");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:40"));
        fav.setName("Media Markt Eschweiler");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Am Landabsatz 5, 41836 Hückelhoven");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:40"));
        fav.setName("Media Markt Hückelhoven");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Breitenbachstraße 50, 41065 Mönchengladbach");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:30"));
        fav.setName("Media Markt Mönchengladbach");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Konrad-Adenauer-Ring 95, 41464 Neuss");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:30"));
        fav.setName("Media Markt Neuss");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Adalbertstraße 100,  52062 Aachen");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:30"));
        fav.setName("Saturn Aachen");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        address = new LookupAddress(this, true, "Kuhgasse 8, 52349 Düren");
        address.setVisible(true);
        fav = new JXTreeRouteAddressFav(address.getMapAddress());
        fav.setDuration(new TimeOfDay("00:30"));
        fav.setName("Saturn Düren");
        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
        fav.updateDot();
        this.FavoriteTable.addItem(fav);
        
        this.getFavoriteTable().updateUI();
    }

    /**
     * loads the Favorites from a File
     * @param file 
     */
    private boolean loadFavorite(File file) {
        LOGGER.info("Öffne Favoritendatei...");
        Object input;
        try {
            try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
                input = in.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.warn(ex);
            return false;
        }
        if (!(input instanceof List)) {
            LOGGER.warn("Keine Liste");
            return false;
        }
        for (Object entry : (ArrayList) input) {
            if (entry instanceof JXTreeRouteAddressFav) {
                this.FavoriteTable.addItem((JXTreeRouteAddressFav) entry);
                this.Karte.addMapMarker(((JXTreeRouteAddressFav) entry).getDot());
            }else {
                LOGGER.warn("Keine Adresse: " + entry.toString());
            }
        }
        this.getFavoriteTable().updateUI();
        return true;
    }
        
    /**
     * saves the Favorites to a File
     * @return success
     */
    private boolean saveFavorite(File file) {
        LOGGER.debug("Speichere Favoritendatei...");
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            
            List<JXTreeRouteAddressFav> temp = new ArrayList<>();
            this.FavoriteTable.getJxTreeRouteEntryList().stream().filter((entry) -> (entry instanceof JXTreeRouteAddressFav)).forEachOrdered((entry) -> {
                temp.add((JXTreeRouteAddressFav) entry);
            });
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(temp);
            }
        } catch(IOException ex) {
            LOGGER.warn(ex);
            return false;
        }
        return true;
    }
    
//-- ActionListener - Methodes
    
    /**
     * AL to add a ClientAddress
     * @param evt 
     */
    private void addressClientAddActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            ((Route) this.Desktop_Pane.getSelectedFrame()).newAddressClientEntry();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to remove a ClientAddress
     * @param evt 
     */
    private void addressClientRemoveActionPerformed(java.awt.event.ActionEvent evt) {  
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            ((Route) this.Desktop_Pane.getSelectedFrame()).removeSelectedAddressClientEntries();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to edit a ClientAddress
     * @param evt 
     */
    private void addressClientEditActionPerformed(java.awt.event.ActionEvent evt) {  
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            ((Route) this.Desktop_Pane.getSelectedFrame()).editSelectedAddressClientEntry();
            this.Karte.updateUI();
        }
    }
    
    
    
    /**
     * AL to add a new Tour
     * @param evt 
     */
    private void tourAddActionPerformed(java.awt.event.ActionEvent evt) {                                          
        createTour();
    }
    
    /**
     * AL to remove the actual Tour
     * @param evt 
     */
    private void tourRemoveActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            ((Route) this.Desktop_Pane.getSelectedFrame()).remove();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to open a Tour
     */
     void tourOpenActionPerformed(java.awt.event.ActionEvent evt) {                                           
        File file = FileIO.getOpenRouteFile(this, this.workingDir);
        if (file != null && file.exists()) {
            this.loadTour(file);
        }
    }
    
    /**
     * AL to save the actual Tour
     */
    private void tourSaveActionPerformed(java.awt.event.ActionEvent evt) {
        String fileName = "";
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            fileName = ((Route)this.Desktop_Pane.getSelectedFrame()).getRouteName();
        }
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy_MM_dd_EEEE");
        saveThisTour(FileIO.getSaveRouteFile(this, new File(isoFormat.format(new Date()) + " - " + fileName), this.workingDir));
    }
    
    /**
     * AL to save a new Copy of the actual Tour
     */
    private void tourSaveAsActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String fileName = "";
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            fileName = ((Route)this.Desktop_Pane.getSelectedFrame()).getRouteName();
        }
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy_MM_dd_EEEE");
        saveThisTour(FileIO.getSaveRouteFile(this, new File(isoFormat.format(new Date()) + " - " + fileName), this.workingDir), false);
    }
    
    /**
     * AL to calculate the Routes at the actual Tour
     * @param evt 
     */
    private void tourCalculateActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            ((Route) this.Desktop_Pane.getSelectedFrame()).calculateRoute();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to print the actual Tour
     * @param evt 
     */
    private void tourPrintActionPerformed(java.awt.event.ActionEvent evt) {                                            
        printTour();
    }
    
    /**
     * AL to print all Tours
     * @param evt 
     */
    private void tourPrintAllActionPerformed(java.awt.event.ActionEvent evt) {                                            
        printAllTours();
    }
    
    /**
     * AL to print all Tours
     * @param evt 
     */
    private void tourPrintPreviewActionPerformed(java.awt.event.ActionEvent evt) {                                            
        printPreview();
    }
    
    /**
     * 
     * @param evt 
     */
    private void tourPrintOptionActionPerformed(java.awt.event.ActionEvent evt) {                                            
        printOption();
    }
    
    
    
    /**
     * AL to fit the Map to the MapMarkers
     * @param evt 
     */
    private void mapFitMapMarkersActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        Karte.setDisplayToFitMapMarkers();
    }
    
    /**
     * AL to set the Favorites visible on the Map
     * @param evt 
     */
    private void mapFavoritSetVisibleActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        boolean visible = false;
        if (evt.getSource() instanceof JCheckBoxMenuItem)
            visible = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
        else if (evt.getSource() instanceof JCheckBox)
            visible = ((JCheckBox) evt.getSource()).isSelected();
        
        if (!evt.getSource().equals(OptionenFavToMap))
            OptionenFavToMap.setSelected(visible);
        if (!evt.getSource().equals(MenuOptionenFavToMap))
            MenuOptionenFavToMap.setSelected(visible);
        
        this.FavoriteTable.setMapMarkerVisible(visible);
        Karte.updateUI();        
    }
    
    private void mapRouteSetVisibleActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
            boolean visible = false;
            if (evt.getSource() instanceof JCheckBoxMenuItem)
                visible = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
            else if (evt.getSource() instanceof JCheckBox)
                visible = ((JCheckBox) evt.getSource()).isSelected();

            if (!evt.getSource().equals(OptionenFavToMap))
                OptionenFavToMap.setSelected(visible);
            if (!evt.getSource().equals(MenuOptionenFavToMap))
                MenuOptionenFavToMap.setSelected(visible);

            ((Route) this.Desktop_Pane.getSelectedFrame()).setMapMarkerVisible(visible);
        }
        Karte.updateUI();        
    }
    

    /**
     * AL to open a Tour
     */
    private void favoriteOpenActionPerformed(java.awt.event.ActionEvent evt) {                                           
        File file = FileIO.getOpenFavoritenFile(this, this.workingDir);
        if (file != null && file.exists()) {
            loadFavorite(file);
        }
    }
    
    /**
     * AL to save the actual Tour
     */
    private void favoriteSaveActionPerformed(java.awt.event.ActionEvent evt) {                                           
        File saveFile = this.FavoritenFile;
        if (!saveFile.exists()) {
            saveFile = FileIO.getSaveFavoritenFile(this, saveFile, this.workingDir);
        }
        if (!saveFavorite(saveFile)) {
            MSG.msgSaveAborted(this, saveFile.getName());
        }
    }
    
    /**
     * CL for Favorites Event
     * @param evt 
     */
    private void favoriteTableValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                           
        LOGGER.info("Favoriten für: {} gesetzt.", ((Route) this.Desktop_Pane.getSelectedFrame()).getTitle());
        ((Route) this.Desktop_Pane.getSelectedFrame()).setFavSelection(this.FavoriteTable.getSelectedRows());
    }
    
    
    
    /**
     * AL for exiting the Programm
     * @param evt 
     */
    private void exitProgramActionPerformed(java.awt.event.ActionEvent evt) {
        for(JInternalFrame frame : this.Desktop_Pane.getAllFrames()) {
            if (frame instanceof Route) {
                Route route = (Route) frame;
                if (!route.isCurrentStateSaved()) {
                    if (MSG.msgSaveBeforeClose(this, route.getRouteName())) {
                        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy_MM_dd_EEEE");
                        saveThisTour(FileIO.getSaveRouteFile(this, new File(isoFormat.format(new Date()) + " - " + route.getRouteName()), this.workingDir), false);
                    }
                }
            }
        }
        parent.exitProgram(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SearchPaneChild = new javax.swing.JPanel();
        jTxtSearchAddress = new javax.swing.JTextField();
        jBtnSearchAddress = new javax.swing.JButton();
        FavoritePaneChild = new javax.swing.JScrollPane();
        FavoriteTable = new de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute();
        OptionPaneChild = new javax.swing.JPanel();
        OptionenFavToMap = new javax.swing.JCheckBox();
        jBtnCenterMap = new javax.swing.JButton();
        jBtnNewEntry = new javax.swing.JButton();
        jBtnRemoveEntry = new javax.swing.JButton();
        jBtnCalcRoute = new javax.swing.JButton();
        jPopupMenuRoute = new javax.swing.JPopupMenu();
        jPopupMenuRoutejMenuItemAdd = new javax.swing.JMenuItem();
        jPopupMenuRoutejMenuItemRemove = new javax.swing.JMenuItem();
        jPopupMenuRouteSeparator1 = new javax.swing.JPopupMenu.Separator();
        jPopupMenuRoutejMenuItemRouteMapVisible = new javax.swing.JCheckBoxMenuItem();
        jPopupMenuRouteSeparator2 = new javax.swing.JPopupMenu.Separator();
        jPopupMenuRoutejMenuItemEdit = new javax.swing.JMenuItem();
        Multi_Split_Pane = new org.jdesktop.swingx.JXMultiSplitPane();
        Window_Pane = new javax.swing.JTabbedPane();
        Karte = new org.openstreetmap.gui.jmapviewer.JMapViewer();
        Console = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtConsole = new javax.swing.JTextArea();
        jbtnConsoleClear = new javax.swing.JButton();
        Info = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtInfo = new javax.swing.JTextArea();
        jbtnInfoClear = new javax.swing.JButton();
        Desktop_Pane = new javax.swing.JDesktopPane();
        Menue_Pane = new org.jdesktop.swingx.JXTaskPaneContainer();
        SearchPane = new org.jdesktop.swingx.JXTaskPane();
        OptionPane = new org.jdesktop.swingx.JXTaskPane();
        TourPane = new org.jdesktop.swingx.JXTaskPane();
        FavoritePane = new org.jdesktop.swingx.JXTaskPane();
        jTB = new javax.swing.JToolBar();
        jTbBtnNew = new javax.swing.JButton();
        jTbBtnOpen = new javax.swing.JButton();
        jTbBtnSave = new javax.swing.JButton();
        jTbSeparator1 = new javax.swing.JToolBar.Separator();
        jTbBtnPrint = new javax.swing.JButton();
        jTbSeparator2 = new javax.swing.JToolBar.Separator();
        jTbBtnNewEntry = new javax.swing.JButton();
        jTbBtnRemoveEntry = new javax.swing.JButton();
        jTbSeparator5 = new javax.swing.JToolBar.Separator();
        jTbBtnCalcRoute = new javax.swing.JButton();
        jTBSeparator6 = new javax.swing.JToolBar.Separator();
        jBtnPDFScan = new javax.swing.JButton();
        jCkbEinzelnachweiss = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        MenuMain = new javax.swing.JMenuBar();
        MenuDatei = new javax.swing.JMenu();
        MenuDateiNeu = new javax.swing.JMenuItem();
        MenuDateiSeperator1 = new javax.swing.JPopupMenu.Separator();
        MenuDateiOpen = new javax.swing.JMenuItem();
        MenuDateiSave = new javax.swing.JMenuItem();
        MenuDateiSaveAs = new javax.swing.JMenuItem();
        MenuDateiSeperator2 = new javax.swing.JPopupMenu.Separator();
        MenuDateiClose = new javax.swing.JMenuItem();
        MenuDateiSeperator3 = new javax.swing.JPopupMenu.Separator();
        MenuDateiFav = new javax.swing.JMenu();
        MenuDateiFavOpen = new javax.swing.JMenuItem();
        MenuDateiFavSave = new javax.swing.JMenuItem();
        MenuDateiSeperator4 = new javax.swing.JPopupMenu.Separator();
        MenueDateiDruckVorschau = new javax.swing.JMenuItem();
        MenuDateiDruckOption = new javax.swing.JMenuItem();
        MenuDateiDruckAktuell = new javax.swing.JMenuItem();
        MenuDateiDruckAlle = new javax.swing.JMenuItem();
        MenuDateiSeperator5 = new javax.swing.JPopupMenu.Separator();
        MenuDateiBeenden = new javax.swing.JMenuItem();
        MenuBearbeiten = new javax.swing.JMenu();
        MenuOptionen = new javax.swing.JMenu();
        MenuOptionenFavToMap = new javax.swing.JCheckBoxMenuItem();
        MenuOptionenCenterMap = new javax.swing.JMenuItem();
        MenuEintraege = new javax.swing.JMenu();
        MenuEintraegeNeu = new javax.swing.JMenuItem();
        MenuEintraegeEntfernen = new javax.swing.JMenuItem();
        MenuHilfe = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        About = new javax.swing.JMenuItem();

        SearchPaneChild.setBackground(new Color(0, 0, 0, 0));

        jTxtSearchAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtSearchAddressActionPerformed(evt);
            }
        });

        jBtnSearchAddress.setText("Suche");
        jBtnSearchAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSearchAddressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SearchPaneChildLayout = new javax.swing.GroupLayout(SearchPaneChild);
        SearchPaneChild.setLayout(SearchPaneChildLayout);
        SearchPaneChildLayout.setHorizontalGroup(
            SearchPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchPaneChildLayout.createSequentialGroup()
                .addComponent(jTxtSearchAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnSearchAddress))
        );
        SearchPaneChildLayout.setVerticalGroup(
            SearchPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTxtSearchAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jBtnSearchAddress))
        );

        FavoritePaneChild.setMaximumSize(new java.awt.Dimension(400, 600));
        FavoritePaneChild.setMinimumSize(new java.awt.Dimension(200, 200));
        FavoritePaneChild.setPreferredSize(new java.awt.Dimension(250, 300));

        FavoriteTable.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                FavoriteTableValueChanged(evt);
            }
        });
        FavoritePaneChild.setViewportView(FavoriteTable);

        OptionPaneChild.setBackground(new java.awt.Color(214, 223, 247));
        OptionPaneChild.setMaximumSize(new java.awt.Dimension(400, 111));
        OptionPaneChild.setMinimumSize(new java.awt.Dimension(200, 111));
        OptionPaneChild.setPreferredSize(new java.awt.Dimension(200, 111));

        OptionenFavToMap.setBackground(new java.awt.Color(214, 223, 247));
        OptionenFavToMap.setSelected(true);
        OptionenFavToMap.setText("Favoriten auf der Karte anzeigen");
        OptionenFavToMap.setOpaque(false);

        jBtnCenterMap.setText("Karte auf Routen zentrieren");

        jBtnNewEntry.setText("+ Eintrag");
        jBtnNewEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNewEntryActionPerformed(evt);
            }
        });

        jBtnRemoveEntry.setText("- Eintrag");

        jBtnCalcRoute.setText("berechne Routen");

        javax.swing.GroupLayout OptionPaneChildLayout = new javax.swing.GroupLayout(OptionPaneChild);
        OptionPaneChild.setLayout(OptionPaneChildLayout);
        OptionPaneChildLayout.setHorizontalGroup(
            OptionPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OptionenFavToMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBtnCenterMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBtnCalcRoute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OptionPaneChildLayout.createSequentialGroup()
                .addComponent(jBtnNewEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnRemoveEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        OptionPaneChildLayout.setVerticalGroup(
            OptionPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionPaneChildLayout.createSequentialGroup()
                .addComponent(OptionenFavToMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnCenterMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OptionPaneChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnNewEntry)
                    .addComponent(jBtnRemoveEntry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnCalcRoute))
        );

        jPopupMenuRoutejMenuItemAdd.setText("Add Address");
        jPopupMenuRoute.add(jPopupMenuRoutejMenuItemAdd);

        jPopupMenuRoutejMenuItemRemove.setText("remove Address");
        jPopupMenuRoute.add(jPopupMenuRoutejMenuItemRemove);
        jPopupMenuRoute.add(jPopupMenuRouteSeparator1);

        jPopupMenuRoutejMenuItemRouteMapVisible.setSelected(true);
        jPopupMenuRoutejMenuItemRouteMapVisible.setText("set Map Visible");
        jPopupMenuRoute.add(jPopupMenuRoutejMenuItemRouteMapVisible);
        jPopupMenuRoute.add(jPopupMenuRouteSeparator2);

        jPopupMenuRoutejMenuItemEdit.setText("edit Item");
        jPopupMenuRoute.add(jPopupMenuRoutejMenuItemEdit);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Routenplaner");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/images/osm32.png")).getImage());
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(1800, 960));
        setSize(new java.awt.Dimension(1800, 960));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        String layoutDef = "(ROW (LEAF name=left weight=0.2) (COLUMN weight=0.8 " +
        "(LEAF name=top weight=0.3) (LEAF name=bottom weight=0.7)))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel( layoutDef );
        Multi_Split_Pane.getMultiSplitLayout().setModel( modelRoot );
        Multi_Split_Pane.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
        Multi_Split_Pane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Window_Pane.setBackground(new java.awt.Color(255, 255, 255));
        Window_Pane.setMinimumSize(new java.awt.Dimension(400, 200));
        Window_Pane.setPreferredSize(new java.awt.Dimension(400, 200));
        Window_Pane.addTab("Karte", Karte);

        txtConsole.setColumns(20);
        txtConsole.setRows(5);
        jScrollPane6.setViewportView(txtConsole);

        jbtnConsoleClear.setText("Löschen");

        javax.swing.GroupLayout ConsoleLayout = new javax.swing.GroupLayout(Console);
        Console.setLayout(ConsoleLayout);
        ConsoleLayout.setHorizontalGroup(
            ConsoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConsoleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnConsoleClear)
                .addContainerGap())
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        ConsoleLayout.setVerticalGroup(
            ConsoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsoleLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnConsoleClear)
                .addContainerGap())
        );

        Window_Pane.addTab("Console", Console);

        txtInfo.setColumns(20);
        txtInfo.setRows(5);
        jScrollPane7.setViewportView(txtInfo);

        jbtnInfoClear.setText("Löschen");

        javax.swing.GroupLayout InfoLayout = new javax.swing.GroupLayout(Info);
        Info.setLayout(InfoLayout);
        InfoLayout.setHorizontalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnInfoClear)
                .addContainerGap())
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        InfoLayout.setVerticalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnInfoClear)
                .addContainerGap())
        );

        Window_Pane.addTab("Info", Info);

        Multi_Split_Pane.add(Window_Pane);

        Desktop_Pane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Desktop_Pane.setMinimumSize(new java.awt.Dimension(200, 50));

        javax.swing.GroupLayout Desktop_PaneLayout = new javax.swing.GroupLayout(Desktop_Pane);
        Desktop_Pane.setLayout(Desktop_PaneLayout);
        Desktop_PaneLayout.setHorizontalGroup(
            Desktop_PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );
        Desktop_PaneLayout.setVerticalGroup(
            Desktop_PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        Multi_Split_Pane.add(Desktop_Pane);

        Menue_Pane.setMaximumSize(new java.awt.Dimension(150, 32767));
        Menue_Pane.setMinimumSize(new java.awt.Dimension(80, 116));
        Menue_Pane.setPreferredSize(new java.awt.Dimension(120, 116));
        org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout1.setGap(10);
        Menue_Pane.setLayout(verticalLayout1);

        SearchPane.setTitle("Suche");
        Menue_Pane.add(SearchPane);

        OptionPane.setName(""); // NOI18N
        OptionPane.setTitle("Optionen");
        Menue_Pane.add(OptionPane);

        TourPane.setTitle("Tourdaten");
        Menue_Pane.add(TourPane);

        FavoritePane.setTitle("Favoriten");
        Menue_Pane.add(FavoritePane);

        Multi_Split_Pane.add(Menue_Pane);

        getContentPane().add(Multi_Split_Pane, java.awt.BorderLayout.CENTER);
        Multi_Split_Pane.add(Menue_Pane, "left" );
        Multi_Split_Pane.add(Window_Pane, "top" );
        Multi_Split_Pane.add(Desktop_Pane, "bottom" );

        jTB.setRollover(true);

        jTbBtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Page 1.png"))); // NOI18N
        jTbBtnNew.setToolTipText("neue Tour");
        jTbBtnNew.setFocusable(false);
        jTbBtnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTbBtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTbBtnNewActionPerformed(evt);
            }
        });
        jTB.add(jTbBtnNew);

        jTbBtnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Folder.png"))); // NOI18N
        jTbBtnOpen.setToolTipText("Tour öffnen");
        jTbBtnOpen.setFocusable(false);
        jTbBtnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnOpen);

        jTbBtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Floppy Disk 2.png"))); // NOI18N
        jTbBtnSave.setToolTipText("Tour schließen");
        jTbBtnSave.setFocusable(false);
        jTbBtnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnSave);
        jTB.add(jTbSeparator1);

        jTbBtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Printer.png"))); // NOI18N
        jTbBtnPrint.setToolTipText("Tour drucken");
        jTbBtnPrint.setFocusable(false);
        jTbBtnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnPrint);
        jTB.add(jTbSeparator2);

        jTbBtnNewEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Add.png"))); // NOI18N
        jTbBtnNewEntry.setToolTipText("neue Adresse");
        jTbBtnNewEntry.setFocusable(false);
        jTbBtnNewEntry.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnNewEntry.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnNewEntry);

        jTbBtnRemoveEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Remove.png"))); // NOI18N
        jTbBtnRemoveEntry.setToolTipText("Adresse löschen");
        jTbBtnRemoveEntry.setFocusable(false);
        jTbBtnRemoveEntry.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnRemoveEntry.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnRemoveEntry);
        jTB.add(jTbSeparator5);

        jTbBtnCalcRoute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Map 2.png"))); // NOI18N
        jTbBtnCalcRoute.setToolTipText("fehlende Routen berechnen");
        jTbBtnCalcRoute.setFocusable(false);
        jTbBtnCalcRoute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTbBtnCalcRoute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTB.add(jTbBtnCalcRoute);
        jTB.add(jTBSeparator6);

        jBtnPDFScan.setText("PDF einscannen");
        jBtnPDFScan.setFocusable(false);
        jBtnPDFScan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnPDFScan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnPDFScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPDFScanActionPerformed(evt);
            }
        });
        jTB.add(jBtnPDFScan);

        jCkbEinzelnachweiss.setText("Einzelnachweise");
        jCkbEinzelnachweiss.setFocusable(false);
        jTB.add(jCkbEinzelnachweiss);
        jTB.add(jSeparator2);

        getContentPane().add(jTB, java.awt.BorderLayout.PAGE_START);

        MenuDatei.setText("Datei");

        MenuDateiNeu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiNeu.setText("Neu");
        MenuDatei.add(MenuDateiNeu);
        MenuDatei.add(MenuDateiSeperator1);

        MenuDateiOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiOpen.setText("Öffnen");
        MenuDatei.add(MenuDateiOpen);

        MenuDateiSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiSave.setText("Speichern");
        MenuDatei.add(MenuDateiSave);

        MenuDateiSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiSaveAs.setText("Speichern unter...");
        MenuDatei.add(MenuDateiSaveAs);
        MenuDatei.add(MenuDateiSeperator2);

        MenuDateiClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiClose.setText("Schließen");
        MenuDatei.add(MenuDateiClose);
        MenuDatei.add(MenuDateiSeperator3);

        MenuDateiFav.setText("Favoriten");

        MenuDateiFavOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiFavOpen.setText("Favoriten neu laden");
        MenuDateiFav.add(MenuDateiFavOpen);

        MenuDateiFavSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiFavSave.setText("Favoriten speichern");
        MenuDateiFav.add(MenuDateiFavSave);

        MenuDatei.add(MenuDateiFav);
        MenuDatei.add(MenuDateiSeperator4);

        MenueDateiDruckVorschau.setText("Vorschau");
        MenuDatei.add(MenueDateiDruckVorschau);

        MenuDateiDruckOption.setText("Drucker Optionen");
        MenuDatei.add(MenuDateiDruckOption);

        MenuDateiDruckAktuell.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiDruckAktuell.setText("aktuellen Plan drucken");
        MenuDatei.add(MenuDateiDruckAktuell);

        MenuDateiDruckAlle.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuDateiDruckAlle.setText("Alle Pläne drucken");
        MenuDatei.add(MenuDateiDruckAlle);
        MenuDatei.add(MenuDateiSeperator5);

        MenuDateiBeenden.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        MenuDateiBeenden.setText("Beenden");
        MenuDatei.add(MenuDateiBeenden);

        MenuMain.add(MenuDatei);

        MenuBearbeiten.setText("Bearbeiten");
        MenuBearbeiten.setEnabled(false);
        MenuMain.add(MenuBearbeiten);

        MenuOptionen.setText("Optionen");

        MenuOptionenFavToMap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        MenuOptionenFavToMap.setSelected(true);
        MenuOptionenFavToMap.setText("Favoriten auf Karte anzeigen");
        MenuOptionen.add(MenuOptionenFavToMap);

        MenuOptionenCenterMap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuOptionenCenterMap.setText("Karte auf Routen zentrieren");
        MenuOptionen.add(MenuOptionenCenterMap);

        MenuMain.add(MenuOptionen);

        MenuEintraege.setText("Einträge");

        MenuEintraegeNeu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuEintraegeNeu.setText("Neu");
        MenuEintraege.add(MenuEintraegeNeu);

        MenuEintraegeEntfernen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        MenuEintraegeEntfernen.setText("Löschen");
        MenuEintraege.add(MenuEintraegeEntfernen);

        MenuMain.add(MenuEintraege);

        MenuHilfe.setText("Hilfe");
        MenuHilfe.add(jSeparator1);

        About.setText("Über ...");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        MenuHilfe.add(About);

        MenuMain.add(MenuHilfe);

        setJMenuBar(MenuMain);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FavoriteTableValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_FavoriteTableValueChanged
        favoriteTableValueChanged(evt);
    }//GEN-LAST:event_FavoriteTableValueChanged

    private void jTxtSearchAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtSearchAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtSearchAddressActionPerformed

    private void jBtnSearchAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSearchAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnSearchAddressActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitProgramActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        JOptionPane.showMessageDialog(this, "Routenplaner\nVersion: " + Routenplaner.getVersionNumber() + "\n\n by AndyB ...", "Über ...", JOptionPane.DEFAULT_OPTION);
    }//GEN-LAST:event_AboutActionPerformed

    private void jTbBtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTbBtnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTbBtnNewActionPerformed

    private void jBtnPDFScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPDFScanActionPerformed
        
        if (this.getFavoriteTable().getSelectedRows().length > 0) {
            
            if(this.Desktop_Pane.getSelectedFrame() != null && this.Desktop_Pane.getSelectedFrame() instanceof Route) {
                Route route = ((Route) this.Desktop_Pane.getSelectedFrame());
                JXTreeRouteAddressFav fav = (JXTreeRouteAddressFav) this.FavoriteTable.getItem(this.FavoriteTable.getSelectedRows()[0]);
                File file = de.abring.pdferkennung.gui.dialogues.FileIO.getOpenPDFFile(this, System.getProperty("user.home"));
                if (file != null && file.exists()) {
                    JscanPDF jscanPDF = new JscanPDF(this, true, route, fav, file, jCkbEinzelnachweiss.isSelected());
                    jscanPDF.setVisible(true);
                }
                this.Desktop_Pane.getSelectedFrame().updateUI();
            }
        
        } else {
            
            int selectedItem = Table.JXTreeTableAddressFavDialog(this, true, "Auftraggeber auswählen:", this.getFavoriteTable().getJxTreeRouteEntryList());
            
            if (selectedItem != Table.DIALOG_ABORT) {
                this.getFavoriteTable().getSelectionModel().addSelectionInterval(selectedItem, selectedItem);
                this.getFavoriteTable().updateUI();
                jBtnPDFScanActionPerformed(evt);
            }
            
        }
        
        
        
        
    }//GEN-LAST:event_jBtnPDFScanActionPerformed

    private void jBtnNewEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNewEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnNewEntryActionPerformed

    // <editor-fold defaultstate="collapsed" desc="My Generated Code">
    /**
     * inits my components
     */
    private void initOtherComponents() {
        this.SearchPane.add(this.SearchPaneChild);
        this.FavoritePane.add(this.FavoritePaneChild);
        this.OptionPane.add(this.OptionPaneChild);
        int[] columns = {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.NAME
        };
        this.FavoriteTable.setKarte(Karte);
        this.FavoriteTable.setColor(Color.YELLOW);
        this.FavoriteTable.handleColumns(columns);
        ((JXTableRowTransferHandlerRoute) this.FavoriteTable.getTransferHandler()).setMoveOrCopy(COPY);
        this.FavoriteTable.updateUI();
        
        // <editor-fold defaultstate="collapsed" desc="All ActionListener">
        MenuDateiNeu.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourAddActionPerformed(evt);
        });

        MenuDateiOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourOpenActionPerformed(evt);
        });

        MenuDateiSave.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourSaveActionPerformed(evt);
        });

        MenuDateiSaveAs.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourSaveAsActionPerformed(evt);
        });

        MenuDateiClose.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourRemoveActionPerformed(evt);
        });
        
        MenuDateiFavOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            favoriteOpenActionPerformed(evt);
        });

        MenuDateiFavSave.addActionListener((java.awt.event.ActionEvent evt) -> {
            favoriteSaveActionPerformed(evt);
        });

        MenueDateiDruckVorschau.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourPrintPreviewActionPerformed(evt);
        });
        
        MenuDateiDruckOption.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourPrintOptionActionPerformed(evt);
        });

        MenuDateiDruckAktuell.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourPrintActionPerformed(evt);
        });

        MenuDateiDruckAlle.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourPrintAllActionPerformed(evt);
        });

        MenuDateiBeenden.addActionListener((java.awt.event.ActionEvent evt) -> {
            exitProgramActionPerformed(evt);
        });
        MenuOptionenFavToMap.addActionListener((java.awt.event.ActionEvent evt) -> {
            mapFavoritSetVisibleActionPerformed(evt);
        });

        MenuOptionenCenterMap.addActionListener((java.awt.event.ActionEvent evt) -> {
            mapFitMapMarkersActionPerformed(evt);
        });
        
        jTbBtnNew.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourAddActionPerformed(evt);
        });

        jTbBtnOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourOpenActionPerformed(evt);
        });

        jTbBtnSave.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourSaveActionPerformed(evt);
        });

        jTbBtnPrint.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourPrintActionPerformed(evt);
        });

        jTbBtnNewEntry.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientAddActionPerformed(evt);
        });

        jTbBtnRemoveEntry.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientRemoveActionPerformed(evt);
        });

        jTbBtnCalcRoute.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourCalculateActionPerformed(evt);
        });
        
        jPopupMenuRoutejMenuItemAdd.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientAddActionPerformed(evt);
        });
        
        jPopupMenuRoutejMenuItemRemove.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientRemoveActionPerformed(evt);
        });
        
        jPopupMenuRoutejMenuItemRouteMapVisible.addActionListener((java.awt.event.ActionEvent evt) -> {
            mapRouteSetVisibleActionPerformed(evt);
        });
        
        jPopupMenuRoutejMenuItemEdit.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientEditActionPerformed(evt);
        });
        
        OptionenFavToMap.addActionListener((java.awt.event.ActionEvent evt) -> {
            mapFavoritSetVisibleActionPerformed(evt);
        });
        
        jBtnCenterMap.addActionListener((java.awt.event.ActionEvent evt) -> {
            mapFitMapMarkersActionPerformed(evt);
        });
        
        jBtnNewEntry.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientAddActionPerformed(evt);
        });
                
        jBtnRemoveEntry.addActionListener((java.awt.event.ActionEvent evt) -> {
            addressClientRemoveActionPerformed(evt);
        });
                
        jBtnCalcRoute.addActionListener((java.awt.event.ActionEvent evt) -> {
            tourCalculateActionPerformed(evt);
        });
        // </editor-fold>
    }

    /**
     * load the Properties from parent
     */
    public void loadProperties() {
        this.programDir = PathFinder.getAbsolutePath() + parent.getProperties().getProperty("programpathpart", "");
        File favorite = new File(this.programDir + parent.getProperties().getProperty("favoritenfile", "\\vorlagen\\favoriten.serialroutefavorit"));
        if (favorite.exists()) {
            if (!loadFavorite(favorite)) {
                createFavorite();
            }
        } else {
            createFavorite();
        }
        File favFile = new File(parent.getProperties().getProperty("favoritenfile", "\\vorlagen\\favoriten.serialroutefavorit"));
        this.FavoritenFile = new File(this.programDir + favFile.getPath());
        this.workingDir = parent.getProperties().getProperty("workingpath", System.getProperty("user.home"));
    }// </editor-fold>
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JPanel Console;
    private javax.swing.JDesktopPane Desktop_Pane;
    private org.jdesktop.swingx.JXTaskPane FavoritePane;
    private javax.swing.JScrollPane FavoritePaneChild;
    private de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute FavoriteTable;
    private javax.swing.JPanel Info;
    public org.openstreetmap.gui.jmapviewer.JMapViewer Karte;
    private javax.swing.JMenu MenuBearbeiten;
    private javax.swing.JMenu MenuDatei;
    private javax.swing.JMenuItem MenuDateiBeenden;
    private javax.swing.JMenuItem MenuDateiClose;
    private javax.swing.JMenuItem MenuDateiDruckAktuell;
    private javax.swing.JMenuItem MenuDateiDruckAlle;
    private javax.swing.JMenuItem MenuDateiDruckOption;
    private javax.swing.JMenu MenuDateiFav;
    private javax.swing.JMenuItem MenuDateiFavOpen;
    private javax.swing.JMenuItem MenuDateiFavSave;
    private javax.swing.JMenuItem MenuDateiNeu;
    private javax.swing.JMenuItem MenuDateiOpen;
    private javax.swing.JMenuItem MenuDateiSave;
    private javax.swing.JMenuItem MenuDateiSaveAs;
    private javax.swing.JPopupMenu.Separator MenuDateiSeperator1;
    private javax.swing.JPopupMenu.Separator MenuDateiSeperator2;
    private javax.swing.JPopupMenu.Separator MenuDateiSeperator3;
    private javax.swing.JPopupMenu.Separator MenuDateiSeperator4;
    private javax.swing.JPopupMenu.Separator MenuDateiSeperator5;
    private javax.swing.JMenu MenuEintraege;
    private javax.swing.JMenuItem MenuEintraegeEntfernen;
    private javax.swing.JMenuItem MenuEintraegeNeu;
    private javax.swing.JMenu MenuHilfe;
    private javax.swing.JMenuBar MenuMain;
    private javax.swing.JMenu MenuOptionen;
    private javax.swing.JMenuItem MenuOptionenCenterMap;
    private javax.swing.JCheckBoxMenuItem MenuOptionenFavToMap;
    private javax.swing.JMenuItem MenueDateiDruckVorschau;
    private org.jdesktop.swingx.JXTaskPaneContainer Menue_Pane;
    private org.jdesktop.swingx.JXMultiSplitPane Multi_Split_Pane;
    private org.jdesktop.swingx.JXTaskPane OptionPane;
    private javax.swing.JPanel OptionPaneChild;
    private javax.swing.JCheckBox OptionenFavToMap;
    private org.jdesktop.swingx.JXTaskPane SearchPane;
    private javax.swing.JPanel SearchPaneChild;
    private org.jdesktop.swingx.JXTaskPane TourPane;
    private javax.swing.JTabbedPane Window_Pane;
    private javax.swing.JButton jBtnCalcRoute;
    private javax.swing.JButton jBtnCenterMap;
    private javax.swing.JButton jBtnNewEntry;
    private javax.swing.JButton jBtnPDFScan;
    private javax.swing.JButton jBtnRemoveEntry;
    private javax.swing.JButton jBtnSearchAddress;
    private javax.swing.JCheckBox jCkbEinzelnachweiss;
    private javax.swing.JPopupMenu jPopupMenuRoute;
    private javax.swing.JPopupMenu.Separator jPopupMenuRouteSeparator1;
    private javax.swing.JPopupMenu.Separator jPopupMenuRouteSeparator2;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemAdd;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemEdit;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemRemove;
    private javax.swing.JCheckBoxMenuItem jPopupMenuRoutejMenuItemRouteMapVisible;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jTB;
    private javax.swing.JToolBar.Separator jTBSeparator6;
    private javax.swing.JButton jTbBtnCalcRoute;
    private javax.swing.JButton jTbBtnNew;
    private javax.swing.JButton jTbBtnNewEntry;
    private javax.swing.JButton jTbBtnOpen;
    private javax.swing.JButton jTbBtnPrint;
    private javax.swing.JButton jTbBtnRemoveEntry;
    private javax.swing.JButton jTbBtnSave;
    private javax.swing.JToolBar.Separator jTbSeparator1;
    private javax.swing.JToolBar.Separator jTbSeparator2;
    private javax.swing.JToolBar.Separator jTbSeparator5;
    private javax.swing.JTextField jTxtSearchAddress;
    private javax.swing.JButton jbtnConsoleClear;
    private javax.swing.JButton jbtnInfoClear;
    private javax.swing.JTextArea txtConsole;
    public javax.swing.JTextArea txtInfo;
    // End of variables declaration//GEN-END:variables
}
