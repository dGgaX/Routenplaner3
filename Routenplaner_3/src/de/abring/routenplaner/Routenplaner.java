/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner;

import de.abring.helfer.PathFinder;
import de.abring.routenplaner.gui.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
    

/**
 *
 * @author Karima
 */
public class Routenplaner {
    
    private static final Logger LOGGER = LogManager.getLogger(Routenplaner.class.getName());
    
    private final Main3 main;
    private final Properties properties;
    private final SplashScreen splashScreen;
    private final Thread splashThread;
    
    /**
     * init Routenplaner
     */
    public Routenplaner() {
        
        LOGGER.info("Programmstart...");
        
        this.splashScreen = new SplashScreen(getVersionNumber());
        this.splashThread = new Thread(() -> {
            splashScreen.setVisible(true);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                splashScreen.dispose();
            }
        });
//        splashThread.start();
        
        properties = new Properties();
        loadProperties();
        setLookAndFeel();
        main = new Main3(this);
        
    }
    
    /**
     * loads the Properties from File
     */
    private void loadProperties(){
        
        try {
            String mainpath = PathFinder.getAbsolutePath();
            File PropertiesFile = new File(mainpath + "\\lcr.prop");
            if(PropertiesFile.exists()){
                LOGGER.info("Hole Einstellungen...");
                getProperties().loadFromXML(new FileInputStream(PropertiesFile));
            } else {
                throw new IOException("Datei " + PropertiesFile.getCanonicalPath() + " existiert nicht!");
            }
        } catch (IOException ex) {
            LOGGER.warn(ex);
        }
        
    }
    
    /**
     * sets the LookAndFeel
     */
    private void setLookAndFeel() {
        /* Set the propper look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (getProperties().getProperty("lookandfeel", "Windows").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
    
    /**
     * Programstart
     * @param filename 
     */
    private void start(String filename) {
        
        /* Create and display the form */
        File fileToOpen = new File(filename);
        main.loadProperties();
        splashScreen.dispose();
        splashThread.interrupt();
        main.setVisible(true);
        if (!fileToOpen.exists() || !main.loadTour(fileToOpen)) {
            //main.createTour();
        }
    }

    /**
     * killing me softly
     * @param exitState
     */
    public void exitProgram(int exitState) {
        LOGGER.info("Schließe das Hauptfenster...");
        main.dispose();
        LOGGER.info("Programmende...");
        System.exit(exitState);
    }
    
    /**
     * getter for Properties
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }
    
    /**
     * main
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileToOpen = "";
        if (args.length > 0) {
            fileToOpen = args[0];
        }
        Routenplaner planer = new Routenplaner();
        planer.start(fileToOpen);
    }
    
    
    public static final String getVersionNumber() { 
        String version = "v"; 
        try { 
            ResourceBundle rb = ResourceBundle.getBundle("version"); 
            version += rb.getString("MAIN");
            version += ".";
            version += rb.getString("MINOR");
            version += ".";
            version += rb.getString("REVISION");
            version += " build ";
            version += rb.getString("BUILD"); 
        } catch (MissingResourceException e) { 
            LOGGER.error("Token ".concat("BUILD").concat(" not in Propertyfile!"), e); 
            version = "vXX.XX.XX build XXX";
        } 
        return version; 
    } 
}
