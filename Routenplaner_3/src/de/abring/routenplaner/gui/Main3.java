/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package de.abring.routenplaner.gui;

import de.abring.helfer.PathFinder;
import de.abring.helfer.maproute.LookupAddress;
import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.pdferkennung.gui.dialogues.JscanPDF;
import de.abring.pdferkennung.gui.dialogues.JscanPDF3;
import de.abring.routenplaner.Routenplaner;
import de.abring.routenplaner.gui.components.mapTiles.MapTileComboBoxModel;
import de.abring.routenplaner.gui.components.mapTiles.MyTilehosterTileSource;
import de.abring.routenplaner.gui.dialogues.FileIO;
import de.abring.routenplaner.gui.dialogues.LoadRouteFromWeb;
import de.abring.routenplaner.gui.dialogues.MSG;
import de.abring.routenplaner.gui.dialogues.Print;
import de.abring.routenplaner.gui.dialogues.Table;
import de.abring.routenplaner.jxtreetableroute.JXNoRootTreeTableModelAddress;
import de.abring.routenplaner.jxtreetableroute.JXTableRowTransferHandlerRoute;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteAddressFav;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteEntry;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteRoute;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteTour;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import static javax.swing.TransferHandler.COPY;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.openstreetmap.gui.jmapviewer.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

/**
 *
 * @author Karima
 */
public class Main3 extends javax.swing.JFrame {

    private static final Logger LOGGER = LogManager.getLogger(Main3.class.getName());
    
    private final Routenplaner parent;
    private File FavoritenFile;
    private String workingDir;
    private String programDir;
    private int tourCounter;
    private double wavelength = 0;
    PageFormat format;
    
    /**
     * Creates new form Main3
     * @param parent
     */
    public Main3(Routenplaner parent) {
        
        LOGGER.info("Starte das Hauptfenster ...");
        
        this.parent = parent;

        this.tourCounter = 0;
        this.format = PrinterJob.getPrinterJob().defaultPage();
        this.format.setOrientation(PageFormat.LANDSCAPE);
        
        double rand = 10.0d; // in mm
        
        Paper paper = this.format.getPaper();
        double margin = (72.0d * rand) / 25.4d;
        paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
        this.format.setPaper(paper);
        
        initComponents();
        initOtherComponents();
        
    }
    
    private void initOtherComponents() {

        Dimension size = this.jSclPneRouteTable.getSize();
        size.width = 150;
        this.jSclPneRouteTable.setSize(size);
        Dimension size2 = this.jTbdPneDesktop.getSize();
        size2.width = 150;
        this.jSclPneRouteTable.setSize(size2);
        
        int[] columns = {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.NAME
        };
        this.FavoriteTable.setKarte(Karte);
        this.FavoriteTable.setColor(Color.YELLOW);
        this.FavoriteTable.handleColumns(columns);
        ((JXTableRowTransferHandlerRoute) this.FavoriteTable.getTransferHandler()).setMoveOrCopy(COPY);
        this.FavoriteTable.updateUI();
        
        int[] columns2 = {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.MAP_VISIBLE,
            JXNoRootTreeTableModelAddress.NAME,
            JXNoRootTreeTableModelAddress.DRIVER,
            JXNoRootTreeTableModelAddress.CO_DRIVER,
            JXNoRootTreeTableModelAddress.CAR
        };
        this.RouteTable.setKarte(Karte);
        this.RouteTable.setColor(Color.YELLOW);
        this.RouteTable.handleColumns(columns2);
        ((JXTableRowTransferHandlerRoute) this.RouteTable.getTransferHandler()).setMoveOrCopy(COPY);
        this.RouteTable.updateUI();
        
        
        String layoutDef = "(ROW (COLUMN weight=0.25 (LEAF name=lefttop weight=0.3) (LEAF name=leftbottom weight=0.7))"
                + "(LEAF  weight=0.6 name=center)"
                + "(COLUMN weight=0.15 (LEAF name=righttop weight=0.5) (LEAF name=rightmiddle weight=0.3) (LEAF name=rightbottom weight=0.2)))"; 
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel( layoutDef ); 
        this.jXPaneBack.getMultiSplitLayout().setModel( modelRoot ); 
        this.jXPaneBack.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );

        this.jXPaneBack.add(this.jSclPneRouteTable, "lefttop" );
        this.jXPaneBack.add(this.jPneNeueTour, "leftbottom" );
        this.jXPaneBack.add(this.Karte, "center" ); 
        this.jXPaneBack.add(this.jSclPneFavoriteTable, "righttop" ); 
        this.jXPaneBack.add(this.OptionPane, "rightmiddle" ); 
        this.jXPaneBack.add(this.jPneRoute, "rightbottom" ); 
        this.jXPaneBack.getMultiSplitLayout().setLayoutByWeight(true);
        this.jXPaneBack.revalidate();
                
        MapTileComboBoxModel mapTileComboBoxModel = new MapTileComboBoxModel();
        
        mapTileComboBoxModel.addElement(new OsmTileSource.Mapnik());
        mapTileComboBoxModel.addElement(new MyTilehosterTileSource.TILEHOSTER_DARK());
        mapTileComboBoxModel.addElement(new MyTilehosterTileSource.TILEHOSTER_POSITRON());
        
        this.jCBXKarteTile.setModel(mapTileComboBoxModel);
        
        Karte.setTileSource((TileSource) this.jCBXKarteTile.getModel().getSelectedItem());
        Karte.updateUI();
        Karte.setDisplayToFitMapMarkers();
        
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
    //-- Tour - Methodes

    /**
     * creates a new Tour
     */
    public void createTour() {
        LOGGER.debug("create new Tour!");
        Route3 tour = new Route3("neue Tour " + String.valueOf(++this.tourCounter), this);
        tour.setTourColor(wavelength);
        wavelength+=1.0;
        tour.getTablePane().setComponentPopupMenu(jPopupMenuRoute);
        this.RouteTable.addItem(tour.getTour());
        this.jTbdPneDesktop.add(tour.getTitle(), tour);
        this.RouteTable.updateUI();
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
        
        Route3 tour = new Route3(tourInput, this);
        tour.setTourColor(wavelength);
        wavelength+=1.0d;
        tour.getTablePane().setComponentPopupMenu(jPopupMenuRoute);
        this.RouteTable.addItem(tour.getTour());
        this.jTbdPneDesktop.add(tour.getTitle(), tour);
        this.RouteTable.updateUI();
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
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3)
            return saveTour(((Route3) this.jTbdPneDesktop.getSelectedComponent()).getTour(), file, overwrite);
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
        return printTour(((Route3) this.jTbdPneDesktop.getSelectedComponent()).getTour());
    }
    
    /**
     * prints the Tour from the actual Route
     * @return printOkay
     */
    private boolean printPreview() {
        LOGGER.debug("printPreview...");
        Print print = new Print(this, true, this.programDir, ((Route3) this.jTbdPneDesktop.getSelectedComponent()).getTour(), this.format, true);
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
        for (Component c : this.jTbdPneDesktop.getComponents()) {
            if (c instanceof Route3) {
                Route3 tour = (Route3) c;
                if (!printTour(tour.getTour())) {
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
        
//        address = new LookupAddress(this, true, "Konrad-Adenauer-Ring 95, 41464 Neuss");
//        address.setVisible(true);
//        fav = new JXTreeRouteAddressFav(address.getMapAddress());
//        fav.setDuration(new TimeOfDay("00:30"));
//        fav.setName("Media Markt Neuss");
//        fav.setAppointment(new Appointment("Öffnungszeit", new TimeOfDay("10:00"),new TimeOfDay("20:00")));
//        fav.updateDot();
//        this.FavoriteTable.addItem(fav);
        
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
        
        this.FavoriteTable.setMapMarkerVisible(false);
        this.FavoriteTable.updateUI();
        Karte.updateUI();
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
        this.FavoriteTable.setMapMarkerVisible(false);
        this.FavoriteTable.updateUI();
        Karte.updateUI();
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
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            ((Route3) this.jTbdPneDesktop.getSelectedComponent()).newAddressClientEntry();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to remove a ClientAddress
     * @param evt 
     */
    private void addressClientRemoveActionPerformed(java.awt.event.ActionEvent evt) { 
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            ((Route3) this.jTbdPneDesktop.getSelectedComponent()).removeSelectedAddressClientEntries();
            this.Karte.updateUI();
        }
    }
    
    /**
     * AL to edit a ClientAddress
     * @param evt 
     */
    private void addressClientEditActionPerformed(java.awt.event.ActionEvent evt) {
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            ((Route3) this.jTbdPneDesktop.getSelectedComponent()).editSelectedAddressClientEntry();
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
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            ((Route3) this.jTbdPneDesktop.getSelectedComponent()).remove();
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
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            fileName = ((Route3) this.jTbdPneDesktop.getSelectedComponent()).getRouteName();
        }
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy_MM_dd_EEEE");
        saveThisTour(FileIO.getSaveRouteFile(this, new File(isoFormat.format(new Date()) + " - " + fileName), this.workingDir));
    }
    
    /**
     * AL to save a new Copy of the actual Tour
     */
    private void tourSaveAsActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String fileName = "";
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            fileName = ((Route3) this.jTbdPneDesktop.getSelectedComponent()).getRouteName();
        }
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy_MM_dd_EEEE");
        saveThisTour(FileIO.getSaveRouteFile(this, new File(isoFormat.format(new Date()) + " - " + fileName), this.workingDir), false);
    }
    
    /**
     * AL to calculate the Routes at the actual Tour
     * @param evt 
     */
    private void tourCalculateActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            ((Route3) this.jTbdPneDesktop.getSelectedComponent()).calculateRoute();
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
        
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            Route3 route = (Route3) this.jTbdPneDesktop.getSelectedComponent();
            boolean visible = false;
            if (evt.getSource() instanceof JCheckBoxMenuItem)
                visible = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
            else if (evt.getSource() instanceof JCheckBox)
                visible = ((JCheckBox) evt.getSource()).isSelected();

            if (!evt.getSource().equals(OptionenFavToMap))
                OptionenFavToMap.setSelected(visible);
            if (!evt.getSource().equals(MenuOptionenFavToMap))
                MenuOptionenFavToMap.setSelected(visible);

            route.setMapMarkerVisible(visible);
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
        if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            Route3 route = (Route3) this.jTbdPneDesktop.getSelectedComponent();

            LOGGER.info("Favoriten für: {} gesetzt.", route.getTitle());
            route.setFavSelection(this.FavoriteTable.getSelectedRows());
        }
    }
    
    
    
    /**
     * AL for exiting the Programm
     * @param evt 
     */
    private void exitProgramActionPerformed(java.awt.event.ActionEvent evt) {
        for (Component c : this.jTbdPneDesktop.getComponents()) {
            if (c instanceof Route3) {
                Route3 route = (Route3) c;
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
    
/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuRoute = new javax.swing.JPopupMenu();
        jPopupMenuRoutejMenuItemAdd = new javax.swing.JMenuItem();
        jPopupMenuRoutejMenuItemRemove = new javax.swing.JMenuItem();
        jPopupMenuRouteSeparator1 = new javax.swing.JPopupMenu.Separator();
        jPopupMenuRoutejMenuItemRouteMapVisible = new javax.swing.JCheckBoxMenuItem();
        jPopupMenuRouteSeparator2 = new javax.swing.JPopupMenu.Separator();
        jPopupMenuRoutejMenuItemEdit = new javax.swing.JMenuItem();
        jPneNeueTour = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
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
        jCBXKarteTile = new javax.swing.JComboBox<>();
        jXPaneBack = new org.jdesktop.swingx.JXMultiSplitPane();
        jSclPneFavoriteTable = new javax.swing.JScrollPane();
        FavoriteTable = new de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute();
        jSclPneRouteTable = new javax.swing.JScrollPane();
        RouteTable = new de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute();
        Karte = new org.openstreetmap.gui.jmapviewer.JMapViewer();
        jTbdPneDesktop = new javax.swing.JTabbedPane();
        OptionPane = new javax.swing.JPanel();
        OptionenFavToMap = new javax.swing.JCheckBox();
        jBtnCenterMap = new javax.swing.JButton();
        jBtnNewEntry = new javax.swing.JButton();
        jBtnRemoveEntry = new javax.swing.JButton();
        jBtnCalcRoute = new javax.swing.JButton();
        jPneRoute = new javax.swing.JPanel();
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

        jPneNeueTour.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPneNeueTour.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(220, 184));
        jPanel1.setMinimumSize(new java.awt.Dimension(220, 184));
        jPanel1.setPreferredSize(new java.awt.Dimension(220, 184));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 100, 30);
        flowLayout1.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout1);

        jButton1.setText("neue Tour");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setText("Tour öffnen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("Touren von der Homepage laden");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jPneNeueTour.add(jPanel1);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/images/splash.png"))); // NOI18N
        jPneNeueTour.add(jLabel1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Routenplaner 3");
        setMinimumSize(new java.awt.Dimension(600, 480));
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setSize(new java.awt.Dimension(1200, 800));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

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

        jCBXKarteTile.setMaximumSize(new java.awt.Dimension(130, 20));
        jCBXKarteTile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBXKarteTileActionPerformed(evt);
            }
        });
        jTB.add(jCBXKarteTile);

        getContentPane().add(jTB, java.awt.BorderLayout.PAGE_START);

        jXPaneBack.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jXPaneBackComponentResized(evt);
            }
        });

        jSclPneFavoriteTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jSclPneFavoriteTable.setPreferredSize(new java.awt.Dimension(200, 300));

        FavoriteTable.setDropMode(javax.swing.DropMode.USE_SELECTION);
        FavoriteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FavoriteTableMouseClicked(evt);
            }
        });
        FavoriteTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FavoriteTableActionPerformed(evt);
            }
        });
        jSclPneFavoriteTable.setViewportView(FavoriteTable);

        jXPaneBack.add(jSclPneFavoriteTable);

        jSclPneRouteTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jSclPneRouteTable.setPreferredSize(new java.awt.Dimension(200, 300));

        RouteTable.setDropMode(javax.swing.DropMode.ON);
        RouteTable.setSelectionMode(1);
        RouteTable.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                RouteTableValueChanged(evt);
            }
        });
        RouteTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteTableActionPerformed(evt);
            }
        });
        RouteTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                RouteTablePropertyChange(evt);
            }
        });
        jSclPneRouteTable.setViewportView(RouteTable);

        jXPaneBack.add(jSclPneRouteTable);

        Karte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Karte.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                KarteComponentResized(evt);
            }
        });
        jXPaneBack.add(Karte);

        jTbdPneDesktop.setPreferredSize(new java.awt.Dimension(200, 300));
        jTbdPneDesktop.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTbdPneDesktopStateChanged(evt);
            }
        });
        jXPaneBack.add(jTbdPneDesktop);

        OptionPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        OptionPane.setMaximumSize(new java.awt.Dimension(400, 111));
        OptionPane.setMinimumSize(new java.awt.Dimension(200, 111));

        OptionenFavToMap.setBackground(new java.awt.Color(214, 223, 247));
        OptionenFavToMap.setText("Favoriten anzeigen");
        OptionenFavToMap.setOpaque(false);

        jBtnCenterMap.setText("Karte zentrieren");

        jBtnNewEntry.setText("+ Eintrag");
        jBtnNewEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNewEntryActionPerformed(evt);
            }
        });

        jBtnRemoveEntry.setText("- Eintrag");

        jBtnCalcRoute.setText("berechne Routen");

        javax.swing.GroupLayout OptionPaneLayout = new javax.swing.GroupLayout(OptionPane);
        OptionPane.setLayout(OptionPaneLayout);
        OptionPaneLayout.setHorizontalGroup(
            OptionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OptionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnCalcRoute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnCenterMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(OptionenFavToMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnRemoveEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnNewEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        OptionPaneLayout.setVerticalGroup(
            OptionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OptionPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OptionenFavToMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnNewEntry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnRemoveEntry)
                .addGap(30, 30, 30)
                .addComponent(jBtnCenterMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnCalcRoute)
                .addContainerGap())
        );

        jXPaneBack.add(OptionPane);

        jPneRoute.setLayout(new java.awt.BorderLayout());
        jXPaneBack.add(jPneRoute);

        getContentPane().add(jXPaneBack, java.awt.BorderLayout.CENTER);

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
        MenuHilfe.add(About);

        MenuMain.add(MenuHilfe);

        setJMenuBar(MenuMain);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnNewEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNewEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnNewEntryActionPerformed

    private void jCBXKarteTileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBXKarteTileActionPerformed
        Karte.setTileSource((TileSource) this.jCBXKarteTile.getModel().getSelectedItem());
        Karte.updateUI();
        
    }//GEN-LAST:event_jCBXKarteTileActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
    }//GEN-LAST:event_formComponentResized

    private void KarteComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_KarteComponentResized

    }//GEN-LAST:event_KarteComponentResized

    private void jXPaneBackComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jXPaneBackComponentResized
        
    }//GEN-LAST:event_jXPaneBackComponentResized

    private void RouteTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteTableActionPerformed
        LOGGER.info("TreeTable changed");
    }//GEN-LAST:event_RouteTableActionPerformed

    private void RouteTableValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_RouteTableValueChanged
        for (Component c : this.jTbdPneDesktop.getComponents()) {
            if (c instanceof Route3) {
                Route3 tour = (Route3) c;
                if (tour.getTour() == this.RouteTable.getItem(this.RouteTable.getSelectedRow())) {
                    this.jPneRoute.removeAll();
                    this.jPneRoute.add(tour.jPaneTour);
                    this.jPneRoute.updateUI();
            
                    this.jTbdPneDesktop.setSelectedComponent(c);
                    this.jTbdPneDesktop.updateUI();
                    return;
                }
            }
        }
    }//GEN-LAST:event_RouteTableValueChanged

    private void jTbdPneDesktopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTbdPneDesktopStateChanged
        
        if (this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
            this.jXPaneBack.remove(this.jPneNeueTour);
            this.jXPaneBack.add(this.jTbdPneDesktop, "leftbottom" );
            this.jXPaneBack.updateUI();
            Route3 route = (Route3) this.jTbdPneDesktop.getSelectedComponent();
            JXTreeRouteTour selTour = route.getTour();
            
            this.jPneRoute.removeAll();
            this.jPneRoute.add(route.jPaneTour);
            this.jPneRoute.updateUI();
            for (JXTreeRouteEntry entry : this.RouteTable.getJxTreeRouteEntryList()) {
                if (entry instanceof JXTreeRouteTour) {
                    JXTreeRouteTour tour = (JXTreeRouteTour) entry;
                    if (tour == selTour) {
                        //this.RouteTable.getModel().
                    }
                }
            }
        }
    }//GEN-LAST:event_jTbdPneDesktopStateChanged

    private void RouteTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_RouteTablePropertyChange
        if (evt.getPropertyName().equals("ItemDropped")) {
            for (Component c : this.jTbdPneDesktop.getComponents()) {
                if (c instanceof Route3) {
                    Route3 tour = (Route3) c;
                    if (tour.getTour() == ((JXTreeRouteTour) evt.getNewValue())) {
                        if (evt.getOldValue() instanceof List)
                            tour.getTablePane().addAllItems(tour.getTour().getEntryList().size() - 2, (List) evt.getOldValue());
                        ActionEvent event = new ActionEvent(tour.getTablePane(), 1, "ItemAdded");
                        tour.getTablePane().getActionListenerList().forEach((action) -> {
                            action.actionPerformed(event);
                        });
                    }
                }
            }
        }
    }//GEN-LAST:event_RouteTablePropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tourAddActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.tourOpenActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void FavoriteTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FavoriteTableActionPerformed
        
    }//GEN-LAST:event_FavoriteTableActionPerformed

    private void FavoriteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FavoriteTableMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
            if (this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
                Route3 route = (Route3) this.jTbdPneDesktop.getSelectedComponent();
                for (int i : this.FavoriteTable.getSelectedRows()) {
                    route.addEntry(route.getTour().getEntryList().size() - 2, new JXTreeRouteAddressFav((JXTreeRouteAddressFav) this.FavoriteTable.getItem(i)));
                }
            }
    }//GEN-LAST:event_FavoriteTableMouseClicked

    private void jBtnPDFScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPDFScanActionPerformed

        if (this.FavoriteTable.getSelectedRows().length > 0) {
            
            if(this.jTbdPneDesktop.getSelectedComponent() != null && this.jTbdPneDesktop.getSelectedComponent() instanceof Route3) {
                Route3 route = (Route3) this.jTbdPneDesktop.getSelectedComponent();
                JXTreeRouteAddressFav fav = (JXTreeRouteAddressFav) this.FavoriteTable.getItem(this.FavoriteTable.getSelectedRows()[0]);
                File file = de.abring.pdferkennung.gui.dialogues.FileIO.getOpenPDFFile(this, System.getProperty("user.home"));
                if (file != null && file.exists()) {
                    int state = this.getState();
                    this.setState(java.awt.Frame.ICONIFIED);
        
        
                    JscanPDF3 jscanPDF3 = new JscanPDF3(null, true, route, fav, file, jCkbEinzelnachweiss.isSelected());
                    jscanPDF3.setVisible(true);
                    this.setState(state);
                }
                route.updateUI();
            }
        
        } else {
            
            int selectedItem = Table.JXTreeTableAddressFavDialog(this, true, "Auftraggeber auswählen:", this.FavoriteTable.getJxTreeRouteEntryList());
            
            if (selectedItem != Table.DIALOG_ABORT) {
                this.FavoriteTable.getSelectionModel().addSelectionInterval(selectedItem, selectedItem);
                this.FavoriteTable.updateUI();
                jBtnPDFScanActionPerformed(evt);
            }
            
        }
    }//GEN-LAST:event_jBtnPDFScanActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        LoadRouteFromWeb loadRoute = new LoadRouteFromWeb(this, true);
        loadRoute.setVisible(true);
 
        for (JXTreeRouteTour tourProt : loadRoute.getTourList()) {
            LOGGER.debug("create new Tour!");
            Route3 tour = new Route3(tourProt, this);
            tour.setTourColor(wavelength);
            wavelength+=1.0;
            tour.getTablePane().setComponentPopupMenu(jPopupMenuRoute);
            this.RouteTable.addItem(tour.getTour());
            this.jTbdPneDesktop.add(tour.getTitle(), tour);
            this.RouteTable.updateUI();
            Karte.setDisplayToFitMapMarkers();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTbBtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTbBtnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTbBtnNewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    public de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute FavoriteTable;
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
    private javax.swing.JPanel OptionPane;
    private javax.swing.JCheckBox OptionenFavToMap;
    private de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute RouteTable;
    private javax.swing.JButton jBtnCalcRoute;
    private javax.swing.JButton jBtnCenterMap;
    private javax.swing.JButton jBtnNewEntry;
    private javax.swing.JButton jBtnPDFScan;
    private javax.swing.JButton jBtnRemoveEntry;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jCBXKarteTile;
    private javax.swing.JCheckBox jCkbEinzelnachweiss;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPneNeueTour;
    private javax.swing.JPanel jPneRoute;
    private javax.swing.JPopupMenu jPopupMenuRoute;
    private javax.swing.JPopupMenu.Separator jPopupMenuRouteSeparator1;
    private javax.swing.JPopupMenu.Separator jPopupMenuRouteSeparator2;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemAdd;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemEdit;
    private javax.swing.JMenuItem jPopupMenuRoutejMenuItemRemove;
    private javax.swing.JCheckBoxMenuItem jPopupMenuRoutejMenuItemRouteMapVisible;
    private javax.swing.JScrollPane jSclPneFavoriteTable;
    private javax.swing.JScrollPane jSclPneRouteTable;
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
    private javax.swing.JTabbedPane jTbdPneDesktop;
    private org.jdesktop.swingx.JXMultiSplitPane jXPaneBack;
    // End of variables declaration//GEN-END:variables

}
