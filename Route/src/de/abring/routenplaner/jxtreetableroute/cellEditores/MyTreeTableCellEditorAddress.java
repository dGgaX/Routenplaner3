
package de.abring.routenplaner.jxtreetableroute.cellEditores;

import de.abring.helfer.gui.*;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
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
