/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.webIO.exception;


/**
 *
 * @author Bring
 */
public class URLRequestExpiredException extends Exception {
    public URLRequestExpiredException(String url) {
        super("Request Expired at URL:\n" + url);
    }
}