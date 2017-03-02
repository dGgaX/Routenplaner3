
package de.abring.routenplaner.jxtreetableroute.cellEditores;

import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.primitives.gui.JComboBoxTimeOfDay;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorTimeOfDay  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxTimeOfDay component = new JComboBoxTimeOfDay();

    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        if (value instanceof String)
            component.setZeit((String) value);
        else if (value instanceof TimeOfDay)
            component.setZeit((TimeOfDay) value);
        
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getZeit();
    }
    
}
