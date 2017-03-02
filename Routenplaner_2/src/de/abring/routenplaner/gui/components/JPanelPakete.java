/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui.components;

import de.abring.helfer.primitives.TimeOfDay;

/**
 *
 * @author Karima
 */
public class JPanelPakete extends javax.swing.JPanel {
    
    private boolean editable = true;
    private int minuten;
    /**
     * @return the jSpinnerK
     */
    public javax.swing.JSpinner getjSpinnerK() {
        return jSpinnerK;
    }

    /**
     * @param jSpinnerK the jSpinnerK to set
     */
    public void setjSpinnerK(javax.swing.JSpinner jSpinnerK) {
        this.jSpinnerK = jSpinnerK;
    }

    /**
     * @return the jSpinnerP
     */
    public javax.swing.JSpinner getjSpinnerP() {
        return jSpinnerP;
    }

    /**
     * @param jSpinnerP the jSpinnerP to set
     */
    public void setjSpinnerP(javax.swing.JSpinner jSpinnerP) {
        this.jSpinnerP = jSpinnerP;
    }

    /**
     * @return the jSpinnerS
     */
    public javax.swing.JSpinner getjSpinnerS() {
        return jSpinnerS;
    }

    /**
     * @param jSpinnerS the jSpinnerS to set
     */
    public void setjSpinnerS(javax.swing.JSpinner jSpinnerS) {
        this.jSpinnerS = jSpinnerS;
    }

    /**
     * @return the jComboBoxDauer
     */
    public TimeOfDay getZeit() {
        return jComboBoxDuration.getZeit();
    }

    /**
     * @param zeit
     */
    public void setZeit(TimeOfDay zeit) {
        this.jComboBoxDuration.setZeit(zeit);
    }
    
    /**
     * Creates new form JPanelPakete
     */
    public JPanelPakete() {
        initComponents();
        minuten = (int) this.jSpinnerS.getValue() * 5;
        minuten += (int) this.jSpinnerK.getValue() * 20;
        minuten += (int) this.jSpinnerP.getValue() * 40;
        
    }
    
    public void increaseStandart() {
        this.getjSpinnerS().setValue((int) this.getjSpinnerS().getValue() + 1);
        calcMinutes();
    }
    public void increaseKomfort() {
        this.getjSpinnerK().setValue((int) this.getjSpinnerK().getValue() + 1);
        calcMinutes();
    }
    public void increasePremium() {
        this.getjSpinnerP().setValue((int) this.getjSpinnerP().getValue() + 1);
        calcMinutes();
    }
    
    private void calcMinutes() {
        int neuMinuten = (int) this.getjSpinnerS().getValue() * 5;
        neuMinuten += (int) this.getjSpinnerK().getValue() * 20;
        neuMinuten += (int) this.getjSpinnerP().getValue() * 40;
        TimeOfDay temp = new TimeOfDay(this.jComboBoxDuration.getZeit());

        temp.addier(new TimeOfDay((neuMinuten - minuten) * 60));
        minuten = neuMinuten;
        this.jComboBoxDuration.setZeit(temp);
        
        this.jComboBoxDuration.updateUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelS = new javax.swing.JLabel();
        jLabelK = new javax.swing.JLabel();
        jLabelP = new javax.swing.JLabel();
        jSpinnerS = new javax.swing.JSpinner();
        jSpinnerK = new javax.swing.JSpinner();
        jSpinnerP = new javax.swing.JSpinner();
        jComboBoxDuration = new de.abring.helfer.primitives.gui.JComboBoxDuration();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Pakete"));

        jLabelS.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelS.setLabelFor(jSpinnerS);
        jLabelS.setText("S");

        jLabelK.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelK.setLabelFor(jSpinnerK);
        jLabelK.setText("K");

        jLabelP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelP.setLabelFor(jSpinnerP);
        jLabelP.setText("P");

        jSpinnerS.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerSStateChanged(evt);
            }
        });

        jSpinnerK.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerKStateChanged(evt);
            }
        });

        jSpinnerP.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerPStateChanged(evt);
            }
        });

        jComboBoxDuration.setMinimumSize(new java.awt.Dimension(90, 20));
        jComboBoxDuration.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxDurationItemStateChanged(evt);
            }
        });
        jComboBoxDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDurationActionPerformed(evt);
            }
        });
        jComboBoxDuration.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBoxDurationPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabelK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabelP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSpinnerS, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                        .addGap(5, 5, 5)
                        .addComponent(jSpinnerK, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(jSpinnerP, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                    .addComponent(jComboBoxDuration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelS)
                    .addComponent(jLabelK)
                    .addComponent(jLabelP))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinnerS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jComboBoxDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerSStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerSStateChanged
        if (editable) {
            editable = false;
            calcMinutes();
            editable = true;
        }
    }//GEN-LAST:event_jSpinnerSStateChanged

    private void jSpinnerKStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerKStateChanged
        if (editable) {
            editable = false;
            calcMinutes();
            editable = true;
        }
    }//GEN-LAST:event_jSpinnerKStateChanged

    private void jSpinnerPStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerPStateChanged
        if (editable) {
            editable = false;
            calcMinutes();
            editable = true;
        }
    }//GEN-LAST:event_jSpinnerPStateChanged

    private void jComboBoxDurationPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBoxDurationPropertyChange
        
    }//GEN-LAST:event_jComboBoxDurationPropertyChange

    private void jComboBoxDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDurationActionPerformed
        if (editable) {
            editable = false;
            calcMinutes();
            editable = true;
        }
    }//GEN-LAST:event_jComboBoxDurationActionPerformed

    private void jComboBoxDurationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxDurationItemStateChanged
        
    }//GEN-LAST:event_jComboBoxDurationItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.abring.helfer.primitives.gui.JComboBoxDuration jComboBoxDuration;
    private javax.swing.JLabel jLabelK;
    private javax.swing.JLabel jLabelP;
    private javax.swing.JLabel jLabelS;
    private javax.swing.JSpinner jSpinnerK;
    private javax.swing.JSpinner jSpinnerP;
    private javax.swing.JSpinner jSpinnerS;
    // End of variables declaration//GEN-END:variables
}
