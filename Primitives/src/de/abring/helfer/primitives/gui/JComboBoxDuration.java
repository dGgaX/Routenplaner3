
package de.abring.helfer.primitives.gui;

import de.abring.helfer.primitives.TimeOfDay;

/**
 *
 * @author Andreas
 */
public class JComboBoxDuration extends JComboBoxTimeOfDay {
    private static final long serialVersionUID = 1L;
    
    public JComboBoxDuration() {
        super();
        this.removeAllItems();
        setPresets();
    }
    
    private void setPresets() {
        addItem(new TimeOfDay("00:00"));
        addItem(new TimeOfDay("00:05"));
        addItem(new TimeOfDay("00:10"));
        addItem(new TimeOfDay("00:15"));
        addItem(new TimeOfDay("00:20"));
        addItem(new TimeOfDay("00:25"));
        addItem(new TimeOfDay("00:30"));
        addItem(new TimeOfDay("00:35"));
        addItem(new TimeOfDay("00:40"));
        addItem(new TimeOfDay("00:45"));
        addItem(new TimeOfDay("00:50"));
        addItem(new TimeOfDay("00:55"));
        addItem(new TimeOfDay("01:00"));
        addItem(new TimeOfDay("01:15"));
        addItem(new TimeOfDay("01:30"));
        addItem(new TimeOfDay("01:45"));
        addItem(new TimeOfDay("02:00"));
        addItem(new TimeOfDay("03:00"));
        addItem(new TimeOfDay("04:00"));
        addItem(new TimeOfDay("05:00"));
        addItem(new TimeOfDay("06:00"));
        addItem(new TimeOfDay("07:00"));
        addItem(new TimeOfDay("08:00"));
    }
}
