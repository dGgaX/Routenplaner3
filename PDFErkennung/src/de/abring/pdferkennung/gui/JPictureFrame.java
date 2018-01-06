/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung.gui;

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
    private static final int PATTERN_SIZE = 5;
    private static final int PATTERN_GAB = 10;
    private static final boolean[][] TOP_LEFT = {{false, false, false, false, false},
                                                 {false, false, false, false, false},
                                                 {false, false,  true,  true,  true},
                                                 {false, false,  true, false, false},
                                                 {false, false,  true, false, false}};
    
    private static final boolean[][] TOP_RIGHT = {{false, false, false, false, false},
                                                  {false, false, false, false, false},
                                                  { true,  true,  true, false, false},
                                                  {false, false,  true, false, false},
                                                  {false, false,  true, false, false}};
    
    private static final boolean[][] BOTTOM_LEFT = {{false, false,  true, false, false},
                                                    {false, false,  true, false, false},
                                                    {false, false,  true,  true,  true},
                                                    {false, false, false, false, false},
                                                    {false, false, false, false, false}};
    
    private static final boolean[][] BOTTOM_RIGHT = {{false, false,  true, false, false},
                                                     {false, false,  true, false, false},
                                                     { true,  true,  true, false, false},
                                                     {false, false, false, false, false},
                                                     {false, false, false, false, false}};
    
    private static final List<Rectangle> TOP_LEFT_CORNERS = new ArrayList<>();
    private static final List<Rectangle> TOP_RIGHT_CORNERS = new ArrayList<>();
    private static final List<Rectangle> BOTTOM_LEFT_CORNERS = new ArrayList<>();
    private static final List<Rectangle> BOTTOM_RIGHT_CORNERS = new ArrayList<>();
    
    
    
    /**
     * Creates new form NewJFrame
     * @param file
     */
    public JPictureFrame(File file) {
        Long time1 = 0l;
        try {
            time1 = System.currentTimeMillis();
            initComponents();
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image = renderer.renderImageWithDPI(6, 300, ImageType.RGB);
            System.out.println((System.currentTimeMillis() - time1));
            
            time1 = System.currentTimeMillis();
            image = getCorners(image);
            System.out.println((System.currentTimeMillis() - time1));
            
            time1 = System.currentTimeMillis();
            image = spinImage(image);
            System.out.println((System.currentTimeMillis() - time1));
            
            this.jLabel.setSize(new Dimension(image.getWidth(), image.getHeight()));
            this.jLabel.setIcon(new ImageIcon(image));
        } catch (IOException ex) {
            Logger.getLogger(JPictureFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static BufferedImage spinImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage binaryImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = binaryImage.createGraphics();
        int ALL_ENTRY_COUNT = TOP_LEFT_CORNERS.size() + TOP_RIGHT_CORNERS.size() + BOTTOM_LEFT_CORNERS.size() + BOTTOM_RIGHT_CORNERS.size();
        if (ALL_ENTRY_COUNT % 4 == 0) {
            int RECTANGLE_COUNT = ALL_ENTRY_COUNT / 4;
            double rotationAngle = 0;
            for (int i = 0; i < RECTANGLE_COUNT; i++) {
                Point topLeft = new Point((int) TOP_LEFT_CORNERS.get(i).getCenterX(), (int) TOP_LEFT_CORNERS.get(i).getCenterY());
                Point topRight = new Point((int) TOP_RIGHT_CORNERS.get(i).getCenterX(), (int) TOP_RIGHT_CORNERS.get(i).getCenterY());
                Point bottomLeft = new Point((int) BOTTOM_LEFT_CORNERS.get(i).getCenterX(), (int) BOTTOM_LEFT_CORNERS.get(i).getCenterY());
                Point bottomRight = new Point((int) BOTTOM_RIGHT_CORNERS.get(i).getCenterX(), (int) BOTTOM_RIGHT_CORNERS.get(i).getCenterY());
                System.out.println();
                System.out.println("Rectangle No.: " + String.valueOf(i));
                System.out.println("Offset Angle: " + String.valueOf(getAngle(topLeft, topRight) - getAngle(topLeft, new Point(topRight.x, topLeft.y))));
//                System.out.println("Offset Angle: " + String.valueOf(getAngle(topLeft, bottomLeft) - getAngle(new Point(bottomLeft.x, topLeft.y), bottomLeft)));
                System.out.println("Offset Angle: " + String.valueOf(getAngle(bottomRight, bottomLeft) - getAngle(bottomRight, new Point(bottomLeft.x, bottomRight.y))));
//                System.out.println("Offset Angle: " + String.valueOf(getAngle(bottomRight, topRight) - getAngle(new Point(topRight.x, bottomRight.y), topRight)));
                
                double angle1 = getAngle(topLeft, topRight) - getAngle(topLeft, new Point(topRight.x, topLeft.y));
                if (angle1 < 0)
                    rotationAngle = Math.min(rotationAngle, angle1);
                else
                    rotationAngle = Math.max(rotationAngle, angle1);
                double angle2 = getAngle(bottomRight, bottomLeft) - getAngle(bottomRight, new Point(bottomLeft.x, bottomRight.y));
                if (angle2 < 0)
                    rotationAngle = Math.min(rotationAngle, angle2);
                else
                    rotationAngle = Math.max(rotationAngle, angle2);
                
            }
            AffineTransform at = new AffineTransform();
            
            if(Math.abs(rotationAngle) > 0.3d) {
                at.translate(w / 2, h / 2);
                at.rotate(Math.PI/180*rotationAngle);
                //at.scale(0.25, 0.25);
                at.translate(-w / 2, -h / 2);
            }
            g2d.drawImage(image, at, null);
            for (int i = 0; i < RECTANGLE_COUNT; i++) {
                Point topLeft = new Point((int) TOP_LEFT_CORNERS.get(i).getCenterX(), (int) TOP_LEFT_CORNERS.get(i).getCenterY());
                Point topRight = new Point((int) TOP_RIGHT_CORNERS.get(i).getCenterX(), (int) TOP_RIGHT_CORNERS.get(i).getCenterY());
                Point bottomLeft = new Point((int) BOTTOM_LEFT_CORNERS.get(i).getCenterX(), (int) BOTTOM_LEFT_CORNERS.get(i).getCenterY());
                Point bottomRight = new Point((int) BOTTOM_RIGHT_CORNERS.get(i).getCenterX(), (int) BOTTOM_RIGHT_CORNERS.get(i).getCenterY());
                
                if (RECTANGLE_COUNT == 3 && i == 1) {
                    g2d.dispose();
                    int sw = (int) topLeft.distance(topRight) + 6;
                    int sh = (int) topLeft.distance(bottomLeft) + 6;
                    BufferedImage subImage = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_RGB);
                    Graphics2D sg2d = subImage.createGraphics();
                    BufferedImage temp = image.getSubimage(topLeft.x - 3, topLeft.y - 3, sw, sh);
                    AffineTransform sat = new AffineTransform();
            
                    if(Math.abs(rotationAngle) > 0.3d) {
                        sat.translate(sw / 2, sh / 2);
                        sat.rotate(Math.PI/180*rotationAngle);
                        //at.scale(0.25, 0.25);
                        sat.translate(-sw / 2, -sh / 2);
                    }
                    sg2d.drawImage(temp, at, null);
            
                    
                    sg2d.dispose();
                    return subImage;
                }
                
                
                g2d.setColor(Color.pink);
                g2d.drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y);
                g2d.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
                g2d.drawLine(bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y);
                g2d.drawLine(bottomRight.x, bottomRight.y, topRight.x, topRight.y);
                
                g2d.setColor(Color.orange);
                g2d.drawLine(topLeft.x, topLeft.y, topRight.x, topLeft.y);
                g2d.drawLine(bottomLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
                g2d.drawLine(bottomRight.x, bottomRight.y, bottomLeft.x, bottomRight.y);
                g2d.drawLine(topRight.x, bottomRight.y, topRight.x, topRight.y);
            }
        }
        
        
        g2d.dispose();
        return binaryImage;
    }
    
    public static double getAngle(Point p1, Point p2) {
        return Math.toDegrees(Math.atan2(p1.x - p2.x, p1.y - p2.y));
    }
    
    private static BufferedImage getCorners(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        List<Rectangle> TOP_LEFT_CORNERS_TEMP = new ArrayList<>();
        List<Rectangle> TOP_RIGHT_CORNERS_TEMP = new ArrayList<>();
        List<Rectangle> BOTTOM_LEFT_CORNERS_TEMP = new ArrayList<>();
        List<Rectangle> BOTTOM_RIGHT_CORNERS_TEMP = new ArrayList<>();
    
        BufferedImage binaryImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = binaryImage.createGraphics();
        g2d.drawImage(image, null, 0, 0);
        
        boolean raster[][] =  new boolean[w][h];
        int ssw = new Color(240, 240, 240).getRGB();
        
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
        
                int rgb = image.getRGB(x, y);
                
                if (rgb > ssw) {
                    raster[x][y] = false;
//                    g2d.setColor(Color.white);
//                    g2d.drawLine(x, y, x, y);
                } else {
                    raster[x][y] = true;
//                    g2d.setColor(Color.black);
//                    g2d.drawLine(x, y, x, y);
                }                    
            }
        }
        
        for (int y = ((PATTERN_SIZE/2)*PATTERN_GAB); y < h - ((PATTERN_SIZE/2)*PATTERN_GAB); y++) {
            for (int x = ((PATTERN_SIZE/2)*PATTERN_GAB); x < w - ((PATTERN_SIZE/2)*PATTERN_GAB); x++) {
                if (findPattern(TOP_LEFT, raster, x-((PATTERN_SIZE/2)*PATTERN_GAB), y-((PATTERN_SIZE/2)*PATTERN_GAB))){
                    boolean added = false;
                    Point p = new Point (x, y);
                    for (Rectangle rect : TOP_LEFT_CORNERS_TEMP) {
                        if(p.distance(rect.getCenterX(), rect.getCenterY()) < Math.max(rect.getCenterX()-rect.getX(), rect.getCenterY()-rect.getY()) + 2) {
                            rect.add(p);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        TOP_LEFT_CORNERS_TEMP.add(new Rectangle(p));
                    }
                }
                if (findPattern(TOP_RIGHT, raster, x-((PATTERN_SIZE/2)*PATTERN_GAB), y-((PATTERN_SIZE/2)*PATTERN_GAB))){
                    boolean added = false;
                    Point p = new Point (x, y);
                    for (Rectangle rect : TOP_RIGHT_CORNERS_TEMP) {
                        if(p.distance(rect.getCenterX(), rect.getCenterY()) < Math.max(rect.getCenterX()-rect.getX(), rect.getCenterY()-rect.getY()) + 2) {
                            rect.add(p);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        TOP_RIGHT_CORNERS_TEMP.add(new Rectangle(p));
                    }
                }
                if (findPattern(BOTTOM_LEFT, raster, x-((PATTERN_SIZE/2)*PATTERN_GAB), y-((PATTERN_SIZE/2)*PATTERN_GAB))){
                    boolean added = false;
                    Point p = new Point (x, y);
                    for (Rectangle rect : BOTTOM_LEFT_CORNERS_TEMP) {
                        if(p.distance(rect.getCenterX(), rect.getCenterY()) < Math.max(rect.getCenterX()-rect.getX(), rect.getCenterY()-rect.getY()) + 2) {
                            rect.add(p);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        BOTTOM_LEFT_CORNERS_TEMP.add(new Rectangle(p));
                    }
                }
                if (findPattern(BOTTOM_RIGHT, raster, x-((PATTERN_SIZE/2)*PATTERN_GAB), y-((PATTERN_SIZE/2)*PATTERN_GAB))){
                    boolean added = false;
                    Point p = new Point (x, y);
                    for (Rectangle rect : BOTTOM_RIGHT_CORNERS_TEMP) {
                        if(p.distance(rect.getCenterX(), rect.getCenterY()) < Math.max(rect.getCenterX()-rect.getX(), rect.getCenterY()-rect.getY()) + 2) {
                            rect.add(p);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        BOTTOM_RIGHT_CORNERS_TEMP.add(new Rectangle(p));
                    }
                }
            }
        }
        int pSize = 9;
        TOP_LEFT_CORNERS_TEMP.stream().filter((rect) -> (rect.getWidth() * rect.getHeight() >= pSize )).forEachOrdered((rect) -> {
            TOP_LEFT_CORNERS.add(rect);
            int x = (int) rect.getCenterX();
            int y = (int) rect.getCenterY();
            System.out.println("TopLeftCorner as:" + String.valueOf(x) + " x " + String.valueOf(y));
//            g2d.setColor(Color.red);
//            g2d.fillOval(x-5, y-5, 10, 10);
//            g2d.setColor(Color.black);
//            g2d.drawOval(x-5, y-5, 10, 10);
        });
        TOP_RIGHT_CORNERS_TEMP.stream().filter((rect) -> (rect.getWidth() * rect.getHeight() >= pSize )).forEachOrdered((rect) -> {
            TOP_RIGHT_CORNERS.add(rect);
            int x = (int) rect.getCenterX();
            int y = (int) rect.getCenterY();
            System.out.println("TopRightCorner as:" + String.valueOf(x) + " x " + String.valueOf(y));
//            g2d.setColor(Color.blue);
//            g2d.fillOval(x-5, y-5, 10, 10);
//            g2d.setColor(Color.black);
//            g2d.drawOval(x-5, y-5, 10, 10);
        });
        BOTTOM_LEFT_CORNERS_TEMP.stream().filter((rect) -> (rect.getWidth() * rect.getHeight() >= pSize )).forEachOrdered((rect) -> {
            BOTTOM_LEFT_CORNERS.add(rect);
            int x = (int) rect.getCenterX();
            int y = (int) rect.getCenterY();
            System.out.println("BottomLeftCorner as:" + String.valueOf(x) + " x " + String.valueOf(y));
//            g2d.setColor(Color.green);
//            g2d.fillOval(x-5, y-5, 10, 10);
//            g2d.setColor(Color.black);
//            g2d.drawOval(x-5, y-5, 10, 10);
        });
        BOTTOM_RIGHT_CORNERS_TEMP.stream().filter((rect) -> (rect.getWidth() * rect.getHeight() >= pSize )).forEachOrdered((rect) -> {
            BOTTOM_RIGHT_CORNERS.add(rect);
            int x = (int) rect.getCenterX();
            int y = (int) rect.getCenterY();
            System.out.println("BottomRightCorner as:" + String.valueOf(x) + " x " + String.valueOf(y));
//            g2d.setColor(Color.yellow);
//            g2d.fillOval(x-5, y-5, 10, 10);
//            g2d.setColor(Color.black);
//            g2d.drawOval(x-5, y-5, 10, 10);
        });

        g2d.dispose();
        return binaryImage;
    }
    
    private static boolean findPattern(boolean[][] pattern, boolean[][] image, int imageX, int imageY) {
        for (int y = 0; y < PATTERN_SIZE; y++) {
            for (int x = 0; x < PATTERN_SIZE; x++) {
                //xy-change spiegelverkehrt
                if (pattern[y][x] != image[x * PATTERN_GAB + imageX][y * PATTERN_GAB + imageY]) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PDFErkennung");

        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane.setViewportView(jLabel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1329, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
}
