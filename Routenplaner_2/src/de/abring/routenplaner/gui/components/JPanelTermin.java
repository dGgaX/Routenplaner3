/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.abring.routenplaner.gui.components;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;

/**
 *
 * @author Karima
 */
public class JPanelTermin extends javax.swing.JPanel {
    
    private boolean editable = true;
    
    public JPanelTermin() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxTimeOfDayStart = new de.abring.helfer.primitives.gui.JComboBoxTimeOfDay();
        jComboBoxTimeOfDayEnd = new de.abring.helfer.primitives.gui.JComboBoxTimeOfDay();
        jComboBoxAppointment = new de.abring.helfer.primitives.gui.JComboBoxAppointment();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Termin"));

        jComboBoxTimeOfDayStart.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTimeOfDayStartItemStateChanged(evt);
            }
        });
        jComboBoxTimeOfDayStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTimeOfDayStartActionPerformed(evt);
            }
        });

        jComboBoxTimeOfDayEnd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTimeOfDayEndItemStateChanged(evt);
            }
        });
        jComboBoxTimeOfDayEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTimeOfDayEndActionPerformed(evt);
            }
        });

        jComboBoxAppointment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxAppointmentItemStateChanged(evt);
            }
        });
        jComboBoxAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAppointmentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 178, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBoxTimeOfDayStart, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jComboBoxTimeOfDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jComboBoxAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxTimeOfDayStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTimeOfDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxTerminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTerminActionPerformed
        
    }//GEN-LAST:event_jComboBoxTerminActionPerformed

    private void jComboBoxUhrzeitVonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUhrzeitVonActionPerformed
        
    }//GEN-LAST:event_jComboBoxUhrzeitVonActionPerformed

    private void jComboBoxUhrzeitBisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUhrzeitBisActionPerformed
        
    }//GEN-LAST:event_jComboBoxUhrzeitBisActionPerformed

    private void jComboBoxAppointmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxAppointmentItemStateChanged
        
    }//GEN-LAST:event_jComboBoxAppointmentItemStateChanged

    private void jComboBoxTimeOfDayStartItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTimeOfDayStartItemStateChanged
    }//GEN-LAST:event_jComboBoxTimeOfDayStartItemStateChanged

    private void jComboBoxTimeOfDayEndItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTimeOfDayEndItemStateChanged

    }//GEN-LAST:event_jComboBoxTimeOfDayEndItemStateChanged

    private void jComboBoxAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAppointmentActionPerformed
        if (editable) {
            editable = false;
            Appointment appointment = this.jComboBoxAppointment.getAppointment();
            this.jComboBoxTimeOfDayStart.setZeit(appointment.getStart());
            this.jComboBoxTimeOfDayEnd.setZeit(appointment.getEnd());
            editable = true;
        }
    }//GEN-LAST:event_jComboBoxAppointmentActionPerformed

    private void jComboBoxTimeOfDayStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTimeOfDayStartActionPerformed
        if (editable) {
            editable = false;
            TimeOfDay start = this.jComboBoxTimeOfDayStart.getZeit();
            TimeOfDay end = this.jComboBoxAppointment.getAppointment().getEnd();
            this.jComboBoxAppointment.setTermin(new Appointment(start, end));
            editable = true;
        }
    }//GEN-LAST:event_jComboBoxTimeOfDayStartActionPerformed

    private void jComboBoxTimeOfDayEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTimeOfDayEndActionPerformed
        if (editable) {
            editable = false;
            TimeOfDay start = this.jComboBoxAppointment.getAppointment().getStart();
            TimeOfDay end = this.jComboBoxTimeOfDayEnd.getZeit();
            this.jComboBoxAppointment.setTermin(new Appointment(start, end));
            editable = true;
        }
    }//GEN-LAST:event_jComboBoxTimeOfDayEndActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.abring.helfer.primitives.gui.JComboBoxAppointment jComboBoxAppointment;
    private de.abring.helfer.primitives.gui.JComboBoxTimeOfDay jComboBoxTimeOfDayEnd;
    private de.abring.helfer.primitives.gui.JComboBoxTimeOfDay jComboBoxTimeOfDayStart;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the termin
     */
    public Appointment getTermin() {
        return this.jComboBoxAppointment.getAppointment();
    }

    /**
     * @param termin the termin to set
     */
    public void setTermin(Appointment termin) {
        this.jComboBoxAppointment.setTermin(termin);
        this.jComboBoxTimeOfDayStart.setZeit(termin.getStart());
        this.jComboBoxTimeOfDayEnd.setZeit(termin.getEnd());
    }

}
