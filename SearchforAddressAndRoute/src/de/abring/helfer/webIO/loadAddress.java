/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.helfer.webIO;

import de.abring.helfer.maproute.*;
import de.abring.helfer.maproute.exception.*;
import de.abring.helfer.webIO.exception.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Bring
 */
public class loadAddress {
    private static final Logger LOG = LogManager.getLogger(loadAddress.class.getName());
        
    /**
     * 
     * @param url
     * @return
     * @throws IOException
     * @throws URLRequestExpiredException
     * @throws URLEmptyDataException 
     */
    private static String getData (String url) throws IOException, URLRequestExpiredException, URLEmptyDataException {
        String result = "";
        URL myURL= new URL(url);
        URLConnection hc = myURL.openConnection();
        LOG.debug("URL: " + url);
        long time1 = System.currentTimeMillis();
        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; de; de-DE; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        try (Reader is = new InputStreamReader( hc.getInputStream() ); BufferedReader in = new BufferedReader( is )) {
            final Thread counter = new Thread(
                () -> {
                    boolean run = true;
                    int sec = 0;
                    while(run) {
                        try {
                            Thread.sleep(1000);
                            sec++;
                            if (sec > 30) {
                                run = false;
                                is.close();
                            }
                        } catch (IOException | InterruptedException e) {
                            run = false;
                        }
                    }
                }
            );
            counter.start();
            
            for ( String line; ( line = in.readLine() ) != null; ) {
                result += line;
            }
            
            if (!counter.isAlive())
                throw new URLRequestExpiredException(url);
            else 
                counter.interrupt();


        }

        if (result.isEmpty())
            throw new URLEmptyDataException(url);
        LOG.trace("Zeit für die Anfrage: " + String.valueOf(System.currentTimeMillis() - time1) + " ms.");
        return result;
    }
    
    private static MapAddress parseAddress(JSONArray array) throws AddressNotValidException {
        MapAddress newAddress = new MapAddress();
        if ((array != null) && !array.isNull(0)) {
            if (array.getJSONObject(0).has("address_components")) {     //Google
                LOG.info("Parse Google ...");
                
                JSONArray Address = array.getJSONObject(0).getJSONArray("address_components");
                String road = "";
                String house_number = "";
                String postcode = "";
                String city = "";
                String city_district = "";
                String country = "";
                for (int i = 0; i < Address.length(); i++) {
                    JSONObject AddressPart = Address.getJSONObject(i);
                    JSONArray AddressPartTypes = AddressPart.getJSONArray("types");
                    String AddressPartLongName = AddressPart.getString("long_name");
                    
                    for (int j = 0; j < AddressPartTypes.length();j++) {
                        
                        if (AddressPartTypes.getString(j).equals("route"))
                            road = AddressPartLongName;
                        if (AddressPartTypes.getString(j).equals("street_number"))
                            house_number = AddressPartLongName;
                        if (AddressPartTypes.getString(j).equals("postal_code"))
                            postcode = AddressPartLongName;
                        if (AddressPartTypes.getString(j).equals("locality"))
                            city = AddressPartLongName;
                        if (AddressPartTypes.getString(j).equals("sublocality"))
                            city_district = AddressPartLongName;
                        if (AddressPartTypes.getString(j).equals("country"))
                            country = AddressPartLongName;
                    }
                    
                }
                if (!city_district.isEmpty()) {
                    city += " - " + city_district;
                }
                Double DLat = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double DLon = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                newAddress.setAddress(road, house_number, postcode, city, country, DLat, DLon);
            } else if (array.getJSONObject(0).has("address")) {         //OSM
                LOG.info("Parse OSM ...");
                
                JSONObject Address = array.getJSONObject(0).getJSONObject("address");
                if (Address.has("road") && 
                    Address.has("house_number") && 
                    Address.has("postcode") &&
                    (Address.has("town") || 
                    Address.has("city")) &&
                    Address.has("country")) {
                    
                    String Stadtname = "";
                    if (Address.has("town")) {
                        Stadtname += Address.getString("town");
                    } else if (Address.has("city")) {
                        Stadtname += Address.getString("city");
                    }
                    if (Address.has("suburb")) {
                        Stadtname += " - " + Address.getString("suburb");
                    }
                    
                    Double DLat = Double.parseDouble(array.getJSONObject(0).getString("lat"));
                    Double DLon = Double.parseDouble(array.getJSONObject(0).getString("lon"));
                    newAddress.setAddress(Address.getString("road"), Address.getString("house_number"), Address.getString("postcode"), Stadtname, Address.getString("country"), DLat, DLon);
                }
            }
        }
        if (!newAddress.isValid())
            throw new AddressNotValidException(array.toString());
        return newAddress;
    }
    
    public static final MapAddress OSM(String Search_Obj) {
        LOG.info("Suche in OSM!");
        String result;
        try {
            result = getData("http://nominatim.openstreetmap.org/search?q=" +
                             URLEncoder.encode(Search_Obj, "UTF-8") + 
                             "&bounded=0&format=json&limit=1&addressdetails=1&email=andreas.bring@rwth-aachen.de&countrycodes=de,nl,be,lu");
            JSONArray array = new JSONArray(result);
            return parseAddress(array);
        } catch (IOException ex) {
            LOG.error(ex);
        } catch (URLRequestExpiredException | URLEmptyDataException | AddressNotValidException ex) {
            LOG.warn(ex.getMessage());
        }
        return null;
    }
    
    public static final MapAddress Google(String Search_Obj) {
        LOG.info("Suche bei Google!");
        String result;
        try {
            result = getData("http://maps.googleapis.com/maps/api/geocode/json?address=" + 
                             URLEncoder.encode(Search_Obj, "UTF-8") + 
                             "&sensor=false&language=de&region=EU");
            JSONArray array = new JSONObject(result.replaceAll("\\s"," ")).getJSONArray("results");
            return parseAddress(array);
        } catch (IOException ex) {
            LOG.error(ex);
        } catch (URLRequestExpiredException | URLEmptyDataException | AddressNotValidException ex) {
            LOG.warn(ex.getMessage());
        }
        return null;
    }
    
    public static final MapAddress search(String searchString) {
        MapAddress address = loadAddress.OSM(searchString);
        if (address == null || !address.isValid())
            address = loadAddress.Google(searchString);
        if (address == null || !address.isValid())
            LOG.error("No valid Address found!");
        else
            return address;
        return null;
    }
    public static final MapAddress search(MapAddress searchMapRoute) {
        if (searchMapRoute.getSuchString().isEmpty())
            return searchMapRoute;
        return search(searchMapRoute.getSuchString());
    }
}
