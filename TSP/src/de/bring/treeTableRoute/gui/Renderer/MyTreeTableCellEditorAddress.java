/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.treeTableRoute.gui.Renderer;

import de.bring.helfer.gui.JComboBoxAddress;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Karima
 */
public class MyTreeTableCellEditorAddress  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxAddress component = new JComboBoxAddress();

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        component.setAddress(value);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getAddress();
    }
    
}
