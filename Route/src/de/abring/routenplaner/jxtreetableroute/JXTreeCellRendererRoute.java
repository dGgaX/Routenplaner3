/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.jxtreetableroute;

import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Bring
 */
public final class JXTreeCellRendererRoute extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 1L;

    private final Icon iconLC = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconLC.png"));
    private final Icon iconMM = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconMM.png"));
    private final Icon iconSa = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconSa.png"));
    private final Icon iconKunde = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconKunde.png"));
    private final Icon iconOSM = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconOSM.png"));
    private final Icon iconW = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconW.png"));
    private final Icon iconP = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconP.png"));
    private final Icon iconFlag = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconFlag.png"));
    private final Icon iconStart = new ImageIcon(getClass().getResource("/de/abring/routenplaner/jxtreetableroute/images/iconStart.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
//        if (value instanceof JXTreeRouteEntry)
//            setText(((JXTreeRouteEntry) value).getName());
        if (value instanceof JXTreeRouteStart) {
            setIcon(iconStart);
        } else if (value instanceof JXTreeRouteEnd) {
            setIcon(iconFlag);
        } else if (value instanceof JXTreeRouteRoute) {
            setIcon(iconOSM);
        } else if (value instanceof JXTreeRouteItem) {
            setIcon(iconP);
        } else if (value instanceof JXTreeRouteAddress) {
            JXTreeRouteAddress address = (JXTreeRouteAddress) value;
//            String text = "";
//            if (address.getID() > 0)
//                text += String.valueOf(address.getID() + ". ");
//            text += address.getName();
            if (address.getName().startsWith("LC-Service") || address.getName().startsWith("Löschmann & Claßen")) {
                setIcon(iconLC);
            } else if (address.getName().startsWith("Media Markt")) {
                setIcon(iconMM);
            } else if (address.getName().startsWith("Saturn")) {
                setIcon(iconSa);
            } else {
                setIcon(iconKunde);
            }
//            setText(text);
        } else if (value instanceof JXTreeRouteTour) {
            setIcon(((JXTreeRouteTour) value).getIcon());
        }
        
        
        setText("");
        return this;
    }
    
}
