package de.bring.treeTableRoute.gui.Renderer;

import de.bring.treeTableRoute.entries.*;
import de.bring.helfer.*;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class NoRootTreeTableModelRouteShort extends AbstractTreeTableModel {
    private final static String[] COLUMN_NAMES = {
        "",
        "Uhrzeit",
        "Dauer",
        "Termin", 
        "Adresse", 
    };
    private final static Class[] COLUMN_CLASS = new Class [] {
        java.lang.String.class,
        java.lang.String.class,
        java.lang.String.class,
        java.lang.String.class,
        java.lang.String.class,
    };
       
    private List<TreeEntry> EntryList;
    
    public NoRootTreeTableModelRouteShort() {
        super(new Object());
        this.EntryList = new ArrayList<>();
    }
    
    public NoRootTreeTableModelRouteShort(List<TreeEntry> EntryList) {
        super(new Object());
        this.EntryList = EntryList;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    @Override
    public Class getColumnClass(int column) {
        return COLUMN_CLASS[column];
    }
    
    @Override
    public boolean isCellEditable(Object node, int column) {
        if (node instanceof TreeAddress) {
            switch (column) {
                case 2:
                    return true;
                case 3:
                    return true;
                case 4:
                    return true;
            }
        }
        if (node instanceof TreeStart) {
            switch (column) {
                case 1:
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLeaf(Object node) {
        return false;
//        if (node instanceof TreeAddress)
//            return ((TreeAddress)node).getOrderList().isEmpty();
//        if (node instanceof TreeOrder)
//            return ((TreeOrder)node).getOrderList().isEmpty();
        //return (node instanceof TreeAddress || node instanceof TreeRoute);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof TreeRoute) {
            TreeRoute route = (TreeRoute) node;
            switch (column) {
                case 1:
                    if (route.getStart().isZero())
                        return "";
                    return route.getStart().getUhrzeitString();
                case 2:
                    if (route.getDauer().isZero())
                        return "";
                    return route.getDauer().getUhrzeitString();
                case 4:
                    if (route.getRoute().isCalculated())
                        return "     " + route.getName();
                    return "     (LL) " + route.getName();
                default:
                    return "";
            }
        }
        if (node instanceof TreeAddress) {
            TreeAddress address = (TreeAddress) node;
            switch (column) {
                case 1:
                    if (address.getStart().isZero())
                        return "";
                    return address.getStart().getUhrzeitString();
                case 2:
                    if (address.getDauer().isZero())
                        return "";
                    return address.getDauer().getUhrzeitString();
                case 3:
                    if (address.getTermin() == null)
                        return "";
                    return address.getTermin().toString();
                    
                case 4:
                    if (address.getAddress() == null)
                        return "";
                    return address.getAddress();
                default:
                    return "";
            }
        }
        if (node instanceof TreeStart) {
            TreeStart start = (TreeStart) node;
            switch (column) {
                case 1:
                    if (start.getStart().isZero()) 
                        return "";
                    return start.getStart().getUhrzeitString();
                default:
                    return "";
            }
        }
        if (node instanceof TreeEnd) {
            TreeEnd end = (TreeEnd) node;
            switch (column) {
                case 1:
                    if (end.getEnd().isZero()) 
                        return "";
                    return end.getEnd().getUhrzeitString();
                default:
                    return "";
            }
        }
        return "";
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof TreeAddress) {
            TreeAddress dept = (TreeAddress) parent;
            return dept.getOrderList().size();
        } else if(parent instanceof TreeOrder) {
            TreeOrder dept = (TreeOrder) parent;
            return dept.getProductList().size();
        } else if (parent instanceof TreeEntry) {
            return 0;
        }
            
        if (EntryList == null)
            return 0;
        return EntryList.size();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof TreeAddress) {
            TreeAddress dept = (TreeAddress) parent;
            return dept.getOrderList().get(index);
        } else if (parent instanceof TreeOrder) {
            TreeOrder dept = (TreeOrder) parent;
            return dept.getProductList().get(index);
        } else if (parent instanceof TreeEntry) {
            return null;
        }
        return EntryList.get(index);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent instanceof TreeOrder) {
            TreeOrder dept = (TreeOrder) parent;
            TreeProduct emp = (TreeProduct) child;
            return dept.getProductList().indexOf(emp);
        } else if(parent instanceof TreeAddress) {
            TreeAddress dept = (TreeAddress) parent;
            TreeOrder emp = (TreeOrder) child;
            return dept.getOrderList().indexOf(emp);
        }
        return 0;
    }
    
   @Override
    public void setValueAt(Object value, Object node, int column) {
        if (node instanceof TreeAddress) {
            TreeAddress address = (TreeAddress) node;
            switch (column) {
                case 1:
                    if (value instanceof Uhrzeit)
                        address.getStart().setZeit((Uhrzeit)value);
                    else if (value instanceof String)
                        address.getStart().setZeit((String)value);
                    return;
                case 2:
                    if (value instanceof Uhrzeit)
                        address.getDauer().setZeit((Uhrzeit)value);
                    else if (value instanceof String)
                        address.getDauer().setZeit((String)value);
                    return;
                case 3:
                    if (value instanceof Termin)
                        address.setTermin((Termin)value);
                    else if (value instanceof String) {
                        Termin temp = address.getTermin();
                        temp.setTermin((String)value);
                        address.setTermin(temp);
                    }
                    return;
                case 4:
                    if (value instanceof Address) {
//                        if (address.getParent() instanceof RouteMain)
//                            ((RouteMain)address.getParent()).removeMapMarker(address.getDot());
//                        address.setAddress((Address)value);
//                        if (address.getParent() instanceof RouteMain)
//                            ((RouteMain)address.getParent()).addMapMarker(address.getDot());
                    }
                    return;
            }
        }
        if (node instanceof TreeStart) {
            TreeStart start = (TreeStart) node;
            switch (column) {
                case 1:
                    if (value instanceof Uhrzeit)
                        start.getStart().setZeit((Uhrzeit)value);
                    else if (value instanceof String)
                        start.getStart().setZeit((String)value);
                    return;
            }
        }
        if (node instanceof TreeEnd) {
            TreeEnd end = (TreeEnd) node;
            switch (column) {
                case 1:
                    if (value instanceof Uhrzeit)
                        end.getEnd().setZeit((Uhrzeit)value);
                    else if (value instanceof String)
                        end.getEnd().setZeit((String)value);
                    return;
            }
        }
    }
}