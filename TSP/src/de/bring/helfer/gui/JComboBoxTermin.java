/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Bring
 */
public class JComboBoxTermin extends JComboBox {
    JTextField text;
    JLabel label;
    private List<Preset> Presets = new ArrayList<Preset>();
    
    public JComboBoxTermin() {
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
    public void setTermin(String value) {
        String[] Zeiten = value.split("-");
        if (Zeiten.length == 2) {
            Preset neu = new Preset(Zeiten[0] + "-" + Zeiten[1], Zeiten[0], Zeiten[1]);
            super.addItem(neu);
            this.setSelectedItem(neu);
        }
    }
    public String getTermin() {
        if (super.getSelectedItem() instanceof Preset) {
            return ((Preset)super.getSelectedItem()).Beginn + "-" + ((Preset)super.getSelectedItem()).Ende;
        } else if (super.getSelectedItem() instanceof String) {
            return ((String)super.getSelectedItem());
        }
        return "10:00-18:00";
    }
    private void setPresets() {
        super.addItem(new Preset("Vormittag", "10:00", "15:00"));
        super.addItem(new Preset("früher Vormittag", "10:00", "12:00"));
        super.addItem(new Preset("später Vormittag", "12:00", "15:00"));
        super.addItem(new Preset("Nachmittag", "14:00", "18:00"));
        super.addItem(new Preset("früher Nachmittag", "14:00", "16:00"));
        super.addItem(new Preset("später Nachmittag", "16:00", "18:00"));
        super.addItem(new Preset("Ganztägig", "10:00", "18:00"));
    }
    @Override
    public String toString() {
        return text.getText();
    }
    
    
    public class Preset implements java.io.Serializable {
        public String Name;
        public String Beginn;
        public String Ende;
        public Preset(String Name, String Beginn, String Ende) {
            this.Name = Name;
            this.Beginn = Beginn;
            this.Ende = Ende;
        }
        @Override
        public String toString() {
            return this.Name;
        }
    }
}
