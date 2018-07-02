/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung.gui.dialogues;

import de.abring.pdferkennung.Cell;
import de.abring.pdferkennung.PictureRecognition;
import de.abring.pdferkennung.gui.JPictureFrame;
import de.abring.pdferkennung.gui.dialogues.FileIO;
import de.abring.routenplaner.gui.Route3;
import de.abring.routenplaner.gui.dialogues.Entry;
import de.abring.routenplaner.gui.imageOCR.OCR;
import de.abring.routenplaner.gui.imageOCR.OCR3;
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
public class JscanPDF3 extends javax.swing.JDialog {

    private final Route3 route;
    private final File file;
    private final JXTreeRouteAddressFav fav;
    private final java.awt.Frame parent;
    private int parentState;
    private final boolean showResults;
    private PDFRenderer renderer ;
    private int pages = 0;
    private int cells = 0;
    private int images = 0;
    private final int resolution = 250;
    private boolean stopRunning = false;
    private int page = 0;
    private int cell = 0;
    private int image = 0;
    
    /**
     * Creates new form JscanPDF
     * @param parent
     * @param modal
     * @param route
     * @param fav
     * @param file
     */
    public JscanPDF3(java.awt.Frame parent, boolean modal, Route3 route, JXTreeRouteAddressFav fav, File file, boolean showResults) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.route = route;
        this.fav = fav;
        this.file = file;
        this.showResults = showResults;
    }

    private void loadFile(File file) {
        
        try {
            PDDocument doc = PDDocument.load(file);
            renderer = new PDFRenderer(doc);
            setPageMax(doc.getNumberOfPages());
            List<Cell> children = new ArrayList<>();
            for(int i = 0; i < pages; i++) {
                setPageValue(i);
                BufferedImage image = null;
                try {
                    image = renderer.renderImageWithDPI(page, resolution, ImageType.RGB);
                } catch (IOException ex) {
                    Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (image != null) {
                    Cell cell = new Cell(image);
                    children.addAll(cell.getChildList());
                }
                if (stopRunning)
                    break;
            }

            this.setCellMax(children.size());
            
            List<JXTreeRouteAddressClient> entries = OCR3.rollkarteOCR(this, true, fav, children);
            
            for (JXTreeRouteAddressClient entry : entries) {
                if (entry != null) {
                    route.addEntry(route.listLength() - 2, entry);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        
    public void freememory(){
        Runtime basurero = Runtime.getRuntime(); 
        basurero.gc();
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
    
    public void setCellValue(int value) {
        if (value >= 0 && value < this.cells) {
            this.cell = value;
            jLblCell.setText("Ich bin bei Auftrag " + String.valueOf(this.cell + 1) + " von " + String.valueOf(this.cells) + ":");
            jBarCell.setValue(this.cell);
//            jLblPage.updateUI();
//            jBarPage.updateUI();
        }
    }
    
    public void setImageValue(int value) {
        if (value >= 0 && value < this.images) {
            this.image = value;
            jLblImage.setText("Lese Abschnitt " + String.valueOf(this.image + 1) + " von " + String.valueOf(this.images) + " ein:");
            jBarImage.setValue(this.image);
//            jLblImage.updateUI();
//            jBarImage.updateUI();
        }
    }
    
    public void imageUpdateUI() {
        this.jBarImage.updateUI();
        this.jLblImage.updateUI();
    }
    
    public void cellUpdateUI() {
        this.jBarCell.updateUI();
        this.jLblCell.updateUI();
    }
    
    public int getImageValue() {
        return this.page;
    }
    
    public int getCellValue() {
        return this.cell;
    }
    
    
    public void setPageMax(int max) {
        if (max > 0) {
            this.pages = max;
            jBarPage.setMaximum(this.pages - 1);
//            jBarPage.updateUI();
        }
    }
    
    public void setCellMax(int max) {
        if (max > 0) {
            this.cells = max;
            jBarCell.setMaximum(this.cells - 1);
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPne = new javax.swing.JPanel();
        jLblPage = new javax.swing.JLabel();
        jBarPage = new javax.swing.JProgressBar();
        jLblCell = new javax.swing.JLabel();
        jBarCell = new javax.swing.JProgressBar();
        jLblImage = new javax.swing.JLabel();
        jBarImage = new javax.swing.JProgressBar();
        jBtnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PDF einlesen");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPne.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jLblPage.setText("Bin bei Seite");
        jLblPage.setMaximumSize(new java.awt.Dimension(2000, 20));
        jLblPage.setMinimumSize(new java.awt.Dimension(100, 20));
        jLblPage.setPreferredSize(new java.awt.Dimension(100, 20));
        jPne.add(jLblPage);
        jPne.add(jBarPage);

        jLblCell.setText("Gleich kommen die Aufträge");
        jLblCell.setMaximumSize(new java.awt.Dimension(2000, 20));
        jLblCell.setMinimumSize(new java.awt.Dimension(100, 20));
        jLblCell.setPreferredSize(new java.awt.Dimension(100, 20));
        jPne.add(jLblCell);
        jPne.add(jBarCell);

        jLblImage.setText("Danach kommen die einzelnen Textblöcke");
        jLblImage.setMaximumSize(new java.awt.Dimension(2000, 20));
        jLblImage.setMinimumSize(new java.awt.Dimension(100, 20));
        jLblImage.setPreferredSize(new java.awt.Dimension(100, 20));
        jPne.add(jLblImage);
        jPne.add(jBarImage);

        jBtnCancel.setText("Abbrechen");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(435, Short.MAX_VALUE)
                .addComponent(jBtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
    private javax.swing.JProgressBar jBarCell;
    private javax.swing.JProgressBar jBarImage;
    private javax.swing.JProgressBar jBarPage;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JLabel jLblCell;
    private javax.swing.JLabel jLblImage;
    private javax.swing.JLabel jLblPage;
    private javax.swing.JPanel jPne;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the parent
     */
    public java.awt.Frame getParent() {
        return parent;
    }
}
