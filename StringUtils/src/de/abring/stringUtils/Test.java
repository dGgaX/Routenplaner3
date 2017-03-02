/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.stringUtils;

import java.util.Scanner;
import java.util.logging.*;

/**
 *
 * @author Karima
 */
public class Test {
    private static final Logger logger = Logger.getLogger(Test.class.getName());
    private static final Level level = Level.WARNING;
    
    private static Scanner userInput = new Scanner(System.in);
    private static StringUtils stringUtils = new StringUtils();
    
    public Test() {
        logger.setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        // PUBLISH this level
        handler.setLevel(level);
        logger.addHandler(handler);

        logger.log(Level.FINE, "Hi, ich bin dein neuer Test!");
    }
    
    public void compute() {
        boolean repeat = true;
        String[] inputA = {
            "Hallo Welt!",
            "Hallo Erde!",
            "Hallo Baum!",
            "Hallo Apfel!",
            "Hallo Karima!",
            "Hallo Eliot!",
            "Hallo Blumentopf!",
            "Hallo Fernseher!",
            "Hallo Panama!"
        };
        double genauigkeit = 0.545643d;
        
        do {
            for(String wort : inputA)
                System.out.println(wort);
            System.out.println("--------------------------------------");
            System.out.println("\"Ende\" = Beenden.");
            System.out.println("--------------------------------------");
            System.out.print("Input: ");
            String inputB = userInput.nextLine();
            System.out.println("Genauigkeit: " + String.valueOf(Math.ceil(genauigkeit * 100)) + "%.");
            if(inputB.equals("Ende"))
                    repeat = false;
            String ergebnis = stringUtils.bestMatch(inputB, inputA, genauigkeit);

            logger.log(Level.FINE, "Ergebnis: {0}", new Object[]{ergebnis});

            System.out.println("--------------------------------------");
            System.out.println(" => \"" + ergebnis + "\"");
            System.out.println("--------------------------------------");
            System.out.println();
        } while(repeat);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Test test = new Test();
        test.compute();
        
    }
    
}
