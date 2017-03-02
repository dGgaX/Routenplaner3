/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.maproute;

import de.abring.helfer.primitives.TimeOfDay;
import de.abring.helfer.webIO.loadAddress;
import de.abring.helfer.webIO.loadRoute;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openstreetmap.gui.jmapviewer.MapLinesDot;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

/**
 *
 * @author AndyB
 */
public class SearchForRoute extends javax.swing.JDialog {
    
    private static final Logger LOG = LogManager.getLogger(SearchForRoute.class.getName());
    private MapRoute mapRoute;
    private MapMarkerDot dot1;
    private MapMarkerDot dot2;
    private MapLinesDot line;
    
    public SearchForRoute(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.mapRoute = new MapRoute();
        search();
    }
    
    public SearchForRoute(Frame parent, boolean modal, MapAddress startAddress, MapAddress endAddress) {
        super(parent, modal);
        initComponents();
        this.mapRoute = new MapRoute(startAddress, endAddress);
        search();
    }
    
    public SearchForRoute(Frame parent, boolean modal, MapRoute mapRoute) {
        super(parent, modal);
        initComponents();
        this.mapRoute = mapRoute;
        search();
    }
    
    private void search() {
        if (jMapViewer.getMapMarkerList().size() > 0) {
            jMapViewer.getMapMarkerList().forEach((dot) -> {
                jMapViewer.deleteMapMarker(dot);
            });
        }
        if (jMapViewer.getMapLinesList().size() > 0) {
            jMapViewer.getMapLinesList().forEach((line2) -> {
                jMapViewer.deleteMapLines(line2);
            });
        }
        if (this.mapRoute.getStartAddress() == null && this.mapRoute.getEndAddress() == null)
            return;
        
        this.mapRoute.setStartAddress(loadAddress.search(this.mapRoute.getStartAddress()));
        this.mapRoute.setEndAddress(loadAddress.search(this.mapRoute.getEndAddress()));

        if (this.mapRoute.getStartAddress() != null && this.mapRoute.getStartAddress().isValid() && this.mapRoute.getEndAddress() != null && this.mapRoute.getEndAddress().isValid())
        this.mapRoute = loadRoute.search(this.mapRoute);
        if (mapRoute != null && this.mapRoute.getLänge() != 0) {
            showData();
        }
    }
    
    private void showData(){
        txtBeschreibung.setText(mapRoute.getBeschreibung());
        lbl_Zeit.setText(mapRoute.getDauer().getDurationString());
        lbl_dis.setText(mapRoute.getLänge() + " Kilometer");
        line = new MapLinesDot(mapRoute.getStartAddress().getLat(), mapRoute.getStartAddress().getLon(), mapRoute.getEndAddress().getLat(), mapRoute.getEndAddress().getLon());
        line.setCoordinates(mapRoute.getCoordinates());
        jMapViewer.addMapLines(line);
        setAddresses();
    }
    
    private void setAddresses() {
        if (mapRoute.getStartAddress() != null) { 
            LOG.info("Start-Address found and parsed ...");
            lbl_HsNr.setText(mapRoute.getStartAddress().getHsNr());
            lbl_Land.setText(mapRoute.getStartAddress().getLand());
            lbl_PLZ.setText(mapRoute.getStartAddress().getPLZ());
            lbl_Stadt.setText(mapRoute.getStartAddress().getStadt());
            lbl_Straße.setText(mapRoute.getStartAddress().getStraße());
            jMapViewer.addMapMarker(new MapMarkerDot(mapRoute.getStartAddress().getLat(), mapRoute.getStartAddress().getLon()));
        } else {
            LOG.warn("Start-Address not found ...");
            this.lbl_Straße.setText("");
            this.lbl_HsNr.setText("");
            this.lbl_PLZ.setText("");
            this.lbl_Stadt.setText("");
            this.lbl_Land.setText("");
        }
        if (mapRoute.getEndAddress() != null) {
            LOG.info("End-Address found and parsed ...");
            lbl_HsNr1.setText(mapRoute.getEndAddress().getHsNr());
            lbl_Land1.setText(mapRoute.getEndAddress().getLand());
            lbl_PLZ1.setText(mapRoute.getEndAddress().getPLZ());
            lbl_Stadt1.setText(mapRoute.getEndAddress().getStadt());
            lbl_Straße1.setText(mapRoute.getEndAddress().getStraße());
            jMapViewer.addMapMarker(new MapMarkerDot(mapRoute.getEndAddress().getLat(), mapRoute.getEndAddress().getLon()));
        } else {
            LOG.warn("Start-Address not found ...");
            this.lbl_Straße1.setText("");
            this.lbl_HsNr1.setText("");
            this.lbl_PLZ1.setText("");
            this.lbl_Stadt1.setText("");
            this.lbl_Land1.setText("");
        }
        jMapViewer.setDisplayToFitMapMarkers();
    }
    
    public MapRoute getMapRoute() {
        return mapRoute;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbl_PLZ = new javax.swing.JLabel();
        lbl_Land = new javax.swing.JLabel();
        lbl_HsNr = new javax.swing.JLabel();
        lbl_Straße = new javax.swing.JLabel();
        lbl_Stadt = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbl_PLZ1 = new javax.swing.JLabel();
        lbl_Land1 = new javax.swing.JLabel();
        lbl_HsNr1 = new javax.swing.JLabel();
        lbl_Straße1 = new javax.swing.JLabel();
        lbl_Stadt1 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMapViewer = new org.openstreetmap.gui.jmapviewer.JMapViewer();
        jPanel2 = new javax.swing.JPanel();
        lbl_Zeit = new javax.swing.JLabel();
        lbl_dis = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBeschreibung = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Route berechnen");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/de/abring/helfer/maproute/images/iconOSM.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Startadresse"));
        jPanel1.setName(""); // NOI18N

        lbl_PLZ.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_PLZ.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Land.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Land.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_HsNr.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_HsNr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Straße.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Straße.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Stadt.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Stadt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Land, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_Straße, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_HsNr, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_PLZ, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_Stadt, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Straße, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HsNr, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_PLZ, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Stadt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Land, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Endadresse"));
        jPanel3.setName(""); // NOI18N

        lbl_PLZ1.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_PLZ1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Land1.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Land1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_HsNr1.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_HsNr1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Straße1.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Straße1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_Stadt1.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Stadt1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_Land1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(lbl_Straße1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbl_HsNr1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(lbl_PLZ1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbl_Stadt1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbl_Straße1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_HsNr1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbl_PLZ1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_Stadt1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lbl_Land1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

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

        jTabbedPane1.addTab("Karte", jMapViewer);

        lbl_Zeit.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_Zeit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        lbl_dis.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        lbl_dis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel1.setText("Zeit:");

        jLabel2.setText("Distanz:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Zeit, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                    .addComponent(lbl_dis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_Zeit, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_dis, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(208, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Zeit & Distanz", jPanel2);

        jScrollPane1.setViewportView(txtBeschreibung);
        txtBeschreibung.setEditable(false);

        jTabbedPane1.addTab("Beschreibung", jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mapRoute = null;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (this.mapRoute.getStartAddress() == null) {
            this.mapRoute.setStartAddress(loadAddress.search(this.mapRoute.getStartAddress()));
        }
        if (this.mapRoute.getEndAddress() == null) {
            this.mapRoute.setEndAddress(loadAddress.search(this.mapRoute.getEndAddress()));
        }
        if (mapRoute == null)
            search();
    }//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private org.openstreetmap.gui.jmapviewer.JMapViewer jMapViewer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_HsNr;
    private javax.swing.JLabel lbl_HsNr1;
    private javax.swing.JLabel lbl_Land;
    private javax.swing.JLabel lbl_Land1;
    private javax.swing.JLabel lbl_PLZ;
    private javax.swing.JLabel lbl_PLZ1;
    private javax.swing.JLabel lbl_Stadt;
    private javax.swing.JLabel lbl_Stadt1;
    private javax.swing.JLabel lbl_Straße;
    private javax.swing.JLabel lbl_Straße1;
    private javax.swing.JLabel lbl_Zeit;
    private javax.swing.JLabel lbl_dis;
    private javax.swing.JTextPane txtBeschreibung;
    // End of variables declaration//GEN-END:variables
}
