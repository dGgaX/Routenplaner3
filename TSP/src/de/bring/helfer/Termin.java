
package de.bring.helfer;

import de.bring.stringUtils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class Termin implements java.io.Serializable {
    
    private Uhrzeit Beginn = new Uhrzeit("09:00");
    private Uhrzeit Ende = new Uhrzeit("18:00");
    private List<Preset> Presets = new ArrayList<Preset>();
    
    public Termin() {
        setPresets();
    }
    public Termin(Uhrzeit Beginn, Uhrzeit Ende) {
        this.Beginn = Beginn;
        this.Ende = Ende;
        setPresets();
    }
    
    public Termin(Termin master) {
        this.Beginn = new Uhrzeit(master.Beginn);
        this.Ende = new Uhrzeit(master.Ende);
        setPresets();
    }
    
    private void setPresets() {
        Presets.add(new Preset("Vormittag", "10:00", "15:00"));
        Presets.add(new Preset("früher Vormittag", "10:00", "12:00"));
        Presets.add(new Preset("später Vormittag", "12:00", "15:00"));
        Presets.add(new Preset("Nachmittag", "14:00", "18:00"));
        Presets.add(new Preset("früher Nachmittag", "14:00", "16:00"));
        Presets.add(new Preset("später Nachmittag", "16:00", "18:00"));
        Presets.add(new Preset("Ganztägig", "10:00", "18:00"));
    }
    public void setBeginn(Uhrzeit Beginn) {
        this.Beginn = Beginn;
    }
    public void setEnde(Uhrzeit Ende) {
        this.Ende = Ende;
    }
    public void setTermin(Uhrzeit Beginn, Uhrzeit Ende) {
        this.Beginn = Beginn;
        this.Ende = Ende;
    }
    public void setTermin(String Suche) {
        String[] Zeiten = Suche.split("-");
        if (Zeiten.length == 2) {
            String Beginn = Zeiten[0];
            String Ende = Zeiten[1];
            this.Beginn.setZeit(Beginn);
            this.Ende.setZeit(Ende);
        } else {
            double genauigkeit = 0.75d;
            double bestMatchingIndex = 0;
            for(Preset Preset : Presets) {
                double matchingIndex = StringUtils.bestMatch(Preset.Name, Suche, false);
                if(matchingIndex > genauigkeit && matchingIndex > bestMatchingIndex) {
                    this.Beginn = new Uhrzeit(Preset.Beginn);
                    this.Ende = new Uhrzeit(Preset.Ende);
                    bestMatchingIndex = matchingIndex;
                }
            }
        }
    }
    
    public Uhrzeit getBeginn() {
        return this.Beginn;
    }
    public Uhrzeit getEnde() {
        return this.Ende;
    }
    @Override
    public String toString() {
        return this.Beginn + "-" + this.Ende;
    }
    
    public class Preset implements java.io.Serializable {
        public String Name;
        public String Beginn;
        public String Ende;
        public Preset(String Name, String Beginn, String Ende) {
            this.Name = Name;
            this.Beginn = Beginn;
            this.Ende = Ende;
        }
    }
}
