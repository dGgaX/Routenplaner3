/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bring.helfer;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Karima
 */
public class Euro implements java.io.Serializable {
    double money;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    
    public Euro() {
        this.money = 0;
    }
    public Euro(String money) {
        try {
            this.money = formatter.parse(money).doubleValue();
        } catch (ParseException ex) {
            if(!money.isEmpty())
                this.money = Double.parseDouble(money);
        }
    }
    public Euro(int money) {
        this.money = money;
    }
    public Euro(float money) {
        this.money = money;
    }
    public Euro(double money) {
        this.money = money;
    }
    public Double getValue() {
        return this.money;
    }
    @Override
    public String toString() {
        return formatter.format(money);
    }
    public Euro multi(int i) {
        return new Euro(money * (double) i);
    }
    public void addier(Euro add) {
        this.money += add.getValue();
    }
    public void subtrahier(Euro add) {
        this.money -= add.getValue();
    }
}
