/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.abring.routenplaner.gui.dialogues;

import de.abring.routenplaner.jxtreetableroute.entries.*;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JViewport;
import net.sf.jtpl.Template;

/**
 *
 * @author Bring
 */
public class Print extends javax.swing.JDialog {
    
    private static final Logger LOGGER = Logger.getLogger(Print.class.getName());
    private static final Level LEVEL = Level.SEVERE;
    
    String text = "";
    String programDir = "";
    PageFormat format;
    private final SimpleDateFormat isoFormat = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy");
    private boolean printDone = false;
    JXTreeRouteTour tour;
    boolean preview;
    
    /**
     * Creates new form Print
     * @param parent "Main" JFrame
     * @param modal "boolean", wait for Dialog to be ready
     * @param programDir
     * @param format
     * @param tour
     * @param preview "boolean" show Preview or not
     */
    public Print(java.awt.Frame parent, boolean modal, String programDir, JXTreeRouteTour tour, PageFormat format, boolean preview) {
        super(parent, modal);
        LOGGER.setLevel(LEVEL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(LEVEL);
        LOGGER.addHandler(handler);

        LOGGER.log(Level.INFO, "Init Print ...");
        
        initComponents();
        this.format = format;
        double scale = 1.3d;
        int pageWidth = (int) Math.round(this.format.getImageableWidth() * scale) ;
        int pageHeight = (int) Math.round(this.format.getImageableHeight() * scale) ;
        this.preview = preview;
        this.programDir = programDir;
        this.tour = tour;
        preparePrint(programDir + "\\vorlagen\\drucken\\drucken.html");
        this.jPanelPrintableEditorPane1.setContentType("text/html");
        this.jPanelPrintableEditorPane1.setText(this.text);
        Dimension printingSize = new Dimension(pageWidth, pageHeight);
        this.setSize(pageWidth + 50, this.getHeight());
        this.jPanelPrintableEditorPane1.setPreferredSize(printingSize);
        this.jPanelPrintableEditorPane1.revalidate();
        JViewport jvp = this.jScrollPane1.getViewport();
        
        jvp.setSize(printingSize);
        
        this.jScrollPane1.setViewport(jvp);
        this.jTextPlane.setText(this.text);
    }
    
    /**
     * 
     */
    private void preparePrint(String template) {
        LOGGER.log(Level.INFO, "Convert Tour to String ...");

        this.text = "";
        try {
            Template tpl = new Template(new File(template));
            tpl.assign("TITLE", tour.getName());
            tpl.assign("DATUM", isoFormat.format(new Date()));
            tpl.assign("TOUR", tour.getName());
            tpl.assign("FAHRER", tour.getDriver());
            tpl.assign("BEIFAHRER", tour.getCoDriver());
            tpl.assign("FAHRZEUG", tour.getCar());
            
            for (JXTreeRouteEntry entry : tour.getEntryList()) {
                if (!(entry instanceof JXTreeRouteRoute)) {
                    tpl.assign("UHRZEIT", entry.getStart().getTimeString().replace(" ", "&nbsp;"));
                    if (entry instanceof JXTreeRouteAddress) {
                        tpl.assign("DAUER", entry.getDuration().getDurationString());
                    } else {
                        tpl.assign("DAUER", "");
                    }
                    tpl.assign("NAME", entry.getName().replace(", ", ",<br>").replace("\n", "<br>"));
                    
                    if (entry instanceof JXTreeRouteStart)
                        tpl.assign("BILD", "iconS.png");
                    else if (entry instanceof JXTreeRouteEnd)
                        tpl.assign("BILD", "iconF.png");
                    else if (entry instanceof JXTreeRouteAddress) {
                        if (entry.getName().startsWith("Löschmann"))
                            tpl.assign("BILD", "iconLC.png");
                        else if (entry.getName().startsWith("Media Markt"))
                            tpl.assign("BILD", "iconMM.png");
                        else if (entry.getName().startsWith("Saturn"))
                            tpl.assign("BILD", "iconSa.png");
                        else
                            tpl.assign("BILD", "iconK.png");
                    } else 
                        tpl.assign("BILD", "iconcross.png");

                    if (entry instanceof JXTreeRouteAddress) {
                        JXTreeRouteAddress address = (JXTreeRouteAddress) entry;
                        tpl.assign("TERMIN", address.getAppointment().getStart() + "&nbsp;Uhr<br>-<br>" + address.getAppointment().getEnd() + "&nbsp;Uhr");
                        tpl.assign("ADRESSE", address.getAddress().toString().replace(" ", "&nbsp;").replace("\n", "<br>"));

                        if (entry instanceof JXTreeRouteAddressClient) {
                            JXTreeRouteAddressClient client = (JXTreeRouteAddressClient) entry;
                            String items = "";
                            
                            for (JXTreeRouteEntry item : client.getItems()) {
                                items += item.getName() + "<br>";
                            }
                            items = items.replace(" ", "&nbsp;");
                            if (items.isEmpty()) {
                                tpl.assign("PRODUKTE", "<b>" + client.getPKSString() + "</b>");
                            } else {
                                tpl.assign("PRODUKTE", items + "<b>" + client.getPKSString() + "</b>");
                            }
                            tpl.assign("EXTRAS", "<b>" + client.getFavorite().getName() + "</b><br>" + (address.getExtras().isEmpty() ? "&nbsp;" : address.getExtras().replace("\n", "<br>")));
                        } else {
                            tpl.assign("PRODUKTE", "");
                            tpl.assign("EXTRAS", "");
                        }
                    } else {
                        tpl.assign("TERMIN", "");
                        tpl.assign("ADRESSE", "");
                        tpl.assign("PRODUKTE", "");
                        tpl.assign("EXTRAS", "");
                    }
                    tpl.parse("main.address");
                }
            }
            tpl.parse("main");
            this.text = tpl.out();
            LOGGER.log(Level.INFO, "Convert Tour to String (DONE) ...");
        } catch (FileNotFoundException | IllegalArgumentException e) {
            LOGGER.log(Level.INFO, "Convert Tour to String (ERROR) ...");
        }
    }

    /**
     * 
     */
    private void printList() {
        LOGGER.log(Level.INFO, "Move to Printer ...");
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this.jPanelPrintableEditorPane1, this.format);
        if (job.printDialog()) {
            try {
                job.print();
                this.printDone = true;
                LOGGER.log(Level.INFO, "Move to Printer (DONE)...");
                this.dispose();
            } catch (PrinterException ex) {
                LOGGER.log(Level.INFO, "Move to Printer (ERROR) ...");
            }
        } else {
            LOGGER.log(Level.INFO, "Move to Printer (ABORTED) ...");
            this.dispose();
        }
    }
    
    /**
     * 
     */
    private void openInBrowser() {
        LOGGER.log(Level.INFO, "Move to Browser ...");
        preparePrint(programDir + "\\vorlagen\\drucken\\drucken_browser.html");
        try {
            SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            File file = new File("dateien/temp/Route-" + tour.getName() + "-" + fileFormat.format(new Date()) + ".html");
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(this.text);
                fileWriter.flush();
            }
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(file.toURI());
                } catch (IOException e) {

                }
            } else {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("xdg-open " + file.toURI().toURL().toString());
                } catch (IOException e) {
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Move to Browser (DONE)...");
        }
        this.printDone = true;
        LOGGER.log(Level.INFO, "Move to Browser (DONE)...");
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelPrintableEditorPane1 = new de.abring.routenplaner.gui.components.JPanelPrintableEditorPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPlane = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 480));
        setName("PrintPreview"); // NOI18N
        setPreferredSize(new java.awt.Dimension(640, 480));
        setSize(new java.awt.Dimension(640, 480));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(100, 25));
        jToolBar1.setMinimumSize(new java.awt.Dimension(100, 25));
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 25));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Exit 2.png"))); // NOI18N
        jButton2.setToolTipText("Exit");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Settings 1.png"))); // NOI18N
        jButton4.setToolTipText("PageOption");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jToolBar1.add(jSeparator2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Printer.png"))); // NOI18N
        jButton1.setToolTipText("Print");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/abring/routenplaner/gui/icons/Browser.png"))); // NOI18N
        jButton3.setToolTipText("Browser");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jScrollPane1.setViewportView(jPanelPrintableEditorPane1);

        jTabbedPane1.addTab("text / html", jScrollPane1);

        jTextPlane.setColumns(20);
        jTextPlane.setRows(5);
        jTextPlane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextPlaneFocusLost(evt);
            }
        });
        jScrollPane3.setViewportView(jTextPlane);

        jTabbedPane1.addTab("text / plain", jScrollPane3);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        printList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        openInBrowser();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        PrinterJob pj = PrinterJob.getPrinterJob();
        format = pj.pageDialog(format);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (this.preview) {
            LOGGER.log(Level.INFO, "Starting Preview ...");
            this.jTextPlane.setText(this.text);
            LOGGER.log(Level.INFO, "Starting Preview (DONE) ...");
        } else {
            printList();
        }
    }//GEN-LAST:event_formWindowOpened

    private void jTextPlaneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPlaneFocusLost
        this.text = this.jTextPlane.getText();
        this.jPanelPrintableEditorPane1.setText(this.text);
    }//GEN-LAST:event_jTextPlaneFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private de.abring.routenplaner.gui.components.JPanelPrintableEditorPane jPanelPrintableEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextPlane;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the printDone
     */
    public boolean isPrintDone() {
        return printDone;
    }
}
