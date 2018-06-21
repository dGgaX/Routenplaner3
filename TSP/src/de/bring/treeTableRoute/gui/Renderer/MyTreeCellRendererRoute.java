
package de.bring.treeTableRoute.gui.Renderer;

import de.bring.treeTableRoute.entries.*;
import java.awt.Component;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRendererRoute extends DefaultTreeCellRenderer {
    
    private final Icon iconLC = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconLC.png"));
    private final Icon iconMM = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconMM.png"));
    private final Icon iconSa = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconSa.png"));
    private final Icon iconKunde = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconKunde.png"));
    private final Icon iconOSM = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconOSM.png"));
    private final Icon iconW = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconW.png"));
    private final Icon iconP = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconP.png"));
    private final Icon iconFlag = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconFlag.png"));
    private final Icon iconStart = new ImageIcon(getClass().getResource("/de/bring/treeTableRoute/images/iconStart.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        setText("");
        if (value instanceof TreeAddress) {
            TreeAddress address = (TreeAddress) value;
            String text = "";
            if (address.getID() > 0)
                text += String.valueOf(address.getID() + ". ");
            if (address.getName().startsWith("LC-Service")) {
                setIcon(iconLC);
            } else if (address.getName().startsWith("Media Markt")) {
                setIcon(iconMM);
            } else if (address.getName().startsWith("Saturn")) {
                setIcon(iconSa);
            } else {
                setIcon(iconKunde);
            }
            setText(text);
        } else if (value instanceof TreeStart) {
            setIcon(iconStart);
        } else if (value instanceof TreeEnd) {
            setIcon(iconFlag);
        } else if (value instanceof TreeRoute) {
            setIcon(iconOSM);
        } else if (value instanceof TreeOrder) {
            setIcon(iconW);
        } else if (value instanceof TreeProduct) {
            setIcon(iconP);
        }
        
        return this;
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
    }
}
