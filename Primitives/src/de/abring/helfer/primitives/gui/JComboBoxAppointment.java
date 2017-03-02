/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.primitives.gui;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.stringUtils.StringUtils;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Bring
 */
public class JComboBoxAppointment extends JComboBox<Appointment> {
    private static final long serialVersionUID = 1L;
    private JTextField text;
    private JLabel label;
    
    public JComboBoxAppointment() {
        super();
        setEditable(true);
        setAppointments();
        label = ((JLabel)this.getRenderer());
        text = ((JTextField)this.getEditor().getEditorComponent());
        label.setHorizontalAlignment(JLabel.RIGHT);
        text.setHorizontalAlignment(JLabel.RIGHT);
        text.selectAll();
        
        this.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                text.selectAll();
            }
        });
    }
    
    public void setTermin(String value) {
        String[] times = value.split("-");
        if (times.length == 2) {
            Appointment newAppointment = new Appointment();
            newAppointment.setName(times[0] + "-" + times[1]);
            newAppointment.getStart().setTime(times[0]);
            newAppointment.getEnd().setTime(times[1]);
            this.setTermin(newAppointment);
        } else {
            double genauigkeit = 0.75d;
            double bestMatchingIndex = 0;
            for(int i = 0; i < this.getItemCount(); i++) {
                Appointment appointment = this.getItemAt(i);
                double matchingIndex = StringUtils.bestMatch(appointment.getName(), value);
                if(matchingIndex > genauigkeit && matchingIndex > bestMatchingIndex) {
                    this.setTermin(appointment);
                    bestMatchingIndex = matchingIndex;
                }
            }
        }
    }
    
    public void setTermin(Appointment newAppointment) {
        ComboBoxModel model = this.getModel();
            int size = model.getSize();
            for(int i=0;i<size;i++) {
                Appointment appointment = (Appointment) model.getElementAt(i);
                if (newAppointment.getStart().getMillis() == appointment.getStart().getMillis() && newAppointment.getEnd().getMillis() == appointment.getEnd().getMillis()) {
                    this.setSelectedItem(appointment);
                    return;
                }
            }
        this.addItem(newAppointment);
        this.sortItems();
        this.setSelectedItem(newAppointment);
    }
    
    public String getTermin() {
        return ((Appointment) this.getSelectedItem()).toString();
    }
    public Appointment getAppointment() {
        return ((Appointment) this.getSelectedItem());
    }
    
    
    protected void sortItems() {
        Appointment first = ((Appointment) this.getItemAt(0));
        for (int index = 1; index < this.getItemCount(); index++) {
            Appointment second = (Appointment) this.getItemAt(index);
            
            if (first.getStart().getMillis() == second.getStart().getMillis()) {
                if (first.getEnd().getMillis() == second.getEnd().getMillis()) {
                    this.removeItem(second);
                    sortItems();
                    return;
                }
                if (first.getEnd().getMillis() > second.getEnd().getMillis()) {
                   Appointment temp = second;
                    this.removeItem(second);
                    this.insertItemAt(temp, index-1);

                    sortItems();
                    return; 
                }
                this.removeItem(second);
                sortItems();
                return;   
            }
            if (first.getStart().getMillis() > second.getStart().getMillis()) {
                Appointment temp = second;
                this.removeItem(second);
                this.insertItemAt(temp, index-1);
                
                sortItems();
                return;
            }
            first = second;
        }
    }
    
    private void setAppointments() {
        addItem(new Appointment("Vormittag", "10:00", "15:00"));
        addItem(new Appointment("früher Vormittag", "10:00", "12:00"));
        addItem(new Appointment("später Vormittag", "12:00", "14:00"));
        addItem(new Appointment("Nachmittag", "14:00", "18:00"));
        addItem(new Appointment("früher Nachmittag", "14:00", "16:00"));
        addItem(new Appointment("später Nachmittag", "16:00", "18:00"));
        addItem(new Appointment("Öffnungszeit", "09:00", "18:00"));
        addItem(new Appointment("Ganztägig", "10:00", "18:00"));
    }
    
    @Override
    public String toString() {
        return text.getText();
    }
}
