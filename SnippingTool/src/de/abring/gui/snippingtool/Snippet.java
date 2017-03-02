/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.gui.snippingtool;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karima
 */
public final class Snippet extends javax.swing.JDialog {
    private Point start = new Point(0, 0);
    private Rectangle bounds = new Rectangle(start);
    java.awt.Frame parent;
    private List<PercentDimension> parts;
    private List<BufferedImage> newImage;
    
    /**
     * Creates new form Overlay
     * @param parent
     * @param modal
     * @param parts
     */
    public Snippet(java.awt.Frame parent, boolean modal, List<PercentDimension> parts) {
        super(parent, modal);
        this.parent = parent;
        this.parts = parts;
        this.newImage = new ArrayList<>();
        setUndecorated(true); 
        initComponents();
        jPntPne.setRectColor(jPntPne.getForeground());
        
        jPntPne.setParts(parts);
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            setOpacity(0.70f);
        }
        
        setBounds(overallScreeSize());
        setVisible(true);
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPntPne = new de.abring.gui.snippingtool.JPaintPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        setName("overlay"); // NOI18N
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jPntPne.setBackground(new java.awt.Color(0, 0, 255));
        jPntPne.setForeground(new java.awt.Color(255, 255, 0));
        jPntPne.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPntPneMouseDragged(evt);
            }
        });
        jPntPne.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPntPneMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPntPneMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPntPneLayout = new javax.swing.GroupLayout(jPntPne);
        jPntPne.setLayout(jPntPneLayout);
        jPntPneLayout.setHorizontalGroup(
            jPntPneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 834, Short.MAX_VALUE)
        );
        jPntPneLayout.setVerticalGroup(
            jPntPneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPntPne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPntPne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPntPneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPntPneMousePressed
        if (evt.getButton() == MouseEvent.BUTTON1) {
            jPntPne.setDrawRect(true);
            start = evt.getPoint();
        }
    }//GEN-LAST:event_jPntPneMousePressed

    private void jPntPneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPntPneMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1) {
            jPntPne.setDrawRect(false);
            bounds = new Rectangle(start);
            bounds.add(evt.getPoint());
            BufferedImage image = getScreenshot();
            for (PercentDimension part : parts) {
                Rectangle partBounds = part.getDimension(bounds);

                System.out.println("-> " + String.valueOf(partBounds.x) + ", " + String.valueOf(partBounds.y) + ", " + String.valueOf(partBounds.width) + ", " + String.valueOf(partBounds.height));

                newImage.add(image.getSubimage(partBounds.x, partBounds.y, partBounds.width, partBounds.height));
            }
            this.dispose();
        }
    }//GEN-LAST:event_jPntPneMouseReleased

    private void jPntPneMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPntPneMouseDragged
        bounds = new Rectangle(start);
        bounds.add(evt.getPoint());
        jPntPne.setRectBounds(bounds);
        jPntPne.repaint();
        jPntPne.updateUI();
    }//GEN-LAST:event_jPntPneMouseDragged

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        this.dispose();
    }//GEN-LAST:event_formKeyTyped

    public static Rectangle overallScreeSize() {
        Rectangle overallBounds = new Rectangle(new Dimension(0,0));
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Rectangle rect = new Rectangle(screenSize);
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for(GraphicsDevice curGs : gs) {
            GraphicsConfiguration[] gc = curGs.getConfigurations();
            for(GraphicsConfiguration curGc : gc) {
                Rectangle bounds = curGc.getBounds();
                overallBounds.add(bounds);
//                try {
//                    Robot robot = new Robot();
//                    BufferedImage currentScreen = robot.createScreenCapture(bounds);
//                    String screenID = curGc.getDevice().getIDstring().replaceAll("([a-zA-Z]:)?(\\\\[a-zA-Z._-]+)+\\\\?", "");
//                    File saveFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + dateString + "-" + screenID + "." + endung);
//                    ImageIO.write(currentScreen, endung, saveFile);
//                } catch (AWTException | IOException ex) {
//                    Logger.getLogger(Snippet.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }
        return overallBounds;
    }
    
    /**
     *
     * @return BufferedImage
     */
    public BufferedImage getScreenshot() {
//        String endung = "jpg";
//        LocalDateTime date = LocalDateTime.now();
//        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS"));
        Rectangle bounds = overallScreeSize();

        try {
            Robot robot = new Robot();
            BufferedImage currentScreen = robot.createScreenCapture(bounds);
//            File saveFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + dateString + "." + endung);
//            ImageIO.write(currentScreen, endung, saveFile);
            return currentScreen;
        } catch (AWTException ex) {
            Logger.getLogger(Snippet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.abring.gui.snippingtool.JPaintPane jPntPne;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the newImage
     */
    public List<BufferedImage> getNewImage() {
        return newImage;
    }
}
