/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.gui.imageOCR;

import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author Karima
 */
public class ImageOCR extends javax.swing.JDialog {
    private static final Logger logger = Logger.getLogger(ImageOCR.class.getName());
    private String string = "";
    private ImageOCR frame = this;
    private BufferedImage image = null;
    
    public ImageOCR(java.awt.Frame parent, boolean modal, BufferedImage image) {
        super(parent, modal);
        if (image == null) {
            logger.log(Level.FINE, "Keine Bild? Kein Text!");
            return;
        }
        logger.log(Level.INFO, "Hab ein Bild! Bitte warten...");
                                
        this.image = image;
        initComponents();
        
        Cursor normalCursor = this.getParent().getCursor();
        this.getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        worker.start();
        worker.interrupt();
        
        this.getParent().setCursor(normalCursor);
    }
    
    private Thread worker = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean run = true;
            while(run) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.log(Level.FINE, "Do OCR!!!");
                    ITesseract instance = new Tesseract();
                    instance.setLanguage("deu");
                    instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK);
                    try {
                        frame.string = instance.doOCR(frame.image).trim();
                    } catch (TesseractException ex) {
                        logger.log(Level.SEVERE, null, "Probleme bei der Texterkennung:\n" + ex);
                    }
                    logger.log(Level.FINE,"OCR finished...");
                    logger.log(Level.INFO, "\n=>\n{0}\n<=", frame.string);
                    frame.dispose();
                }
            }
            if (frame.isVisible()) {
                logger.log(Level.FINE,"Keiner zuhause, breche ab.");
                frame.dispose();
            }
        }
    });
    
    public String getString() {
        return this.string;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setResizable(false);

        btnCancel.setText("Abbrechen");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel1.setText("verarbeite...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(btnCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(btnCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        frame.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
