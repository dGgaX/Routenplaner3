/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.maproute;

import de.abring.helfer.webIO.loadRoute;
import java.awt.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Karima
 */
public class LookupRoute extends javax.swing.JDialog {
    
    private static final Logger LOG = LogManager.getLogger(LookupAddress.class.getName());
    private MapRoute mapRoute;
    
    /**
     * Searches the fastest Route between Start and End int the MapRoute-Object
     * @param parent
     * @param modal
     * @param mapRoute 
     */
    public LookupRoute(java.awt.Frame parent, boolean modal, MapRoute mapRoute) {
        super(parent, modal);
        this.mapRoute = mapRoute;
        initComponents();
    }
    
    /**
     * the MapRoute either calculated or not.
     * @return MapAddress
     */
    public MapRoute getMapRoute() {
        return this.mapRoute;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new org.jdesktop.swingx.JXBusyLabel();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/de/abring/helfer/maproute/images/iconOSM.png")).getImage());
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        label.setText("Ich schau mal, ob mir im Internet jemand die Route berechnen kann!");

        btnCancel.setText("Abbrechen");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
        label.setBusy(true);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mapRoute.setCalculated(false);
        LOG.warn("Search canceled ...");
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (mapRoute != null && mapRoute instanceof MapRoute) {
            mapRoute = loadRoute.search(mapRoute);
            if (mapRoute != null && mapRoute.getStartAddress().isValid() && mapRoute.getEndAddress().isValid())
                LOG.info("Route found and parsed ...");
        } else {
            LOG.error("No Valid Search-Parameter ...");
        }
        this.dispose();
    }//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private org.jdesktop.swingx.JXBusyLabel label;
    // End of variables declaration//GEN-END:variables
}
