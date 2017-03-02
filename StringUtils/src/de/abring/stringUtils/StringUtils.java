/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.stringUtils;

import java.util.logging.*;

/**
 *
 * @author Karima
 */
public final class StringUtils {
    
    private static final Logger logger = Logger.getLogger(StringUtils.class.getName());
    private static final Level level = Level.SEVERE;
    
    public StringUtils() {
        logger.setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        // PUBLISH this level
        handler.setLevel(level);
        logger.addHandler(handler);

        logger.log(Level.FINE, "Hi, ich bin dein neues StringUtility!");
    }

    public static String[] nGramme(String _processString, int n) {
        int anzahlChars = _processString.length();
        int anzahlNGramme =  anzahlChars - n + 1;
        logger.log(Level.FINE, "Anzahl der N-Gramme bei einer Wortl\u00e4nge von {0} mit n = {1}, ist: {2}", new Object[]{String.valueOf(anzahlChars), String.valueOf(n), String.valueOf(anzahlNGramme)});
        String[] nGramme = null;
        try {
            nGramme = new String[anzahlNGramme];
            for(int i = 0; i < anzahlNGramme; i++)
                nGramme[i] = _processString.substring(i, i + n);
            } catch (Exception exc) {
                logger.log(Level.SEVERE, "Oh, da gab''s wohl einen Fehler!\n{0}");
                return null;
            }
        if (logger.isLoggable(Level.FINE)) {
            String alleNGramme = new String();
            for (String nGramm: nGramme)
                alleNGramme += nGramm + " ";
            logger.log(Level.FINE, "Hier sind die N-Gramm: {0}", new Object[]{alleNGramme});
        }
        return nGramme;
    }
    
    public static String[] monoGramme(String _processString) {
        return nGramme(_processString, 1);
    }

    public static String[] biGramme(String _processString) {
        return nGramme(_processString, 2);
    }

    public static String[] triGramme(String _processString) {
        return nGramme(_processString, 3);
    }

    public static String[] tetraGramme(String _processString) {
        return nGramme(_processString, 4);
    }

    public static double nGrammFuzzyness (String[] nGrammeA, String[] nGrammeB) {
        if(nGrammeA == null || nGrammeB == null || nGrammeA.length == 0 || nGrammeB.length == 0 || nGrammeA[0].length() != nGrammeB[0].length())
            return -1;
        int i = 0;
        int r = 0;
        for(String nGrammA : nGrammeA)
            for(String nGrammB : nGrammeB) {
                i++;
                if(nGrammB.equals(nGrammA)){
                    r++;
                    logger.log(Level.FINER, "{0} == {1}", new Object[]{nGrammA, nGrammB});
                } else {
                    logger.log(Level.FINER, "{0} != {1}", new Object[]{nGrammA, nGrammB});
                }
            }
        logger.log(Level.FINE, "Anzahl der N-Gramme in A: {0}, in B: {1}, Anzahl der Vergleiche: {2}, davon Übereinstimmend: {3}", new Object[]{String.valueOf(nGrammeA.length), String.valueOf(nGrammeB.length), String.valueOf(i), String.valueOf(r)});
        
        
        double ergebnis = ((double)r / nGrammeA.length + (double)r / nGrammeA.length) / 2;
        
        logger.log(Level.INFO, "Der Koeffizient liegt bei: {0}", new Object[]{String.valueOf(ergebnis)});
        return ergebnis;
    }
    
    public static double bestMatch(String wort1, String wort2) {
        return nGrammFuzzyness(biGramme(wort1), biGramme(wort2)) + nGrammFuzzyness(triGramme(wort1), triGramme(wort2)) / 2;
            
    }
    public static String bestMatch(String wort, String[] wortListe){
        return bestMatch(wort, wortListe, 0.0d);
    }
    
    public static String bestMatch(String wort, String[] wortListe, double genauigkeit){
        String bestMatchingWort = new String();
        double bestMatchingIndex = 0;
        for(String listenWort : wortListe) {
            double matchingIndex = nGrammFuzzyness(biGramme(wort), biGramme(listenWort)) + nGrammFuzzyness(triGramme(wort), triGramme(listenWort)) / 2;
            if(matchingIndex > genauigkeit && matchingIndex > bestMatchingIndex) {
                logger.log(Level.INFO, "Ändere Match zu: {0}", new Object[]{listenWort});
                bestMatchingWort = listenWort;
                bestMatchingIndex = matchingIndex;
            }
        }
        return bestMatchingWort;
    }
    
    public static boolean findMatch(String wort, String[] wortListe, double genauigkeit) {
        return !bestMatch(wort, wortListe, genauigkeit).equals("");
    }
}
