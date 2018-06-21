
package de.bring.treeTableRoute.gui;

import de.bring.helfer.*;
import de.bring.treeTableRoute.entries.*;
import de.bring.treeTableRoute.gui.Renderer.MyTreeCellRendererRoute;
import de.bring.treeTableRoute.gui.Renderer.TableRowTransferHandlerAddress;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author Andreas
 */
public class JXTreeTableRoute extends JXTreeTable {
    List<TreeEntry> entryList = new ArrayList<>();
    protected int formerSelectedRow = 0;
    protected MyTreeCellRendererRoute treeCellRenderer = new MyTreeCellRendererRoute();
    public JXTreeTableRoute() {
        super();
        setTreeCellRenderer(treeCellRenderer);
        setRowHeight(22);
        setIntercellSpacing(new Dimension(2, 0));
        addHighlighter(HighlighterFactory.createAlternateStriping(new Color(235, 245, 255), Color.white));
        setTransferHandler(new TableRowTransferHandlerAddress(this));
    }
    public void addEntry(TreeEntry entry) {
        this.entryList.add(entry);
        refreshTable();
    }
    public void addEntry(int i, TreeEntry entry) {
        this.entryList.add(i, entry);
        refreshTable();
    }
    public void removeEntry(int i) {
        this.entryList.remove(i);
        refreshTable();
    }
    public void removeEntry(TreeEntry entry) {
        this.entryList.remove(entry);
        refreshTable();
    }
    public TreeEntry getEntryAt(int i) {
        return this.entryList.get(i);
    }
    public TreeEntry getEntryAtAddress(Address a) {
        for (TreeEntry entry :  this.entryList) {
            if (entry instanceof TreeAddress) {
                TreeAddress ta = ((TreeAddress) entry);
                if (ta.getAddress().equals(a))
                    return entry;
            }
        }
        return null;
    }
    public int getIndexOf(TreeEntry entry) {
        return this.entryList.indexOf(entry);
    }
    public void refreshTable() {
        Uhrzeit prevEnde = null;
        int ID = 0;
        for (TreeEntry entry : this.entryList) {
            if (entry instanceof TreeAddress) {
                TreeAddress address = (TreeAddress) entry;
                if (prevEnde != null)
                    address.setStart(prevEnde);
                address.setID(++ID);
                prevEnde = address.getEnde();
            } else if (entry instanceof TreeRoute) {
                TreeRoute route = (TreeRoute) entry;
                if (prevEnde != null)
                    route.setStart(prevEnde);
                prevEnde = route.getEnde();
            } else if (entry instanceof TreeStart) {
                TreeStart start = (TreeStart) entry;
                if (prevEnde != null)
                    start.setStart(prevEnde);
                prevEnde = start.getEnd();
            } else if (entry instanceof TreeEnd) {
                TreeEnd end = (TreeEnd) entry;
                if (prevEnde != null)
                    end.setEnd(prevEnde);
                prevEnde = end.getEnd();
            }
            
        }
        //updateUI();    
        repaint();
    }
    public List<TreeEntry> getEntryList() {
        return this.entryList;
    }
    public void setTreeList(List<TreeEntry> entryList) {
        this.entryList = entryList;
    }
    public int getFormerSelectedRow() {
        return this.formerSelectedRow;
    }
    public void setFormerSelectedRow() {
        this.formerSelectedRow = this.getSelectedRow();
    }
    public void setColumnWidth(int column, int width, boolean resizeable){
        getColumnModel().getColumn(column).setResizable(resizeable);
        getColumnModel().getColumn(column).setWidth(width);
        getColumnModel().getColumn(column).setPreferredWidth(width);
        if (!resizeable) {
            getColumnModel().getColumn(column).setMaxWidth(width);
            getColumnModel().getColumn(column).setMinWidth(width);
        }
    }
    public void setColumnAlignment(int column, int alignment) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        getTableHeader().getColumnModel().getColumn(column).setCellRenderer(renderer);
        getColumnModel().getColumn(column).setCellRenderer(renderer);
    }
    
}
