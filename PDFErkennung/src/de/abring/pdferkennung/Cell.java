/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Karima
 */
public class Cell {

    private BufferedImage image;
        
    private Rectangle positionOnParent = null;
    
    private float shaddow = 0.9f;
    
    private final List<Cell> children = new ArrayList<>();
    private final List<Cell> parts = new ArrayList<>();
    
    /**
     *
     * @param image
     */
    public Cell (BufferedImage image) {
        this.image = image;
        
        updateChildren();
        
        for (Cell child : this.children) {
            child.updateParts();
        }
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }
    
    /**
     * @return the image
     */
    public BufferedImage getMaskedImage() {
        BufferedImage binaryImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = binaryImage.createGraphics();
        //g2d.drawImage(image, null, 0, 0);
        
        int rnd_red = (int)Math.floor(Math.random() * 255.0f);
        int rnd_green = (int)Math.floor(Math.random() * 255.0f);
        int rnd_blue = (int)Math.floor(Math.random() * 255.0f);
                
                
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                
                int red   = Math.min(Math.max(0, (rgb >> 16) & 0xFF), 255);
                int green = Math.min(Math.max(0, (rgb >> 8 ) & 0xFF), 255);
                int blue  = Math.min(Math.max(0, (rgb      ) & 0xFF), 255);
                Color mrgb = new Color(Math.round((red + rnd_red) / 2.0f), Math.round((green + rnd_green) / 2.0f), Math.round((blue + rnd_blue) / 2.0f));
                
                binaryImage.setRGB(x, y, mrgb.getRGB());
            }
        }
        
        for (int i = 0; i < this.children.size(); i++) {
            Cell child = this.children.get(i);
            if (child.getPositionOnParent()!= null) {
                g2d.drawImage(child.getMaskedImage(), child.getPositionOnParent().x, child.getPositionOnParent().y, null);
            }
            g2d.drawString(String.valueOf(i), child.getPositionOnParent().x, child.getPositionOnParent().y + 10);
        }
        
        for (int i = 0; i < this.parts.size(); i++) {
            Cell part = this.parts.get(i);
            if (part.getPositionOnParent()!= null) {
                g2d.drawImage(part.getMaskedImage(), part.getPositionOnParent().x, part.getPositionOnParent().y, null);
            }
            g2d.drawString(String.valueOf(i), part.getPositionOnParent().x, part.getPositionOnParent().y + 10);
        }
        
        return binaryImage;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the children
     */
    public List<Cell> getChildList() {
        return children;
    }

    /**
     * @return the parts
     */
    public List<Cell> getPartsList() {
        return parts;
    }
    /**
     * @param c
     * @return a child
     */
    public Cell getChild(int c) {
        return children.get(c);
    }

    /**
     * @param p
     * @return a part
     */
    public Cell getPart(int p) {
        return parts.get(p);
    }

    /**
     * 
     */
    private void updateChildren() {
        
        System.out.println("Ich schau mal nach neuen Kindern!");
        System.out.println("-----------------------------------------------------------------------");
        
        List<Cell> newChildren = Manipulator.FIND_FRAMED_CHILDREN(this.image);
        if (newChildren != null) {
            this.children.clear();
            this.children.addAll(newChildren);
        }
    }

    /**
     * 
     */
    private void updateParts() {
        
        System.out.println("Ich schau mal nach neuen Parts!");
        System.out.println("-----------------------------------------------------------------------");
        
        List<Cell> newParts = Manipulator.FIND_FRAMED_PARTS(this.image);
        if (newParts != null) {
            this.parts.clear();
            this.parts.addAll(newParts);
        }
        
        sortParts();
        
    }

    /**
     * Sort Parts like appierence on Parent ...
     */
    private void sortParts() {
        Collections.sort(this.parts, new Comparator<Cell>() {
        
            @Override
            public int compare(Cell c1, Cell c2) {
                    if (c2.getPositionOnParent().x - c1.getPositionOnParent().x > 10 && c1.getPositionOnParent().x < c2.getPositionOnParent().x) {
                        return -1;
                    }
                    return 1;
            }
            
        });
        
        Collections.sort(this.parts, new Comparator<Cell>() {
        
            @Override
            public int compare(Cell c1, Cell c2) {
                    if (c2.getPositionOnParent().y - c1.getPositionOnParent().y > 10 && c1.getPositionOnParent().y < c2.getPositionOnParent().y) {
                        return -1;
                    }
                    return 1;
            }
            
        });
    }
    
    /**
     * @return the positionOnParent
     */
    public Rectangle getPositionOnParent() {
        return positionOnParent;
    }

    /**
     * @param positionOnParent the positionOnParent to set
     */
    public void setPositionOnParent(Rectangle positionOnParent) {
        this.positionOnParent = positionOnParent;
    }

    /**
     * @return the shaddow
     */
    public float getShaddow() {
        return shaddow;
    }

    /**
     * @param shaddow the shaddow to set
     */
    public void setShaddow(float shaddow) {
        this.children.forEach((child) -> {
            child.setShaddow(shaddow);
        });
        this.parts.forEach((part) -> {
            part.setShaddow(shaddow);
        });
        this.shaddow = shaddow;
    }
    
    
}
