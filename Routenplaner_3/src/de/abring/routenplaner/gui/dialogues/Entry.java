/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui.dialogues;

import de.abring.routenplaner.gui.imageOCR.OCR;
import de.abring.helfer.maproute.LookupAddress;
import de.abring.helfer.maproute.MapAddress;
import de.abring.helfer.maproute.SearchForAddress;
import de.abring.gui.snippingtool.*;
import de.abring.helfer.primitives.*;
import de.abring.routenplaner.jxtreetableroute.JXNoRootTreeTableModelAddress;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openstreetmap.gui.jmapviewer.*;

/**
 *
 * @author Karima
 */
public class Entry extends javax.swing.JDialog {
    java.awt.Frame parent;
    int parentState;
    private JXTreeRouteAddressClient entry;
    private List<PercentDimension> parts = new ArrayList<>();
        
    //Suche Pakete
    private static final String[] paketeBausteine = {
        "liefer",
        "paket",
        "lieferpaket"
    };
    //Suche SubPakete
    private static final String[] subPaketeBausteine = {
        "geräte",
        "zuschlag",
        "mehrgerätezuschlag"
    };
    //Suche Fina
    private static final String[] finaBausteine = {
        "finanzierung"
    };
    //Suche Altger.handling
    private static final String[] handlBausteine = {
        "handling",
        "altger",
        "altgerhandling"
    };
    //Suche Garantie-Verlängerung
    private static final String[] garantBausteine = {
        "garantie",
        "verlängerung",
        "garantie-verlängerung"
    };
    
    /**
     * Creates new form NewJDialog
     * @param parent
     * @param modal
     * @param address
     * @param favorite
     */
    public Entry(java.awt.Frame parent, boolean modal, MapAddress address, JXTreeRouteAddressFav favorite) {
        super(parent, modal);
        this.parent = parent;
        if (this.parent != null) {
            this.parentState = this.parent.getState();
            this.parent.setState(java.awt.Frame.ICONIFIED);
        }
        initComponents();
    
        if (address == null) {
            address = new MapAddress(jTxtSearchAddress.getText());
            this.jTxtSearchAddress.requestFocus();
        } else {
            this.jTblProdukte.requestFocus();
        }
        
        entry = new JXTreeRouteAddressClient(address, favorite);
        entry.setDuration(new TimeOfDay("00:05"));
        entry.setAppointment(new Appointment(new TimeOfDay("10:00"), new TimeOfDay("18:00")));
        initOtherComponents();
    }
    
    public Entry(java.awt.Frame parent, boolean modal, JXTreeRouteAddressFav favorite) {
        super(parent, modal);
        this.parent = parent;
        if (this.parent != null) {
            this.parentState = this.parent.getState();
            this.parent.setState(java.awt.Frame.ICONIFIED);
        }
        initComponents();
        
        MapAddress address = new MapAddress(jTxtSearchAddress.getText());
        this.jTxtSearchAddress.requestFocus();
        
        entry = new JXTreeRouteAddressClient(address, favorite);
        entry.setDuration(new TimeOfDay("00:05"));
        entry.setAppointment(new Appointment(new TimeOfDay("10:00"), new TimeOfDay("18:00")));
        initOtherComponents();
    }

    public Entry(java.awt.Frame parent, boolean modal, JXTreeRouteAddressClient _entry) {
        super(parent, modal);
        this.parent = parent;
        if (this.parent != null) {
            this.parentState = this.parent.getState();
            this.parent.setState(java.awt.Frame.ICONIFIED);
        }
        
        initComponents();
        
        if (_entry != null) {
            this.entry = _entry;
            this.jTblProdukte.requestFocus();
        }
        initOtherComponents();
        
        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
    }
    
    private void initOtherComponents() {
        this.jTxtName.setText(entry.getName());
        
        this.jTxtSearchAddress.setText(entry.getAddress().toString());
        
        this.jPanelTermin.setTermin(entry.getAppointment());
        
        this.jPanelPakete.getjSpinnerP().setValue(entry.getP()) ;
        this.jPanelPakete.getjSpinnerK().setValue(entry.getK()) ;
        this.jPanelPakete.getjSpinnerS().setValue(entry.getS()) ;
        this.jPanelPakete.setZeit(entry.getDuration());
        
        this.jTblProdukte.setJxTreeRouteEntryList(entry.getItems());
        this.jTblProdukte.setComponentPopupMenu(jPopupItems);
        int[] columns = {
            JXNoRootTreeTableModelAddress.EMPTY,
            JXNoRootTreeTableModelAddress.NAME,
        };
        this.jTblProdukte.handleColumns(columns);
        this.jTblProdukte.updateUI();
        
        this.jTextExtras.setText(entry.getExtras());
        this.jLblFav.setText(entry.getFavorite().getName());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupItems = new javax.swing.JPopupMenu();
        jMenuAdd = new javax.swing.JMenuItem();
        jMenuDelete = new javax.swing.JMenuItem();
        jPnlName = new javax.swing.JPanel();
        jTxtName = new javax.swing.JTextField();
        jPnlAddress = new javax.swing.JPanel();
        jTxtSearchAddress = new javax.swing.JTextField();
        jButtonSuche = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanelPakete = new de.abring.routenplaner.gui.components.JPanelPakete();
        jPanelTermin = new de.abring.routenplaner.gui.components.JPanelTermin();
        jPanel6 = new javax.swing.JPanel();
        jLblFav = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTblProdukte = new de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute();
        jBtnProduktPlus = new javax.swing.JButton();
        jBtnProduktMinus = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextExtras = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();
        jBtnSnipRollkarte = new javax.swing.JButton();

        jMenuAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Add.png"))); // NOI18N
        jMenuAdd.setText("Add");
        jMenuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAddActionPerformed(evt);
            }
        });
        jPopupItems.add(jMenuAdd);

        jMenuDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Remove.png"))); // NOI18N
        jMenuDelete.setText("Delete");
        jMenuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDeleteActionPerformed(evt);
            }
        });
        jPopupItems.add(jMenuDelete);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Eintrag");
        setMinimumSize(new java.awt.Dimension(469, 359));
        setSize(new java.awt.Dimension(469, 359));

        jPnlName.setBorder(javax.swing.BorderFactory.createTitledBorder("Name"));

        jTxtName.setText("*");
        jTxtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtNameActionPerformed(evt);
            }
        });
        jTxtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTxtNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPnlNameLayout = new javax.swing.GroupLayout(jPnlName);
        jPnlName.setLayout(jPnlNameLayout);
        jPnlNameLayout.setHorizontalGroup(
            jPnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTxtName)
        );
        jPnlNameLayout.setVerticalGroup(
            jPnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlNameLayout.createSequentialGroup()
                .addComponent(jTxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPnlAddress.setBorder(javax.swing.BorderFactory.createTitledBorder("Adresse"));

        jTxtSearchAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtSearchAddressKeyPressed(evt);
            }
        });

        jButtonSuche.setText("Suche");
        jButtonSuche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSucheActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPnlAddressLayout = new javax.swing.GroupLayout(jPnlAddress);
        jPnlAddress.setLayout(jPnlAddressLayout);
        jPnlAddressLayout.setHorizontalGroup(
            jPnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlAddressLayout.createSequentialGroup()
                .addComponent(jTxtSearchAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSuche))
        );
        jPnlAddressLayout.setVerticalGroup(
            jPnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTxtSearchAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonSuche))
        );

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Auftraggeber"));

        jLblFav.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLblFav.setText("jLabel");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLblFav, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLblFav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPakete, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelTermin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelPakete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTermin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jSplitPane2.setDividerLocation(160);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Produkte"));

        jScrollPane3.setViewportView(jTblProdukte);

        jBtnProduktPlus.setText("+");
        jBtnProduktPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnProduktPlusActionPerformed(evt);
            }
        });

        jBtnProduktMinus.setText("-");
        jBtnProduktMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnProduktMinusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jBtnProduktPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnProduktMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnProduktPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnProduktMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jSplitPane2.setLeftComponent(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Extras"));

        jTextExtras.setColumns(20);
        jTextExtras.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTextExtras.setRows(2);
        jTextExtras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextExtrasKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextExtras);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel5);

        jSplitPane1.setRightComponent(jSplitPane2);

        jPanel3.setMaximumSize(new java.awt.Dimension(600, 45));
        jPanel3.setMinimumSize(new java.awt.Dimension(453, 45));

        jBtnOK.setText("OK");
        jBtnOK.setEnabled(false);
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnOKActionPerformed(evt);
            }
        });

        jBtnCancel.setText("Abbrechen");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        jBtnSnipRollkarte.setText("Snipping Rollkarte");
        jBtnSnipRollkarte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSnipRollkarteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBtnSnipRollkarte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnOK)
                .addGap(16, 16, 16))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnOK)
                    .addComponent(jBtnCancel)
                    .addComponent(jBtnSnipRollkarte))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnlAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
            .addComponent(jPnlName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPnlName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPnlAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addGap(4, 4, 4)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnOKActionPerformed
        
        entry.setAppointment(this.jPanelTermin.getTermin());
        entry.setP((int) this.jPanelPakete.getjSpinnerP().getValue());
        entry.setK((int) this.jPanelPakete.getjSpinnerK().getValue());
        entry.setS((int) this.jPanelPakete.getjSpinnerS().getValue());
        entry.setDuration(new TimeOfDay(this.jPanelPakete.getZeit()));
        entry.setName(this.jTxtName.getText());
        
        
        
        //entry.ss.setProdukte(this.jTblProdukte.getText());
        
        
        
        
        entry.setExtras(this.jTextExtras.getText());
        this.setVisible(false);
        if (this.parent != null) {
            this.parent.setState(this.parentState);
        }
        
        
    }//GEN-LAST:event_jBtnOKActionPerformed

    private void jButtonSucheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSucheActionPerformed
        MapAddress address = new MapAddress(jTxtSearchAddress.getText());
        if (jTxtSearchAddress.getText().length() > 5) {
            LookupAddress lookup = new LookupAddress(parent, true, address);
            lookup.setVisible(true);
            address = lookup.getMapAddress();
            if (!address.isValid() || address.getStraße().isEmpty()) {
                SearchForAddress search = new SearchForAddress(parent, true, jTxtSearchAddress.getText());
                search.setVisible(true);
                address = search.getMapAddress();
            }
        } else {
            SearchForAddress search = new SearchForAddress(parent, true, jTxtSearchAddress.getText());
            search.setVisible(true);
            address = search.getMapAddress();
        }
        if (address.isValid() && !address.getStraße().isEmpty()) {
            this.getEntry().setAddress(address);
            this.entry.setDot(new MapMarkerDotWithNumber(address.getLat(), address.getLon()));
            jTxtSearchAddress.setText(address.toString());
            this.jPanelPakete.requestFocus();
        }
        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
    }//GEN-LAST:event_jButtonSucheActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        entry = null;
        this.setVisible(false);
        if (this.parent != null) {
            this.parent.setState(this.parentState);
        }
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jTxtSearchAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtSearchAddressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSucheActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jTxtSearchAddress.setText("");
        }
        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
    }//GEN-LAST:event_jTxtSearchAddressKeyPressed

    private void jTextExtrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextExtrasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && evt.isShiftDown()) {
            this.jBtnOK.requestFocus();
        }
    }//GEN-LAST:event_jTextExtrasKeyPressed

    private void jTxtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtNameKeyPressed
        
    }//GEN-LAST:event_jTxtNameKeyPressed

    private void jMenuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAddActionPerformed
        String name = JOptionPane.showInputDialog(this, "Produkt:");
        this.jTblProdukte.addItem(new JXTreeRouteItem(name));
        this.jTblProdukte.updateUI();
    }//GEN-LAST:event_jMenuAddActionPerformed

    private void jTxtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtNameActionPerformed
        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
    }//GEN-LAST:event_jTxtNameActionPerformed

    private void jTxtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtNameKeyReleased
        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
    }//GEN-LAST:event_jTxtNameKeyReleased

    private void jMenuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDeleteActionPerformed
        List<JXTreeRouteEntry> selectedEntries = new ArrayList<>();
        int[] selectedRows = this.jTblProdukte.getSelectedRows();
        
        for (int i = 0; i < selectedRows.length; i++) {
            JXTreeRouteEntry delentry = this.jTblProdukte.getItem(selectedRows[i]);
            if (delentry instanceof JXTreeRouteItem)
                selectedEntries.add(delentry);
        }
        this.jTblProdukte.removeItems(selectedEntries);
        this.jTblProdukte.updateUI();
    }//GEN-LAST:event_jMenuDeleteActionPerformed

    private void jBtnProduktPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnProduktPlusActionPerformed
        String name = JOptionPane.showInputDialog(this, "Produkt:");
        this.jTblProdukte.addItem(new JXTreeRouteItem(name));
        this.jTblProdukte.updateUI();
    }//GEN-LAST:event_jBtnProduktPlusActionPerformed

    private void jBtnProduktMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnProduktMinusActionPerformed
        List<JXTreeRouteEntry> selectedEntries = new ArrayList<>();
        int[] selectedRows = this.jTblProdukte.getSelectedRows();
        
        for (int i = 0; i < selectedRows.length; i++) {
            JXTreeRouteEntry delentry = this.jTblProdukte.getItem(selectedRows[i]);
            if (delentry instanceof JXTreeRouteItem)
                selectedEntries.add(delentry);
        }
        this.jTblProdukte.removeItems(selectedEntries);
        this.jTblProdukte.updateUI();
    }//GEN-LAST:event_jBtnProduktMinusActionPerformed

    private void jBtnSnipRollkarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSnipRollkarteActionPerformed
        this.entry = OCR.rollkarteOCR(null, true, this.entry.getFavorite());
        initOtherComponents();

        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());

    }//GEN-LAST:event_jBtnSnipRollkarteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JButton jBtnProduktMinus;
    private javax.swing.JButton jBtnProduktPlus;
    private javax.swing.JButton jBtnSnipRollkarte;
    private javax.swing.JButton jButtonSuche;
    private javax.swing.JLabel jLblFav;
    private javax.swing.JMenuItem jMenuAdd;
    private javax.swing.JMenuItem jMenuDelete;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private de.abring.routenplaner.gui.components.JPanelPakete jPanelPakete;
    private de.abring.routenplaner.gui.components.JPanelTermin jPanelTermin;
    private javax.swing.JPanel jPnlAddress;
    private javax.swing.JPanel jPnlName;
    private javax.swing.JPopupMenu jPopupItems;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private de.abring.routenplaner.jxtreetableroute.JXTreeTableRoute jTblProdukte;
    private javax.swing.JTextArea jTextExtras;
    private javax.swing.JTextField jTxtName;
    private javax.swing.JTextField jTxtSearchAddress;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the entry
     */
    public JXTreeRouteAddressClient getEntry() {
        return entry;
    }
}
