/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer.gui;

import de.bring.helfer.Address;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Karima
 */
public class LookupAddress extends javax.swing.JDialog {
    private Address mapAddress;
    private LookupAddress frame = this;
    
    public LookupAddress(java.awt.Frame parent, boolean modal, Address mapAddress) {
        super(parent, modal);
        this.mapAddress = mapAddress;
        initComponents();
        this.setTitle(mapAddress.getSuchString());
        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker()); 
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter()); 
        label.setBusy(true); 
    }
    public LookupAddress(java.awt.Frame parent, boolean modal, String suchString) {
        super(parent, modal);
        this.mapAddress = new Address(suchString);
        initComponents();
        this.setTitle(suchString);
        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker()); 
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter()); 
        label.setBusy(true); 
    }
    private Thread worker = new Thread(
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
                        if (mapAddress.getSuchString().length() >= 3) {
                            run = !OSM_Address(mapAddress.getSuchString().toLowerCase());
                            if (run) {
                                run = !Google_Address(mapAddress.getSuchString().toLowerCase());
                            }
                        }
                    }
                    if (sec > 600) {
                        run = false;
                        Logger.getLogger(LookupRoute.class.getName()).log(Level.INFO, "Server timed out.");
                        btnCancelActionPerformed(null);
                    }
                }
                frame.dispose();
            }
            public boolean OSM_Address (String Search_Obj) {
                JSONArray Results = Load_OSM_Address(Search_Obj);
                if ((Results != null) && !Results.isNull(0)){
                    JSONObject Address = Results.getJSONObject(0).getJSONObject("address");
                    if (!(Address.has("road") && Address.has("house_number") && Address.has("postcode") && Address.has("city") && Address.has("city_district") && Address.has("country"))) {
                        return false;
                    }
                    String Stadtname = "";
                    if (Address.has("city")) {
                        Stadtname += Address.getString("city");
                        if (Address.has("city_district")) {
                            Stadtname += " - " + Address.getString("city_district");
                        }
                    }
                    Double DLat = Double.parseDouble(Results.getJSONObject(0).getString("lat"));
                    Double DLon = Double.parseDouble(Results.getJSONObject(0).getString("lon"));
                    mapAddress.setAddress(Address.getString("road"), Address.getString("house_number"), Address.getString("postcode"), Stadtname, Address.getString("country"), DLat, DLon);
                    return mapAddress.isValid();
                }
                return false;
            }
            public JSONArray Load_OSM_Address(String Search_Obj) {
                String IP = new String();
                String URL_String = new String();
                try {
                    URL_String = "http://nominatim.openstreetmap.org/search?q=" +
                                 URLEncoder.encode(Search_Obj, "UTF-8") + 
                                 "&bounded=0&format=json&limit=1&addressdetails=1&email=andreas.bring@rwth-aachen.de&countrycodes=de,nl,be,lu";
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
                if (!IP.isEmpty())
                    return new JSONArray(IP);
                return null;
            }
            public boolean Google_Address (String Search_Obj) {
                JSONArray Results = Load_Google_Address(Search_Obj).getJSONArray("results");
                if ((Results != null) && !Results.isNull(0)) {
                    JSONArray Address = Results.getJSONObject(0).getJSONArray("address_components");
                    String road = "";
                    String house_number = "";
                    String postcode = "";
                    String city = "";
                    String city_district = "";
                    String country = "";
                    String Stadtname = "";
                    
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
                    if (!city.isEmpty()) {
                        Stadtname += city;
                        if (!city_district.isEmpty()) {
                            Stadtname += " - " + city_district;
                        }
                    }
                    Double DLat = Results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double DLon = Results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    mapAddress.setAddress(road, house_number, postcode, Stadtname, country, DLat, DLon);
                    return mapAddress.isValid();
                }
                return false;
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
            if (!IP.isEmpty())
                return new JSONObject(IP.replaceAll("\\s",""));
            return null;
        }
        }
    );
    
    public Address getMapAddress() {
        return this.mapAddress;
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

        label.setText("Ich schau mal, ob mir im Internet jemand diese Adresse kennt!");

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
        frame.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (mapAddress instanceof Address) {
            System.out.println("neue Addresse\nSuche:  \"" + mapAddress.getSuchString() + "\"");
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
