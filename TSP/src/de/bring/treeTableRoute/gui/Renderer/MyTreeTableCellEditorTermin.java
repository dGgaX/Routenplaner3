
package de.bring.treeTableRoute.gui.Renderer;

import de.bring.helfer.gui.JComboBoxTermin;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorTermin  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxTermin component = new JComboBoxTermin();

    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        if (value instanceof String)
            component.setTermin((String) value);
        return component;
    }

    public Object getCellEditorValue() {
        return component.getTermin();
    }
    
}
