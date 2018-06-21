/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer.gui;

import de.bring.helfer.Route;
import de.bring.helfer.Address;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Karima
 */
public class LookupRoute extends javax.swing.JDialog {
    private LookupRoute frame = this;
    private Route mapRoute;
    public LookupRoute(java.awt.Frame parent, boolean modal, Route mapRoute) {
        super(parent, modal);
        this.mapRoute = mapRoute;
        initComponents();
        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker()); 
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter()); 
        label.setBusy(true); 
    }
    private final Thread worker = new Thread(
        new Runnable() {
            @Override
            public void run() {
                boolean run = true;
                int sec = 0;
                while(run) {
                    try {
                        Thread.sleep(100);
                        sec++;
                    } catch (InterruptedException e) {
                        run = YN_Route(mapRoute.getStartAddress(), mapRoute.getEndAddress());
                    }
                    if (sec > 600) {
                        run = false;
                        Logger.getLogger(LookupRoute.class.getName()).log(Level.INFO, "Server timed out.");
                        btnCancelActionPerformed(null);
                    }
                }
                frame.dispose();
            }
            public boolean YN_Route (Address start, Address ziel) {
                if (mapRoute.getStartAddress().isValid() && mapRoute.getEndAddress().isValid()) {
                    JSONObject Results = Load_YN_Route(mapRoute.getStartAddress(), mapRoute.getEndAddress());
                    if ((Results != null) && Results.has("properties") && Results.has("coordinates")){
                        JSONArray coordinates = Results.getJSONArray("coordinates");
                        double distance = Results.getJSONObject("properties").getDouble("distance");
                        int traveltime = Results.getJSONObject("properties").getInt("traveltime");
                        String description = Results.getJSONObject("properties").getString("description");
                        mapRoute.setCoordinates(coordinates);
                        mapRoute.setLänge(distance);
                        mapRoute.setZeit(traveltime);
                        mapRoute.setBeschreibung(description);
                        return false;
                    }
                }
                return true;
            }
            public JSONObject Load_YN_Route(Address start, Address ziel) {
                double slat = start.getLat();
                double slon = start.getLon();
                double zlat = ziel.getLat();
                double zlon = ziel.getLon();
                String IP = new String();
                String URL_String = "http://www.yournavigation.org/api/1.0/gosmore.php?format=geojson&flat=" + String.valueOf(slat) + "&flon=" + String.valueOf(slon) + "&tlat=" + String.valueOf(zlat) + "&tlon=" + String.valueOf(zlon) + "&v=motorcar&fast=1&layer=mapnik&instructions=1";
                try {
                    URL myURL= new URL(URL_String);
                    Reader is = new InputStreamReader( myURL.openStream() );
                    BufferedReader in = new BufferedReader( is );
                    for ( String s; ( s = in.readLine() ) != null; ) {
                        IP += s;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LookupRoute.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
                if (!IP.isEmpty()) {
                    return new JSONObject(IP);
                }
                return null;
            }
        }
    );
    public Route getMapRoute() {
        return this.mapRoute;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new org.jdesktop.swingx.JXBusyLabel();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/org/openstreetmap/gui/jmapviewer/images/iconOSM.png")).getImage());
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

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mapRoute.setCalculated(false);
        frame.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (mapRoute instanceof Route) {
            System.out.println("berechne Route\nvon:  [ " + String.valueOf(mapRoute.getStartAddress().getLat()) + ", " + String.valueOf(mapRoute.getStartAddress().getLon()) + " ]");
            System.out.println("nach: [ " + String.valueOf(mapRoute.getEndAddress().getLat()) + ", " + String.valueOf(mapRoute.getEndAddress().getLon()) + " ]");
            worker.start();
            worker.interrupt();
        } else {
            frame.dispose();
        }
    }//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private org.jdesktop.swingx.JXBusyLabel label;
    // End of variables declaration//GEN-END:variables
}
