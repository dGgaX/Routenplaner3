
package de.bring.helfer.gui;

import de.bring.helfer.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Andreas
 */
public class JComboBoxUhrzeit extends JComboBox {
    JTextField text;
    JLabel label;
    public JComboBoxUhrzeit() {
        super();
        setEditable(true);
        setPresets();
        JLabel label = ((JLabel)this.getRenderer());
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
    public void setZeit(String value) {
        Uhrzeit temp = new Uhrzeit(value);
            for (int i = 0; i < this.getItemCount(); i++) {
                Object entry = this.getItemAt(i);
                if (entry instanceof Preset) {
                    if (temp.toString().equals(((Preset)entry).getZeit())) {
                        this.setSelectedIndex(i);
                        return;
                    }
                        
                } else if (entry instanceof String) {
                    if (temp.toString().equals(((String)entry))) {
                        this.setSelectedIndex(i);
                        return;
                    }
                }
            }
            Preset neu = new Preset(temp.getDauerString(), temp.toString());
            super.addItem(neu);
            sortItems();
            
            this.setSelectedItem(neu);
    }
    public String getZeit() {
        if (super.getSelectedItem() instanceof Preset) {
            return ((Preset)super.getSelectedItem()).getZeit();
        } else if (super.getSelectedItem() instanceof String) {
            if (((String)super.getSelectedItem()).isEmpty())
                return "00:00";
            Uhrzeit temp = new Uhrzeit((String)super.getSelectedItem());
            for (int i = 0; i < this.getItemCount(); i++) {
                Object entry = this.getItemAt(i);
                if (entry instanceof Preset) {
                    if (temp.toString().equals(((Preset)entry).getZeit())) {
                        this.setSelectedIndex(i);
                        return temp.toString();
                    }
                        
                } else if (entry instanceof String) {
                    if (temp.toString().equals(((String)entry))) {
                        this.setSelectedIndex(i);
                        return temp.toString();
                    }
                }
            }
            Preset neu = new Preset(temp.getUhrzeitString(), temp.toString());
            super.addItem(neu);
            sortItems();
            
            this.setSelectedItem(neu);
            return ((Preset)super.getSelectedItem()).getZeit();
        } else {
            return "00:00";
        }
    }
    private void sortItems() {
        long i = ((Preset)this.getItemAt(0)).getMillis();
        for (int index = 1; index < this.getItemCount(); index++) {
            Object entry = this.getItemAt(index);
            if (entry instanceof Preset) {
                long j = ((Preset)entry).getMillis();
                if (i == j) {
                    this.removeItem(entry);
                    sortItems();
                    return;   
                }
                if (i > j) {
                    Object temp = entry;
                    this.removeItem(entry);
                    this.insertItemAt(temp, index-1);
                    sortItems();
                    return;
                }
                i = j;
            }
        }
    }
    
    private void setPresets() {
        super.addItem(new Preset("06:00 Uhr", "06:00"));
        super.addItem(new Preset("06:30 Uhr", "06:30"));
        super.addItem(new Preset("07:00 Uhr", "07:00"));
        super.addItem(new Preset("07:30 Uhr", "07:30"));
        super.addItem(new Preset("08:00 Uhr", "08:00"));
        super.addItem(new Preset("08:30 Uhr", "08:30"));
        super.addItem(new Preset("09:00 Uhr", "09:00"));
        super.addItem(new Preset("09:30 Uhr", "09:30"));
        super.addItem(new Preset("10:00 Uhr", "10:00"));
        super.addItem(new Preset("10:30 Uhr", "10:30"));
        super.addItem(new Preset("11:00 Uhr", "11:00"));
        super.addItem(new Preset("11:30 Uhr", "11:30"));
        super.addItem(new Preset("12:00 Uhr", "12:00"));
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
