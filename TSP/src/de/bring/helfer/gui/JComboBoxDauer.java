
package de.bring.helfer.gui;

import de.bring.helfer.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Andreas
 */
public class JComboBoxDauer extends JComboBox {
    JTextField text;
    JLabel label;
    public JComboBoxDauer() {
        super();
        setEditable(true);
        setPresets();
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
            Preset neu = new Preset(temp.getDauerString(), temp.toString());
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
        super.addItem(new Preset("0 Min.", "00:00"));
        super.addItem(new Preset("5 Min.", "00:05"));
        super.addItem(new Preset("10 Min.", "00:10"));
        super.addItem(new Preset("15 Min.", "00:15"));
        super.addItem(new Preset("20 Min.", "00:20"));
        super.addItem(new Preset("25 Min.", "00:25"));
        super.addItem(new Preset("30 Min.", "00:30"));
        super.addItem(new Preset("35 Min.", "00:35"));
        super.addItem(new Preset("40 Min.", "00:40"));
        super.addItem(new Preset("45 Min.", "00:45"));
        super.addItem(new Preset("50 Min.", "00:50"));
        super.addItem(new Preset("55 Min.", "00:55"));
        super.addItem(new Preset("1 Std.", "01:00"));
        super.addItem(new Preset("1 1/4 Std.", "01:15"));
        super.addItem(new Preset("1 1/2 Std.", "01:30"));
        super.addItem(new Preset("1 3/4 Std.", "01:45"));
        super.addItem(new Preset("2 Std.", "02:00"));
        super.addItem(new Preset("3 Std.", "03:00"));
        super.addItem(new Preset("4 Std.", "04:00"));
        super.addItem(new Preset("5 Std.", "05:00"));
        super.addItem(new Preset("6 Std.", "06:00"));
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
