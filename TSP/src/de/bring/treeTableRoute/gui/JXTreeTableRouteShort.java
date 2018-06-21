/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.treeTableRoute.gui;

import de.bring.treeTableRoute.gui.Renderer.*;
import javax.swing.JLabel;

/**
 *
 * @author Karima
 */
public class JXTreeTableRouteShort extends JXTreeTableRoute {
    MyTreeTableCellEditorAddress cellEditorAddress = new MyTreeTableCellEditorAddress();
    MyTreeTableCellEditorDauer cellEditorDauer = new MyTreeTableCellEditorDauer();
    MyTreeTableCellEditorTermin cellEditorTermin = new MyTreeTableCellEditorTermin();
    MyTreeTableCellEditorUhrzeit cellEditorUhrzeit = new MyTreeTableCellEditorUhrzeit();
    
    public JXTreeTableRouteShort() {
        super();
        setTreeTableModel(new NoRootTreeTableModelRouteShort(getEntryList()));
        setColumnWidth(0, 60, false);
        setColumnWidth(1, 60, false);
        setColumnWidth(2, 60, false);
        setColumnWidth(3, 80, false);
        setColumnWidth(4, 200, true);
        setColumnAlignment(0, JLabel.RIGHT);
        setColumnAlignment(1, JLabel.RIGHT);
        setColumnAlignment(2, JLabel.RIGHT);
        setColumnAlignment(3, JLabel.RIGHT);
        cellEditorAddress.addCellEditorListener(new javax.swing.event.CellEditorListener() {
            @Override
            public void editingCanceled(javax.swing.event.ChangeEvent evt) {
            }
            @Override
            public void editingStopped(javax.swing.event.ChangeEvent evt) {
                cellEditorAddressEditingStopped(evt);
            }
        });
        cellEditorTermin.addCellEditorListener(new javax.swing.event.CellEditorListener() {
            @Override
            public void editingCanceled(javax.swing.event.ChangeEvent evt) {
            }
            @Override
            public void editingStopped(javax.swing.event.ChangeEvent evt) {
                cellEditorTerminEditingStopped(evt);
            }
        });
        cellEditorDauer.addCellEditorListener(new javax.swing.event.CellEditorListener() {
            @Override
            public void editingCanceled(javax.swing.event.ChangeEvent evt) {
            }
            @Override
            public void editingStopped(javax.swing.event.ChangeEvent evt) {
                cellEditorDauerEditingStopped(evt);
            }
        });
        cellEditorUhrzeit.addCellEditorListener(new javax.swing.event.CellEditorListener() {
            @Override
            public void editingCanceled(javax.swing.event.ChangeEvent evt) {
            }
            @Override
            public void editingStopped(javax.swing.event.ChangeEvent evt) {
                cellEditorUhrzeitEditingStopped(evt);
            }
        });
        getColumnModel().getColumn(1).setCellEditor(cellEditorUhrzeit);
        getColumnModel().getColumn(2).setCellEditor(cellEditorDauer);
        getColumnModel().getColumn(3).setCellEditor(cellEditorTermin);
        getColumnModel().getColumn(4).setCellEditor(cellEditorAddress);
    }
    
    
    private void cellEditorUhrzeitEditingStopped(javax.swing.event.ChangeEvent evt) {
        refreshTable();
        super.treeCellRenderer.updateUI();
    }

    private void cellEditorDauerEditingStopped(javax.swing.event.ChangeEvent evt) {
        refreshTable();
        super.treeCellRenderer.updateUI();
    }

    private void cellEditorTerminEditingStopped(javax.swing.event.ChangeEvent evt) {
        refreshTable();
        super.treeCellRenderer.updateUI();
    }

    private void cellEditorAddressEditingStopped(javax.swing.event.ChangeEvent evt) {
        refreshTable();
        super.treeCellRenderer.updateUI();
    }
}
