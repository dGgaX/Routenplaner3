package de.abring.pdferkennung.gui.dialogues;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Karima
 */
public class FileIO {
    private static final String FILE_ENDING = ".pdf";
    public static final File getOpenPDFFile(Component frame, String workingDir) {
        JFileChooser chooser = new JFileChooser(workingDir);
        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(FILE_ENDING);
            }
            @Override
            public String getDescription() {
                return "PDF-Dateien";
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
        chooser.setDialogTitle("PDF öffnen");
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
//    public static final File getSaveRouteFile(Component frame, File fileName, String workingDir) {
//        JFileChooser chooser = new JFileChooser(workingDir);
//        javax.swing.filechooser.FileFilter PDFFilter = new javax.swing.filechooser.FileFilter() {
//            @Override
//            public boolean accept(java.io.File f) {
//                if (f.isDirectory())
//                    return true;
//                return f.getName().toLowerCase().endsWith(FILE_ENDING);
//            }
//            @Override
//            public String getDescription() {
//                return "Route-Dateien";
//            }
//        };
//        javax.swing.filechooser.FileFilter AllFilter = new javax.swing.filechooser.FileFilter() {
//            @Override
//            public boolean accept(java.io.File f) {
//                return true;
//            }
//            @Override
//            public String getDescription() {
//                return "Alle Dateien";
//            }
//        };
//        chooser.setDialogTitle("Route speichern");
//        chooser.addChoosableFileFilter(AllFilter);
//        chooser.setFileFilter(PDFFilter);
//        chooser.setAcceptAllFileFilterUsed(false);
//        chooser.setMultiSelectionEnabled(false);
//        chooser.setSelectedFile(fileName);
//        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) { 
//            File saveFile = chooser.getSelectedFile();
//            if (!saveFile.getName().endsWith(FILE_ENDING)) {
//                saveFile = new File(saveFile.getAbsolutePath() + FILE_ENDING);
//            }
//            return saveFile;
//        } else {
//            return null;
//        }
//    }
}
