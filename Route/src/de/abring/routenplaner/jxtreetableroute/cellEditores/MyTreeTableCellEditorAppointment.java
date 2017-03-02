
package de.abring.routenplaner.jxtreetableroute.cellEditores;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.gui.JComboBoxAppointment;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorAppointment  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxAppointment component = new JComboBoxAppointment();

    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        if (value instanceof Appointment)
            component.setTermin((Appointment) value);
        if (value instanceof String)
            component.setTermin((String) value);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getAppointment();
    }
    
}
