
package de.abring.routenplaner.jxtreetableroute.cellEditores;

import de.abring.helfer.gui.JTextFieldGoogle;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorGoogle  extends AbstractCellEditor implements TableCellEditor {
    
    JTextFieldGoogle component = new JTextFieldGoogle();

    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        component.setText(value);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getText();
    }
    
}
