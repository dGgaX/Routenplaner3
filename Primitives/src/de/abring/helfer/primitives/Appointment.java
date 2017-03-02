
package de.abring.helfer.primitives;

/**
 *
 * @author Andreas
 */
public class Appointment implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private TimeOfDay start;
    private TimeOfDay end;
    
    public Appointment() {
        this.start = new TimeOfDay("10:00");
        this.end = new TimeOfDay("20:00");
        this.name = getTermin();
    }
    
    public Appointment(TimeOfDay start, TimeOfDay end) {
        this.start = start;
        this.end = end;
        this.name = getTermin();
    }
   
    public Appointment(String name, TimeOfDay start, TimeOfDay end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
    
    public Appointment(String name, String start, String end) {
        this.name = name;
        this.start = new TimeOfDay(start);
        this.end = new TimeOfDay(end);
    }
    
    public Appointment(Appointment master) {
        this.name = master.getName();
        this.start = new TimeOfDay(master.getStart());
        this.end = new TimeOfDay(master.getEnd());
    }
    
    public final String getTermin() {
        if (name == null)
            return "";
        if (this.name.isEmpty()) {
            return this.start.getTimeString() + " bis " + this.end.getTimeString();
        }
        return this.name + " von " + this.start.getTimeString() + " bis " + this.end.getTimeString();
    }
    
    public void setTermin(TimeOfDay start, TimeOfDay end) {
        this.setStart(start);
        this.setEnd(end);
    }
    
    public void setTermin(String name, TimeOfDay start, TimeOfDay end) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
    }
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the start
     */
    public TimeOfDay getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(TimeOfDay start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public TimeOfDay getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(TimeOfDay end) {
        this.end = end;
    }
    
    @Override
    public String toString() {
        if (getName().isEmpty()) {
            return this.getStart() + "-" + this.getEnd();
        }
        return this.getName() + " (" + this.getStart() + "-" + this.getEnd() + ")";
    }
}
