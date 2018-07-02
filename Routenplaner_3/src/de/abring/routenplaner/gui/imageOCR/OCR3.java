/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package de.abring.routenplaner.gui.imageOCR;

import de.abring.gui.imageOCR.*;
import de.abring.gui.snippingtool.*;
import de.abring.helfer.maproute.*;
import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.pdferkennung.Cell;
import de.abring.pdferkennung.gui.JPictureFrame;
import de.abring.pdferkennung.gui.dialogues.JscanPDF;
import de.abring.pdferkennung.gui.dialogues.JscanPDF3;
import de.abring.pdferkennung.gui.dialogues.showPDFPage;
import de.abring.routenplaner.gui.dialogues.Entry;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import de.abring.stringUtils.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Karima
 */
public class OCR3 {
    private static final Logger LOGGER = LogManager.getLogger(OCR3.class.getName());
     
    //Suche Pakete
    private static final String[] paketeBausteine = {
        "liefer",
        "paket",
        "ww-lieferpaket",
        "bw-lieferpaket",
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
    //Suche Garantie-Verlängerung
    private static final String[] auslassungBausteine = {
        "coupon",
        "gutschein"
    };

    public static final List<JXTreeRouteAddressClient> rollkarteOCR(JscanPDF3 parent, boolean modal, JXTreeRouteAddressFav fav, List<Cell> frames) {                                        
        
        java.awt.Component frameComp = null;
        
        List<JXTreeRouteAddressClient> entries = new ArrayList<>();
        try {
            if (parent != null) {
                parent.setImageMax(6);
                frameComp = parent;//.getParent();
            }
            Appointment actualAppointment = new Appointment("Ganztägig", "10:00", "18:00");
            for (int i = 0; i < frames.size(); i++) {

                if (parent != null) {
                    parent.setCellValue(i);
                    parent.cellUpdateUI();
                }

                Cell frame = frames.get(i);

                if (frame.getPartsList().size() > 0) {
                    List<String> frameTitle = imageToString(frame.getPart(0).getImage());

                    if (frameTitle.isEmpty())
                        continue;


                    String[] frameTitleArray = frameTitle.get(0).split(" ");

                    if (StringUtils.findMatch("Rollkarte", frameTitleArray, 0.8d)) {

                        //Neuer Titel mit neuer TourZeit!!!
                        List<String> partStrings;

                        for (int t = frame.getPartsList().size() - 1; t >= 0; t--) {
                            partStrings = imageToString(frame.getPart(t).getImage());

                            if (partStrings.isEmpty())
                                continue;

                            String[] partStringArray = partStrings.get(0).split(" ");

                            if (StringUtils.findMatch("Samstagslieferung", partStringArray, 0.8d)) {
                                actualAppointment = new Appointment("Samstagslieferung", "10:00", "18:00");
                                break;
                            }
                            if (StringUtils.findMatch("Tourzeit", partStringArray, 0.8d)) {
                                if (StringUtils.findMatch("10:00", partStringArray, 0.8d)) {
                                    actualAppointment = new Appointment("Vormittag", "10:00", "15:00");
                                } else if (StringUtils.findMatch("13:00", partStringArray, 0.8d)) {
                                    actualAppointment = new Appointment("Nachmittag", "13:00", "18:00");
                                }
                                break;
                            }
                        }


                    } else if (StringUtils.findMatch("Belegnr.", frameTitleArray, 0.8d)) {

                        //Neuer Eintrag mit 20 Parts!!!

                        List<Cell> frameParts = new ArrayList<>();
                        if (frame.getPartsList().size() > 17 && frame.getPartsList().size() <= 20) {
                            frameParts.addAll(frame.getPartsList());
                        } else {
                            if (i < frames.size() - 1) {
                                int combinedSize = frames.get(i + 1).getPartsList().size() + frame.getPartsList().size();
                                if (combinedSize > 17 && combinedSize <= 20) {
                                    frameParts.addAll(frame.getPartsList());
                                    frameParts.addAll(frames.get(++i).getPartsList());
                                }
                            }
                        }
                        JXTreeRouteAddressClient entry = new JXTreeRouteAddressClient(new MapAddress(), fav);

                        entry.setDuration(new TimeOfDay("00:05"));
                        entry.setAppointment(new Appointment(actualAppointment));
                        entry.setExtras("");

                            
                        if (frameParts.size() == 20) {
                            List<BufferedImage> images = new ArrayList<>();

                            images.add(frameParts.get(7).getImage()); // Lieferadresse
                            images.add(frameParts.get(6).getImage()); // Kundenadresse
                            images.add(frameParts.get(8).getImage()); // Hilfsgeschäft
                            images.add(frameParts.get(9).getImage());// Text 1
                            images.add(frameParts.get(11).getImage());// Text 2
                            images.add(frameParts.get(12).getImage());// Artikel
                            JXTreeRouteAddressClient entry2 = fillEntry(parent, modal, entry, images);
                            if (entry2 != null) {
                                entries.add(entry2);
                            } else {
                                int x = frameParts.get(0).getPositionOnParent().x;
                                int y = frameParts.get(0).getPositionOnParent().y;
                                int w = 1850;//frameParts.get(frameParts.size() - 1).getPositionOnParent().x + frameParts.get(frameParts.size() - 1).getPositionOnParent().width;
                                int h = frameParts.get(frameParts.size() - 1).getPositionOnParent().y + frameParts.get(frameParts.size() - 1).getPositionOnParent().height;

                                BufferedImage fromParts = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2d = fromParts.createGraphics();
                                g2d.setBackground(Color.BLACK);
                                g2d.clearRect(0, 0, w, h);
                                for (Cell part : frameParts) {
                                    g2d.drawImage(part.getImage(), part.getPositionOnParent().x, part.getPositionOnParent().y, part.getPositionOnParent().width, part.getPositionOnParent().height, null);
                                }

                                showPDFPage showPage = new showPDFPage(null, false, fromParts, 0.5f);
                                showPage.setVisible(true);

                                Entry newEntry = new Entry(null, true, entry);
                                newEntry.setVisible(true);
                                if (newEntry.getEntry() != null && !newEntry.getEntry().getName().isEmpty() && newEntry.getEntry().getAddress() != null && newEntry.getEntry().getAddress().isValid() && !newEntry.getEntry().getAddress().getStraße().isEmpty()) {
                                    LOGGER.trace("Neuer Eintrag!");
                                    entries.add(newEntry.getEntry());
                                }
                                showPage.dispose();
                            }
                        } else if (frameParts.size() > 17 && frameParts.size() < 20) {
                            int x = frameParts.get(0).getPositionOnParent().x;
                            int y = frameParts.get(0).getPositionOnParent().y;
                            int w = 1850;//frameParts.get(frameParts.size() - 1).getPositionOnParent().x + frameParts.get(frameParts.size() - 1).getPositionOnParent().width;
                            int h = frameParts.get(frameParts.size() - 1).getPositionOnParent().y + frameParts.get(frameParts.size() - 1).getPositionOnParent().height;

                            BufferedImage fromParts = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2d = fromParts.createGraphics();
                            g2d.setBackground(Color.BLACK);
                            g2d.clearRect(0, 0, w, h);
                            for (Cell part : frameParts) {
                                g2d.drawImage(part.getImage(), part.getPositionOnParent().x, part.getPositionOnParent().y, part.getPositionOnParent().width, part.getPositionOnParent().height, null);
                            }
                            
                            showPDFPage showPage = new showPDFPage(null, false, fromParts, 0.5f);
                            showPage.setVisible(true);
                            
                            Entry newEntry = new Entry(null, true, entry);
                            newEntry.setVisible(true);
                            if (newEntry.getEntry() != null && !newEntry.getEntry().getName().isEmpty() && newEntry.getEntry().getAddress() != null && newEntry.getEntry().getAddress().isValid() && !newEntry.getEntry().getAddress().getStraße().isEmpty()) {
                                LOGGER.trace("Neuer Eintrag!");
                                entries.add(newEntry.getEntry());
                            }
                            showPage.dispose();
            
                        } else {
                            JOptionPane.showMessageDialog(frameComp, "Hier ist ein Fehler aufgetreten,\r\nich kann '" + frameTitle.get(0) + "' nicht einlesen.\r\n Bitte merken und von Hand einlesen ...", "Fehler ...", JOptionPane.INFORMATION_MESSAGE);
            
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameComp, "Hier ist ein Fehler aufgetreten,\r\nich kann nicht weiter machen...", "Fehler ...", JOptionPane.INFORMATION_MESSAGE);
            java.util.logging.Logger.getLogger(OCR3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }
        
    private static JXTreeRouteAddressClient fillEntry (JscanPDF3 parent, boolean modal, JXTreeRouteAddressClient entry, List<BufferedImage> newImages) {

        java.awt.Component frame = null;
        if (parent != null)
            frame = parent;//.getParent();
        
        for(int i = 0; i < newImages.size(); i++) {
            
            if (parent != null) {
                parent.setImageValue(i);
                parent.imageUpdateUI();
            }
            
            BufferedImage image = newImages.get(i);
            showPDFPage showPage = new showPDFPage(null, false, image, 0.5f);
            showPage.setVisible(true);
                        
            ImageOCR imageOCR = new ImageOCR(null, modal, image);
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
                        entry.setName(zeilen.get(1).substring(0, 1).toUpperCase() + zeilen.get(1).substring(1).toLowerCase() + ", " + zeilen.get(0).substring(0, 1).toUpperCase() + zeilen.get(0).substring(1).toLowerCase());
                        String addressString = "";
                        for (int j = 2; j < zeilen.size() - 2; j++) {
                            addressString += zeilen.get(j) + ", ";
                        }
                        showPage.requestFocus();
                        MapAddress address = getAddress(null, modal, addressString);
                        if (address != null) {
                            entry.setAddress(address);
                            entry.setDot(new MapMarkerDotWithNumber(entry.getAddress().getLat(), entry.getAddress().getLon()));
                        } else {
                            return null;
                        }
                    }
                    break;
                case 1:// Kundenadresse
                    if (zeilen.size() > 2 && (!entry.getAddress().isValid() || entry.getAddress().getHsNr().isEmpty())) {
                        entry.setName(zeilen.get(2).substring(0, 1).toUpperCase() + zeilen.get(2).substring(1).toLowerCase() + ", " + zeilen.get(1).substring(0, 1).toUpperCase() + zeilen.get(1).substring(1).toLowerCase());
                        String addressString = "";
                        for (int j = 3; j < zeilen.size() - 2; j++) {
                            addressString += zeilen.get(j) + ", ";
                        }
                        showPage.requestFocus();
                        MapAddress address = getAddress(null, modal, addressString);
                        if (address != null) {
                            entry.setAddress(address);
                            entry.setDot(new MapMarkerDotWithNumber(entry.getAddress().getLat(), entry.getAddress().getLon()));
                        } else {
                            return null;
                        }
                    }
                    break;
                case 2:// Hilfsgeschäft
                    //this.jTxtSearchAddress.setText(imageOCR.getString());
                    break;
                case 3:// BelegInfo
                case 4:// Notizen
                    String notizen = entry.getExtras();
                    for (String zeile : zeilen) {
                        notizen += zeile + " ";
                    }
                    entry.setExtras(notizen);
                    break;
                case 5:// Geräte
                    boolean isAuslass = false;
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
                            }
                            for(String artikelTeil : artikel.split(" ")) {
                            
                                artikelTeil = artikelTeil.toLowerCase();
                                
                                isAuslass = StringUtils.findMatch(artikelTeil, auslassungBausteine, 0.75d);
                                isPaket = StringUtils.findMatch(artikelTeil, paketeBausteine, 0.75d);
                                isSubPaket = StringUtils.findMatch(artikelTeil, subPaketeBausteine, 0.75d);
                                isAlthandl = StringUtils.findMatch(artikelTeil, handlBausteine, 0.75d);
                                isFina = StringUtils.findMatch(artikelTeil, finaBausteine, 0.75d);
                                isGarantie = StringUtils.findMatch(artikelTeil, garantBausteine, 0.75d);

                                if (isAuslass || isPaket || isSubPaket || isAlthandl || isFina || isGarantie) {
                                    if (isPaket) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                            entry.setS(entry.getS() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:05"));
                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
                                            entry.setK(entry.getK() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:20"));
                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
                                            entry.setP(entry.getP() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:40"));
                                        } else {

                                        }
                                    }
                                    else if (isSubPaket) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                            entry.setS(entry.getS() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:05"));
                                        } else if (StringUtils.findMatch("KOMFORT", worte, 0.75d)) {
                                            entry.setK(entry.getK() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:20"));
                                        } else if (StringUtils.findMatch("PREMIUM", worte, 0.75d)) {
                                            entry.setP(entry.getP() + 1);
                                            entry.getDuration().addier(new TimeOfDay("00:40"));
                                        } else {

                                        }
                                    }
                                    else if (isAlthandl) {
                                        if (StringUtils.findMatch("STANDART", worte, 0.75d)) {
                                            entry.setExtras("Altgerät\n\r" + entry.getExtras());
                                        } else {

                                        }
                                    }
                                    break;
                                }
                            }
                            if (!(isPaket || isSubPaket || isAlthandl || isFina || isGarantie || isAuslass)) {
                                artikel = artikel.trim();
                                entry.addItem(new JXTreeRouteItem(artikel));
                            }
                        }
                    }
                    break;

            }
            showPage.dispose();
        }
        return entry;
    }
    
    private static List<String> imageToString (BufferedImage image) {
        
        ImageOCR imageOCR = new ImageOCR(null, true, image);
        imageOCR.requestFocus();
        imageOCR.setVisible(true);
        List<String> z = Arrays.asList(imageOCR.getString().split("\n"));  
        List<String> zeilen = new ArrayList<>(); 
        for (String zeile : z) {
            zeile = zeile.trim();
            if (!zeile.isEmpty() && !zeile.equals("%")) {
                zeilen.add(zeile);
            }
        }
        return zeilen;
    }
    
    private static MapAddress getAddress(java.awt.Frame parent, boolean modal, String text) {
        MapAddress address = new MapAddress(text);
        if (text.length() > 5) {
            LookupAddress lookup = new LookupAddress(parent, modal, address);
            lookup.setVisible(true);
            address = lookup.getMapAddress();
            if (address == null || !address.isValid() || address.getStraße().isEmpty()) {
                SearchForAddress search = new SearchForAddress(parent, modal, text);
                search.setVisible(true);
                address = search.getMapAddress();
            }
        } else {
            SearchForAddress search = new SearchForAddress(parent, modal, text);
            search.setVisible(true);
            address = search.getMapAddress();
        }
        if (address != null && address.isValid() && !address.getStraße().isEmpty()) {
            return address;
        }
        return null;
    }
}
