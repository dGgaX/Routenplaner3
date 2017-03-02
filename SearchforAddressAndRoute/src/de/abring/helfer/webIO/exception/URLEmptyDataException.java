/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.webIO.exception;

import de.abring.helfer.maproute.MapAddress;

/**
 *
 * @author Bring
 */
public class URLEmptyDataException extends Exception {
    public URLEmptyDataException(String url) {
        super("No Results at URL:\n" + url);
    }
}