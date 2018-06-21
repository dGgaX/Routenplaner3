
package de.bring.helfer;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/**
 *
 * @author Andreas
 */
public class Coordinates implements java.io.Serializable {
        private List<entry> coordinates = new ArrayList<entry>();
        public Coordinates() {
        }
        public Coordinates(JSONArray array) {
            for(int i = 0; i < array.length(); i++) {
                this.coordinates.add(new entry(array.getJSONArray(i).getDouble(0), array.getJSONArray(i).getDouble(1)));
            }
        }
        public Coordinates(List<entry> coordinates) {
            this.coordinates = coordinates;
        }
        public void addEntry(double lat, double lon) {
            this.coordinates.add(new entry(lon, lat));
        }
        public JSONArray getJSONList() {
            JSONArray ret = new JSONArray();
            for(entry eachEntry : coordinates) {
                JSONArray inner = new JSONArray();
                inner.put(eachEntry.getLat());
                inner.put(eachEntry.getLon());
                ret.put(inner);
            }
            return ret;
        }
        public List<entry> getArrayList() {
            return this.coordinates;
        }
        private class entry implements java.io.Serializable {
            private double[] latlon = new double[2];
            public entry() {
                this.latlon[0] = 200;
                this.latlon[1] = 200;
            }
            public entry(double lat, double lon) {
                this.latlon[0] = lat;
                this.latlon[1] = lon;
            }
            public double[] get() {
                return this.latlon;
            }
            public double getLat() {
                return this.latlon[0];
            }
            public double getLon() {
                return this.latlon[1];
            }
        }
    }