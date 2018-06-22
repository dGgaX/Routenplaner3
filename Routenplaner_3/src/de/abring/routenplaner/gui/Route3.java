/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package de.abring.routenplaner.gui;


import de.abring.routenplaner.jxtreetableroute.entries.*;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.RainbowColor;
import de.abring.routenplaner.jxtreetableroute.*;
import de.abring.routenplaner.gui.components.JPanelTour;
import de.abring.routenplaner.gui.dialogues.Entry;
import de.abring.routenplaner.gui.dialogues.Table;
import de.abring.routenplaner.jxtreetableroute.JXTreeRouteCopy;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import org.openstreetmap.gui.jmapviewer.MapLinesDot;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Karima
 */
public class Route3 extends javax.swing.JPanel {

    /**
     * @return the tour
     */
    public JXTreeRouteTour getTour() {
        return tour;
    }
    
    private static final Logger LOGGER = LogManager.getLogger(Route3.class.getName());
    
    private int[] favSelection;
    private boolean currentStateSaved;
    
    private String title = "";
    
    /**
     * @return the currentStateSaved
     */
    public boolean isCurrentStateSaved() {
        return currentStateSaved;
    }
            
    /**
     * @return the Name
     */
    public String getRouteName() {
        return this.tour.getName();
    }

    /**
     * @param name
     */
    public void setRouteName(String name) {
        this.setTitle(name);
        this.tour.setName(name);
    }
    /**
     * @return the Driver
     */
    public String getDriver() {
        return this.tour.getDriver();
    }

    /**
     * @param Driver the Driver to set
     */
    public void setDriver(String Driver) {
        this.tour.setDriver(Driver);
    }

    /**
     * @return the CoDriver
     */
    public String getCoDriver() {
        return this.tour.getCoDriver();
    }

    /**
     * @param CoDriver the CoDriver to set
     */
    public void setCoDriver(String CoDriver) {
        this.tour.setCoDriver(CoDriver);
    }

    /**
     * @return the Car
     */
    public String getCar() {
        return this.tour.getCar();
    }

    /**
     * @param Car the Car to set
     */
    public void setCar(String Car) {
        this.tour.setCar(Car);
    }

    private final JXTreeRouteTour tour;
    Main3 parent;
    JPanelTour jPaneTour;
    private boolean markerVisible;
    int[] tableColumns;
    /**
     * Creates new dialog Route
     * @param name
     * @param parent
     */
        
    /**
     * Creates new dialog Route
     * @param name
     * @param parent
     */
    public Route3(String name, Main3 parent) {
        super();
        
        LOGGER.info("lege neue Route: " + name + " an.");
        
        this.parent = parent;
        this.markerVisible = true;
        this.currentStateSaved = false;
        this.setTitle(name);
        this.tour = new JXTreeRouteTour(name);
        this.tour.setName(name);
        this.favSelection = new int[0];
        this.initComponents();
        this.jPaneTour = new JPanelTour(this);
        this.tablePane.setColor(this.tour.getColor());
        this.tablePane.setKarte(parent.Karte);
        this.tablePane.setJxTreeRouteEntryList(this.tour.getEntryList());
        this.tablePane.addActionListener((java.awt.event.ActionEvent evt) -> {
            if(evt.getSource() == this.tablePane) {
                if (evt.getActionCommand().equals("ItemAdded") || evt.getActionCommand().equals("ItemRemoved") || evt.getActionCommand().equals("ItemAddressChanged")) {
                    List<JXTreeRouteEntry> selectedEntries = new ArrayList<>();
                    for (int i = this.tablePane.getSelectedRows().length - 1; i >= 0; i--) {
                        selectedEntries.add(this.tablePane.getItem(this.tablePane.getSelectedRows()[i]));
                    }

                    recreateRoute();

                    this.tablePane.clearSelection();
                    selectedEntries.forEach((entry) -> {
                        int index = this.tablePane.getItemIndexOf(entry);
                        this.tablePane.getSelectionModel().addSelectionInterval(index, index);
                    });
        
                    refreshTable();
                } else if (evt.getActionCommand().equals("ItemStartChanged") || evt.getActionCommand().equals("ItemDurationChanged")) {
                    refreshTable();
                }
            }
        });
        
        this.tablePane.getModel().addTableModelListener((TableModelEvent evt) -> {
            if (evt.getType() == TableModelEvent.INSERT || evt.getType() == TableModelEvent.DELETE)
            refreshTable();
        });
        
        tableColumns =  new int[] {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.ID,
            JXNoRootTreeTableModelAddress.TIME,
            JXNoRootTreeTableModelAddress.DURATION,
            JXNoRootTreeTableModelAddress.APPOINTMENT,
            JXNoRootTreeTableModelAddress.ADDRESS_ROUTE,
        };
        this.tablePane.handleColumns(tableColumns);
        
        setTourColor(Color.RED);

        addBasicEntries("Löschmann", new TimeOfDay("09:00"), new TimeOfDay("00:20"), new TimeOfDay("00:00"));
    }

    /**
     * Creates new dialog Route
     * @param tour
     * @param parent
     */
    public Route3(JXTreeRouteTour tour, Main3 parent) {
        super();
        
        LOGGER.info("lege neue Route: {} an.", tour.getName());
        
        this.parent = parent;
        this.markerVisible = true;
        this.currentStateSaved = false;
        this.tour = tour;
        this.setTitle(tour.getName());
        this.favSelection = new int[0];
        this.initComponents();
        this.jPaneTour = new JPanelTour(this);
        this.tablePane.setColor(this.tour.getColor());
        this.tablePane.setKarte(parent.Karte);
        this.tablePane.setJxTreeRouteEntryList(this.tour.getEntryList());
        this.tablePane.addActionListener((java.awt.event.ActionEvent evt) -> {
            if(evt.getSource() == this.tablePane) {
                if (evt.getActionCommand().equals("ItemAdded") || evt.getActionCommand().equals("ItemRemoved") || evt.getActionCommand().equals("ItemAddressChanged")) {
                    List<JXTreeRouteEntry> selectedEntries = new ArrayList<>();
                    for (int i = this.tablePane.getSelectedRows().length - 1; i >= 0; i--) {
                        selectedEntries.add(this.tablePane.getItem(this.tablePane.getSelectedRows()[i]));
                    }

                    recreateRoute();

                    this.tablePane.clearSelection();
                    selectedEntries.forEach((entry) -> {
                        int index = this.tablePane.getItemIndexOf(entry);
                        this.tablePane.getSelectionModel().addSelectionInterval(index, index);
                    });
        
                    refreshTable();
                } else if (evt.getActionCommand().equals("ItemStartChanged") || evt.getActionCommand().equals("ItemDurationChanged")) {
                    refreshTable();
                }
            }
        });
        
        this.tablePane.getModel().addTableModelListener((TableModelEvent evt) -> {
            if (evt.getType() == TableModelEvent.INSERT || evt.getType() == TableModelEvent.DELETE)
            refreshTable();
        });
        
        tableColumns =  new int[] {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.ID,
            JXNoRootTreeTableModelAddress.NAME,
            JXNoRootTreeTableModelAddress.TIME,
            JXNoRootTreeTableModelAddress.DURATION,
            JXNoRootTreeTableModelAddress.APPOINTMENT,
            JXNoRootTreeTableModelAddress.ITEMS,
            JXNoRootTreeTableModelAddress.ADDRESS_ROUTE,
            JXNoRootTreeTableModelAddress.FAVORIT,
        };
        this.tablePane.handleColumns(tableColumns);
        
        for (JXTreeRouteEntry entry : this.tour.getEntryList()) {
            if (entry instanceof JXTreeRouteAddress)
                addMapMarker(((JXTreeRouteAddress) entry).getDot());
            if (entry instanceof JXTreeRouteRoute)
                addMapLines(((JXTreeRouteRoute) entry).getMapLinesDot());
            
        }
        
    }
        
    /**
     * adds the Start, End and 2 Favs to the List
     * @param name (Name of the Fav or what the Fav starts with ...)
     */
    private void addBasicEntries(String name, TimeOfDay start, TimeOfDay firstDuration, TimeOfDay secondDuration) {
        LOGGER.debug("add basic Listelements");
        //add Start
        addEntry(new JXTreeRouteStart(start));
        
        //get the Favorite with the given Name
        JXTreeRouteAddressFav fav = null;
        JXTreeRouteEntry entry = parent.FavoriteTable.getItemthatStartsWith(name);
        if (entry instanceof JXTreeRouteAddressFav)
            fav = (JXTreeRouteAddressFav) entry;
        if (fav != null) {
            //add the first Favorite
            JXTreeRouteAddressFav fav2 = new JXTreeRouteAddressFav(fav);
            fav2.setDuration(firstDuration);
            addEntry(fav2);
            //add the second Favorite
            fav2 = new JXTreeRouteAddressFav(fav);
            fav2.setDuration(secondDuration);
            addEntry(fav2);
            
        }
        //add End
        addEntry(new JXTreeRouteEnd());
        this.recreateRoute();
    }

    // <editor-fold defaultstate="collapsed" desc="List Manipulation Operation">
    /**
     * gets the Item at Index "index"
     * @param index
     * @return JXTreeRouteEntry
     */
    public JXTreeRouteEntry getItem(int index) {
        return this.tablePane.getItem(index);
    }
    
    public void remove() {
        
    }
    /**
     * gets the first Item thats starts with "name"
     * @param name
     * @return JXTreeRouteEntry
     */
    public JXTreeRouteEntry getItemthatStartsWith(String name) {
        return this.tablePane.getItemthatStartsWith(name);
    }
    
    /**
     * create new Address Client Entry in List
     */
    public void newAddressClientEntry() {
        if (this.parent.FavoriteTable.getSelectedRows().length > 0) {
            
            Entry newEntry = new Entry(this.parent, true, (JXTreeRouteAddressFav) this.parent.FavoriteTable.getItem(this.parent.FavoriteTable.getSelectedRows()[0]));
            newEntry.setVisible(true);
            
            if (newEntry.getEntry() != null && !newEntry.getEntry().getName().isEmpty() && newEntry.getEntry().getAddress() != null && newEntry.getEntry().getAddress().isValid() && !newEntry.getEntry().getAddress().getStraße().isEmpty()) {
                LOGGER.trace("Neuer Eintrag!");
                this.addEntry(this.listLength() - 2, newEntry.getEntry());
            }
            
        } else {
            int selectedItem = Table.JXTreeTableAddressFavDialog(this.parent, true, "Auftraggeber auswählen:", this.parent.FavoriteTable.getJxTreeRouteEntryList());
            if (selectedItem != Table.DIALOG_ABORT) {
                this.parent.FavoriteTable.getSelectionModel().addSelectionInterval(selectedItem, selectedItem);
                this.parent.FavoriteTable.updateUI();
                newAddressClientEntry();
            }
        }
    
    
    }
    
    public void editSelectedAddressClientEntry() {
        int[] selectedRows = this.tablePane.getSelectedRows();
        
        for (int i = 0; i < selectedRows.length; i++) {
            JXTreeRouteEntry entry = this.tablePane.getItem(selectedRows[i]);
            if (entry instanceof JXTreeRouteAddressClient) {
                JXTreeRouteAddressClient address = (JXTreeRouteAddressClient) entry;
                JXTreeRouteAddressClient addressCopy = (JXTreeRouteAddressClient) JXTreeRouteCopy.copy(address);
                addressCopy.setAddress(address.getAddress());
                
                Entry newEntry = new Entry(this.parent, true, addressCopy);
                newEntry.setVisible(true);

                if (newEntry.getEntry() != null && !newEntry.getEntry().getName().isEmpty() && newEntry.getEntry().getAddress() != null && newEntry.getEntry().getAddress().isValid() && !newEntry.getEntry().getAddress().getStraße().isEmpty()) {
                    
                    this.tablePane.removeItem(entry);
                    this.tablePane.addItem(selectedRows[i], newEntry.getEntry());
                    refreshTable();
                    this.tablePane.updateUI();
                }
            }
        }
    }
    
    public void removeSelectedAddressClientEntries() {
        List<JXTreeRouteEntry> selectedEntries = new ArrayList<>();
        int[] selectedRows = this.tablePane.getSelectedRows();
        
        for (int i = 0; i < selectedRows.length; i++) {
            JXTreeRouteEntry entry = this.tablePane.getItem(selectedRows[i]);
            if (entry instanceof JXTreeRouteAddress)
                selectedEntries.add(entry);
        }
        this.tablePane.removeItems(selectedEntries);
    }
    
    /**
     * add Entry to List
     * @param entry (Der Eintrag)
     */
    public void addEntry(JXTreeRouteEntry entry) {
        this.tablePane.addItem(entry);
        this.recreateRoute();
        this.tablePane.updateUI();
    }
    
    /**
     * add Entry to List
     * @param index (Listen-Index, an dem der Eintrag eingefügt werden soll)
     * @param entry (Der Eintrag)
     */
    public void addEntry(int index, JXTreeRouteEntry entry) {
        this.tablePane.addItem(index, entry);
        this.recreateRoute();
        this.tablePane.updateUI();
    }
    
    /**
     * remove Entry from List
     * @param item (Der Eintrag)
     */
    public void removeEntry(JXTreeRouteEntry item) {
        this.tablePane.removeItem(item);
        this.recreateRoute();
        this.tablePane.repaint();
    }
    
    /**
     * remove Entry from List
     * @param index (Listen-Index, an dem der Eintrag eingefügt werden soll)
     */
    public void removeEntry(int index) {
        this.tablePane.removeItem(index);
        this.recreateRoute();
        this.tablePane.updateUI();
        this.tablePane.repaint();
    }
    
    /**
     * Number of Entries in the List
     * @return the Size of the List
     */
    public int listLength() {
        return this.tablePane.listLength();
    }
    
    /**
     * sucht alle Routen in der Liste, die noch nicht berechnet sind.
     * Diese werden dann ausgeführt...
     */
    public final void calculateRoute() {
        LOGGER.trace("fehlende Routen berechnen...");
        Color ossi = Color.DARK_GRAY;
        this.tour.getEntryList().stream().filter((entry) -> (entry instanceof JXTreeRouteAddress)).map((entry) -> ((JXTreeRouteAddress) entry).getRoute()).filter((route) -> (!(route.getRoute().isCalculated()))).forEachOrdered((route) -> {
            if (route.getLine() != null) {
                MapLinesDot  calcLine = new MapLinesDot(ossi, route.getLine().getStartLat(), route.getLine().getStartLon(), route.getLine().getStopLat(), route.getLine().getStopLon());
                removeMapLines(route.getLine());
                addMapLines(calcLine);
                route.calcRoute();
                route.setDuration(route.getRoute().getDauer());
    //            route.setName("Diese Route beträgt " + String.valueOf(route.getRoute().getLänge()) + " km.");
                removeMapLines(calcLine);
                addMapLines(route.getLine());
            }
        });
        refreshTable();
    }
    
    /**
     * adds, deletes or moves all Routes in the List ...
     */
    public final void recreateRoute() {
        LOGGER.trace("fehlende Routen hinzufügen...");

        /**
         * gehe die ganze Liste durch
         */
        for(int i = 0; i < this.tour.getEntryList().size() - 2; i++) {
            //JXTreeRouteEntry entryPrev = this.tour.getEntryList().get(i - 1);
            JXTreeRouteEntry entryThis = this.tour.getEntryList().get(i);
            JXTreeRouteEntry entryNext = this.tour.getEntryList().get(i + 1);
            
            if (entryThis instanceof JXTreeRouteAddress) {
                JXTreeRouteAddress addressThis = (JXTreeRouteAddress) entryThis;
                JXTreeRouteRoute routeThis = addressThis.getRoute();
                    
                if (entryNext instanceof JXTreeRouteAddress) {
                    JXTreeRouteAddress addressNext = (JXTreeRouteAddress) entryNext;
                    JXTreeRouteRoute routeNext = addressNext.getRoute();
                    
                    if (routeThis.getRoute().getEndAddress() == null || !routeThis.getRoute().getEndAddress().equals(addressNext.getAddress())) {
                        this.parent.Karte.deleteMapLines(routeThis.getMapLinesDot());
                        routeThis.getRoute().setEndAddress(addressNext.getAddress());
                        routeThis.updateLine();
                        routeThis.getMapLinesDot().setColor(this.tour.getColor());
                        this.parent.Karte.addMapLines(routeThis.getMapLinesDot());
                        this.parent.Karte.updateUI();
                        this.parent.Karte.setDisplayToFitMapMarkers();

                    }

                } else if (entryNext instanceof JXTreeRouteEnd) {
                    
                    if (routeThis.getRoute().getEndAddress() == null || !routeThis.getRoute().getEndAddress().equals(addressThis.getAddress())) {
                        this.parent.Karte.deleteMapLines(routeThis.getMapLinesDot());
                        routeThis.getRoute().setEndAddress(addressThis.getAddress());
                        routeThis.updateLine();
                        routeThis.getMapLinesDot().setColor(this.tour.getColor());
                        this.parent.Karte.addMapLines(routeThis.getMapLinesDot());
                        this.parent.Karte.updateUI();
                        this.parent.Karte.setDisplayToFitMapMarkers();

                    }
                }
            }             
        }
        refreshTable();
    }
//
//            /**
//             * Schaue, ob 2 Adressen hintereinander kommen.
//             * Wenn, füge eine neue Route in die Mitte ein.
//             */
//            if (entryThis instanceof JXTreeRouteAddress && entryNext instanceof JXTreeRouteAddress) {
//                JXTreeRouteRoute newRoute = new JXTreeRouteRoute(new MapRoute(((JXTreeRouteAddress) entryThis).getAddress(), ((JXTreeRouteAddress) entryNext).getAddress()));
//                newRoute.setDuration(newRoute.getRoute().getDauer());
//                addEntry(i + 1, newRoute);
//                
//                recreateRoute();
//                return;
//            }
//            
//            /**
//             * Schaue, ob die Route zur vorigen und nächsten Adresse passt.
//             * Wenn nicht, lösche die Route und gehe die Liste noch mal von vorne durch.
//             * danach verlasse die Liste (itteration)
//             */
//            if (entryPrev instanceof JXTreeRouteAddress && entryThis instanceof JXTreeRouteRoute && entryNext instanceof JXTreeRouteAddress) {
//                JXTreeRouteAddress addressPrev = (JXTreeRouteAddress) entryPrev;
//                JXTreeRouteRoute routeThis = (JXTreeRouteRoute) entryThis;
//                JXTreeRouteAddress addressNext = (JXTreeRouteAddress) entryNext;
//
//                if (!routeThis.getRoute().getStartAddress().equals(addressPrev.getAddress()) || !routeThis.getRoute().getEndAddress().equals(addressNext.getAddress())) {
//                    removeEntry(entryThis);
//                    
//                    recreateRoute();
//                    return;
//                }
//            }
//            
//            /**
//             * Schaue, ob 2 Routen hintereinander kommen.
//             * Wenn, lösche die beide Route und gehe die Liste noch mal von vorne durch.
//             * danach verlasse die Liste (itteration)
//             */
//            if (entryPrev instanceof JXTreeRouteRoute && entryThis instanceof JXTreeRouteRoute) {
//                removeEntry(entryPrev);
//                removeEntry(entryThis);
//                
//                recreateRoute();
//                return;
//            }
            
//        }
//        refreshTable();
//    }
    
    /**
     * update Times and IDs
     */
    public final void refreshTable() {
        TimeOfDay prevEnde = null;
        int ID = 0;
        for (JXTreeRouteEntry entry : this.tour.getEntryList()) {
            if (prevEnde != null)
                entry.setStart(prevEnde);
            prevEnde = entry.getEnd();
            if (entry instanceof JXTreeRouteAddress) {
                JXTreeRouteAddress address = (JXTreeRouteAddress) entry;
                address.setID(++ID);
                if (address.getDot() != null)
                    address.getDot().setID(ID);
            }
        }
        this.tablePane.updateUI();
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Map Manipulation Operation">
    /**
     * sets the Color for the Addresses and Routes of this Sheed
     * @param color 
     */
    public final void setTourColor(Color color) {
        this.tour.setColor(color);
        
        this.tablePane.setColor(color);
    }

    public void setMapMarkerVisible(boolean visible) {
        this.markerVisible = visible;
        this.tour.setMapVisible(visible);
        this.tablePane.setMapMarkerVisible(visible);
        this.jPaneTour.updateTexte();
//        this.parent.setjPopupMenuRoutejMenuItemRouteMapVisibleSelected(visible);
        this.parent.Karte.updateUI(); 
    }
    
    public void setTourColor(double i) {
        double nm;
        nm = (((i * 4 / 10) + 3.8) * 100);
        int[] RGB = RainbowColor.wav2RGB(nm);
        setTourColor(new Color(RGB[0], RGB[1], RGB[2]));
    }
    public void setTourColor(int r, int g, int b) {
        setTourColor(new Color(r, g, b));
    }
    
    public final Color getColor() {
        return this.tour.getColor();
    }
    
    /**
     * add the MapMarker from the parents Map
     * @param marker 
     */
    public final void addMapMarker(MapMarkerDot marker) {
        if (marker != null && this.parent.Karte != null) {
            LOGGER.trace("add MapMarker: {}", marker.toString());
            marker.setColor(this.tour.getColor());
            parent.Karte.addMapMarker(marker);
            //parent.Karte.updateUI();
            tablePane.updateUI();
        } else {
            LOGGER.warn("cannot add MapMarker!!!");
        }
    }
    
    /**
     * add the MapLine from the parents Map
     * @param line 
     */
    public final void addMapLines(MapLinesDot line) {
        if (line != null && this.parent.Karte != null) {
            LOGGER.trace("add MapMarker: {}", line.toString());
            line.setColor(this.tour.getColor());
            parent.Karte.addMapLines(line);
            //parent.Karte.updateUI();
            tablePane.updateUI();
        } else {
            LOGGER.warn("cannot add MapLine!!!");
        }
    }
    
    /**
     * remove the MapMarker from the parents Map
     * @param marker 
     */
    public final void removeMapMarker(MapMarkerDot marker) {
        if (marker != null && this.parent.Karte != null) {
            LOGGER.trace("remove MapMarker: {}", marker.toString());
            parent.Karte.deleteMapMarker(marker);
            //parent.Karte.updateUI();
        } else {
            LOGGER.warn("cannot remove MapMarker!!!");
        }
    }
    
    /**
     * remove the MapLine from the parents Map
     * @param line 
     */
    public final void removeMapLines(MapLinesDot line) {
        if (line != null && this.parent.Karte != null) {
            LOGGER.trace("remove MapLine: {}", line.toString());
            parent.Karte.deleteMapLines(line);
            //parent.Karte.updateUI();
        } else {
            LOGGER.warn("cannot remove MapLine!!!");
        }
    }// </editor-fold>
    
    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {                                            
//        LOGGER.trace("Route: {} aktiviert. Schreibe {} Einträge.", getTitle(), String.valueOf(this.favSelection.length));
//        
//        
//        //update TourPane 
//        this.parent.getTourPane().removeAll();
//        this.jPaneTour.updateTexte();
//        this.parent.getTourPane().add(this.jPaneTour);
//        this.parent.getTourPane().updateUI();
//        this.parent.setjPopupMenuRoutejMenuItemRouteMapVisibleSelected(this.markerVisible);
//        int[] temp = this.favSelection;
//        //update FavPane
//        this.parent.getFavoriteTable().clearSelection();
//        for (int i : temp) {
//            if (i < this.parent.getFavoriteTable().listLength())
//                this.parent.getFavoriteTable().getSelectionModel().addSelectionInterval(i, i);
//        }
    }                                           

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {                                    
//        if (evt.getPropertyName().equals("maximum")) {
//            if ((boolean)evt.getNewValue()) {
//                //((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
//                //this.setBorder(null);
//            } else {
//                //this.updateUI();
//            }
//        } else {
//            System.out.println(evt.getPropertyName());
//        }
    }                                   

    
    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    /**
     * @return the favSelection
     */
    public int[] getFavSelection() {
        return favSelection;
    }

    /**
     * @param favSelection the favSelection to set
     */
    public void setFavSelection(int[] favSelection) {
        this.favSelection = favSelection;
    }

    /**
     * @return the tablePane
     */
    public de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute getTablePane() {
        return tablePane;
    }

    /**
     * @return the markerVisible
     */
    public boolean isMarkerVisible() {
        return markerVisible;
    }

    /**
     * @param markerVisible the markerVisible to set
     */
    public void setMarkerVisible(boolean markerVisible) {
        this.markerVisible = markerVisible;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPane = new javax.swing.JScrollPane();
        tablePane = new de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute();
        jBtn_ = new javax.swing.JButton();

        tablePane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tablePaneComponentResized(evt);
            }
        });
        backPane.setViewportView(tablePane);

        jBtn_.setText("_");
        jBtn_.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtn_.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jBtn_.setMaximumSize(new java.awt.Dimension(20, 20));
        jBtn_.setMinimumSize(new java.awt.Dimension(20, 20));
        jBtn_.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 814, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(backPane, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jBtn_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(backPane, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jBtn_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablePaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tablePaneComponentResized
        if (tablePane.getWidth() > 800 && tableColumns.length != 9) {
            tableColumns =  new int[] {
                JXNoRootTreeTableModelAddress.EMPTY,
                JXNoRootTreeTableModelAddress.ID,
                JXNoRootTreeTableModelAddress.NAME,
                JXNoRootTreeTableModelAddress.TIME,
                JXNoRootTreeTableModelAddress.DURATION,
                JXNoRootTreeTableModelAddress.APPOINTMENT,
                JXNoRootTreeTableModelAddress.ITEMS,
                JXNoRootTreeTableModelAddress.ADDRESS_ROUTE,
                JXNoRootTreeTableModelAddress.FAVORIT,
            };
            this.tablePane.handleColumns(tableColumns);

        } else if (tablePane.getWidth() > 300 && tablePane.getWidth() <= 800 && tableColumns.length != 6) {
            tableColumns =  new int[] {
                JXNoRootTreeTableModelAddress.EMPTY,
                JXNoRootTreeTableModelAddress.ID,
                JXNoRootTreeTableModelAddress.TIME,
                JXNoRootTreeTableModelAddress.DURATION,
                JXNoRootTreeTableModelAddress.APPOINTMENT_SHORT,
                JXNoRootTreeTableModelAddress.ADDRESS_ROUTE_REV,
            };
            this.tablePane.handleColumns(tableColumns);

        } else if (tablePane.getWidth() <= 300 && tableColumns.length != 4) {
            tableColumns =  new int[] {
                JXNoRootTreeTableModelAddress.EMPTY,
                JXNoRootTreeTableModelAddress.ID,
                JXNoRootTreeTableModelAddress.TIME,
                JXNoRootTreeTableModelAddress.ADDRESS_ROUTE_REV,
            };
            this.tablePane.handleColumns(tableColumns);

        }
    }//GEN-LAST:event_tablePaneComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane backPane;
    private javax.swing.JButton jBtn_;
    private de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute tablePane;
    // End of variables declaration//GEN-END:variables
}
