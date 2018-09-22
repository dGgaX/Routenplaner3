package de.abring.routenplaner.jxtreetableroute;

import de.abring.helfer.primitives.Appointment;
import de.abring.helfer.primitives.TimeOfDay;
import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.util.List;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import de.abring.helfer.maproute.MapAddress;



public class JXNoRootTreeTableModelAddress extends AbstractTreeTableModel {
    
    public static final int EMPTY               = 0;
    public static final int ID                  = 1;
    public static final int NAME                = 2;
    public static final int TIME                = 3;
    public static final int DURATION            = 4;
    public static final int ITEMS               = 5;
    public static final int APPOINTMENT         = 6;
    public static final int APPOINTMENT_SHORT   = 15;
    public static final int ADDRESS             = 7;
    public static final int ROUTE               = 8;
    public static final int ADDRESS_ROUTE       = 9;
    public static final int ADDRESS_ROUTE_REV   = 16;
    public static final int FAVORIT             = 10;
    public static final int DRIVER              = 11;
    public static final int CO_DRIVER           = 12;
    public static final int CAR                 = 13;
    public static final int MAP_VISIBLE         = 14;
    public static final int INFO                = 17;
           
    
    private final List<JXTreeRouteEntry> entryList;
    private final int[] columnNames;
    
    public JXNoRootTreeTableModelAddress(List<JXTreeRouteEntry> entryList, int[] columnNames) {
        super(new Object());
        this.entryList = entryList;
        this.columnNames = columnNames;
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof JXTreeRouteEntry;
    }
    
    @Override
    public String getColumnName(int column) {
        switch (this.columnNames[column]) {
            case JXNoRootTreeTableModelAddress.ID:
                return "Nr";
            case JXNoRootTreeTableModelAddress.NAME:
                return "Name";
            case JXNoRootTreeTableModelAddress.TIME:
                return "Zeit";
            case JXNoRootTreeTableModelAddress.DURATION:
                return "Dauer";
            case JXNoRootTreeTableModelAddress.ITEMS:
                return "Artikel";
            case JXNoRootTreeTableModelAddress.APPOINTMENT:
            case JXNoRootTreeTableModelAddress.APPOINTMENT_SHORT:
                return "Termin";
            case JXNoRootTreeTableModelAddress.ADDRESS:
                return "Adresse";
            case JXNoRootTreeTableModelAddress.ROUTE:
                return "Route";
            case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE:
            case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE_REV:
                return "Adresse/Route";
            case JXNoRootTreeTableModelAddress.FAVORIT:
                return "Auftraggeber";
            case JXNoRootTreeTableModelAddress.DRIVER:
                return "Fahrer";
            case JXNoRootTreeTableModelAddress.CO_DRIVER:
                return "Beifahrer";
            case JXNoRootTreeTableModelAddress.CAR:
                return "Fahrzeug";
            case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                return "Auf Karte";
            case JXNoRootTreeTableModelAddress.INFO:
                return "Info";
        }
        return "";
    }
    
    @Override
    public Class getColumnClass(int column) {
        switch (this.columnNames[column]) {
            case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                return java.lang.Boolean.class;
        }
        return java.lang.String.class;
    }
    
    @Override
    public boolean isCellEditable(Object node, int column) {
        if (node instanceof JXTreeRouteStart) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.TIME:
                    return true;
            }
        } else if (node instanceof JXTreeRouteEnd) {
            //Nix verändern
        } else if (node instanceof JXTreeRouteRoute) {
            //Nix verändern
        } else if (node instanceof JXTreeRouteAddressFav) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.DURATION:
                    return true;
            }
        } else if (node instanceof JXTreeRouteAddressClient) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.NAME:
                case JXNoRootTreeTableModelAddress.DURATION:
                case JXNoRootTreeTableModelAddress.ITEMS:
                case JXNoRootTreeTableModelAddress.APPOINTMENT:
                case JXNoRootTreeTableModelAddress.ADDRESS:
                case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE:
                    return true;
            }
        } else if (node instanceof JXTreeRouteTour) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.DRIVER:
                case JXNoRootTreeTableModelAddress.CO_DRIVER:
                case JXNoRootTreeTableModelAddress.CAR:
                case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                case JXNoRootTreeTableModelAddress.INFO:
                    return true;
            }
        } else if (node instanceof JXTreeRouteItem) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.ITEMS:
                    return true;
            }
        } 
        return false;
    }
    
    @Override
    public Object getValueAt(Object node, int column) {
        switch (this.columnNames[column]) {
            case JXNoRootTreeTableModelAddress.ID:
                if (node instanceof JXTreeRouteAddress) {
                    JXTreeRouteAddress address = (JXTreeRouteAddress) node;
                    if (address.getID() > 0) {
                        return String.valueOf(address.getID() + ".");
                    }
                }
                return "";
            case JXNoRootTreeTableModelAddress.NAME:
                if (node instanceof JXTreeRouteEntry) {
                    return ((JXTreeRouteEntry) node).getName();
                }
                return "";
            case JXNoRootTreeTableModelAddress.TIME:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).getEnd().getTimeString();
                } else if (node instanceof JXTreeRouteEntry) {
                    return ((JXTreeRouteEntry) node).getStart().getTimeString();
                }
                return "";
            case JXNoRootTreeTableModelAddress.DURATION:
                if (node instanceof JXTreeRouteEntry) {
                    JXTreeRouteEntry entry = (JXTreeRouteEntry) node;
                    if (entry.getDuration().isZero())
                        return "";
                    return ((JXTreeRouteEntry) node).getDuration().getDurationString();
                }
                return "";
            case JXNoRootTreeTableModelAddress.ITEMS:
                if (node instanceof JXTreeRouteAddressClient) {
                    JXTreeRouteAddressClient address = (JXTreeRouteAddressClient) node;
                    String text = address.itemListToString();
                    text += address.getPKSString();
                    return text;
                } else if (node instanceof JXTreeRouteItem) {
                    return ((JXTreeRouteItem) node).getName();
                }            
                            
                return "";
            case JXNoRootTreeTableModelAddress.APPOINTMENT:
                if (node instanceof JXTreeRouteAddress) {
                    return ((JXTreeRouteAddress) node).getAppointment();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.APPOINTMENT_SHORT:
                if (node instanceof JXTreeRouteAddress) {
                    Appointment ap = ((JXTreeRouteAddress) node).getAppointment();
                    return ap.getStart() + " - " + ap.getEnd();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.ADDRESS:
                if (node instanceof JXTreeRouteAddress) {
                    return ((JXTreeRouteAddress) node).getAddressName();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.ROUTE:
                if (node instanceof JXTreeRouteRoute) {
                    return ((JXTreeRouteRoute) node).getRoute().toString();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE:
                if (node instanceof JXTreeRouteAddress) {
                    return ((JXTreeRouteAddress) node).getAddressName();
                } 
                if (node instanceof JXTreeRouteRoute) {
                    return ((JXTreeRouteRoute) node).getRoute().toString();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE_REV:
                if (node instanceof JXTreeRouteAddress) {
                    MapAddress ad = ((JXTreeRouteAddress) node).getAddress();
                    String landKurz = "";
                    if (ad.getLand().equals("Deutschland"))
                        landKurz = "DE";
                    if (ad.getLand().equals("Niederlande"))
                        landKurz = "NL";
                    if (ad.getLand().equals("Belgien"))
                        landKurz = "BE";
                    return landKurz + "-" + ad.getPLZ() + " " + ad.getStadt() + ", " + ad.getStraße() + " " + ad.getHsNr();
                } 
                if (node instanceof JXTreeRouteRoute) {
                    return ((JXTreeRouteRoute) node).getRoute().toString();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.FAVORIT:
                if (node instanceof JXTreeRouteAddressClient) {
                    return ((JXTreeRouteAddressClient) node).getFavorite().getName();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.DRIVER:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).getDriver();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.CO_DRIVER:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).getCoDriver();
                }            
                return "";
            case JXNoRootTreeTableModelAddress.CAR:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).getCar();
                }            
            case JXNoRootTreeTableModelAddress.INFO:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).getInfo();
                }            
            case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                if (node instanceof JXTreeRouteTour) {
                    return ((JXTreeRouteTour) node).isMapVisible();
                }            
                return false;
        }
        return "";
    }

    @Override
    public Object getChild(Object o, int i) {
        return this.entryList.get(i);
    }

    @Override
    public int getChildCount(Object o) {
        if (this.entryList == null) {
            return 0;
        }
        return this.entryList.size();
    }

    @Override
    public int getIndexOfChild(Object o, Object o1) {
        return 0;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        if (node instanceof JXTreeRouteStart) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.TIME:
                    if (value instanceof TimeOfDay)
                        ((JXTreeRouteStart) node).getStart().setTime((TimeOfDay)value);
                    else if (value instanceof String)
                        ((JXTreeRouteStart) node).getStart().setTime((String)value);
                    break;
            }
        } else if (node instanceof JXTreeRouteEnd) {
            //Nix verändern
        } else if (node instanceof JXTreeRouteRoute) {
            //Nix verändern
        } else if (node instanceof JXTreeRouteAddressFav) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.DURATION:
                    if (value instanceof TimeOfDay)
                        ((JXTreeRouteAddressFav) node).getDuration().setTime((TimeOfDay)value);
                    else if (value instanceof Integer)
                        ((JXTreeRouteAddressFav) node).getDuration().setTimeInMin((Integer)value);
                    else if (value instanceof String)
                        ((JXTreeRouteAddressFav) node).getDuration().setTime((String)value);
                    break;
            }
        } else if (node instanceof JXTreeRouteAddressClient) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.NAME:
                    if (value instanceof String)
                        ((JXTreeRouteAddressClient) node).setName((String) value);
                    break;
                case JXNoRootTreeTableModelAddress.DURATION:
                    if (value instanceof TimeOfDay)
                        ((JXTreeRouteAddressClient) node).getDuration().setTime((TimeOfDay)value);
                    else if (value instanceof Integer)
                        ((JXTreeRouteAddressClient) node).getDuration().setTimeInMin((Integer)value);
                    else if (value instanceof String)
                        ((JXTreeRouteAddressClient) node).getDuration().setTime((String)value);
                    break;
                case JXNoRootTreeTableModelAddress.ITEMS:
                    if (value instanceof String[]) {
                        ((JXTreeRouteAddressClient) node).clearItemList();
                        for (String item : (String[]) value)
                            ((JXTreeRouteAddressClient) node).addItem(new JXTreeRouteItem(item));
                    } else if (value instanceof String) {
                        ((JXTreeRouteAddressClient) node).addItem(new JXTreeRouteItem((String) value));
                    }
                    break;
                case JXNoRootTreeTableModelAddress.APPOINTMENT:
                    if (value instanceof Appointment)
                        ((JXTreeRouteAddressClient) node).setAppointment((Appointment) value);
                    break;
                case JXNoRootTreeTableModelAddress.ADDRESS:
                    if (value instanceof MapAddress)
                        ((JXTreeRouteAddressClient) node).setAddress((MapAddress) value);
                    break;
                case JXNoRootTreeTableModelAddress.ADDRESS_ROUTE:
                    if (value instanceof MapAddress)
                        ((JXTreeRouteAddressClient) node).setAddress((MapAddress) value);
                    break;
            }
        } else if (node instanceof JXTreeRouteTour) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.DRIVER:
                    if (value instanceof String)
                        ((JXTreeRouteTour) node).setDriver((String) value);
                    break;
                case JXNoRootTreeTableModelAddress.CO_DRIVER:
                    if (value instanceof String)
                        ((JXTreeRouteTour) node).setCoDriver((String) value);
                    break;
                case JXNoRootTreeTableModelAddress.CAR:
                    if (value instanceof String)
                        ((JXTreeRouteTour) node).setCar((String) value);
                    break;
                case JXNoRootTreeTableModelAddress.MAP_VISIBLE:
                    if (value instanceof Boolean)
                        ((JXTreeRouteTour) node).setMapVisible((Boolean) value);
                    break;
                case JXNoRootTreeTableModelAddress.INFO:
                    if (value instanceof String)
                        ((JXTreeRouteTour) node).setInfo((String) value);
                    break;
            }
        } else if (node instanceof JXTreeRouteItem) {
            switch (this.columnNames[column]) {
                case JXNoRootTreeTableModelAddress.ITEMS:
                    if (value instanceof String)
                        ((JXTreeRouteItem) node).setName((String) value);
                    else if (value instanceof JXTreeRouteItem)
                        ((JXTreeRouteItem) node).setName(((JXTreeRouteItem) node).getName());
                    break;
            }
        } 
    }
}