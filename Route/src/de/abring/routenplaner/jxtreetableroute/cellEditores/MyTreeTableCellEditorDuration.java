
package de.abring.routenplaner.jxtreetableroute.cellEditores;

import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.primitives.gui.JComboBoxDuration;
import de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorDuration  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxDuration component = new JComboBoxDuration();
    
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
