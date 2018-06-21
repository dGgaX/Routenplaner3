
package de.bring.treeTableRoute.gui.Renderer;

import de.bring.helfer.gui.JComboBoxUhrzeit;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Andreas
 */
public class MyTreeTableCellEditorUhrzeit  extends AbstractCellEditor implements TableCellEditor {
    
    JComboBoxUhrzeit component = new JComboBoxUhrzeit();

    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        if (value instanceof String)
            component.setZeit((String) value);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getZeit();
    }
    
}
