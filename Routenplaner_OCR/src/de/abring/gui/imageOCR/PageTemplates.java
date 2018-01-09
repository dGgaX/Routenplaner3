/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.gui.imageOCR;

import de.abring.gui.snippingtool.*;
import de.abring.stringUtils.StringUtils;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Karima
 */
public class PageTemplates {
        
    //Suche Pakete
    private static final String[] paketeBausteine = {
        "liefer",
        "paket",
        "lieferpaket"
    };
    //Suche SubPakete
    private static final String[] subPaketeBausteine = {
        "geräte",
        "zuschlag",
        "mehrgerätezuschlag"
    };
    //Suche Fina
    private static final String[] finaBausteine = {
        "finanzierung"
    };
    //Suche Altger.handling
    private static final String[] handlBausteine = {
        "handling",
        "altger",
        "altgerhandling"
    };
    //Suche Garantie-Verlängerung
    private static final String[] garantBausteine = {
        "garantie",
        "verlängerung",
        "garantie-verlängerung"
    };
    
    public static final void jBtnOCR(java.awt.Frame parent, boolean modal) {                                        
        //JXTreeRouteAddressClient entry = new JXTreeRouteAddressClient(new MapAddress(), new JXTreeRouteAddressFav(new MapAddress()));
        List<PercentDimension> parts = new ArrayList<>();
        parts.clear();
        
        parts.add(new PercentDimension("X0.300f", "X0.069f", "W0.300f", "W0.230f")); // Lieferadresse
        parts.add(new PercentDimension("X0.000f", "X0.069f", "W0.300f", "W0.230f")); // Kundenadresse
        parts.add(new PercentDimension("X0.600f", "X0.069f", "W0.200f", "W0.230f")); // Hilfsgeschäft
        parts.add(new PercentDimension("X0.800f", "X0.069f", "W0.200f", "W0.100f")); // BelegInfo
        parts.add(new PercentDimension("X0.800f", "X0.200f", "W0.200f", "W0.100f")); // Notizen
        parts.add(new PercentDimension("X0.000f", "X0.299f", "W1.000f", "W-0.081f"));// Geräte
        
        Snippet imageSnippet = new Snippet(parent, modal, parts);
        List<BufferedImage> newImages = imageSnippet.getNewImage();
        for(int i = 0; i < newImages.size(); i++) {
            BufferedImage image = newImages.get(i);
            ImageOCR imageOCR = new ImageOCR(parent, true, image);
            imageOCR.requestFocus();
            imageOCR.setVisible(true);
            
            List<String> z = Arrays.asList(imageOCR.getString().split("\n"));  
            List<String> zeilen = new ArrayList<>(); 
            for (String zeile : z) {
                zeile = zeile.trim();
                if (!zeile.isEmpty()) {
                    zeilen.add(zeile);
                }
            }
            
            switch (i) {
                case 0:// Lieferadresse
                    if (zeilen.size() > 2) {

                    }
                    break;
                case 1:// Kundenadresse
//                    if (!entry.getAddress().isValid() || entry.getAddress().getHsNr().isEmpty()) {
//                    }
                    break;
                case 2:// Hilfsgeschäft
                    //this.jTxtSearchAddress.setText(imageOCR.getString());
                    break;
                case 3:// BelegInfo
                case 4:// Notizen
                    for (String zeile : zeilen) {
                    }
                    break;
                case 5:// Geräte
                    boolean isPaket = false;
                    boolean isSubPaket = false;
                    boolean isAlthandl = false;
                    boolean isFina = false;
                    boolean isGarantie = false;

                    for (String zeile : zeilen) {
                        if (zeile.length() > 3 && !zeile.equals(zeilen.get(0))) {
                            //zersäge die Produkte
                            String[] worte = zeile.split(" ");
                            String artikel = "";
                            for (int j = 3; j < worte.length - 2; j++) {
                                artikel += worte[j] + " ";


                                isPaket = StringUtils.findMatch(worte[j], paketeBausteine, 0.75d);
                                isSubPaket = StringUtils.findMatch(worte[j], subPaketeBausteine, 0.75d);
                                isAlthandl = StringUtils.findMatch(worte[j], handlBausteine, 0.75d);
                                isFina = StringUtils.findMatch(worte[j], finaBausteine, 0.75d);
                                isGarantie = StringUtils.findMatch(worte[j], garantBausteine, 0.75d);

                                if (isPaket || isSubPaket || isAlthandl || isFina || isGarantie) {
                                    if (isPaket) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
                                        } else {

                                        }
                                    }
                                    else if (isSubPaket) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
                                        } else {

                                        }
                                    }
                                    else if (isAlthandl) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                        } else {

                                        }
                                    }
                                    break;
                                }
                            }
                            if (!(isPaket || isSubPaket || isAlthandl || isFina || isGarantie)) {
                                artikel = artikel.trim();
                            }
                        }
                    }
                    break;

            }
        }
//        return entry;
    }                                       

//    public static final JXTreeRouteAddressClient jBtnOCR(java.awt.Frame parent, boolean modal) {                                        
//        JXTreeRouteAddressClient entry = new JXTreeRouteAddressClient(new MapAddress(), new JXTreeRouteAddressFav(new MapAddress()));
//        List<PercentDimension> parts = new ArrayList<>();
//        parts.clear();
//        
//        parts.add(new PercentDimension("X0.300f", "X0.069f", "W0.300f", "W0.230f")); // Lieferadresse
//        parts.add(new PercentDimension("X0.000f", "X0.069f", "W0.300f", "W0.230f")); // Kundenadresse
//        parts.add(new PercentDimension("X0.600f", "X0.069f", "W0.200f", "W0.230f")); // Hilfsgeschäft
//        parts.add(new PercentDimension("X0.800f", "X0.069f", "W0.200f", "W0.100f")); // BelegInfo
//        parts.add(new PercentDimension("X0.800f", "X0.200f", "W0.200f", "W0.100f")); // Notizen
//        parts.add(new PercentDimension("X0.000f", "X0.299f", "W1.000f", "W-0.081f"));// Geräte
//        
//        Snippet imageSnippet = new Snippet(parent, modal, parts);
//        List<BufferedImage> newImages = imageSnippet.getNewImage();
//        for(int i = 0; i < newImages.size(); i++) {
//            BufferedImage image = newImages.get(i);
//            ImageOCR imageOCR = new ImageOCR(parent, true, image);
//            imageOCR.requestFocus();
//            imageOCR.setVisible(true);
//            
//            List<String> z = Arrays.asList(imageOCR.getString().split("\n"));  
//            List<String> zeilen = new ArrayList<>(); 
//            for (String zeile : z) {
//                zeile = zeile.trim();
//                if (!zeile.isEmpty()) {
//                    zeilen.add(zeile);
//                }
//            }
//            
//            switch (i) {
//                case 0:// Lieferadresse
//                    if (zeilen.size() > 2) {
//
//                    }
//                    break;
//                case 1:// Kundenadresse
//                    if (!entry.getAddress().isValid() || entry.getAddress().getHsNr().isEmpty()) {
//                    }
//                    break;
//                case 2:// Hilfsgeschäft
//                    //this.jTxtSearchAddress.setText(imageOCR.getString());
//                    break;
//                case 3:// BelegInfo
//                case 4:// Notizen
//                    for (String zeile : zeilen) {
//                    }
//                    break;
//                case 5:// Geräte
//                    boolean isPaket = false;
//                    boolean isSubPaket = false;
//                    boolean isAlthandl = false;
//                    boolean isFina = false;
//                    boolean isGarantie = false;
//
//                    for (String zeile : zeilen) {
//                        if (zeile.length() > 3 && !zeile.equals(zeilen.get(0))) {
//                            //zersäge die Produkte
//                            String[] worte = zeile.split(" ");
//                            String artikel = "";
//                            for (int j = 3; j < worte.length - 2; j++) {
//                                artikel += worte[j] + " ";
//
//
//                                isPaket = StringUtils.findMatch(worte[j], paketeBausteine, 0.75d);
//                                isSubPaket = StringUtils.findMatch(worte[j], subPaketeBausteine, 0.75d);
//                                isAlthandl = StringUtils.findMatch(worte[j], handlBausteine, 0.75d);
//                                isFina = StringUtils.findMatch(worte[j], finaBausteine, 0.75d);
//                                isGarantie = StringUtils.findMatch(worte[j], garantBausteine, 0.75d);
//
//                                if (isPaket || isSubPaket || isAlthandl || isFina || isGarantie) {
//                                    if (isPaket) {
//                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
//                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
//                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
//                                        } else {
//
//                                        }
//                                    }
//                                    else if (isSubPaket) {
//                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
//                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
//                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
//                                        } else {
//
//                                        }
//                                    }
//                                    else if (isAlthandl) {
//                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
//                                        } else {
//
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                            if (!(isPaket || isSubPaket || isAlthandl || isFina || isGarantie)) {
//                                artikel = artikel.trim();
//                            }
//                        }
//                    }
//                    break;
//
//            }
//        }
//        return entry;
//    }                
    
//    private void jBtnOCR1() {                                         
//        parts.clear();
//        
//        parts.add(new PercentDimension("X0.300f", "X0.069f", "W0.300f", "W0.230f")); // Lieferadresse
//        parts.add(new PercentDimension("X0.000f", "X0.069f", "W0.300f", "W0.230f")); // Kundenadresse
//        parts.add(new PercentDimension("X0.600f", "X0.069f", "W0.200f", "W0.230f")); // Hilfsgeschäft
//        parts.add(new PercentDimension("X0.800f", "X0.069f", "W0.200f", "W0.100f")); // BelegInfo
//        parts.add(new PercentDimension("X0.800f", "X0.200f", "W0.200f", "W0.100f")); // Notizen
//        parts.add(new PercentDimension("X0.000f", "X0.299f", "W1.000f", "W-0.081f"));// Geräte
//
//        this.jPanelPakete.getjSpinnerS().setValue((int) 0);
//        this.jPanelPakete.getjSpinnerK().setValue((int) 0);
//        this.jPanelPakete.getjSpinnerP().setValue((int) 0);
//        
//        Snippet imageSnippet = new Snippet(this.parent, true, parts);
//        List<BufferedImage> newImages = imageSnippet.getNewImage();
//        for(int i = 0; i < newImages.size(); i++) {
//            BufferedImage image = newImages.get(i);
//            ImageOCR imageOCR = new ImageOCR(parent, true, image);
//            imageOCR.requestFocus();
//            imageOCR.setVisible(true);
//            
//            List<String> z = Arrays.asList(imageOCR.getString().split("\n"));  
//            List<String> zeilen = new ArrayList<>(); 
//            for (String zeile : z) {
//                zeile = zeile.trim();
//                if (!zeile.isEmpty()) {
//                    zeilen.add(zeile);
//                }
//            }
//            
//            switch (i) {
//                case 0:// Lieferadresse
//                    if (zeilen.size() > 2) {
//                        this.jTxtName.setText(zeilen.get(0) + " " + zeilen.get(1));
//                        this.jTxtSearchAddress.setText(zeilen.get(2).toUpperCase().replace("STR.", "STRASSE") + " " + zeilen.get(3));
//                        this.jButtonSucheActionPerformed(null);
//                    }
//                    break;
//                case 1:// Kundenadresse
//                    if (!this.entry.getAddress().isValid() || this.entry.getAddress().getHsNr().isEmpty()) {
//                        this.jTxtName.setText(zeilen.get(1) + " " + zeilen.get(2));
//                        this.jTxtSearchAddress.setText(zeilen.get(3).toUpperCase().replace("STR.", "STRASSE") + " " + zeilen.get(4));
//                        this.jButtonSucheActionPerformed(null);
//                    }
//                    break;
//                case 2:// Hilfsgeschäft
//                    //this.jTxtSearchAddress.setText(imageOCR.getString());
//                    break;
//                case 3:// BelegInfo
//                case 4:// Notizen
//                    for (String zeile : zeilen) {
//                        if (zeile.length() > 3) {
//                            this.jTextExtras.append(zeile);
//                            this.jTextExtras.append(" ");
//                        }
//                    }
//                    break;
//                case 5:// Geräte
//                    boolean isPaket = false;
//                    boolean isSubPaket = false;
//                    boolean isFina = false;
//                    boolean isGarantie = false;
//
//                    for (String zeile : zeilen) {
//                        if (zeile.length() > 3 && !zeile.equals(zeilen.get(0))) {
//                            //zersäge die Produkte
//                            String[] worte = zeile.split(" ");
//                            String artikel = "";
//                            for (int j = 3; j < worte.length - 2; j++) {
//                                artikel += worte[j] + " ";
//
//
//                                isPaket = StringUtils.findMatch(worte[j], paketeBausteine, 0.75d);
//                                isSubPaket = StringUtils.findMatch(worte[j], subPaketeBausteine, 0.75d);
//                                isFina = StringUtils.findMatch(worte[j], finaBausteine, 0.75d);
//                                isGarantie = StringUtils.findMatch(worte[j], garantBausteine, 0.75d);
//
//                                if (isPaket || isSubPaket || isFina || isGarantie) {
//                                    if (isPaket) {
//                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
//                                            this.jPanelPakete.increaseStandart();
//                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
//                                            this.jPanelPakete.increaseKomfort();
//                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
//                                            this.jPanelPakete.increasePremium();
//                                        } else {
//
//                                        }
//                                    }
//                                    else if (isSubPaket) {
//                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
//                                            this.jPanelPakete.increaseStandart();
//                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
//                                            this.jPanelPakete.increaseKomfort();
//                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
//                                            this.jPanelPakete.increasePremium();
//                                        } else {
//
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                            if (!(isPaket || isSubPaket || isFina || isGarantie)) {
//                                //Unterscheide Produkte von Paketen
//                                artikel = artikel.trim();
//                                this.jTblProdukte.addItem(new JXTreeRouteItem(artikel));
//                            }
//                        }
//                    }
//                    break;
//            }
//        }
//        this.jBtnOK.setEnabled(!this.jTxtName.getText().isEmpty() && this.getEntry().getAddress() != null && this.getEntry().getAddress().isValid() && !this.getEntry().getAddress().getStraße().isEmpty());
//    }
}
