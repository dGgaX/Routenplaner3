/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.gui.snippingtool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karima
 */
public class JPaintPane extends javax.swing.JPanel {

    private Rectangle rectBounds = new Rectangle(new Dimension(0, 0));
    private boolean drawRect = false;
    private Color rectColor = Color.black;
    
    private List<PercentDimension> parts = new ArrayList<>();
    
    /**
     *
     */
    public JPaintPane() {
        initComponents();
    }
    
    /**
     *
     * @param g the Graphics Elements
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (drawRect) {
            Color temp = g.getColor();
            g.setColor(rectColor);
            g.drawRect(rectBounds.x, rectBounds.y, rectBounds.width, rectBounds.height);
        
            for (PercentDimension part : parts) {
                Rectangle partBounds = part.getDimension(rectBounds);
                g.drawRect(partBounds.x, partBounds.y, partBounds.width, partBounds.height);
            }
            g.setColor(temp);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param drawRect the drawRect to set
     */
    public void setDrawRect(boolean drawRect) {
        this.drawRect = drawRect;
    }

    /**
     * @param rectColor the rectColor to set
     */
    public void setRectColor(Color rectColor) {
        this.rectColor = rectColor;
    }

    /**
     * @param rectBounds the rectBounds to set
     */
    public void setRectBounds(Rectangle rectBounds) {
        this.rectBounds = rectBounds;
    }

    /**
     * @return the parts
     */
    public List<PercentDimension> getParts() {
        return parts;
    }

    /**
     * @param parts the parts to set
     */
    public void setParts(List<PercentDimension> parts) {
        this.parts = parts;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
