
package de.abring.helfer.primitives.gui;

import de.abring.helfer.primitives.TimeOfDay;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Andreas
 */
public class JComboBoxTimeOfDay extends JComboBox<TimeOfDay> {
    private static final long serialVersionUID = 1L;
    private JTextField text;
    private final JLabel label;
    
    public JComboBoxTimeOfDay() {
        super();
        setEditable(true);
        setPresets();
        label = ((JLabel)this.getRenderer());
        text = ((JTextField)this.getEditor().getEditorComponent());
        label.setHorizontalAlignment(JLabel.RIGHT);
        text.setHorizontalAlignment(JLabel.RIGHT);
        text.selectAll();
        
        this.addItemListener((java.awt.event.ItemEvent evt) -> {
            text.selectAll();
        });
    
    }
    
    public void setZeit(String value) {
        setZeit(new TimeOfDay(value));
    }
    
    public void setZeit(TimeOfDay value) {
        addItem(value);
        sortItems();
        setSelectedItem(value);
    }
        
    protected void sortItems() {
        TimeOfDay first = (TimeOfDay) this.getItemAt(0);
        for (int index = 1; index < this.getItemCount(); index++) {
            TimeOfDay second = this.getItemAt(index);
            if (first.getMillis() == second.getMillis()) {
                this.removeItem(second);
                sortItems();
                return;   
            }
            if (first.getMillis() > second.getMillis()) {
                TimeOfDay temp = second;
                this.removeItem(second);
                this.insertItemAt(temp, index-1);
                sortItems();
                return;
            }
            first = second;
        }
    }
    
    private void setPresets() {
        addItem(new TimeOfDay("06:00"));
        addItem(new TimeOfDay("06:30"));
        addItem(new TimeOfDay("07:00"));
        addItem(new TimeOfDay("07:30"));
        addItem(new TimeOfDay("08:00"));
        addItem(new TimeOfDay("08:30"));
        addItem(new TimeOfDay("09:00"));
        addItem(new TimeOfDay("09:30"));
        addItem(new TimeOfDay("10:00"));
        addItem(new TimeOfDay("10:30"));
        addItem(new TimeOfDay("11:00"));
        addItem(new TimeOfDay("11:30"));
        addItem(new TimeOfDay("12:00"));
        addItem(new TimeOfDay("12:30"));
        addItem(new TimeOfDay("13:00"));
        addItem(new TimeOfDay("13:30"));
        addItem(new TimeOfDay("14:00"));
        addItem(new TimeOfDay("14:30"));
        addItem(new TimeOfDay("15:00"));
        addItem(new TimeOfDay("15:30"));
        addItem(new TimeOfDay("16:00"));
        addItem(new TimeOfDay("16:30"));
        addItem(new TimeOfDay("17:00"));
        addItem(new TimeOfDay("17:30"));
        addItem(new TimeOfDay("18:00"));
        addItem(new TimeOfDay("18:30"));
        addItem(new TimeOfDay("19:00"));
        addItem(new TimeOfDay("19:30"));
    }
    
    public TimeOfDay getZeit() {
        if (!(getSelectedItem() instanceof TimeOfDay)) {
            Object o = getSelectedItem();
            removeItem(o);
            if (o instanceof String) {
                setZeit((String) o);
            }
        }
        return (TimeOfDay)getSelectedItem();
    }
    
    @Override
    public String toString() {
        return getSelectedItem().toString();
    }
    
}
