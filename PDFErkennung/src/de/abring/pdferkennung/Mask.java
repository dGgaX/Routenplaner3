/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.pdferkennung;

/**
 *
 * @author Karima
 */
public class Mask {
    public static final byte NULL = -1;
    public static final byte FALSE = 0;
    public static final byte TRUE = 1;
    
    public static final byte[][] TOP_LEFT = {{ NULL,  NULL,  NULL,  NULL,  NULL},
                                             { NULL, FALSE, FALSE, FALSE, FALSE},
                                             { NULL, FALSE,  TRUE,  TRUE,  TRUE},
                                             { NULL, FALSE,  TRUE, FALSE, FALSE},
                                             { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][] TOP_RIGHT = {{ NULL,  NULL,  NULL,  NULL,  NULL},
                                              {FALSE, FALSE, FALSE, FALSE,  NULL},
                                              { TRUE,  TRUE,  TRUE, FALSE,  NULL},
                                              {FALSE, FALSE,  TRUE, FALSE,  NULL},
                                              { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][] BOTTOM_LEFT = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                                { NULL, FALSE,  TRUE, FALSE, FALSE},
                                                { NULL, FALSE,  TRUE,  TRUE,  TRUE},
                                                { NULL, FALSE, FALSE, FALSE, FALSE},
                                                { NULL,  NULL,  NULL,  NULL,  NULL}};
    
    public static final byte[][] BOTTOM_RIGHT = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                                 {FALSE, FALSE,  TRUE, FALSE,  NULL},
                                                 { TRUE,  TRUE,  TRUE, FALSE,  NULL},
                                                 {FALSE, FALSE, FALSE, FALSE,  NULL},
                                                 { NULL,  NULL,  NULL,  NULL,  NULL}};
    
    public static final byte[][] TOP_T = {{ NULL,  NULL,  NULL,  NULL,  NULL},
                                          {FALSE, FALSE, FALSE, FALSE, FALSE},
                                          { TRUE,  TRUE,  TRUE,  TRUE,  TRUE},
                                          {FALSE, FALSE,  TRUE, FALSE, FALSE},
                                          { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][] LEFT_T = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                           { NULL, FALSE,  TRUE, FALSE, FALSE},
                                           { NULL, FALSE,  TRUE,  TRUE,  TRUE},
                                           { NULL, FALSE,  TRUE, FALSE, FALSE},
                                           { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][] RIGHT_T = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                            {FALSE, FALSE,  TRUE, FALSE,  NULL},
                                            { TRUE,  TRUE,  TRUE, FALSE,  NULL},
                                            {FALSE, FALSE,  TRUE, FALSE,  NULL},
                                            { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][] BOTTOM_T = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                             {FALSE, FALSE,  TRUE, FALSE, FALSE},
                                             { TRUE,  TRUE,  TRUE,  TRUE,  TRUE},
                                             {FALSE, FALSE, FALSE, FALSE, FALSE},
                                             { NULL,  NULL,  NULL,  NULL,  NULL}};
    
    public static final byte[][] CENTER_CROSS = {{ NULL, FALSE,  TRUE, FALSE,  NULL},
                                                 {FALSE, FALSE,  TRUE, FALSE, FALSE},
                                                 { TRUE,  TRUE,  TRUE,  TRUE,  TRUE},
                                                 {FALSE, FALSE,  TRUE, FALSE, FALSE},
                                                 { NULL, FALSE,  TRUE, FALSE,  NULL}};
    
    public static final byte[][][] ALL_MASKS = {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
                                                   TOP_T, LEFT_T, RIGHT_T, BOTTOM_T,
                                                   CENTER_CROSS};

    public static final byte[][][] CORNER_MASKS = {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};

    public static final byte[][][] CENTER_MASKS = {TOP_T, LEFT_T, RIGHT_T, BOTTOM_T,
                                                      CENTER_CROSS};
    public static final int PATTERN_SIZE = 5;
}
