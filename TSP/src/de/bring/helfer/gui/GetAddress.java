/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer.gui;

import de.bring.helfer.Address;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.CaretEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

/**
 *
 * @author Karima
 */
public class GetAddress extends javax.swing.JDialog {
    private Address mapAddress = null;
    private MapMarkerDot dot = new MapMarkerDot(50.7848552, 6.05316197193059);
    private Boolean runWorker= true;
    
    public GetAddress(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public GetAddress(java.awt.Frame parent, boolean modal, String text) {
        super(parent, modal);
        initComponents();
        this.txt_search.setText(text);
        
    }
    
    private final Thread worker = new Thread(new Runnable() {
        @Override
        public void run() {
            while(runWorker) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    if (txt_search.getText().length() >= 3) {
                        mapAddress = OSM_Address(txt_search.getText().toLowerCase());
                        if (mapAddress == null) {
                            mapAddress = Google_Address(txt_search.getText().toLowerCase());
                        }
                        if (mapAddress != null) {
                            jMapViewer1.deleteMapMarker(dot);
                            dot = new MapMarkerDot(mapAddress.getLat(), mapAddress.getLon());
                            jMapViewer1.addMapMarker(dot);
                            jMapViewer1.setDisplayToFitMapMarkers();
                            lbl_Straße.setText(mapAddress.getStraße());
                            lbl_HsNr.setText(mapAddress.getHsNr());
                            lbl_PLZ.setText(mapAddress.getPLZ());
                            lbl_Stadt.setText(mapAddress.getStadt());
                            lbl_Land.setText(mapAddress.getLand());
                            mapAddress.setSuchString(txt_search.getText());
                        }
                    }
                }
            }
        }
        
        public Address OSM_Address (String Search_Obj) {
            JSONArray Results = Load_OSM_Address(Search_Obj);
            if ((Results != null) && !Results.isNull(0)){
                JSONObject Address = Results.getJSONObject(0).getJSONObject("address");
                if (!(Address.has("road") && Address.has("house_number") && Address.has("postcode") && Address.has("city") && Address.has("city_district") && Address.has("country"))) {
                    return null;
                }
                String AddressString = "";
                if (Address.has("road")) {
                    AddressString += Address.getString("road");
                }
                if (Address.has("house_number")) {
                    if (Address.has("road")) {
                        AddressString += " ";
                    }
                    AddressString += Address.getString("house_number") + ",";
                }
                if (Address.has("postcode")) {
                    if (Address.has("road") || Address.has("house_number")) {
                        AddressString += " ";
                    }
                    AddressString += Address.getString("postcode");
                }
                String Stadtname = "";
                if (Address.has("city")) {
                    if (Address.has("road") || Address.has("house_number") || Address.has("postcode")) {
                        AddressString += " ";
                    }
                    Stadtname += Address.getString("city");
                    if (Address.has("city_district")) {
                        Stadtname += " - " + Address.getString("city_district");
                    }
                }
                AddressString += Stadtname;
                if (Address.has("country")) {
                    if (Address.has("road") || Address.has("house_number") || Address.has("postcode") || Address.has("city")) {
                        AddressString += ", ";
                    }
                    AddressString += Address.getString("country");
                }
                Double DLat = Double.parseDouble(Results.getJSONObject(0).getString("lat"));
                Double DLon = Double.parseDouble(Results.getJSONObject(0).getString("lon"));
                
                return new Address(Address.getString("road"), Address.getString("house_number"), Address.getString("postcode"), Stadtname, Address.getString("country"), DLat, DLon);
            }
            return null;
        }
        public JSONArray Load_OSM_Address(String Search_Obj) {
            String IP = new String();
            String URL_String = new String();
            try {
                URL_String = "http://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(Search_Obj, "UTF-8") + "&bounded=0&format=json&limit=1&addressdetails=1&email=andreas.bring@rwth-aachen.de&countrycodes=de,nl,be,lu";
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GetAddress.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            try {
                URL myURL= new URL(URL_String);
                Reader is = new InputStreamReader( myURL.openStream() );
                BufferedReader in = new BufferedReader( is );
                for ( String s; ( s = in.readLine() ) != null; ) {
                    IP += s;
                }
            } catch (IOException ex) {
                Logger.getLogger(GetAddress.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            if (!IP.isEmpty()) {
                return new JSONArray(IP);
            }
            return null;
        }
        public Address Google_Address (String Search_Obj) {
            JSONArray Results = Load_Google_Address(Search_Obj).getJSONArray("results");
            if ((Results != null) && !Results.isNull(0)) {
                String AddressString = "";
                JSONArray Address = Results.getJSONObject(0).getJSONArray("address_components");
                String road = "";
                String house_number = "";
                String postcode = "";
                String city = "";
                String city_district = "";
                String country = "";
                
                for (int i = 0; i < Address.length(); i++) {
                    JSONObject AddressPart = Address.getJSONObject(i);
                    JSONArray AddressPartTypes = AddressPart.getJSONArray("types");
                    String AddressPartLongName = AddressPart.getString("long_name");
                    if (AddressPartTypes.getString(0).equals("route"))
                        road = AddressPartLongName;
                    if (AddressPartTypes.getString(0).equals("street_number"))
                        house_number = AddressPartLongName;
                    if (AddressPartTypes.getString(0).equals("postal_code"))
                        postcode = AddressPartLongName;
                    if (AddressPartTypes.getString(0).equals("locality") && AddressPartTypes.getString(1).equals("political"))
                        city = AddressPartLongName;
                    if (AddressPartTypes.getString(0).equals("sublocality") && AddressPartTypes.getString(1).equals("political"))
                        city_district = AddressPartLongName;
                    if (AddressPartTypes.getString(0).equals("country") && AddressPartTypes.getString(1).equals("political"))
                        country = AddressPartLongName;
                }
                if (!road.isEmpty()) {
                    AddressString += road;
                }
                if (!house_number.isEmpty()) {
                    if (!road.isEmpty()) {
                        AddressString += " ";
                    }
                    AddressString += house_number + ",";
                }
                if (!postcode.isEmpty()) {
                    if (!road.isEmpty() || !house_number.isEmpty()) {
                        AddressString += " ";
                    }
                    AddressString += postcode;
                }
                String Stadtname = "";
                if (!city.isEmpty()) {
                    if (!road.isEmpty() || !house_number.isEmpty() || !postcode.isEmpty()) {
                        AddressString += " ";
                    }
                    Stadtname += city;
                    if (!city_district.isEmpty()) {
                        Stadtname += " - " + city_district;
                    }
                }
                AddressString += Stadtname;
                if (!country.isEmpty()) {
                    if (!road.isEmpty() || !house_number.isEmpty() || !postcode.isEmpty() || !city.isEmpty()) {
                        AddressString += ", ";
                    }
                    AddressString += country;
                }
                Double DLat = Results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double DLon = Results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                return new Address(road, house_number, postcode, Stadtname, country, DLat, DLon);
            }
            return null;
        }
        public JSONObject Load_Google_Address(String Search_Obj) {
            String IP = new String();
            String URL_String = new String();
            try {
                URL_String = "http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(Search_Obj, "UTF-8") + "&sensor=false";
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GetAddress.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            try {
                URL myURL= new URL(URL_String);
                Reader is = new InputStreamReader( myURL.openStream() );
                BufferedReader in = new BufferedReader( is );
                for ( String s; ( s = in.readLine() ) != null; ) {
                    IP += s;
                }
                
            } catch (IOException ex) {
                Logger.getLogger(GetAddress.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            if (!IP.isEmpty()) {
                return new JSONObject(IP.replaceAll("\\s"," "));
            }
            return null;
        }
    });
    
    public Address getMapAddress() {
        return this.mapAddress;
    }
    
    public String getSearchText() {
        return this.txt_search.getText();
    }
    public String getText() {
        if (isValidAddress())
            return this.mapAddress.getStraße() + " " + this.mapAddress.getHsNr() + " \n" + this.mapAddress.getPLZ() + " " + this.mapAddress.getStadt() + " \n" + this.mapAddress.getLand();
        return "Keine Adresse gefunden für: " + this.txt_search.getText();
    }
    
    public boolean isValidAddress() {
        if (this.mapAddress ==  null)
            return false;
        return (this.mapAddress.getLat() >= -90 &&
                this.mapAddress.getLat() <= 90 &&
                this.mapAddress.getLon() >= -180 &&
                this.mapAddress.getLon() <= 180);
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
        jMapViewer1 = new org.openstreetmap.gui.jmapviewer.JMapViewer();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adresssuche");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/org/openstreetmap/gui/jmapviewer/images/iconOSM.png")).getImage());
        setName("dialogMapAdress"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addComponent(jMapViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jMapViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOKActionPerformed(null);
        }
    }//GEN-LAST:event_txt_searchKeyPressed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        runWorker = false;
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        runWorker = false;
        mapAddress = null;
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        worker.start();
        if (!txt_search.getText().isEmpty())
            worker.interrupt();
        txt_search.addCaretListener((CaretEvent e) -> {
            worker.interrupt();
        });
    }//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private org.openstreetmap.gui.jmapviewer.JMapViewer jMapViewer1;
    private javax.swing.JLabel lbl_HsNr;
    private javax.swing.JLabel lbl_Land;
    private javax.swing.JLabel lbl_PLZ;
    private javax.swing.JLabel lbl_Stadt;
    private javax.swing.JLabel lbl_Straße;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
