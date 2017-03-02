/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import de.abring.helfer.webIO.*;
import de.abring.helfer.maproute.*;

/**
 *
 * @author Bring
 */
public class loadAddressMain {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        loadAddressMain loadAddress = new loadAddressMain();
    }
    
    private loadAddressMain () {
        MapAddress address1 = loadAddress.search("Am Dorfend 12, Xanten");
        if (address1 != null && address1.isValid())
            System.out.println(address1.toString());
        
        System.out.println("---------------------------------");
        
        MapAddress address2 = loadAddress.search("Kastanienweg 9, Aachen");
        if (address2 != null && address2.isValid()) {
            System.out.println(address2.toString());
        }
        
        System.out.println("---------------------------------");
        
        MapRoute route = loadRoute.search(new MapRoute(address1, address2));
        if (route != null && route.getStartAddress().isValid() && route.getEndAddress().isValid()) {
            System.out.println(route.toString());
            System.out.println(route.getBeschreibung().replaceAll("<br>", "\n"));
        }
        SearchForRoute sfr = new SearchForRoute(null, false, route);
        sfr.setVisible(true);
    }
    
}
