/*
 * MainView.java
 */
package com.indragunawan.restobiz.app;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Date;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The application's main frame.
 */
public class MainView extends FrameView {

    private MenuEditView menuEdit;
    private DatabaseSetupView dbSetup;
    private TransaksiView transaksi;
    private KonfigurasiView konfigurasi;
    private GeneralConfig cfg = new GeneralConfig();
    private LoginDialogView loginDialog;
    private GantiPasswordView gantiPassword;
    private LembarInformasiView lembarInformasi;
    private KasHarianView kasHarian;
    private KriteriaLaporan kriteriaLaporan;
    private SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
    private Date tanggalAwal;
    private Date tanggalAkhir;
    private ArrayList<Integer> kriteriaShift = new ArrayList<Integer>();
    private KriteriaTop10 kriteriaTop10;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private static Integer reqDBVersion = 2;

    public MainView(SingleFrameApplication app) {
        super(app);

        initComponents();

        Boolean IsValidAccess = Boolean.valueOf(System.getenv("RESTO_VALID_ACCESS"));

        if (!IsValidAccess && cfg.getReportLocation().isEmpty()) {
            TerminateApplication();
        }

        if (cfg.isFirstTime() && cfg.isDbConnected()) {
            if (IsValidAccess) {
                cfg.createDatabaseTable(getDefaultScriptsDirectory() + "restobiz.sql");
            } else {
                TerminateApplication();
            }
        }

        if (!cfg.isFirstTime() && cfg.isDbConnected()) {
            if (cfg.getDatabaseVersion() < 2) {
                JOptionPane.showMessageDialog(null, "Database perlu diupgrade untuk kompatibilitas dengan versi terbaru");
                if (IsValidAccess) {
                    if (cfg.createDatabaseTable(getDefaultScriptsDirectory() + "upgrade2.sql")) {
                        cfg.setDatabaseVersion(2);
                    } else {
                        JOptionPane.showMessageDialog(null, "Database gagal diupgrade", "Perhatian", JOptionPane.OK_OPTION);
                    }
                } else {
                    TerminateApplication();
                }
            }
        }

        if (cfg.getReportLocation().isEmpty()) {
            cfg.setReportLocation(getDefaultReportDirectory());
        }

        if (cfg.isUserLogged()) {
            cfg.clearLoggedUser();
        }

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    public String getDefaultReportDirectory() {
        String location = ".." + File.separator + "reports" + File.separator + ".";
        File f = new File(location);
        try {
            location = f.getCanonicalPath() + File.separator;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return location;
    }

    public String getDefaultScriptsDirectory() {
        String location = ".." + File.separator + "scripts" + File.separator + ".";
        File f = new File(location);
        try {
            location = f.getCanonicalPath() + File.separator;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return location;
    }

    private void TerminateApplication() throws HeadlessException {
        JOptionPane.showMessageDialog(null, "Harap eksekusi melalui start.sh atau start.bat");
        System.exit(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        restobizToolBar = new javax.swing.JToolBar();
        transaksiToolButton = new javax.swing.JButton();
        kasHarianToolBar = new javax.swing.JButton();
        kasHarianToolSeparator = new javax.swing.JToolBar.Separator();
        menuToolBar = new javax.swing.JButton();
        passwordToolButton = new javax.swing.JButton();
        passwordToolSeparator = new javax.swing.JToolBar.Separator();
        infoToolBar = new javax.swing.JButton();
        backgroundImageLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        transaksiMenuItem = new javax.swing.JMenuItem();
        kasHarianMenuItem = new javax.swing.JMenuItem();
        kasHarianSeparator = new javax.swing.JSeparator();
        menuMenuItem = new javax.swing.JMenuItem();
        menuSeparator = new javax.swing.JSeparator();
        logoutMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        laporanMenu = new javax.swing.JMenu();
        daftarMenuMenuItem = new javax.swing.JMenuItem();
        daftarMenuSeparator = new javax.swing.JSeparator();
        laporanTransaksiMenuItem = new javax.swing.JMenuItem();
        laporanKasMenuItem = new javax.swing.JMenuItem();
        laporanKasSeparator = new javax.swing.JSeparator();
        rekapPenjualanMenuItem = new javax.swing.JMenuItem();
        top10MenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        databaseMenuItem = new javax.swing.JMenuItem();
        konfigurasiMenuItem = new javax.swing.JMenuItem();
        konfigurasiSeparator = new javax.swing.JSeparator();
        passwordMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        infoMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        restobizToolBar.setRollover(true);
        restobizToolBar.setName("restobizToolBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getActionMap(MainView.class, this);
        transaksiToolButton.setAction(actionMap.get("showTransaksi")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getResourceMap(MainView.class);
        transaksiToolButton.setIcon(resourceMap.getIcon("transaksiToolButton.icon")); // NOI18N
        transaksiToolButton.setText(resourceMap.getString("transaksiToolButton.text")); // NOI18N
        transaksiToolButton.setFocusable(false);
        transaksiToolButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        transaksiToolButton.setName("transaksiToolButton"); // NOI18N
        transaksiToolButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        restobizToolBar.add(transaksiToolButton);

        kasHarianToolBar.setAction(actionMap.get("showKasHarian")); // NOI18N
        kasHarianToolBar.setIcon(resourceMap.getIcon("kasHarianToolBar.icon")); // NOI18N
        kasHarianToolBar.setText(resourceMap.getString("kasHarianToolBar.text")); // NOI18N
        kasHarianToolBar.setFocusable(false);
        kasHarianToolBar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kasHarianToolBar.setName("kasHarianToolBar"); // NOI18N
        kasHarianToolBar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        restobizToolBar.add(kasHarianToolBar);

        kasHarianToolSeparator.setName("kasHarianToolSeparator"); // NOI18N
        restobizToolBar.add(kasHarianToolSeparator);

        menuToolBar.setAction(actionMap.get("showMenuEdit")); // NOI18N
        menuToolBar.setIcon(resourceMap.getIcon("menuToolBar.icon")); // NOI18N
        menuToolBar.setText(resourceMap.getString("menuToolBar.text")); // NOI18N
        menuToolBar.setFocusable(false);
        menuToolBar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuToolBar.setName("menuToolBar"); // NOI18N
        menuToolBar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        restobizToolBar.add(menuToolBar);

        passwordToolButton.setAction(actionMap.get("showGantiPasswordView")); // NOI18N
        passwordToolButton.setIcon(resourceMap.getIcon("passwordToolButton.icon")); // NOI18N
        passwordToolButton.setText(resourceMap.getString("passwordToolButton.text")); // NOI18N
        passwordToolButton.setFocusable(false);
        passwordToolButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        passwordToolButton.setName("passwordToolButton"); // NOI18N
        passwordToolButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        restobizToolBar.add(passwordToolButton);

        passwordToolSeparator.setName("passwordToolSeparator"); // NOI18N
        restobizToolBar.add(passwordToolSeparator);

        infoToolBar.setAction(actionMap.get("showInformation")); // NOI18N
        infoToolBar.setIcon(resourceMap.getIcon("infoToolBar.icon")); // NOI18N
        infoToolBar.setText(resourceMap.getString("infoToolBar.text")); // NOI18N
        infoToolBar.setFocusable(false);
        infoToolBar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        infoToolBar.setName("infoToolBar"); // NOI18N
        infoToolBar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        restobizToolBar.add(infoToolBar);

        backgroundImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backgroundImageLabel.setIcon(resourceMap.getIcon("backgroundImageLabel.icon")); // NOI18N
        backgroundImageLabel.setText(resourceMap.getString("backgroundImageLabel.text")); // NOI18N
        backgroundImageLabel.setDoubleBuffered(true);
        backgroundImageLabel.setName("backgroundImageLabel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(restobizToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addComponent(backgroundImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(restobizToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 508, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setMnemonic('F');
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        transaksiMenuItem.setAction(actionMap.get("showTransaksi")); // NOI18N
        transaksiMenuItem.setMnemonic('T');
        transaksiMenuItem.setText(resourceMap.getString("transaksiMenuItem.text")); // NOI18N
        transaksiMenuItem.setName("transaksiMenuItem"); // NOI18N
        fileMenu.add(transaksiMenuItem);

        kasHarianMenuItem.setAction(actionMap.get("showKasHarian")); // NOI18N
        kasHarianMenuItem.setMnemonic('K');
        kasHarianMenuItem.setText(resourceMap.getString("kasHarianMenuItem.text")); // NOI18N
        kasHarianMenuItem.setName("kasHarianMenuItem"); // NOI18N
        fileMenu.add(kasHarianMenuItem);

        kasHarianSeparator.setName("kasHarianSeparator"); // NOI18N
        fileMenu.add(kasHarianSeparator);

        menuMenuItem.setAction(actionMap.get("showMenuEdit")); // NOI18N
        menuMenuItem.setMnemonic('M');
        menuMenuItem.setText(resourceMap.getString("menuMenuItem.text")); // NOI18N
        menuMenuItem.setName("menuMenuItem"); // NOI18N
        fileMenu.add(menuMenuItem);

        menuSeparator.setName("menuSeparator"); // NOI18N
        fileMenu.add(menuSeparator);

        logoutMenuItem.setAction(actionMap.get("showLoginDialog")); // NOI18N
        logoutMenuItem.setMnemonic('L');
        logoutMenuItem.setName("logoutMenuItem"); // NOI18N
        fileMenu.add(logoutMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        laporanMenu.setMnemonic('L');
        laporanMenu.setText(resourceMap.getString("laporanMenu.text")); // NOI18N
        laporanMenu.setName("laporanMenu"); // NOI18N

        daftarMenuMenuItem.setAction(actionMap.get("printDaftarMenu")); // NOI18N
        daftarMenuMenuItem.setMnemonic('M');
        daftarMenuMenuItem.setText(resourceMap.getString("daftarMenuMenuItem.text")); // NOI18N
        daftarMenuMenuItem.setName("daftarMenuMenuItem"); // NOI18N
        laporanMenu.add(daftarMenuMenuItem);

        daftarMenuSeparator.setName("daftarMenuSeparator"); // NOI18N
        laporanMenu.add(daftarMenuSeparator);

        laporanTransaksiMenuItem.setAction(actionMap.get("showLaporanTransaksi")); // NOI18N
        laporanTransaksiMenuItem.setMnemonic('T');
        laporanTransaksiMenuItem.setText(resourceMap.getString("laporanTransaksiMenuItem.text")); // NOI18N
        laporanTransaksiMenuItem.setName("laporanTransaksiMenuItem"); // NOI18N
        laporanMenu.add(laporanTransaksiMenuItem);

        laporanKasMenuItem.setAction(actionMap.get("showLaporanKas")); // NOI18N
        laporanKasMenuItem.setMnemonic('K');
        laporanKasMenuItem.setText(resourceMap.getString("laporanKasMenuItem.text")); // NOI18N
        laporanKasMenuItem.setName("laporanKasMenuItem"); // NOI18N
        laporanMenu.add(laporanKasMenuItem);

        laporanKasSeparator.setName("laporanKasSeparator"); // NOI18N
        laporanMenu.add(laporanKasSeparator);

        rekapPenjualanMenuItem.setAction(actionMap.get("showLaporanPenjualan")); // NOI18N
        rekapPenjualanMenuItem.setMnemonic('P');
        rekapPenjualanMenuItem.setText(resourceMap.getString("rekapPenjualanMenuItem.text")); // NOI18N
        rekapPenjualanMenuItem.setName("rekapPenjualanMenuItem"); // NOI18N
        laporanMenu.add(rekapPenjualanMenuItem);

        top10MenuItem.setAction(actionMap.get("showTop10Penjualan")); // NOI18N
        top10MenuItem.setMnemonic('o');
        top10MenuItem.setText(resourceMap.getString("top10MenuItem.text")); // NOI18N
        top10MenuItem.setName("top10MenuItem"); // NOI18N
        laporanMenu.add(top10MenuItem);

        menuBar.add(laporanMenu);

        toolsMenu.setMnemonic('T');
        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        databaseMenuItem.setAction(actionMap.get("showDBSetup")); // NOI18N
        databaseMenuItem.setMnemonic('D');
        databaseMenuItem.setText(resourceMap.getString("databaseMenuItem.text")); // NOI18N
        databaseMenuItem.setName("databaseMenuItem"); // NOI18N
        toolsMenu.add(databaseMenuItem);

        konfigurasiMenuItem.setAction(actionMap.get("showKonfigurasi")); // NOI18N
        konfigurasiMenuItem.setMnemonic('K');
        konfigurasiMenuItem.setText(resourceMap.getString("konfigurasiMenuItem.text")); // NOI18N
        konfigurasiMenuItem.setName("konfigurasiMenuItem"); // NOI18N
        toolsMenu.add(konfigurasiMenuItem);

        konfigurasiSeparator.setName("konfigurasiSeparator"); // NOI18N
        toolsMenu.add(konfigurasiSeparator);

        passwordMenuItem.setAction(actionMap.get("showGantiPasswordView")); // NOI18N
        passwordMenuItem.setMnemonic('P');
        passwordMenuItem.setText(resourceMap.getString("passwordMenuItem.text")); // NOI18N
        passwordMenuItem.setName("passwordMenuItem"); // NOI18N
        toolsMenu.add(passwordMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        infoMenuItem.setAction(actionMap.get("showInformation")); // NOI18N
        infoMenuItem.setMnemonic('I');
        infoMenuItem.setText(resourceMap.getString("infoMenuItem.text")); // NOI18N
        infoMenuItem.setName("infoMenuItem"); // NOI18N
        helpMenu.add(infoMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 616, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void showLoginDialog() {
        if (cfg.isUserLogged()) {
            cfg.clearLoggedUser();
        }
        if ((loginDialog != null) && (!loginDialog.isVisible())) {
            loginDialog = null;
        }
        if (loginDialog == null) {
            JFrame mainFrame = MainApp.getApplication().getMainFrame();
            loginDialog = new LoginDialogView(mainFrame, true);
            loginDialog.setLocationRelativeTo(mainFrame);
        }
        MainApp.getApplication().show(loginDialog);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MainApp.getApplication().getMainFrame();
            aboutBox = new AboutBoxView(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MainApp.getApplication().show(aboutBox);
    }

    @Action
    public void showMenuEdit() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(1300)) {
            if ((menuEdit != null) && (!menuEdit.isVisible())) {
                menuEdit = null;
            }
            if (menuEdit == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                menuEdit = new MenuEditView(mainFrame, true);
                menuEdit.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(menuEdit);
        }
    }

    @Action
    public void showDBSetup() {
        Boolean confirmed = true;

        if (cfg.isDbConnected()) {
            if (!cfg.confirmRestricted(3100)) {
                if (JOptionPane.showConfirmDialog(null, "Koneksi database telah terhubung dengan baik.\nAnda yakin akan masuk ke konfigurasi database?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    confirmed = true;
                } else {
                    confirmed = false;
                }
            } else {
                confirmed = false;
            }
        }

        if (confirmed) {
            if (dbSetup == null) {
                if ((dbSetup != null) && (!dbSetup.isVisible())) {
                    dbSetup = null;
                }
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                dbSetup = new DatabaseSetupView(mainFrame, true);
                dbSetup.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(dbSetup);
        }
    }

    @Action
    public void showTransaksi() {
        if (cfg.confirmConnected() && cfg.confirmUserLogged() && cfg.confirmMenuExists() && !cfg.confirmRestricted(1100)) {
            if ((transaksi != null) && (!transaksi.isVisible())) {
                transaksi = null;
            }
            if (transaksi == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                transaksi = new TransaksiView(mainFrame, false);
                transaksi.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(transaksi);
        }
    }

    @Action
    public void showKonfigurasi() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(3200)) {
            if ((konfigurasi != null) && (!konfigurasi.isVisible())) {
                konfigurasi = null;
            }
            if (konfigurasi == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                konfigurasi = new KonfigurasiView(mainFrame, false);
                konfigurasi.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(konfigurasi);
        }
    }

    @Action
    public void showGantiPasswordView() {
        if (cfg.confirmConnected()) {
            if ((gantiPassword != null) && (!gantiPassword.isVisible())) {
                gantiPassword = null;
            }
            if (gantiPassword == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                gantiPassword = new GantiPasswordView(mainFrame, false);
                gantiPassword.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(gantiPassword);
        }
    }

    @Action
    public void showInformation() {
        if ((lembarInformasi != null) && (!lembarInformasi.isVisible())) {
            lembarInformasi = null;
        }
        if (lembarInformasi == null) {
            JFrame mainFrame = MainApp.getApplication().getMainFrame();
            lembarInformasi = new LembarInformasiView(mainFrame, false);
            lembarInformasi.setLocationRelativeTo(mainFrame);
        }
        MainApp.getApplication().show(lembarInformasi);
    }

    @Action
    public void showKasHarian() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(1200)) {
            if ((kasHarian != null) && (!kasHarian.isVisible())) {
                kasHarian = null;
            }
            if (kasHarian == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                kasHarian = new KasHarianView(mainFrame, false);
                kasHarian.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(kasHarian);
        }
    }

    @Action
    @SuppressWarnings("unchecked")
    public void showLaporanTransaksi() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(2200)) {
            if ((kriteriaLaporan != null) && (!kriteriaLaporan.isVisible())) {
                kriteriaLaporan = null;
            }
            if (kriteriaLaporan == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                kriteriaLaporan = new KriteriaLaporan(mainFrame, true);
                kriteriaLaporan.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(kriteriaLaporan);
            if (kriteriaLaporan.getConfirmOpen()) {

                kriteriaShift = kriteriaLaporan.getKriteriaShift();
                tanggalAwal = kriteriaLaporan.getTanggalAwal();
                tanggalAkhir = kriteriaLaporan.getTanggalAkhir();

                try {

                    Map parameters = new HashMap();

                    parameters = cfg.getCompanyReportHeader();

                    parameters.put("TANGGAL_AWAL", dateSQL.format(tanggalAwal));
                    parameters.put("TANGGAL_AKHIR", dateSQL.format(tanggalAkhir));
                    parameters.put("RENTANG_SHIFT", kriteriaShift);

                    cfg.previewReport(null, "sales.jasper", parameters, "Sales Report", true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Action
    public void printDaftarMenu() {
        if (cfg.confirmConnected()) {
            cfg.printMenuList();
        }
    }

    @Action
    @SuppressWarnings("unchecked")
    public void showLaporanKas() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(2300)) {
            if ((kriteriaLaporan != null) && (!kriteriaLaporan.isVisible())) {
                kriteriaLaporan = null;
            }
            if (kriteriaLaporan == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                kriteriaLaporan = new KriteriaLaporan(mainFrame, true);
                kriteriaLaporan.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(kriteriaLaporan);
            if (kriteriaLaporan.getConfirmOpen()) {

                kriteriaShift = kriteriaLaporan.getKriteriaShift();
                tanggalAwal = kriteriaLaporan.getTanggalAwal();
                tanggalAkhir = kriteriaLaporan.getTanggalAkhir();

                try {

                    Map parameters = new HashMap();

                    parameters = cfg.getCompanyReportHeader();

                    parameters.put("TANGGAL_AWAL", dateSQL.format(tanggalAwal));
                    parameters.put("TANGGAL_AKHIR", dateSQL.format(tanggalAkhir));
                    parameters.put("RENTANG_SHIFT", kriteriaShift);

                    cfg.previewReport(null, "cashreport.jasper", parameters, "Cash Report", true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Action
    @SuppressWarnings("unchecked")
    public void showLaporanPenjualan() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(2400)) {
            if ((kriteriaLaporan != null) && (!kriteriaLaporan.isVisible())) {
                kriteriaLaporan = null;
            }
            if (kriteriaLaporan == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                kriteriaLaporan = new KriteriaLaporan(mainFrame, true);
                kriteriaLaporan.setLocationRelativeTo(mainFrame);
            }
            kriteriaLaporan.setPeriodeOnly(true);
            MainApp.getApplication().show(kriteriaLaporan);
            if (kriteriaLaporan.getConfirmOpen()) {

                kriteriaShift = kriteriaLaporan.getKriteriaShift();
                tanggalAwal = kriteriaLaporan.getTanggalAwal();
                tanggalAkhir = kriteriaLaporan.getTanggalAkhir();

                try {

                    Map parameters = new HashMap();

                    parameters = cfg.getCompanyReportHeader();

                    parameters.put("TANGGAL_AWAL", dateSQL.format(tanggalAwal));
                    parameters.put("TANGGAL_AKHIR", dateSQL.format(tanggalAkhir));
                    parameters.put("RENTANG_SHIFT", kriteriaShift);

                    cfg.previewReport(null, "salescharts.jasper", parameters, "Sales Charts", true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Action
    @SuppressWarnings("unchecked")
    public void showTop10Penjualan() {
        if (cfg.confirmConnected() && !cfg.confirmRestricted(2500)) {
            if ((kriteriaTop10 != null) && (!kriteriaTop10.isVisible())) {
                kriteriaTop10 = null;
            }
            if (kriteriaTop10 == null) {
                JFrame mainFrame = MainApp.getApplication().getMainFrame();
                kriteriaTop10 = new KriteriaTop10(mainFrame, true);
                kriteriaTop10.setLocationRelativeTo(mainFrame);
            }
            MainApp.getApplication().show(kriteriaTop10);
            if (kriteriaTop10.getConfirmOpen()) {

                String kriteriaKelompok = kriteriaTop10.getKriteriaKelompok();
                tanggalAwal = kriteriaTop10.getTanggalAwal();
                tanggalAkhir = kriteriaTop10.getTanggalAkhir();

                try {

                    Map parameters = new HashMap();

                    parameters = cfg.getCompanyReportHeader();

                    parameters.put("TANGGAL_AWAL", dateSQL.format(tanggalAwal));
                    parameters.put("TANGGAL_AKHIR", dateSQL.format(tanggalAkhir));
                    parameters.put("KELOMPOK", kriteriaKelompok);

                    cfg.previewReport(null, "top10list.jasper", parameters, "Top 10 Charts", true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundImageLabel;
    private javax.swing.JMenuItem daftarMenuMenuItem;
    private javax.swing.JSeparator daftarMenuSeparator;
    private javax.swing.JMenuItem databaseMenuItem;
    private javax.swing.JMenuItem infoMenuItem;
    private javax.swing.JButton infoToolBar;
    private javax.swing.JMenuItem kasHarianMenuItem;
    private javax.swing.JSeparator kasHarianSeparator;
    private javax.swing.JButton kasHarianToolBar;
    private javax.swing.JToolBar.Separator kasHarianToolSeparator;
    private javax.swing.JMenuItem konfigurasiMenuItem;
    private javax.swing.JSeparator konfigurasiSeparator;
    private javax.swing.JMenuItem laporanKasMenuItem;
    private javax.swing.JSeparator laporanKasSeparator;
    private javax.swing.JMenu laporanMenu;
    private javax.swing.JMenuItem laporanTransaksiMenuItem;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuMenuItem;
    private javax.swing.JSeparator menuSeparator;
    private javax.swing.JButton menuToolBar;
    private javax.swing.JMenuItem passwordMenuItem;
    private javax.swing.JButton passwordToolButton;
    private javax.swing.JToolBar.Separator passwordToolSeparator;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem rekapPenjualanMenuItem;
    private javax.swing.JToolBar restobizToolBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem top10MenuItem;
    private javax.swing.JMenuItem transaksiMenuItem;
    private javax.swing.JButton transaksiToolButton;
    // End of variables declaration//GEN-END:variables
}
