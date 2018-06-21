/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.treeTableRoute.gui.Renderer;

import de.bring.treeTableRoute.entries.*;
import de.bring.treeTableRoute.gui.JXTreeTableRoute;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Handles drag & drop row reordering
 * @author Karima
 */

public class TableRowTransferHandlerAddress extends TransferHandler {
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(JXTreeTableRoute.class, DataFlavor.javaJVMLocalObjectMimeType, "TreeTableRow");
    private JXTreeTableRoute table = null;

    public TableRowTransferHandlerAddress(JXTreeTableRoute table) {
       this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
       assert (c == table);
       table.setFormerSelectedRow();
       return new DataHandler(table, localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (info.getComponent() instanceof JXTreeTableRoute) {
            JXTreeTableRoute target = (JXTreeTableRoute) info.getComponent();
            JXTreeTableRoute.DropLocation dl = (JXTreeTableRoute.DropLocation) info.getDropLocation();
            TreeEntry entry = table.getEntryAt(dl.getRow());
            if (entry instanceof TreeAddress) {
                boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
                table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
                return b;
            }
            
        }
        return false;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (info.getComponent() instanceof JXTreeTableRoute) {
            JXTreeTableRoute target = (JXTreeTableRoute) info.getComponent();
            JXTreeTableRoute.DropLocation dl = (JXTreeTableRoute.DropLocation) info.getDropLocation();
            int index = dl.getRow();
            int max = table.getModel().getRowCount();
            if (index < 0 || index > max)
               index = max;
            target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            try {
                if (info.isDataFlavorSupported(localObjectFlavor)) {
                    JXTreeTableRoute source = (JXTreeTableRoute) info.getTransferable().getTransferData(localObjectFlavor);
                    int rowFrom = source.getFormerSelectedRow();
                    if (target == source) {
                        if (rowFrom != -1 && rowFrom != index) {
                            //((Reorderable)table.getTreeTableModel()).reorder(rowFrom, index);
                            TreeAddress temp = (TreeAddress)table.getEntryAt(rowFrom);
                            target.removeEntry(rowFrom);
                            target.addEntry(index, temp);
                            if (index > rowFrom)
                                index--;
                            target.getSelectionModel().addSelectionInterval(index, index);
                            return true;
                        }
                    } else {
                        if (rowFrom != -1) {
                            TreeAddress temp = new TreeAddress((TreeAddress)table.getEntryAt(rowFrom));
                            target.addEntry(index, temp);
                            target.getSelectionModel().addSelectionInterval(index, index);
                            return true;
                            }
                        }
                    }
                } catch (UnsupportedFlavorException | IOException e) {
                }
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (act == TransferHandler.MOVE) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            table.refreshTable();
            table.updateUI();
            table.setRowSelectionInterval(table.getSelectedRow() - 1, table.getSelectedRow() - 1);
            table.refreshTable();
            table.updateUI();
        }

    }
}