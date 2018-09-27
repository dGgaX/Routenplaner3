/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung.gui.dialogues;

import de.abring.pdferkennung.PictureRecognition;
import de.abring.pdferkennung.gui.JPictureFrame;
import de.abring.pdferkennung.gui.dialogues.FileIO;
import de.abring.routenplaner.gui.Route3;
import de.abring.routenplaner.gui.dialogues.Entry;
import de.abring.routenplaner.gui.imageOCR.OCR;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteAddress;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteAddressClient;
import de.abring.routenplaner.jxtreetableroute.entries.JXTreeRouteAddressFav;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Karima
 */
public class JscanPDF extends javax.swing.JDialog {

    private final List<JXTreeRouteAddress> addressList;
    private final File file;
    private final JXTreeRouteAddressFav fav;
    private final java.awt.Frame parent;
    private final boolean showResults;
    private PDFRenderer renderer ;
    private int pages = 0;
    private int images = 0;
    private final int resolution = 250;
    private boolean stopRunning = false;
    private int image = 0;
    private int page = 0;
    
    /**
     * Creates new form JscanPDF
     * @param parent
     * @param modal
     * @param fav
     * @param file
     */
    public JscanPDF(java.awt.Frame parent, boolean modal, JXTreeRouteAddressFav fav, File file, boolean showResults) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.addressList = new ArrayList<>();
        this.fav = fav;
        this.file = file;
        this.showResults = showResults;
    }

    private void loadFile(File file) {
        try {
            PDDocument doc = PDDocument.load(file);
            renderer = new PDFRenderer(doc);
            setPageMax(doc.getNumberOfPages());
            for(int i = 0; i < pages; i++) {
                setPageValue(i);
                showPage(i);
                if (stopRunning)
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void showPage(int page) {
        BufferedImage image;
        try {
            image = renderer.renderImageWithDPI(page, resolution, ImageType.RGB);
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        image = PictureRecognition.frameFinder(image, 1, Math.round(resolution / 30.0f));
        if (image != null) {
            JXTreeRouteAddressClient entry = OCR.rollkarteOCR(this, true, fav, image);
            if (entry != null) {
                if (this.showResults) {
                    showPDFPage imageFrame = new showPDFPage(this.parent, false, image, 1.0f);
                    Entry entryFrame = new Entry(this.parent, true, entry);
                    imageFrame.setVisible(true);
                    entryFrame.setVisible(true);
                    imageFrame.dispose();
                    entryFrame.dispose();
                    entry = entryFrame.getEntry();
                }
                if (entry != null) {
                    addressList.add(entry);
                }
            }
        }
        freememory();
    }
    
    public void freememory(){
        Runtime basurero = Runtime.getRuntime(); 
        basurero.gc();
    }
    public void setImageValue(int value) {
        if (value >= 0 && value < this.images) {
            this.image = value;
            jLblImage.setText("Lese Bild " + String.valueOf(this.image + 1) + " von " + String.valueOf(this.images) + " ein:");
            jBarImage.setValue(this.image);
//            jLblImage.updateUI();
//            jBarImage.updateUI();
        }
    }
    public void imageUpdateUI() {
        this.jBarImage.updateUI();
        this.jLblImage.updateUI();
    }
    
    public int getImageValue() {
        return this.page;
    }
    
    public void setPageValue(int value) {
        if (value >= 0 && value < this.pages) {
            this.page = value;
            jLblPage.setText("Ich bin bei Seite " + String.valueOf(this.page + 1) + " von " + String.valueOf(this.pages) + ":");
            jBarPage.setValue(this.page);
//            jLblPage.updateUI();
//            jBarPage.updateUI();
        }
    }
    
    public void setImageMax(int max) {
        if (max > 0) {
            this.images = max;
            jBarImage.setMaximum(this.images - 1);
//            jBarImage.updateUI();
            
        }
    }
    
    public void setPageMax(int max) {
        if (max > 0) {
            this.pages = max;
            jBarPage.setMaximum(this.pages - 1);
//            jBarPage.updateUI();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtnCancel = new javax.swing.JButton();
        jLblImage = new javax.swing.JLabel();
        jBarImage = new javax.swing.JProgressBar();
        jLblPage = new javax.swing.JLabel();
        jBarPage = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PDF einlesen");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jBtnCancel.setText("Abbrechen");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        jLblImage.setText("Lese Teile ein");

        jLblPage.setText("Bin bei Seite");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBtnCancel))
                    .addComponent(jBarImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addComponent(jBarPage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLblImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLblPage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLblImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBarImage, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLblPage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBarPage, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnCancel)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        new Thread(){
            @Override
            public void run(){
                loadFile(file);
                dispose();
            }
        }.start();

    }//GEN-LAST:event_formWindowOpened

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        stopRunning = true;
        this.dispose();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar jBarImage;
    private javax.swing.JProgressBar jBarPage;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JLabel jLblImage;
    private javax.swing.JLabel jLblPage;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the parent
     */
    @Override
    public java.awt.Frame getParent() {
        return parent;
    }

    /**
     * @return the addressList
     */
    public List<JXTreeRouteAddress> getAddressList() {
        return addressList;
    }
}
