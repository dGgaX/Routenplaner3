/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.primitives;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Karima
 */
public class Currency implements java.io.Serializable {
    double money;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    
    public Currency() {
        this.money = 0;
    }
    public Currency(String money) {
        try {
            this.money = formatter.parse(money).doubleValue();
        } catch (ParseException ex) {
            if(!money.isEmpty())
                this.money = Double.parseDouble(money);
        }
    }
    public Currency(int money) {
        this.money = money;
    }
    public Currency(float money) {
        this.money = money;
    }
    public Currency(double money) {
        this.money = money;
    }
    public Double getValue() {
        return this.money;
    }
    public String toString() {
        return formatter.format(money);
    }
    public Currency divide(int i) {
        return new Currency(money*Double.valueOf(i));
    }
    public Currency multiply(int i) {
        return new Currency(money*Double.valueOf(i));
    }
    public void sumUp(Currency add) {
        this.money += add.getValue();
    }
    public void subtract(Currency add) {
        this.money -= add.getValue();
    }
}
