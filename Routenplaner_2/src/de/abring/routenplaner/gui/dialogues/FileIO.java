/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui.dialogues;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Karima
 */
public class FileIO {
    
    public static final File getOpenRouteFile(Component frame, String workingDir) {
        JFileChooser chooser = new JFileChooser(workingDir);
        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".serlcroute");
            }
            @Override
            public String getDescription() {
                return "Route-Dateien";
            }
        };
        javax.swing.filechooser.FileFilter AllFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return "Alle Dateien";
            }
        };
        chooser.setDialogTitle("Route öffnen");
        chooser.addChoosableFileFilter(AllFilter);
        chooser.setFileFilter(PDFFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
    public static final File getOpenFavoritenFile(Component frame, String programDir) {
        JFileChooser chooser = new JFileChooser(programDir + "\\vorlagen");
        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".serlcfav");
            }
            @Override
            public String getDescription() {
                return "Favoriten-Dateien";
            }
        };
        javax.swing.filechooser.FileFilter AllFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return "Alle Dateien";
            }
        };
        chooser.setDialogTitle("Favoriten öffnen");
        chooser.addChoosableFileFilter(AllFilter);
        chooser.setFileFilter(PDFFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
    public static final File getSaveRouteFile(Component frame, File fileName, String workingDir) {
        JFileChooser chooser = new JFileChooser(workingDir);
        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".serlcroute");
            }
            @Override
            public String getDescription() {
                return "Route-Dateien";
            }
        };
        javax.swing.filechooser.FileFilter AllFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return "Alle Dateien";
            }
        };
        chooser.setDialogTitle("Route speichern");
        chooser.addChoosableFileFilter(AllFilter);
        chooser.setFileFilter(PDFFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(fileName);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) { 
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
    public static final File getSaveFavoritenFile(Component frame, File fileName, String programDir) {
        JFileChooser chooser = new JFileChooser(programDir + "\\vorlagen");
        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".serlcfav");
            }
            @Override
            public String getDescription() {
                return "Favoriten-Dateien";
            }
        };
        javax.swing.filechooser.FileFilter AllFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return "Alle Dateien";
            }
        };
        chooser.setDialogTitle("Favoriten speichern");
        chooser.addChoosableFileFilter(AllFilter);
        chooser.setFileFilter(PDFFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(fileName);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) { 
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
    
}
