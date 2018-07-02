/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.maproute;

import de.abring.helfer.webIO.loadAddress;
import java.awt.event.KeyEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 *
 * @author Karima
 */
public class SearchForAddress extends javax.swing.JDialog {
    
    private static final Logger LOG = LogManager.getLogger(SearchForAddress.class.getName());
    private MapAddress mapAddress;
    private MapMarkerDot dot;
    
    public SearchForAddress(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.mapAddress = new MapAddress();
        search();
    }
    
    public SearchForAddress(java.awt.Frame parent, boolean modal, String eingabe) {
        super(parent, modal);
        initComponents();
        this.mapAddress = new MapAddress(eingabe);
        search();
    }
    
    private void search() {
        if (jMapViewer.getMapMarkerList().size() > 0) {
            jMapViewer.getMapMarkerList().forEach((dot2) -> {
                jMapViewer.deleteMapMarker(dot2);
            });
        }
        if (mapAddress != null && mapAddress instanceof MapAddress && !mapAddress.getSuchString().isEmpty()) {
            mapAddress = loadAddress.search(mapAddress);
            if (mapAddress != null && mapAddress.isValid()) {
                LOG.info("Address found and parsed ...");
                this.txt_search.setText(mapAddress.getSuchString());
                this.lbl_Straße.setText(mapAddress.getStraße());
                this.lbl_HsNr.setText(mapAddress.getHsNr());
                this.lbl_PLZ.setText(mapAddress.getPLZ());
                this.lbl_Stadt.setText(mapAddress.getStadt());
                this.lbl_Land.setText(mapAddress.getLand());
                this.jMapViewer.addMapMarker(new MapMarkerDot(mapAddress.getLat(), mapAddress.getLon()));
                this.jMapViewer.setDisplayToFitMapMarkers();
            } else {
                LOG.warn("Address not found ...");
                this.txt_search.setText(mapAddress.getSuchString());
                this.lbl_Straße.setText("");
                this.lbl_HsNr.setText("");
                this.lbl_PLZ.setText("");
                this.lbl_Stadt.setText("");
                this.lbl_Land.setText("");
            }
        } else {
            LOG.error("No Valid Search-Parameter ...");
        }
    }
    
    public MapAddress getMapAddress() {
        return this.mapAddress;
    }
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_PLZ = new javax.swing.JLabel();
        lbl_HsNr = new javax.swing.JLabel();
        lbl_Straße = new javax.swing.JLabel();
        txt_search = new javax.swing.JTextField();
        lbl_Land = new javax.swing.JLabel();
        lbl_Stadt = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jMapViewer = new org.openstreetmap.gui.jmapviewer.JMapViewer();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/de/abring/helfer/maproute/images/iconOSM.png")).getImage());
        setName("dialogMapAdress"); // NOI18N

        lbl_PLZ.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_PLZ.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_HsNr.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_HsNr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Straße.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Straße.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchKeyPressed(evt);
            }
        });

        lbl_Land.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Land.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Stadt.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Stadt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Abbrechen");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel1.setText("F3 = neue Suche!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Land, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_search)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_Straße, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_HsNr, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lbl_PLZ, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_Stadt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addComponent(jMapViewer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Straße, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HsNr, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_PLZ, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Stadt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Land, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMapViewer, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyPressed
        if (mapAddress != null) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER && mapAddress.isValid()) {
                btnOKActionPerformed(null);
            } else if (evt.getKeyCode() == KeyEvent.VK_F3 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
                mapAddress.setSuchString(txt_search.getText());
                search();
            }
        }
    }//GEN-LAST:event_txt_searchKeyPressed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        LOG.warn("Search succeed ...");
        if (!mapAddress.isValid()) {
            mapAddress.setSuchString(txt_search.getText());
            search();
        }
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mapAddress.setStraße("");
        LOG.warn("Search canceled ...");
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private org.openstreetmap.gui.jmapviewer.JMapViewer jMapViewer;
    private javax.swing.JLabel lbl_HsNr;
    private javax.swing.JLabel lbl_Land;
    private javax.swing.JLabel lbl_PLZ;
    private javax.swing.JLabel lbl_Stadt;
    private javax.swing.JLabel lbl_Straße;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
