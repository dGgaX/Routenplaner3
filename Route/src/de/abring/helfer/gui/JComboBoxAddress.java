
package de.abring.helfer.gui;

import de.abring.helfer.primitives.TimeOfDay;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import de.abring.helfer.maproute.LookupAddress;
import de.abring.helfer.maproute.MapAddress;

/**
 *
 * @author Andreas
 */
public class JComboBoxAddress extends JComboBox<Object> {
    private static final long serialVersionUID = 1L;
    private JTextField text;
    private final JLabel label;
    private MapAddress oldAddress = null;
    private MapAddress newAddress = null;

    public JComboBoxAddress() {
        super();
        setEditable(true);
        label = ((JLabel)this.getRenderer());
        text = ((JTextField)this.getEditor().getEditorComponent());
        label.setHorizontalAlignment(JLabel.RIGHT);
        text.setHorizontalAlignment(JLabel.RIGHT);
        text.selectAll();
        
        this.addItemListener((java.awt.event.ItemEvent evt) -> {
            text.selectAll();
        });
    
    }
    public void setAddress(Object value) {
        if (value instanceof MapAddress) {
            MapAddress temp = (MapAddress)value;
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof MapAddress) {
                    MapAddress entry = (MapAddress)this.getItemAt(i);
                    if (temp.toString().equals(entry.toString())) {
                        this.setSelectedIndex(i);
                        return;
                    }
                } else if (this.getItemAt(i) instanceof String) {
                    String entry = (String)this.getItemAt(i);
                    if (temp.toString().equals(entry)) {
                        this.setSelectedIndex(i);
                        return;
                    }
                }
            }
            if (temp.isValid()) {
                addAddress(temp);
//                sortItems();
                this.setSelectedItem(temp);
            }
        } else if (value instanceof String) {
            String temp = (String)value;
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof MapAddress) {
                    MapAddress entry = (MapAddress)this.getItemAt(i);
                    if (temp.equals(entry.toString())) {
                        this.setSelectedIndex(i);
                        return;
                    }
                } else if (this.getItemAt(i) instanceof String) {
                    String entry = (String)this.getItemAt(i);
                    if (temp.equals(entry)) {
                        this.setSelectedIndex(i);
                        return;
                    }
                }
            }
            LookupAddress lookup = new LookupAddress(null, true, temp);
            lookup.setVisible(true);
            MapAddress neu = lookup.getMapAddress();
            if (neu.isValid()) {
                addAddress(neu);
//                sortItems();
                this.setSelectedItem(neu);
            }
        }
    }
    
    public Object getAddress() {
        if (super.getSelectedItem() == null) {
            return "";
        }
        if (super.getSelectedItem() instanceof String) {
            String temp = (String)super.getSelectedItem();
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof MapAddress) {
                    MapAddress entry = (MapAddress)this.getItemAt(i);
                    if (temp.equals(entry.toString())) {
                        this.setSelectedIndex(i);
                        return super.getSelectedItem();
                    }
                } else if (this.getItemAt(i) instanceof String) {
                    String entry = (String)this.getItemAt(i);
                    if (temp.equals(entry)) {
                        this.setSelectedIndex(i);
                        return super.getSelectedItem();
                    }
                }
            }
            LookupAddress lookup = new LookupAddress(null, true, temp);
            lookup.setVisible(true);
            MapAddress neu = lookup.getMapAddress();
            if (neu.isValid()) {
                addAddress(neu);
//                sortItems();
                this.setSelectedItem(neu);
            }
        }
        return super.getSelectedItem();
    }
//    private void sortItems() {
//        MapAddress start = (MapAddress)this.getItemAt(0);
//        String i = ((MapAddress)this.getItemAt(0));
//        for (int index = 1; index < this.getItemCount(); index++) {
//            Object entry = this.getItemAt(index);
//            if (entry instanceof MapAddress) {
//                long j = ((MapAddress)entry).getMillis();
//                if (i == j) {
//                    this.removeItem(entry);
//                    sortItems();
//                    return;   
//                }
//                if (i > j) {
//                    Object temp = entry;
//                    this.removeItem(entry);
//                    this.insertItemAt(temp, index-1);
//                    sortItems();
//                    return;
//                }
//                i = j;
//            }
//        }
//    }
    
    private void addAddress(MapAddress address) {
        addItem(address);
    }

    /**
     * @return the oldAddress
     */
    public MapAddress getOldAddress() {
        return oldAddress;
    }

    /**
     * @return the newAddress
     */
    public MapAddress getNewAddress() {
        return newAddress;
    }
}
