/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui.dialogues;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Karima
 */
public class MSG {
    /**
     * msgSaveAborted
     * @param frame
     * @param file 
     */
    public static final void msgSaveAborted(Component frame, String file) {
        JOptionPane.showMessageDialog(frame, "Achtung, die Datei \"" + file + "\" konnte nicht gespeichert werden.", "Hinweis", JOptionPane.OK_OPTION);
    }
    
    /**
     * msgOpenAborted
     * @param frame 
     */
    public static final void msgOpenAborted(Component frame) {
        JOptionPane.showMessageDialog(frame, "Achtung, die Datei konnte nicht geöffnet werden.", "Hinweis", JOptionPane.OK_OPTION);
    }
    
    /**
     * msgPrintAborted
     * @param frame 
     */
    public static final void msgPrintAborted(Component frame) {
        JOptionPane.showMessageDialog(frame, "Achtung, die Daten konnten nicht gedruckt werden.", "Hinweis", JOptionPane.OK_OPTION);
    }
    
    /**
     * msgSaveChanges
     * @param frame
     * @return 
     */
    public static final boolean msgSaveChanges(Component frame) {
        return (JOptionPane.showConfirmDialog(frame, "Sollen die Änderungen vorher gespeichert werden?", "Achtung!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }
    
}
