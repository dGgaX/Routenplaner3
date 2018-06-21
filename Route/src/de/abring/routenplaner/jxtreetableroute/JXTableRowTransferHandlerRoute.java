/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute;

import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;


/**
 *
 * @author Karima
 */
public class JXTableRowTransferHandlerRoute extends TransferHandler {
    
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(JXTreeTableRoute.class, DataFlavor.javaJVMLocalObjectMimeType, "TreeTableRow");
    private final List<JXTreeRouteEntry> oldEntries = new ArrayList<>();
    private final List<JXTreeRouteEntry> newEntries = new ArrayList<>();
    private final JXTreeTableRoute table;
    private int moveOrCopy = MOVE;
    
    public JXTableRowTransferHandlerRoute(JXTreeTableRoute table) {
        this.table = table;
    }
    
    public JXTableRowTransferHandlerRoute(JXTreeTableRoute table, int moveOrCopy) {
        this.table = table;
        this.moveOrCopy = moveOrCopy;
    }
    
    @Override
    public int getSourceActions(JComponent c) {
        return moveOrCopy;
    }

    @Override
    public Transferable createTransferable(JComponent c) {
        JXTreeTableRoute tta = (JXTreeTableRoute) c;
        return new DataHandler(tta, localObjectFlavor.getMimeType());
    }

    @Override
    public void exportDone(JComponent c, Transferable t, int action) {
        if (action == MOVE) {

            JXTreeTableRoute tta = (JXTreeTableRoute) c;

            if (!newEntries.isEmpty()) {

                oldEntries.forEach((entry) -> {
                    tta.removeItem(entry);
                });
                oldEntries.clear();

                if (newEntries.size() > 0) {
                    tta.clearSelection();
                    newEntries.forEach((entry) -> {
                        int index = tta.getItemIndexOf(entry);
                        tta.getSelectionModel().addSelectionInterval(index, index);
                    });
                }
                
            } else {

                int[] selectedRows = tta.getSelectedRows();


                oldEntries.clear();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    JXTreeRouteEntry entry = tta.getItem(selectedRows[i]);
                    oldEntries.add(entry);
                }
                for (JXTreeRouteEntry entry: oldEntries) {
                    tta.removeItem(entry);
                }
                for (PropertyChangeListener listener : tta.getPropertyChangeListeners()) {
                    listener.propertyChange(new PropertyChangeEvent(table,
                   "ItemsDeleted",
                   null,
                   null));
                }
            }
        }
    }
    
    @Override
    public boolean canImport(TransferSupport supp) {
        JXTreeTableRoute.DropLocation dl = (JXTreeTableRoute.DropLocation) supp.getDropLocation();
        int index = dl.getRow();
        if (index >= table.getRowCount()) {
            index = table.getRowCount() - 1;
        } else if(index < 0) {
            index = 0;
        }
        
        return supp.getComponent() instanceof JXTreeTableRoute && supp.isDrop() && supp.isDataFlavorSupported(localObjectFlavor) &&
                ((table.getItem(index) instanceof JXTreeRouteAddress) ||
                (table.getItem(index) instanceof JXTreeRouteTour));
    }

    @Override
    public boolean importData(TransferSupport supp) {
        if (!canImport(supp)) {
            return false;
        }

        // Fetch the Transferable and its data
        Transferable t = supp.getTransferable();
        try {
            JXTreeTableRoute tta = (JXTreeTableRoute) t.getTransferData(localObjectFlavor);
            // Fetch the drop location
            JXTreeTableRoute.DropLocation dl = (JXTreeTableRoute.DropLocation) supp.getDropLocation();
            int index = dl.getRow();
            
            JXTreeRouteEntry targetEntry = table.getItem(index);
            
            // Insert the data at this location
            int[] selectedRows = tta.getSelectedRows();
            
//            List<JXTreeRouteEntry> source = tta.getJxTreeRouteEntryList();
//            List<JXTreeRouteEntry> target = table.getJxTreeRouteEntryList();
            
            oldEntries.clear();
            newEntries.clear();
            
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                JXTreeRouteEntry entry = tta.getItem(selectedRows[i]);
                oldEntries.add(entry);
                newEntries.add(JXTreeRouteCopy.copy(entry));
            }
            
            if (targetEntry instanceof JXTreeRouteAddress) {
                table.addAllItems(index, newEntries);
                if (tta != table) {
                    if (newEntries.size() > 0) {
                        table.clearSelection();
                        newEntries.forEach((entry) -> {
                            int index2 = table.getItemIndexOf(entry);
                            table.getSelectionModel().addSelectionInterval(index2, index2);
                        });
                    }
                    newEntries.clear();
                }
            } else if (targetEntry instanceof JXTreeRouteTour) {
                //((JXTreeRouteTour) targetEntry).getEntryList().addAll(((JXTreeRouteTour) targetEntry).getEntryList().size() - 2, newEntries);
                for (PropertyChangeListener listener : table.getPropertyChangeListeners()) {
                    listener.propertyChange(new PropertyChangeEvent(table,
                   "ItemDropped",
                   newEntries,
                   targetEntry));
                }
            }
            
            
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(JXTableRowTransferHandlerRoute.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return true;
    }

    /**
     * @return the moveOrCopy
     */
    public int getMoveOrCopy() {
        return moveOrCopy;
    }

    /**
     * @param moveOrCopy the moveOrCopy to set
     */
    public void setMoveOrCopy(int moveOrCopy) {
        this.moveOrCopy = moveOrCopy;
    }
}
