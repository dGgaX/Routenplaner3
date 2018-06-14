/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package de.abring.routenplaner.gui.components.mapTiles;

import java.util.ArrayList;
import java.util.List;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

/**
 *
 * @author Bring
 */
public class MapTileComboBoxModel implements MutableComboBoxModel {

    private final List<TileSource> entries = new ArrayList<>();
    private int selectedIndex = -1;
    
    @Override
    public void addElement(Object e) {
        if (e instanceof TileSource) {
            entries.add((TileSource) e);
            selectedIndex = entries.indexOf((TileSource) e);
        }
    }

    @Override
    public void removeElement(Object o) {
        if (o instanceof TileSource) {
            selectedIndex = entries.indexOf((TileSource) o) - 1;
            entries.remove((TileSource) o);
        }
    }

    @Override
    public void insertElementAt(Object e, int i) {
        if (e instanceof TileSource)
            entries.add(i, (TileSource) e);
    }

    @Override
    public void removeElementAt(int i) {
        selectedIndex = i - 1;
        entries.remove(i);
    }

    @Override
    public void setSelectedItem(Object o) {
        if (o instanceof TileSource) {
            selectedIndex = entries.indexOf((TileSource) o);
        }
    }

    @Override
    public Object getSelectedItem() {
        return entries.get(selectedIndex);
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public Object getElementAt(int i) {
        return entries.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener ll) {
    }

    @Override
    public void removeListDataListener(ListDataListener ll) {
    }
    
}
