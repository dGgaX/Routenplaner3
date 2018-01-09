/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung.gui;

import de.abring.pdferkennung.PictureRecognition;
import de.abring.pdferkennung.gui.dialogues.FileIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Karima
 */
public class JPictureFrame extends javax.swing.JFrame {
    private PDFRenderer renderer ;
    private int pages = 0;
    private int page = 0;
    private int resolution = 250;
    
    /**
     * Creates new form NewJFrame
     */
    public JPictureFrame() {
        initComponents();
    }
    
    /**
     * Creates new form NewJFrame
     * @param file
     */
    public JPictureFrame(File file) {
        initComponents();
        loadFile(file);
    }
    
    
    private void loadFile(File file) {
        try {
            PDDocument doc = PDDocument.load(file);
            renderer = new PDFRenderer(doc);
            pages = doc.getNumberOfPages();
            page = 0;
            this.jLabel1.setText("Page: " + String.valueOf(page + 1) + " / " + String.valueOf(pages));
            showPage(page);
            changeBtnState();
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
            this.jLabel1.setText("Page: 0 / 0");
            this.jLabel.setSize(this.jScrollPane.getSize());
            this.jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/pdferkennung/image/red-x.png")));
            changeBtnState();
        }
        
        
    }
    
    private void showPage(int page) {
        BufferedImage image = null;
        try {
            image = renderer.renderImageWithDPI(page, resolution, ImageType.RGB);
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        image = PictureRecognition.frameFinder(image, 1, Math.round(resolution / 30.0f));
            
        if (image == null) {
            this.jLabel.setSize(this.jScrollPane.getSize());
            this.jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/pdferkennung/image/red-x.png")));
        } else {
            this.jLabel.setSize(new Dimension(image.getWidth(), image.getHeight()));
            this.jLabel.setIcon(new ImageIcon(image));
        }
    }
    
    private void changeBtnState() {
        jBtnLeft.setEnabled(page > 0);
        jBtnRight.setEnabled(page < pages - 1);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jLabel = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnOpenPDF = new javax.swing.JButton();
        jBtnLeft = new javax.swing.JButton();
        jBtnRight = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PDFErkennung");

        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/pdferkennung/image/red-x.png"))); // NOI18N
        jScrollPane.setViewportView(jLabel);

        jToolBar1.setRollover(true);

        jBtnOpenPDF.setText("openPDF");
        jBtnOpenPDF.setFocusable(false);
        jBtnOpenPDF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnOpenPDF.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnOpenPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnOpenPDFActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnOpenPDF);

        jBtnLeft.setText("left");
        jBtnLeft.setFocusable(false);
        jBtnLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLeftActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnLeft);

        jBtnRight.setText("right");
        jBtnRight.setFocusable(false);
        jBtnRight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRightActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnRight);

        jLabel1.setText("Page: 0 / 0");
        jToolBar1.add(jLabel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1329, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLeftActionPerformed
        page--;
        this.jLabel1.setText("Page: " + String.valueOf(page + 1) + " / " + String.valueOf(pages));
        showPage(page);
        changeBtnState();
    }//GEN-LAST:event_jBtnLeftActionPerformed

    private void jBtnRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRightActionPerformed
        page++;
        this.jLabel1.setText("Page: " + String.valueOf(page + 1) + " / " + String.valueOf(pages));
        showPage(page);
        changeBtnState();
    }//GEN-LAST:event_jBtnRightActionPerformed

    private void jBtnOpenPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnOpenPDFActionPerformed
        File file = FileIO.getOpenPDFFile(this, System.getProperty("user.home"));
        if (file != null && file.exists())
            loadFile(file);
    }//GEN-LAST:event_jBtnOpenPDFActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnLeft;
    private javax.swing.JButton jBtnOpenPDF;
    private javax.swing.JButton jBtnRight;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}
