/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.maproute.exception;

import de.abring.helfer.maproute.MapAddress;

/**
 *
 * @author Bring
 */
public class AddressNotValidException extends Exception {
    public AddressNotValidException(String text) {
        super("Address at: " + text + " is not Valid!");
    }
}