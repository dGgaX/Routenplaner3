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
import de.abring.pdferkennung.gui.dialogues.JscanPDF;
import de.abring.routenplaner.gui.Main;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import de.abring.stringUtils.StringUtils;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstreetmap.gui.jmapviewer.MapMarkerDotWithNumber;

/**
 *
 * @author Karima
 */
public class OCR {
    private static final Logger LOGGER = LogManager.getLogger(OCR.class.getName());
     
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

    public static final JXTreeRouteAddressClient rollkarteOCR(JscanPDF parent, boolean modal, JXTreeRouteAddressFav fav, BufferedImage image) {                                        
        List<PercentDimension> parts = new ArrayList<>();
        List<BufferedImage> newImages = new ArrayList<>();
        parts.clear();
        
        parts.add(new PercentDimension("X0.300f", "X0.069f", "W0.300f", "W0.230f")); // Lieferadresse
        parts.add(new PercentDimension("X0.000f", "X0.069f", "W0.300f", "W0.230f")); // Kundenadresse
        parts.add(new PercentDimension("X0.600f", "X0.069f", "W0.200f", "W0.230f")); // Hilfsgeschäft
        parts.add(new PercentDimension("X0.800f", "X0.069f", "W0.200f", "W0.100f")); // BelegInfo
        parts.add(new PercentDimension("X0.800f", "X0.200f", "W0.200f", "W0.100f")); // Notizen
        parts.add(new PercentDimension("X0.000f", "X0.299f", "W1.000f", "W-0.081f"));// Geräte
        
        parent.setImageMax(parts.size());
        parent.setImageValue(0);
        
        int i = 0;
        for (PercentDimension part : parts) {
            Rectangle partBounds = part.getDimension(new Rectangle(0, 0, image.getWidth(), image.getHeight()));

            System.out.println("-> " + String.valueOf(image.getWidth()) + ", " + String.valueOf(image.getHeight()));
            
            System.out.println("-> " + part.toString());
            
            System.out.println("-> " + String.valueOf(partBounds.x) + ", " + String.valueOf(partBounds.y) + ", " + String.valueOf(partBounds.width) + ", " + String.valueOf(partBounds.height));

            try {
                newImages.add(image.getSubimage(partBounds.x, partBounds.y, partBounds.width, partBounds.height));
            } catch (RasterFormatException ex) {
                LOGGER.error("RasterFormatException: " + ex.getMessage());
                return null;
            }
        }
        return rollkarteOCR(parent, modal, fav, newImages);
    }
    
    public static final JXTreeRouteAddressClient rollkarteOCR(JscanPDF parent, boolean modal, JXTreeRouteAddressFav fav) {
        List<PercentDimension> parts = new ArrayList<>();
        parts.clear();
        
        parts.add(new PercentDimension("X0.300f", "X0.069f", "W0.300f", "W0.230f")); // Lieferadresse
        parts.add(new PercentDimension("X0.000f", "X0.069f", "W0.300f", "W0.230f")); // Kundenadresse
        parts.add(new PercentDimension("X0.600f", "X0.069f", "W0.200f", "W0.230f")); // Hilfsgeschäft
        parts.add(new PercentDimension("X0.800f", "X0.069f", "W0.200f", "W0.100f")); // BelegInfo
        parts.add(new PercentDimension("X0.800f", "X0.200f", "W0.200f", "W0.100f")); // Notizen
        parts.add(new PercentDimension("X0.000f", "X0.299f", "W1.000f", "W-0.081f"));// Geräte
        
        Snippet imageSnippet = new Snippet(parent.getParent(), modal, parts);
        List<BufferedImage> newImages = imageSnippet.getNewImage();
        
        
        return rollkarteOCR(parent, modal, fav, newImages);
    }
    
    public static final JXTreeRouteAddressClient rollkarteOCR(JscanPDF parent, boolean modal, JXTreeRouteAddressFav fav, List<BufferedImage> newImages) {
        JXTreeRouteAddressClient entry = new JXTreeRouteAddressClient(new MapAddress(), fav);
        
        entry.setDuration(new TimeOfDay("00:05"));
        entry.setAppointment(new Appointment("Samstagslieferung", new TimeOfDay("10:00"), new TimeOfDay("17:00")));
        entry.setExtras("");
        
        java.awt.Frame frame = null;
        if (parent != null)
            frame = parent.getParent();
            
        
        for(int i = 0; i < newImages.size(); i++) {
            
            if (parent != null) {
                parent.setImageValue(i);
                parent.imageUpdateUI();
            }
            
            BufferedImage image = newImages.get(i);
            ImageOCR imageOCR = new ImageOCR(frame, modal, image);
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
                        entry.setAddress(getAddress(frame, modal, addressString));
                        if (entry.getAddress() != null)
                            entry.setDot(new MapMarkerDotWithNumber(entry.getAddress().getLat(), entry.getAddress().getLon()));
                    }
                    break;
                case 1:// Kundenadresse
                    if (!entry.getAddress().isValid() || entry.getAddress().getHsNr().isEmpty()) {
                        entry.setName(zeilen.get(2).substring(0, 1).toUpperCase() + zeilen.get(2).substring(1).toLowerCase() + ", " + zeilen.get(1).substring(0, 1).toUpperCase() + zeilen.get(1).substring(1).toLowerCase());
                        String addressString = "";
                        for (int j = 3; j < zeilen.size() - 2; j++) {
                            addressString += zeilen.get(j) + ", ";
                        }
                        entry.setAddress(getAddress(frame, modal, addressString));
                        if (entry.getAddress() != null)
                            entry.setDot(new MapMarkerDotWithNumber(entry.getAddress().getLat(), entry.getAddress().getLon()));
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
        }
        return entry;
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
