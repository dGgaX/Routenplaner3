/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karima
 */
public class Manipulator {
    
    
    public static final int MARGIN = 25;
    private static final int PATTERN_SIZE = Mask.PATTERN_SIZE;
    private static final int PATTERN_GAB = 10;
    private static final byte NULL = Mask.NULL;
    private static final byte FALSE = Mask.FALSE;
    private static final byte TRUE = Mask.TRUE;
    
    /**
     * 
     * @param image
     * @return 
     */
    public static byte[][] IMAGE_TO_BYTERASTER (BufferedImage image) {
        byte byteRaster[][] = new byte[image.getWidth()][image.getHeight()];
        int ssw = new Color(240, 240, 240).getRGB();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb <= ssw)
                    byteRaster[x][y] = TRUE;
                else
                    byteRaster[x][y] = FALSE;
            }
        }
        return byteRaster;
    }
    
    /**
     * 
     * @param image
     * @return children
     */
    public static List<Cell> FIND_FRAMED_CHILDREN (BufferedImage image) {
        List<Cell> children = new ArrayList<>();
        
        List<List<Rectangle>> corners = new ArrayList<List<Rectangle>>();
        
        for (int i = 0; i < Mask.CORNER_MASKS.length; i++)
            corners.add(new ArrayList<>());
        
        image = image.getSubimage(MARGIN, MARGIN, image.getWidth() - MARGIN, image.getHeight() - MARGIN);
                
        if (image == null)
            return children;
        
        image = getPattern(image, corners, Mask.CORNER_MASKS, true);
        
//        image = spinImage(image, corners);
        if (image == null)
            return children;
        
        List<Cell> cells = cropImagesToChildren(image, corners);
        
        if (cells == null)
            return children;
        
        children.addAll(cells);
        
        return children;
    }

    /**
     * 
     * @param image
     * @return parts
     */
    public static List<Cell> FIND_FRAMED_PARTS (BufferedImage image) {
        List<Cell> parts = new ArrayList<>();
        
        List<List<Rectangle>> ts = new ArrayList<List<Rectangle>>();
        
        for (int i = 0; i < Mask.ALL_MASKS.length; i++)
            ts.add(new ArrayList<>());
        
        if (image == null)
            return parts;
        
        image = getPattern(image, ts, Mask.ALL_MASKS, true);
        
//        image = spinImage(image, corners);
        if (image == null)
            return parts;
        
        List<Cell> cells = cropImagesToParts(image, ts);
        
        if (cells == null)
            return cells;
        
        parts.addAll(cells);
        
        return parts;
    }

    private static BufferedImage getPattern(BufferedImage image, List<List<Rectangle>> pattern, byte[][][] masks, boolean doubleCheck) {
        int w = image.getWidth();
        int h = image.getHeight();
        int pSize = 2;
        BufferedImage binaryImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = binaryImage.createGraphics();
        g2d.drawImage(image, null, 0, 0);
        
        
        byte raster[][] = IMAGE_TO_BYTERASTER(image);
        
        List<List<Rectangle>> tempPattern2 = new ArrayList<List<Rectangle>>();
        
        
        for (int i = 0; i < masks.length; i++) {
            
            List<Rectangle> tempPattern = new ArrayList<>();
            
            byte[][] mask = masks[i];
            
            for (int y = ((PATTERN_SIZE/2)*PATTERN_GAB); y < h - ((PATTERN_SIZE/2)*PATTERN_GAB); y++) {
                for (int x = ((PATTERN_SIZE/2)*PATTERN_GAB); x < w - ((PATTERN_SIZE/2)*PATTERN_GAB); x++) {
                    if (findPattern(mask, raster, x-((PATTERN_SIZE/2)*PATTERN_GAB), y-((PATTERN_SIZE/2)*PATTERN_GAB))){
                        boolean added = false;
                        Point p = new Point (x, y);
                        for (Rectangle rect : tempPattern) {
                            if(p.distance(rect.getCenterX(), rect.getCenterY()) < Math.max(rect.getCenterX()-rect.getX(), rect.getCenterY()-rect.getY()) + 2) {
                                rect.add(p);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            Rectangle newRect = new Rectangle(p);
                            //newRect.setSize(1, 1);
                            tempPattern.add(newRect);
                        }
                    }
                }
            }
            List<Rectangle> thisPattern = new ArrayList<>();
            
            for (Rectangle rect2 : tempPattern) {
                    
                if (rect2.getWidth() * rect2.getHeight() >= pSize) {
                    thisPattern.add(rect2);
                }
                
            }
            
            tempPattern2.add(thisPattern);
            
        }
        
        if (doubleCheck) {
            for (int i = 0; i < masks.length; i++) {
        
                int dist = 10;
                for (Rectangle rect2 : tempPattern2.get(i)) {
                    int vert = 0;
                    int hori = 0;
                    for (int j = 0; j < masks.length; j++) {
                        for (Rectangle rect3 : tempPattern2.get(j)) {
                            if (!rect2.equals(rect3)) {
                                if ((rect2.y - rect3.y) < dist && (rect2.y - rect3.y) > -dist) {
                                    vert++;
                                }
                                if ((rect2.x - rect3.x) < dist && (rect2.x - rect3.x) > -dist) {
                                    hori++;
                                }
                            }
                        }
                    }
                    if (vert > 0 && hori > 0) {
                        pattern.get(i).add(rect2);
                        int x = (int) rect2.getCenterX();
                        int y = (int) rect2.getCenterY();
                        System.out.println("Corner at " + String.valueOf(x) + " x " + String.valueOf(y));
//                        Color c = Color.WHITE;
//                        
//                        switch(i) {
//                            case 0:
//                                c = Color.RED;
//                                break;
//                            case 1:
//                                c = Color.GREEN;
//                                break;
//                            case 2:
//                                c = Color.BLUE;
//                                break;
//                            case 3:
//                                c = Color.YELLOW;
//                                break;
//                            case 4:
//                                c = Color.CYAN;
//                                break;
//                            case 5:
//                                c = Color.MAGENTA;
//                                break;
//                            case 6:
//                                c = Color.PINK;
//                                break;
//                            case 7:
//                                c = Color.ORANGE;
//                                break;
//                            case 8:
//                                c = Color.GRAY;
//                                break;
//                        }
//                        
//                        g2d.setColor(c);
//                        g2d.fillOval(x-5, y-5, 10, 10);
//                        g2d.setColor(Color.black);
//                        g2d.drawOval(x-5, y-5, 10, 10);
                    }
                }
            }
        } else {
            pattern.clear();
            pattern.addAll(tempPattern2);
        }
        return binaryImage;
    }

    private static boolean findPattern(byte[][] pattern, byte[][] image, int imageX, int imageY) {

        for (int y = 0; y < PATTERN_SIZE; y++) {
            for (int x = 0; x < PATTERN_SIZE; x++) {
                //xy-change spiegelverkehrt
                    if (pattern[y][x] != image[x * PATTERN_GAB + imageX][y * PATTERN_GAB + imageY] && pattern[y][x] != NULL) {
                        return false;
                    }
            }
        }
        return true;
    }

    private static BufferedImage spinImage(BufferedImage image, List<List<Rectangle>> corners) {
//        int w = image.getWidth();
//        int h = image.getHeight();
//        BufferedImage binaryImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = binaryImage.createGraphics();
//        int ALL_ENTRY_COUNT = corners[0].size() + TOP_RIGHT_CORNERS.size() + BOTTOM_LEFT_CORNERS.size() + BOTTOM_RIGHT_CORNERS.size();
//        if (ALL_ENTRY_COUNT % 4 == 0 && TOP_LEFT_CORNERS.size() == TOP_RIGHT_CORNERS.size() && TOP_RIGHT_CORNERS.size() == BOTTOM_LEFT_CORNERS.size() && BOTTOM_LEFT_CORNERS.size() == BOTTOM_RIGHT_CORNERS.size()) {
//            int RECTANGLE_COUNT = ALL_ENTRY_COUNT / 4;
//            double rotationAngle = 0;
//            for (int i = 0; i < RECTANGLE_COUNT; i++) {
//                Point topLeft = new Point((int) TOP_LEFT_CORNERS.get(i).getCenterX(), (int) TOP_LEFT_CORNERS.get(i).getCenterY());
//                Point topRight = new Point((int) TOP_RIGHT_CORNERS.get(i).getCenterX(), (int) TOP_RIGHT_CORNERS.get(i).getCenterY());
//                Point bottomLeft = new Point((int) BOTTOM_LEFT_CORNERS.get(i).getCenterX(), (int) BOTTOM_LEFT_CORNERS.get(i).getCenterY());
//                Point bottomRight = new Point((int) BOTTOM_RIGHT_CORNERS.get(i).getCenterX(), (int) BOTTOM_RIGHT_CORNERS.get(i).getCenterY());
//                System.out.println();
//                System.out.println("Rectangle No.: " + String.valueOf(i));
//                System.out.println("Offset Angle: " + String.valueOf(getAngle(topLeft, topRight) - getAngle(topLeft, new Point(topRight.x, topLeft.y))));
////                System.out.println("Offset Angle: " + String.valueOf(getAngle(topLeft, bottomLeft) - getAngle(new Point(bottomLeft.x, topLeft.y), bottomLeft)));
//                System.out.println("Offset Angle: " + String.valueOf(getAngle(bottomRight, bottomLeft) - getAngle(bottomRight, new Point(bottomLeft.x, bottomRight.y))));
////                System.out.println("Offset Angle: " + String.valueOf(getAngle(bottomRight, topRight) - getAngle(new Point(topRight.x, bottomRight.y), topRight)));
//                
//                double angle1 = getAngle(topLeft, topRight) - getAngle(topLeft, new Point(topRight.x, topLeft.y));
//                if (angle1 < 0)
//                    rotationAngle = Math.min(rotationAngle, angle1);
//                else
//                    rotationAngle = Math.max(rotationAngle, angle1);
//                double angle2 = getAngle(bottomRight, bottomLeft) - getAngle(bottomRight, new Point(bottomLeft.x, bottomRight.y));
//                if (angle2 < 0)
//                    rotationAngle = Math.min(rotationAngle, angle2);
//                else
//                    rotationAngle = Math.max(rotationAngle, angle2);
//                
//            }
//            AffineTransform at = new AffineTransform();
//            
//            if(Math.abs(rotationAngle) > 0.3d) {
//                at.translate(w / 2, h / 2);
//                at.rotate(Math.PI/180*rotationAngle);
//                //at.scale(0.25, 0.25);
//                at.translate(-w / 2, -h / 2);
//            }
//            g2d.drawImage(image, at, null);
//        
//            g2d.dispose();
//            return binaryImage;
//        } 
        return null;
    }
    
    public static double getAngle(Point p1, Point p2) {
        return Math.toDegrees(Math.atan2(p1.x - p2.x, p1.y - p2.y));
    }
    
    private static List<Cell> cropImagesToChildren(BufferedImage image, List<List<Rectangle>> corners) {
        
        int ALL_ENTRY_COUNT = 0;
        for (List<Rectangle> corner : corners) {
            ALL_ENTRY_COUNT += corner.size();
        }
        if (ALL_ENTRY_COUNT % 4 == 0) {
            List<Cell> cells = new ArrayList<>();
            int RECTANGLE_COUNT = ALL_ENTRY_COUNT / 4;
            
            if (RECTANGLE_COUNT == 0) {
                return cells;
            }
                
            System.out.print("Mach mir ");
            System.out.print(RECTANGLE_COUNT);
            System.out.println(" neue Kinder!!!");
            System.out.println("------------------------------------------------");
            
            for (int i = 0; i < RECTANGLE_COUNT; i++) {
                Point topLeft = new Point((int) corners.get(0).get(i).getCenterX(), (int) corners.get(0).get(i).getCenterY());
                Point topRight = new Point((int) corners.get(1).get(i).getCenterX(), (int) corners.get(1).get(i).getCenterY());
                Point bottomLeft = new Point((int) corners.get(2).get(i).getCenterX(), (int) corners.get(2).get(i).getCenterY());
                //Point bottomRight = new Point((int) corners[3].get(i).getCenterX(), (int) corners[3].get(i).getCenterY());
                int sw = (int) topLeft.distance(topRight) + 0;
                int sh = (int) topLeft.distance(bottomLeft) + 0;
                
                int x = (int) Math.round(topLeft.x - MARGIN);
                int y = (int) Math.round(topLeft.y - MARGIN);
                int w = (int) Math.round(sw + (2 * MARGIN) + 1);
                int h = (int) Math.round(sh + (2 * MARGIN) + 1);


                BufferedImage temp = image.getSubimage(x, y, w, h);

                Cell child = new Cell(temp);
                child.setPositionOnParent(new Rectangle(x + MARGIN, y + MARGIN, w - MARGIN, h - MARGIN));
                //child.setPositionOnParent(new Rectangle(x - MARGIN, y - MARGIN, w + (2 * MARGIN) + 1, h + (2 * MARGIN) + 1));
                cells.add(child);
                System.out.println("Neues Kind!!!");
                

            }
            return cells;
        }
        return null;
    }

    private static List<Cell> cropImagesToParts(BufferedImage image, List<List<Rectangle>> parts) {
        int allCorners = 0;
        int allTs      = 0;
        int allCrosses = 0;
        int allTL = parts.get(0).size() + 
                    parts.get(4).size() + 
                    parts.get(5).size() + 
                    parts.get(8).size();
        
        for (int i = 0; i < 4; i++) {
            allCorners += parts.get(i).size();
        }
        for (int i = 4; i < 8; i++) {
            allTs += parts.get(i).size();
        }
        allCrosses += parts.get(8).size();
        
                
        if (allCorners % 4 == 0 && (allTs - (allCrosses * 2)) % 2 == 0) {
            List<Cell> cells = new ArrayList<>();
        
            if (allTL == 0) {
                return cells;
            }
            
            System.out.print("Mach mir ");
            System.out.print(allTL);
            System.out.println("neue Parts!!!");
            System.out.println("------------------------------------------------");
            
            //ALL TopLeftCorners
            cells.addAll(findEveryPart(image, parts, 0));
            //ALL TopTs
            cells.addAll(findEveryPart(image, parts, 4));
            //ALL LeftTs
            cells.addAll(findEveryPart(image, parts, 5));
            //ALL Crosses
            cells.addAll(findEveryPart(image, parts, 8));
            
            
            return cells;
        }
        
        return null;
        
    }

    private static List<Cell> findEveryPart (BufferedImage image, List<List<Rectangle>> parts, int i) {
        List<Cell> cells = new ArrayList<>();
        int dist = 10;
        for (Rectangle corner : parts.get(i)) {
            //Have a look to AllParts to find the nearest Part
            Rectangle nearestHoriPart = null;
            Rectangle nearestVertPart = null;
            for (int j = 0; j < parts.size(); j++) {
                for (Rectangle part : parts.get(j)) {
                    //Vertical
                    if ((corner.y - part.y) < dist && (corner.y - part.y) > -dist && 
                        (j == 1 || j == 4 || j == 6 || j == 8)) {
                        if (nearestVertPart == null && part.x - corner.x > 0) {
                            nearestVertPart = part;
                        } else if (part.x - corner.x > 0 && part.x - corner.x < nearestVertPart.x - corner.x && 
                        (j == 1 || j == 4 || j == 6 || j == 8)) {
                            nearestVertPart = part;
                        }
                    }
                    //Horizontal
                    if ((corner.x - part.x) < dist && (corner.x - part.x) > -dist && 
                        (j == 2 || j == 5 || j == 7 || j == 8)) {
                        if (nearestHoriPart == null && part.y - corner.y > 0) {
                            nearestHoriPart = part;
                        } else if (part.y - corner.y > 0 && part.y - corner.y < nearestHoriPart.y - corner.y && 
                        (j == 2 || j == 5 || j == 7 || j == 8)) {
                            nearestHoriPart = part;
                        }

                    }
                }
            }
            if (nearestVertPart == null || nearestHoriPart == null) {
                continue;
            }
            
            int bbMargin = 3;
            int x = (int) Math.round(corner.getCenterX()) + bbMargin;
            int y = (int) Math.round(corner.getCenterY()) + bbMargin;
            int w = (int) Math.round(nearestVertPart.getCenterX()) - x - bbMargin;
            int h = (int) Math.round(nearestHoriPart.getCenterY()) - y - bbMargin;
                
            BufferedImage temp = image.getSubimage(x, y, w, h);
            
            Cell part = new Cell(temp);
            part.setPositionOnParent(new Rectangle(x, y, w, h));
            cells.add(part);
            System.out.println("Neuer Part!!!");
            
        }
        
        return cells;
    }
}
