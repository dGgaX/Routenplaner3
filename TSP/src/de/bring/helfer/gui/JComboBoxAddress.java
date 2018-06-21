
package de.bring.helfer.gui;

import de.bring.helfer.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import de.bring.helfer.gui.LookupAddress;
import de.bring.helfer.Address;

/**
 *
 * @author Andreas
 */
public class JComboBoxAddress extends JComboBox {
    JTextField text;
    JLabel label;
    public JComboBoxAddress() {
        super();
        setEditable(true);
        label = ((JLabel)this.getRenderer());
        text = ((JTextField)this.getEditor().getEditorComponent());
        label.setHorizontalAlignment(JLabel.RIGHT);
        text.setHorizontalAlignment(JLabel.RIGHT);
        text.selectAll();
        
        this.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                text.selectAll();
            }
        });
    
    }
    public void setAddress(Object value) {
        if (value instanceof Address) {
            Address temp = (Address)value;
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof Address) {
                    Address entry = (Address)this.getItemAt(i);
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
                sortItems();
                this.setSelectedItem(temp);
            }
        } else if (value instanceof String) {
            String temp = (String)value;
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof Address) {
                    Address entry = (Address)this.getItemAt(i);
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
            Address neu = lookup.getMapAddress();
            if (neu.isValid()) {
                addAddress(neu);
                sortItems();
                this.setSelectedItem(neu);
            }
        }
    }
    public Object getAddress() {
        if (super.getSelectedItem() == null)
            return "";
        if (super.getSelectedItem() instanceof String) {
            String temp = (String)super.getSelectedItem();
            for (int i = 0; i < this.getItemCount(); i++) {
                if (this.getItemAt(i) instanceof Address) {
                    Address entry = (Address)this.getItemAt(i);
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
            Address neu = lookup.getMapAddress();
            if (neu.isValid()) {
                addAddress(neu);
                sortItems();
                this.setSelectedItem(neu);
            }
        }
        return super.getSelectedItem();
    }
    private void sortItems() {
//        Address start = (Address)this.getItemAt(0);
//        String i = ((Address)this.getItemAt(0));
//        for (int index = 1; index < this.getItemCount(); index++) {
//            Object entry = this.getItemAt(index);
//            if (entry instanceof Address) {
//                long j = ((Address)entry).getMillis();
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
    }
    
    private void addAddress(Address address) {
        super.addItem(address);
    }
    public class Preset implements java.io.Serializable {
        public String Name;
        public String Zeit;
        public Preset(String Name, String Zeit) {
            this.Name = Name;
            this.Zeit = Zeit;
        }
        public String getName() {
            return this.Name;
        }
        public void setName(String Name) {
            this.Name=Name;
        }

        public String getZeit() {
            return this.Zeit;
        }
        public void setZeit(String Zeit) {
            this.Zeit=Zeit;
        }
        public long getMillis() {
            Uhrzeit temp = new Uhrzeit(this.Zeit);
            return temp.getMillis();
        }
        @Override
        public String toString() {
            return this.Name;
        }
    }
}
