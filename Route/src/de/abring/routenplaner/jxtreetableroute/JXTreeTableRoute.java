/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute;

import de.abring.routenplaner.jxtreetableroute.cellEditores.*;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

/**
 *
 * @author Bring
 */
public final class JXTreeTableRoute extends JXTreeTable {
    
    private List<JXTreeRouteEntry> jxTreeRouteEntryList;
    private final List<ActionListener> actionListenerList = new ArrayList<>();
    private JMapViewer Karte = null;
    private Color color = Color.YELLOW;
    int[] columns = {
        JXNoRootTreeTableModelAddress.EMPTY,
        JXNoRootTreeTableModelAddress.ID,
        JXNoRootTreeTableModelAddress.NAME,
        JXNoRootTreeTableModelAddress.TIME,
        JXNoRootTreeTableModelAddress.DURATION,
        JXNoRootTreeTableModelAddress.APPOINTMENT,
        JXNoRootTreeTableModelAddress.ITEMS,
        JXNoRootTreeTableModelAddress.ADDRESS_ROUTE,
        JXNoRootTreeTableModelAddress.FAVORIT,
        JXNoRootTreeTableModelAddress.DRIVER,
        JXNoRootTreeTableModelAddress.CO_DRIVER,
        JXNoRootTreeTableModelAddress.CAR
    };
    
    /**
     * init
     */
    public JXTreeTableRoute() {
        super();
        this.jxTreeRouteEntryList = new ArrayList<>();
        initComponents();
        handleColumns(columns);
    }
    
    /**
     * init
     * @param jxTreeRouteEntryList 
     */
    public JXTreeTableRoute(List<JXTreeRouteEntry> jxTreeRouteEntryList) {
        super();
        this.jxTreeRouteEntryList = jxTreeRouteEntryList;
        initComponents();
        handleColumns(columns);
    }
    
    
    
    /**
     * initComponents
     *
     */
    private void initComponents() {
        setDragEnabled(true);
        setDropMode(DropMode.INSERT_ROWS);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addHighlighter(HighlighterFactory.createAlternateStriping(new Color(235, 245, 255), Color.white));
        setTreeCellRenderer(new JXTreeCellRendererRoute());
        setRowHeight(22);
        setTransferHandler(new JXTableRowTransferHandlerRoute(this));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("ItemsDeleted")) {
                    removeItemListeners();
                }
            }
        });
    }
   
    
    
    public int getItemIndexOf(JXTreeRouteEntry item) {
        return this.jxTreeRouteEntryList.indexOf(item);
    }
    
    public JXTreeRouteEntry getItem(int index) {
        return this.jxTreeRouteEntryList.get(index);
    }
    
    public JXTreeRouteEntry getItemthatStartsWith(String name) {
        for (JXTreeRouteEntry entry : this.jxTreeRouteEntryList) {
            if (entry.getName().startsWith(name))
                return entry;
        }
        return null;
    }
    
    public void addItem(JXTreeRouteEntry item) {
        this.jxTreeRouteEntryList.add(item);
        if (item instanceof JXTreeRouteAddress) {
            if (this.Karte != null) {
                JXTreeRouteAddress address = (JXTreeRouteAddress) item;
                address.getDot().setColor(this.color);
                this.Karte.addMapMarker(address.getDot());
                this.Karte.updateUI();
            }
            addItemListeners();
        } else if (item instanceof JXTreeRouteRoute) {
            if (this.Karte != null) {
                JXTreeRouteRoute route = (JXTreeRouteRoute) item;
                route.getMapLinesDot().setColor(this.color);
                this.Karte.addMapLines(route.getMapLinesDot());
                this.Karte.updateUI();
            }
        }
    }
    
    public void addItem(int index, JXTreeRouteEntry item) {
        this.jxTreeRouteEntryList.add(index, item);
        if (item instanceof JXTreeRouteAddress) {
            if (this.Karte != null) {
                JXTreeRouteAddress address = (JXTreeRouteAddress) item;
                address.getDot().setColor(this.color);
                this.Karte.addMapMarker(address.getDot());
                this.Karte.updateUI();
            }
            addItemListeners();
        } else if (item instanceof JXTreeRouteRoute) {
            if (this.Karte != null) {
                JXTreeRouteRoute route = (JXTreeRouteRoute) item;
                route.getMapLinesDot().setColor(this.color);
                this.Karte.addMapLines(route.getMapLinesDot());
                this.Karte.updateUI();
            }
        }
    }
    
    public void addAllItems(int index, List<JXTreeRouteEntry> items) {
        items.stream().map((item) -> {
            this.jxTreeRouteEntryList.add(index, item);
            return item;
        }).forEachOrdered((item) -> {
            if (item instanceof JXTreeRouteAddress) {
                if (this.Karte != null) {
                    JXTreeRouteAddress address = (JXTreeRouteAddress) item;
                    address.getDot().setColor(this.color);
                    this.Karte.addMapMarker(address.getDot());
                    this.Karte.updateUI();
                }
            } else if (item instanceof JXTreeRouteRoute) {
                if (this.Karte != null) {
                    JXTreeRouteRoute route = (JXTreeRouteRoute) item;
                    route.getMapLinesDot().setColor(this.color);
                    this.Karte.addMapLines(route.getMapLinesDot());
                    this.Karte.updateUI();
                }
            }
        });
                addItemListeners();
    }

    public void addAllItems(List<JXTreeRouteEntry> items) {
        items.stream().map((item) -> {
            this.jxTreeRouteEntryList.add(item);
            return item;
        }).forEachOrdered((item) -> {
            if (item instanceof JXTreeRouteAddress) {
                if (this.Karte != null) {
                    JXTreeRouteAddress address = (JXTreeRouteAddress) item;
                    address.getDot().setColor(this.color);
                    this.Karte.addMapMarker(address.getDot());
                    this.Karte.updateUI();
                }
            } else if (item instanceof JXTreeRouteRoute) {
                if (this.Karte != null) {
                    JXTreeRouteRoute route = (JXTreeRouteRoute) item;
                    route.getMapLinesDot().setColor(this.color);
                    this.Karte.addMapLines(route.getMapLinesDot());
                    this.Karte.updateUI();
                }
            }
        });
        addItemListeners();
    }
    
    public void removeItem(JXTreeRouteEntry item) {
        if (item instanceof JXTreeRouteAddress) {
            if (this.Karte != null) {
                this.Karte.deleteMapMarker(((JXTreeRouteAddress) item).getDot());
                this.Karte.updateUI();
            }
            this.jxTreeRouteEntryList.remove(item);
            removeItemListeners();
            return;
        } else if (item instanceof JXTreeRouteRoute) {
            if (this.Karte != null) {
                this.Karte.deleteMapLines(((JXTreeRouteRoute) item).getMapLinesDot());
                this.Karte.updateUI();
            }
        }
        this.jxTreeRouteEntryList.remove(item);
    }
    
    public void removeItems(List<JXTreeRouteEntry> items) {
        items.stream().forEach((item) -> {
            removeItem(item);
        });
    }
            
    public void removeItem(int index) {
        if (getItem(index) instanceof JXTreeRouteAddress) {
            if (this.Karte != null) {
                this.Karte.deleteMapMarker(((JXTreeRouteAddress) getItem(index)).getDot());
                this.Karte.updateUI();
            }
            this.jxTreeRouteEntryList.remove(index);
            removeItemListeners();
            return;
        } else if (getItem(index) instanceof JXTreeRouteRoute) {
            if (this.Karte != null) {
                this.Karte.deleteMapLines(((JXTreeRouteRoute) getItem(index)).getMapLinesDot());
                this.Karte.updateUI();
            }
        }
        this.jxTreeRouteEntryList.remove(index);
    }

    public int listLength() {
        return this.jxTreeRouteEntryList.size();
    }

    public void entryListForEach(Consumer<? super JXTreeRouteEntry> cnsmr) {
        jxTreeRouteEntryList.forEach(cnsmr);
    }
    
    
    
// Alle ActionListener!!!
    
    public void addActionListener(ActionListener al) {
        actionListenerList.add(al);
    }

    public void removeActionListener(ActionListener al) {
        actionListenerList.remove(al);
    }

    private void addItemListeners() {
        ActionEvent event = new ActionEvent(this, 1, "ItemAdded");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }

    private void removeItemListeners() {
        ActionEvent event = new ActionEvent(this, 2, "ItemRemoved");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }
    
    private void changeItemStartListeners() {
        ActionEvent event = new ActionEvent(this, 3, "ItemStartChanged");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }
    
    private void changeItemDurationListeners() {
        ActionEvent event = new ActionEvent(this, 4, "ItemDurationChanged");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }
    
    private void changeItemAppointmentListeners() {
        ActionEvent event = new ActionEvent(this, 5, "ItemAppointmentChanged");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }
    
    private void changeItemAddressListeners() {
        ActionEvent event = new ActionEvent(this, 6, "ItemAddressChanged");
        actionListenerList.forEach((action) -> {
            action.actionPerformed(event);
        });
    }
    

    
    
    /**
     * Gib mir eine neue Liste!
     * @param jxTreeRouteEntryList the jxTreeRouteEntryList to set
     */
    public void setJxTreeRouteEntryList(List<JXTreeRouteEntry> jxTreeRouteEntryList) {
        this.jxTreeRouteEntryList = jxTreeRouteEntryList;
        handleColumns(columns);
    }
    
    /**
     * setze Spaltenbreite
     * @param column
     * @param width
     * @param resizeable 
     */
    public void setColumnWidth(int column, int width, boolean resizeable){
        getColumnModel().getColumn(column).setResizable(resizeable);
        getColumnModel().getColumn(column).setWidth(width);
        getColumnModel().getColumn(column).setPreferredWidth(width);
        if (!resizeable) {
            getColumnModel().getColumn(column).setMaxWidth(width);
            getColumnModel().getColumn(column).setMinWidth(width);
        }
    }
    
    /**
     * Zellenausrichtung!
     * @param column
     * @param alignment 
     */
    public void setColumnAlignment(int column, int alignment) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        getTableHeader().getColumnModel().getColumn(column).setCellRenderer(renderer);
        getColumnModel().getColumn(column).setCellRenderer(renderer);
    }

    /**
     * Setze die Spalten anhand der Namen ...
     * @param columnNames 
     */
    public void handleColumns(int[] columnNames) {
        setTreeTableModel(new JXNoRootTreeTableModelAddress(jxTreeRouteEntryList, columnNames));
        
        CellEditorListener cellEditorListener = new CellEditorListener() {

            @Override
            public void editingStopped(ChangeEvent e) {
                if (e.getSource() instanceof MyTreeTableCellEditorAddress)
                    changeItemAddressListeners();
                else if (e.getSource() instanceof MyTreeTableCellEditorTimeOfDay)
                    changeItemStartListeners();
                else if (e.getSource() instanceof MyTreeTableCellEditorDuration)
                    changeItemDurationListeners();
                else if (e.getSource() instanceof MyTreeTableCellEditorAppointment)
                    changeItemAppointmentListeners();
                
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        };
        
        MyTreeTableCellEditorAddress myTreeTableCellEditorAddress = new MyTreeTableCellEditorAddress();
        myTreeTableCellEditorAddress.addCellEditorListener(cellEditorListener);
        
        MyTreeTableCellEditorTimeOfDay myTreeTableCellEditorTimeOfDay = new MyTreeTableCellEditorTimeOfDay();
        myTreeTableCellEditorTimeOfDay.addCellEditorListener(cellEditorListener);
        
        MyTreeTableCellEditorDuration myTreeTableCellEditorDuration = new MyTreeTableCellEditorDuration();
        myTreeTableCellEditorDuration.addCellEditorListener(cellEditorListener);
        
        MyTreeTableCellEditorAppointment myTreeTableCellEditorAppointment = new MyTreeTableCellEditorAppointment();
        myTreeTableCellEditorAppointment.addCellEditorListener(cellEditorListener);
                    
        for (int i = 0; i < this.getColumnCount(); i++) {
            switch (columnNames[i]) {
                case JXNoRootTreeTableModelAddress.EMPTY:
                    setColumnWidth(i, 50, false);
                    break;
                case JXNoRootTreeTableModelAddress.ID:
                    setColumnAlignment(i, JLabel.RIGHT);
                    setColumnWidth(i, 30, false);
                    break;
                case JXNoRootTreeTableModelAddress.NAME:
                    setColumnWidth(i, 120, true);
                    break;

                case JXNoRootTreeTableModelAddress.TIME:
                    setColumnAlignment(i, JLabel.RIGHT);
                    setColumnWidth(i, 80, false);
                    getColumnModel().getColumn(i).setCellEditor(myTreeTableCellEditorTimeOfDay);
                    break;

                case JXNoRootTreeTableModelAddress.DURATION:
                    setColumnAlignment(i, JLabel.RIGHT);
                    setColumnWidth(i, 80, false);
                    getColumnModel().getColumn(i).setCellEditor(myTreeTableCellEditorDuration);
                    break;

                case JXNoRootTreeTableModelAddress.ITEMS:
                    setColumnWidth(i, 120, true);
                    break;

                case JXNoRootTreeTableModelAddress.APPOINTMENT:
                    setColumnAlignment(i, JLabel.RIGHT);
                    setColumnWidth(i, 160, false);
                    getColumnModel().getColumn(i).setCellEditor(myTreeTableCellEditorAppointment);
                    break;

                case JXNoRootTreeTableModelAddress.ADDRESS:
                    setColumnWidth(i, 160, true);
                    getColumnModel().getColumn(i).setCellEditor(myTreeTableCellEditorAddress);
                    break;

                case JXNoRootTreeTableModelAddress.ROUTE:
                    setColumnWidth(i, 160, true);
                    break;

                case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE:
                    setColumnWidth(i, 160, true);
                    getColumnModel().getColumn(i).setCellEditor(myTreeTableCellEditorAddress);
                    break;

                case JXNoRootTreeTableModelAddress.FAVORIT:
                    setColumnWidth(i, 120, true);
                    
                    break;
                case JXNoRootTreeTableModelAddress.CAR:
                    setColumnWidth(i, 60, true);
                    
                    break;
                case JXNoRootTreeTableModelAddress.DRIVER:
                    setColumnWidth(i, 25, true);
                    
                    break;
                case JXNoRootTreeTableModelAddress.CO_DRIVER:
                    setColumnWidth(i, 25, true);
                    
                    break;
                case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                    setColumnWidth(i, 25, false);
                    
                    break;
            }
        }
    }
    
    
    
    
    /**
     * setze Karte
     * @param Karte the Karte to set
     */
    public void setKarte(JMapViewer Karte) {
        this.Karte = Karte;
    }

    /**
     * Die Farbe der Punkte
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
        updateMapMarkerLine();
    }
        
    /**
     * Die Farbe der Punkte
     * @return the color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * toggle MapComponent visibility
     * @param visible 
     */
    public void setMapMarkerVisible(boolean visible) {
        this.jxTreeRouteEntryList.forEach((entry) -> {
            if (entry instanceof JXTreeRouteAddress)
                ((JXTreeRouteAddress) entry).getDot().setVisible(visible);
            else if (entry instanceof JXTreeRouteRoute)
                ((JXTreeRouteRoute) entry).getMapLinesDot().setVisible(visible);
        });
        if(this.Karte != null)
            this.Karte.updateUI();
    }
    
    /**
     * mach mal die Farbe neu!
     */
    public void updateMapMarkerLine() {
        this.jxTreeRouteEntryList.forEach((entry) -> {
            if (entry instanceof JXTreeRouteAddress) {
                ((JXTreeRouteAddress) entry).getDot().setColor(this.color);
            } else if (entry instanceof JXTreeRouteRoute) {
                ((JXTreeRouteRoute) entry).getMapLinesDot().setColor(this.color);
            }
        });
        if(this.Karte != null)
            this.Karte.updateUI();
    }

    /**
     * @return the jxTreeRouteEntryList
     */
    public List<JXTreeRouteEntry> getJxTreeRouteEntryList() {
        return jxTreeRouteEntryList;
    }
}
