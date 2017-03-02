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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Karima
 */
public class loadRoute {
    private static final Logger LOG = LogManager.getLogger(loadRoute.class.getName());
    
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
    
    private static MapRoute parseRoute(JSONObject object) throws RouteNotValidException {
        MapRoute newRoute = new MapRoute();
        if ((object != null) && object.has("properties") && object.has("coordinates")){
            LOG.info("Parse YourNavigation ...");
            JSONArray coordinates = object.getJSONArray("coordinates");
            double distance = object.getJSONObject("properties").getDouble("distance");
            int traveltime = object.getJSONObject("properties").getInt("traveltime");
            String description = object.getJSONObject("properties").getString("description");
            newRoute.setCoordinates(coordinates);
            newRoute.setLänge(distance);
            newRoute.setZeit(traveltime);
            newRoute.setBeschreibung(description.replaceAll("<br>", "\n"));
            newRoute.setCalculated(true);
        } else {
            throw new RouteNotValidException("Wrong JSON-Object");
        }
        return newRoute;
    }
    
    public static final MapRoute YourNavigation(double startLat, double startLon, double endLat, double endLon) {
        LOG.info("Suche bei YourNavigation!");
        String result;
        try {
            result = getData("http://www.yournavigation.org/api/1.0/gosmore.php?format=geojson" + 
                             "&flat=" + String.valueOf(startLat) + "&flon=" + String.valueOf(startLon) + 
                             "&tlat=" + String.valueOf(endLat) + "&tlon=" + String.valueOf(endLon) +
                             "&v=motorcar&fast=1&layer=mapnik&instructions=1");
            JSONObject object = new JSONObject(result);
            return parseRoute(object);
        } catch (IOException ex) {
            LOG.error(ex);
        } catch (URLRequestExpiredException | URLEmptyDataException | RouteNotValidException ex) {
            LOG.warn(ex.getMessage());
        }
        return null;
    }
    
    public static final MapRoute search(MapRoute searchMapRoute) {
        if (searchMapRoute.isCalculated())
            return searchMapRoute;
        double startLat = searchMapRoute.getStartAddress().getLat();
        double startLon = searchMapRoute.getStartAddress().getLon();
        double endLat = searchMapRoute.getEndAddress().getLat();
        double endLon = searchMapRoute.getEndAddress().getLon();
        MapRoute temp = search(startLat, startLon, endLat, endLon);
        searchMapRoute.setCoordinates(temp.getCoordinates());
        searchMapRoute.setLänge(temp.getLänge());
        searchMapRoute.setZeit(temp.getZeit());
        searchMapRoute.setBeschreibung(temp.getBeschreibung());
        searchMapRoute.setCalculated(temp.isCalculated());
        return searchMapRoute;
    }
    
    public static final MapRoute search(double startLat, double startLon, double endLat, double endLon) {
        MapRoute route = loadRoute.YourNavigation(startLat, startLon, endLat, endLon);
        if (route == null || route.getBeschreibung().isEmpty() || route.getCoordinates().length() == 0)
            LOG.error("No valid Address found!");
        else
            return route;
        return null;
    }
}

