/*
 * KonfigurasiView.java
 *
 * Created on August 14, 2008, 12:27 PM
 */
package com.indragunawan.restobiz.app;

import com.indragunawan.restobiz.app.model.JenisBayarRD;
import com.indragunawan.restobiz.app.model.JenisBayarTM;
import com.indragunawan.restobiz.app.model.KebijakanViewRD;
import com.indragunawan.restobiz.app.model.KebijakanViewTM;
import com.indragunawan.restobiz.app.model.OperatorViewRD;
import com.indragunawan.restobiz.app.model.OperatorViewTM;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author  igoens
 */
public class KonfigurasiView extends javax.swing.JDialog {

    private static final long serialVersionUID = 750409478868922395L;

    private ModulePrivilages mp = new ModulePrivilages();
    private GeneralConfig cfg = new GeneralConfig();
    private Boolean firstTime;
    private List hakAksesList;
    private int i;

    /** Creates new form KonfigurasiView */
    public KonfigurasiView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        firstTime = true;
        fillNilaiKonfigurasi();
    }

    private Boolean isValidUserInput() {
        Boolean r = true;
        if (namaUserField.getText().length() == 0) {
            r = false;
            JOptionPane.showMessageDialog(this, "Nama User tidak boleh kosong");
            namaUserField.requestFocus();
        } else if (String.valueOf(passwordField.getPassword()).length() == 0) {
            r = false;
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong");
            passwordField.requestFocus();
        } else if (namaAsliField.getText().length() == 0) {
            r = false;
            JOptionPane.showMessageDialog(this, "Nama Asli harus diisi");
            namaAsliField.requestFocus();
        } else if (String.valueOf(hakAksesField.getSelectedIndex()).length() == 0) {
            r = false;
            JOptionPane.showMessageDialog(this, "Pilih Hak Akses operator");
            hakAksesField.requestFocus();
        }
        return r;
    }

    private void fillGroupList() {
        try {
            hakAksesList = cfg.getAksesList();

            groupField.removeAllItems();
            for (i = 0; i <= hakAksesList.size() - 1; i++) {
                if (!String.valueOf(hakAksesList.get(i)).equals("ADMIN")) {
                    groupField.addItem(String.valueOf(hakAksesList.get(i)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillHakAksesOperator() {
        try {
            hakAksesList = cfg.getAksesList();

            hakAksesField.removeAllItems();
            for (i = 0; i <= hakAksesList.size() - 1; i++) {
                hakAksesField.addItem(String.valueOf(hakAksesList.get(i)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillNilaiGeneral() {
        jumlahShiftField.setText(String.valueOf(cfg.getMaxShift()));
        lokasiReportField.setText(String.valueOf(cfg.getReportLocation()));
        nomorTransaksiField.setText(String.valueOf(cfg.getNomorTransaksi()));
        nomorKuitansiField.setText(String.valueOf(cfg.getNomorKuitansi()));
        nomorKasField.setText(String.valueOf(cfg.getNomorKas()));
        discountField.setText(String.valueOf(cfg.getGlobalDiscount()));
        pajakField.setText(String.valueOf(cfg.getVatValue()));
        jumlahShiftField.requestFocus();
    }

    private void fillNilaiGroup() {
        fillUserGroupList();
        fillSystemGroupList();
        resetInputUserGroup();
    }

    private void fillNilaiKebijakan() {
        List tmp = mp.getAvailableModules();

        kebijakanField.removeAllItems();
        for (i = 0; i <= tmp.size() - 1; i++) {
            kebijakanField.addItem(String.valueOf(tmp.get(i)));
        }
    }

    private void fillNilaiOperator() {
        operatorTable.setModel(new OperatorViewTM(cfg.getOperatorList()));
        for (i = 0; i < operatorTable.getColumnCount(); i++) {
            operatorTable.getColumnModel().getColumn(i).setCellRenderer(new OperatorViewRD());
        }
        fillHakAksesOperator();
        resetInputOperator();
    }

    private void fillNilaiPembayaran() {
        jenisBayarTable.setModel(new JenisBayarTM(cfg.getJenisBayarList()));
        for (i = 0; i < jenisBayarTable.getColumnCount(); i++) {
            jenisBayarTable.getColumnModel().getColumn(i).setCellRenderer(new JenisBayarRD());
        }
    }

    private void fillNilaiPerusahaan() {
        perusahaanField.setText(String.valueOf(cfg.getNamaPerusahaan()));
        jenisUsahaField.setText(String.valueOf(cfg.getJenisUsaha()));
        alamatField.setText(String.valueOf(cfg.getAlamatUsaha()));
        teleponField.setText(String.valueOf(cfg.getTelepon()));
        pesanPromosiField.setText(String.valueOf(cfg.getPesanPromosi()));
        perusahaanField.requestFocus();
    }

    private void fillNilaiKonfigurasi() {
        String actionTab;
        actionTab = contentTabbedPanel.getTitleAt(contentTabbedPanel.getSelectedIndex());

        if (actionTab.equals("General")) {
            fillNilaiGeneral();
        }

        if (actionTab.equals("Perusahaan")) {
            fillNilaiPerusahaan();
        }

        if (actionTab.equals("Pembayaran")) {
            fillNilaiPembayaran();
        }
    }

    private void fillSystemGroupList() {
        DefaultListModel model = new DefaultListModel();
        List temp = cfg.getSystemGroupList();

        for (i = 0; i <= temp.size() - 1; i++) {
            model.addElement(temp.get(i));
        }

        systemGroupList.setModel(model);
    }

    private void fillUserGroupList() {
        DefaultListModel model = new DefaultListModel();
        List temp = cfg.getUserGroupList();

        for (i = 0; i <= temp.size() - 1; i++) {
            model.addElement(temp.get(i));
        }

        userGroupList.setModel(model);
    }

    private void fillSkemaTable() {
        skemaTable.setModel(new KebijakanViewTM(cfg.getSkemaList()));
        for (i = 0; i < skemaTable.getColumnCount(); i++) {
            skemaTable.getColumnModel().getColumn(i).setCellRenderer(new KebijakanViewRD());
        }
    }

    private void resetInputKebijakan() {
        kebijakanField.setSelectedIndex(-1);
        groupField.setSelectedIndex(-1);
        kebijakanField.requestFocus();
    }

    private void resetInputOperator() {
        namaUserField.setText("");
        namaAsliField.setText("");
        passwordField.setText("");
        hakAksesField.setSelectedIndex(-1);
    }

    private void resetInputUserGroup() {
        tambahGroupField.setText("");
        tambahGroupField.requestFocus();
    }

    private Boolean saveNilaiKofigurasi() {
        Boolean result = false;
        String actionTab;

        actionTab = contentTabbedPanel.getTitleAt(contentTabbedPanel.getSelectedIndex());

        if (actionTab.equals("General")) {
            result = saveNilaiGeneral();
        }

        if (actionTab.equals("Perusahaan")) {
            result = saveNilaiPerusahaan();
        }

        return result;
    }

    private Boolean saveNilaiGeneral() {
        /** Save method for general */
        Boolean result = false;
        try {
            cfg.setMaxShift(Integer.valueOf(jumlahShiftField.getText()));
            cfg.setReportLocation(String.valueOf(lokasiReportField.getText()));
            cfg.setGlobalDiscount(Double.valueOf(discountField.getText()));
            cfg.setVatValue(Double.valueOf(pajakField.getText()));
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private Boolean saveNilaiPerusahaan() {
        Boolean result = false;
        try {
            cfg.setNamaPerusahaan(String.valueOf(perusahaanField.getText()));
            cfg.setJenisUsaha(String.valueOf(jenisUsahaField.getText()));
            cfg.setAlamatUsaha(String.valueOf(alamatField.getText()));
            cfg.setTelepon(String.valueOf(teleponField.getText()));
            cfg.setPesanPromosi(String.valueOf(pesanPromosiField.getText()));
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jenisBayarPopup = new javax.swing.JPopupMenu();
        hapusMenuItem = new javax.swing.JMenuItem();
        userGroupPopup = new javax.swing.JPopupMenu();
        hapusUserGroup = new javax.swing.JMenuItem();
        skemaPopup = new javax.swing.JPopupMenu();
        hapusSkema = new javax.swing.JMenuItem();
        userPopup = new javax.swing.JPopupMenu();
        hapusUser = new javax.swing.JMenuItem();
        headerPanel = new javax.swing.JPanel();
        judulLabel = new javax.swing.JLabel();
        subJudulLabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        batalButton = new javax.swing.JButton();
        simpanButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        contentTabbedPanel = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        kuitansiPanel = new javax.swing.JPanel();
        nomorKuitansiField = new javax.swing.JTextField();
        nomorKuitansiLabel = new javax.swing.JLabel();
        komponenHargaPanel = new javax.swing.JPanel();
        pajakLabel = new javax.swing.JLabel();
        pajakField = new javax.swing.JTextField();
        discountField = new javax.swing.JTextField();
        discountLabel = new javax.swing.JLabel();
        konfigurasiUmumPanel = new javax.swing.JPanel();
        jumlahShiftLabel = new javax.swing.JLabel();
        jumlahShiftField = new javax.swing.JTextField();
        lokasiReportLabel = new javax.swing.JLabel();
        lokasiReportField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        nomorKasPanel = new javax.swing.JPanel();
        nomorKasField = new javax.swing.JTextField();
        nomorKasLabel = new javax.swing.JLabel();
        transaksiPanel = new javax.swing.JPanel();
        nomorTransaksiLabel = new javax.swing.JLabel();
        nomorTransaksiField = new javax.swing.JTextField();
        catatanPanel = new javax.swing.JPanel();
        catatanLabel = new javax.swing.JLabel();
        perusahaanPanel = new javax.swing.JPanel();
        informasiPerusahaanPanel = new javax.swing.JPanel();
        perusahaanLabel = new javax.swing.JLabel();
        perusahaanField = new javax.swing.JTextField();
        jenisUsahaLabel = new javax.swing.JLabel();
        jenisUsahaField = new javax.swing.JTextField();
        alamatLabel = new javax.swing.JLabel();
        alamatField = new javax.swing.JTextField();
        teleponLabel = new javax.swing.JLabel();
        teleponField = new javax.swing.JTextField();
        pesanPromosiLabel = new javax.swing.JLabel();
        pesanPromosiField = new javax.swing.JTextField();
        caraBayarPanel = new javax.swing.JPanel();
        jenisBayarScrollPane = new javax.swing.JScrollPane();
        jenisBayarTable = new javax.swing.JTable();
        jenisLabel = new javax.swing.JLabel();
        jenisField = new javax.swing.JTextField();
        negasiCheckBox = new javax.swing.JCheckBox();
        tambahJenisBayarButton = new javax.swing.JButton();
        hapusButton = new javax.swing.JButton();
        operatorPanel = new javax.swing.JPanel();
        schemeTabbedPanel = new javax.swing.JTabbedPane();
        userPanel = new javax.swing.JPanel();
        operatorScrollPane = new javax.swing.JScrollPane();
        operatorTable = new javax.swing.JTable();
        namaUserText = new javax.swing.JLabel();
        namaUserField = new javax.swing.JTextField();
        namaAsliLabel = new javax.swing.JLabel();
        namaAsliField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        hakAksesLabel = new javax.swing.JLabel();
        hakAksesField = new javax.swing.JComboBox();
        tambahUserButton = new javax.swing.JButton();
        hapusUserButton = new javax.swing.JButton();
        groupPanel = new javax.swing.JPanel();
        userGroupScrollPane = new javax.swing.JScrollPane();
        userGroupList = new javax.swing.JList();
        systemGroupScrollPane = new javax.swing.JScrollPane();
        systemGroupList = new javax.swing.JList();
        systemGroupLabel = new javax.swing.JLabel();
        userGroupLabel = new javax.swing.JLabel();
        tambahGroupField = new javax.swing.JTextField();
        tambahGroupButton = new javax.swing.JButton();
        hapusGroupButton = new javax.swing.JButton();
        skemaPanel = new javax.swing.JPanel();
        skemaScrollPane = new javax.swing.JScrollPane();
        skemaTable = new javax.swing.JTable();
        kebijakanField = new javax.swing.JComboBox();
        kebijakanLabel = new javax.swing.JLabel();
        groupField = new javax.swing.JComboBox();
        groupLabel = new javax.swing.JLabel();
        blockButton = new javax.swing.JButton();
        hapusSkemaButton = new javax.swing.JButton();

        jenisBayarPopup.setName("jenisBayarPopup"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getActionMap(KonfigurasiView.class, this);
        hapusMenuItem.setAction(actionMap.get("deleteJenisPembayaran")); // NOI18N
        hapusMenuItem.setName("hapusMenuItem"); // NOI18N
        jenisBayarPopup.add(hapusMenuItem);

        userGroupPopup.setName("userGroupPopup"); // NOI18N

        hapusUserGroup.setAction(actionMap.get("deleteUserGroup")); // NOI18N
        hapusUserGroup.setName("hapusUserGroup"); // NOI18N
        userGroupPopup.add(hapusUserGroup);

        skemaPopup.setName("skemaPopup"); // NOI18N

        hapusSkema.setAction(actionMap.get("deleteUserPrivilages")); // NOI18N
        hapusSkema.setName("hapusSkema"); // NOI18N
        skemaPopup.add(hapusSkema);

        userPopup.setName("userPopup"); // NOI18N

        hapusUser.setAction(actionMap.get("deleteUserLogin")); // NOI18N
        hapusUser.setName("hapusUser"); // NOI18N
        userPopup.add(hapusUser);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getResourceMap(KonfigurasiView.class);
        setTitle(resourceMap.getString("restobizKonfigurasi.title")); // NOI18N
        setName("restobizKonfigurasi"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        headerPanel.setBackground(resourceMap.getColor("headerPanel.background")); // NOI18N
        headerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headerPanel.setName("headerPanel"); // NOI18N

        judulLabel.setFont(resourceMap.getFont("judulLabel.font")); // NOI18N
        judulLabel.setText(resourceMap.getString("judulLabel.text")); // NOI18N
        judulLabel.setName("judulLabel"); // NOI18N

        subJudulLabel.setText(resourceMap.getString("subJudulLabel.text")); // NOI18N
        subJudulLabel.setName("subJudulLabel"); // NOI18N

        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
        imageLabel.setText(resourceMap.getString("imageLabel.text")); // NOI18N
        imageLabel.setName("imageLabel"); // NOI18N

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(subJudulLabel))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(judulLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                        .addComponent(imageLabel)))
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageLabel)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(judulLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subJudulLabel)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        footerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        footerPanel.setName("footerPanel"); // NOI18N

        batalButton.setAction(actionMap.get("closeKonfigurasiView")); // NOI18N
        batalButton.setMnemonic('T');
        batalButton.setText(resourceMap.getString("batalButton.text")); // NOI18N
        batalButton.setName("batalButton"); // NOI18N
        batalButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                batalButtonKeyPressed(evt);
            }
        });

        simpanButton.setAction(actionMap.get("saveKonfigurasi")); // NOI18N
        simpanButton.setMnemonic('S');
        simpanButton.setText(resourceMap.getString("simpanButton.text")); // NOI18N
        simpanButton.setName("simpanButton"); // NOI18N
        simpanButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                simpanButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout footerPanelLayout = new javax.swing.GroupLayout(footerPanel);
        footerPanel.setLayout(footerPanelLayout);
        footerPanelLayout.setHorizontalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerPanelLayout.createSequentialGroup()
                .addContainerGap(516, Short.MAX_VALUE)
                .addComponent(simpanButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batalButton)
                .addContainerGap())
        );

        footerPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {batalButton, simpanButton});

        footerPanelLayout.setVerticalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(footerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batalButton)
                    .addComponent(simpanButton))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        contentPanel.setName("contentPanel"); // NOI18N

        contentTabbedPanel.setName("contentTabbedPanel"); // NOI18N
        contentTabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contentTabbedPanelStateChanged(evt);
            }
        });

        generalPanel.setName("generalPanel"); // NOI18N

        kuitansiPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("kuitansiPanel.border.title"))); // NOI18N
        kuitansiPanel.setName("kuitansiPanel"); // NOI18N

        nomorKuitansiField.setEditable(false);
        nomorKuitansiField.setName("nomorKuitansiField"); // NOI18N

        nomorKuitansiLabel.setLabelFor(nomorKuitansiField);
        nomorKuitansiLabel.setText(resourceMap.getString("nomorKuitansiLabel.text")); // NOI18N
        nomorKuitansiLabel.setName("nomorKuitansiLabel"); // NOI18N

        javax.swing.GroupLayout kuitansiPanelLayout = new javax.swing.GroupLayout(kuitansiPanel);
        kuitansiPanel.setLayout(kuitansiPanelLayout);
        kuitansiPanelLayout.setHorizontalGroup(
            kuitansiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kuitansiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kuitansiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nomorKuitansiLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(nomorKuitansiField, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                .addContainerGap())
        );
        kuitansiPanelLayout.setVerticalGroup(
            kuitansiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kuitansiPanelLayout.createSequentialGroup()
                .addComponent(nomorKuitansiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nomorKuitansiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        komponenHargaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("komponenHargaPanel.border.title"))); // NOI18N
        komponenHargaPanel.setName("komponenHargaPanel"); // NOI18N

        pajakLabel.setDisplayedMnemonic('P');
        pajakLabel.setLabelFor(pajakField);
        pajakLabel.setText(resourceMap.getString("pajakLabel.text")); // NOI18N
        pajakLabel.setName("pajakLabel"); // NOI18N

        pajakField.setText(resourceMap.getString("pajakField.text")); // NOI18N
        pajakField.setName("pajakField"); // NOI18N

        discountField.setText(resourceMap.getString("discountField.text")); // NOI18N
        discountField.setName("discountField"); // NOI18N

        discountLabel.setDisplayedMnemonic('D');
        discountLabel.setLabelFor(discountField);
        discountLabel.setText(resourceMap.getString("discountLabel.text")); // NOI18N
        discountLabel.setName("discountLabel"); // NOI18N

        javax.swing.GroupLayout komponenHargaPanelLayout = new javax.swing.GroupLayout(komponenHargaPanel);
        komponenHargaPanel.setLayout(komponenHargaPanelLayout);
        komponenHargaPanelLayout.setHorizontalGroup(
            komponenHargaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(komponenHargaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(komponenHargaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountField, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(discountLabel)
                    .addComponent(pajakLabel)
                    .addComponent(pajakField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap())
        );
        komponenHargaPanelLayout.setVerticalGroup(
            komponenHargaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(komponenHargaPanelLayout.createSequentialGroup()
                .addComponent(discountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pajakLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pajakField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        konfigurasiUmumPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("konfigurasiUmumPanel.border.title"))); // NOI18N
        konfigurasiUmumPanel.setName("konfigurasiUmumPanel"); // NOI18N

        jumlahShiftLabel.setDisplayedMnemonic('h');
        jumlahShiftLabel.setLabelFor(jumlahShiftField);
        jumlahShiftLabel.setText(resourceMap.getString("jumlahShiftLabel.text")); // NOI18N
        jumlahShiftLabel.setName("jumlahShiftLabel"); // NOI18N

        jumlahShiftField.setText(resourceMap.getString("jumlahShiftField.text")); // NOI18N
        jumlahShiftField.setName("jumlahShiftField"); // NOI18N

        lokasiReportLabel.setDisplayedMnemonic('R');
        lokasiReportLabel.setLabelFor(lokasiReportField);
        lokasiReportLabel.setText(resourceMap.getString("lokasiReportLabel.text")); // NOI18N
        lokasiReportLabel.setName("lokasiReportLabel"); // NOI18N

        lokasiReportField.setText(resourceMap.getString("lokasiReportField.text")); // NOI18N
        lokasiReportField.setName("lokasiReportField"); // NOI18N

        browseButton.setAction(actionMap.get("browseReportLocation")); // NOI18N
        browseButton.setMnemonic('B');
        browseButton.setText(resourceMap.getString("browseButton.text")); // NOI18N
        browseButton.setName("browseButton"); // NOI18N
        browseButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                browseButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout konfigurasiUmumPanelLayout = new javax.swing.GroupLayout(konfigurasiUmumPanel);
        konfigurasiUmumPanel.setLayout(konfigurasiUmumPanelLayout);
        konfigurasiUmumPanelLayout.setHorizontalGroup(
            konfigurasiUmumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, konfigurasiUmumPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(konfigurasiUmumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jumlahShiftField, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jumlahShiftLabel)
                    .addComponent(lokasiReportLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, konfigurasiUmumPanelLayout.createSequentialGroup()
                        .addComponent(lokasiReportField, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton)))
                .addContainerGap())
        );
        konfigurasiUmumPanelLayout.setVerticalGroup(
            konfigurasiUmumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(konfigurasiUmumPanelLayout.createSequentialGroup()
                .addComponent(jumlahShiftLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jumlahShiftField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lokasiReportLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(konfigurasiUmumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(lokasiReportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        nomorKasPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("nomorKasPanel.border.title"))); // NOI18N
        nomorKasPanel.setName("nomorKasPanel"); // NOI18N

        nomorKasField.setEditable(false);
        nomorKasField.setName("nomorKasField"); // NOI18N

        nomorKasLabel.setLabelFor(nomorKasField);
        nomorKasLabel.setText(resourceMap.getString("nomorKasLabel.text")); // NOI18N
        nomorKasLabel.setName("nomorKasLabel"); // NOI18N

        javax.swing.GroupLayout nomorKasPanelLayout = new javax.swing.GroupLayout(nomorKasPanel);
        nomorKasPanel.setLayout(nomorKasPanelLayout);
        nomorKasPanelLayout.setHorizontalGroup(
            nomorKasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nomorKasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nomorKasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nomorKasField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(nomorKasLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap())
        );
        nomorKasPanelLayout.setVerticalGroup(
            nomorKasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nomorKasPanelLayout.createSequentialGroup()
                .addComponent(nomorKasLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nomorKasField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        transaksiPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("transaksiPanel.border.title"))); // NOI18N
        transaksiPanel.setName("transaksiPanel"); // NOI18N

        nomorTransaksiLabel.setLabelFor(nomorTransaksiField);
        nomorTransaksiLabel.setText(resourceMap.getString("nomorTransaksiLabel.text")); // NOI18N
        nomorTransaksiLabel.setName("nomorTransaksiLabel"); // NOI18N

        nomorTransaksiField.setEditable(false);
        nomorTransaksiField.setText(resourceMap.getString("nomorTransaksiField.text")); // NOI18N
        nomorTransaksiField.setName("nomorTransaksiField"); // NOI18N

        javax.swing.GroupLayout transaksiPanelLayout = new javax.swing.GroupLayout(transaksiPanel);
        transaksiPanel.setLayout(transaksiPanelLayout);
        transaksiPanelLayout.setHorizontalGroup(
            transaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transaksiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nomorTransaksiLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(nomorTransaksiField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap())
        );
        transaksiPanelLayout.setVerticalGroup(
            transaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaksiPanelLayout.createSequentialGroup()
                .addComponent(nomorTransaksiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nomorTransaksiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        catatanPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        catatanPanel.setName("catatanPanel"); // NOI18N

        catatanLabel.setText(resourceMap.getString("catatanLabel.text")); // NOI18N
        catatanLabel.setName("catatanLabel"); // NOI18N

        javax.swing.GroupLayout catatanPanelLayout = new javax.swing.GroupLayout(catatanPanel);
        catatanPanel.setLayout(catatanPanelLayout);
        catatanPanelLayout.setHorizontalGroup(
            catatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(catatanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(catatanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        catatanPanelLayout.setVerticalGroup(
            catatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, catatanPanelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(catatanLabel)
                .addContainerGap())
        );

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(catatanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalPanelLayout.createSequentialGroup()
                                .addComponent(transaksiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kuitansiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(konfigurasiUmumPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nomorKasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(komponenHargaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(komponenHargaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(konfigurasiUmumPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kuitansiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nomorKasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(transaksiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(catatanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        contentTabbedPanel.addTab(resourceMap.getString("generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        perusahaanPanel.setName("perusahaanPanel"); // NOI18N

        informasiPerusahaanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("informasiPerusahaanPanel.border.title"))); // NOI18N
        informasiPerusahaanPanel.setName("informasiPerusahaanPanel"); // NOI18N

        perusahaanLabel.setDisplayedMnemonic('P');
        perusahaanLabel.setLabelFor(perusahaanField);
        perusahaanLabel.setText(resourceMap.getString("perusahaanLabel.text")); // NOI18N
        perusahaanLabel.setName("perusahaanLabel"); // NOI18N

        perusahaanField.setText(resourceMap.getString("perusahaanField.text")); // NOI18N
        perusahaanField.setName("perusahaanField"); // NOI18N
        perusahaanField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                perusahaanFieldKeyPressed(evt);
            }
        });

        jenisUsahaLabel.setDisplayedMnemonic('U');
        jenisUsahaLabel.setLabelFor(jenisUsahaField);
        jenisUsahaLabel.setText(resourceMap.getString("jenisUsahaLabel.text")); // NOI18N
        jenisUsahaLabel.setName("jenisUsahaLabel"); // NOI18N

        jenisUsahaField.setText(resourceMap.getString("jenisUsahaField.text")); // NOI18N
        jenisUsahaField.setName("jenisUsahaField"); // NOI18N
        jenisUsahaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jenisUsahaFieldKeyPressed(evt);
            }
        });

        alamatLabel.setDisplayedMnemonic('A');
        alamatLabel.setLabelFor(alamatField);
        alamatLabel.setText(resourceMap.getString("alamatLabel.text")); // NOI18N
        alamatLabel.setName("alamatLabel"); // NOI18N

        alamatField.setText(resourceMap.getString("alamatField.text")); // NOI18N
        alamatField.setName("alamatField"); // NOI18N
        alamatField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                alamatFieldKeyPressed(evt);
            }
        });

        teleponLabel.setDisplayedMnemonic('F');
        teleponLabel.setLabelFor(teleponField);
        teleponLabel.setText(resourceMap.getString("teleponLabel.text")); // NOI18N
        teleponLabel.setName("teleponLabel"); // NOI18N

        teleponField.setText(resourceMap.getString("teleponField.text")); // NOI18N
        teleponField.setName("teleponField"); // NOI18N
        teleponField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                teleponFieldKeyPressed(evt);
            }
        });

        pesanPromosiLabel.setDisplayedMnemonic('e');
        pesanPromosiLabel.setLabelFor(pesanPromosiField);
        pesanPromosiLabel.setText(resourceMap.getString("pesanPromosiLabel.text")); // NOI18N
        pesanPromosiLabel.setName("pesanPromosiLabel"); // NOI18N

        pesanPromosiField.setText(resourceMap.getString("pesanPromosiField.text")); // NOI18N
        pesanPromosiField.setName("pesanPromosiField"); // NOI18N

        javax.swing.GroupLayout informasiPerusahaanPanelLayout = new javax.swing.GroupLayout(informasiPerusahaanPanel);
        informasiPerusahaanPanel.setLayout(informasiPerusahaanPanelLayout);
        informasiPerusahaanPanelLayout.setHorizontalGroup(
            informasiPerusahaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informasiPerusahaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(informasiPerusahaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(perusahaanField, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addComponent(perusahaanLabel)
                    .addComponent(jenisUsahaLabel)
                    .addComponent(jenisUsahaField, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addComponent(alamatLabel)
                    .addComponent(alamatField, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addComponent(teleponLabel)
                    .addComponent(teleponField, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addComponent(pesanPromosiLabel)
                    .addComponent(pesanPromosiField, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
                .addContainerGap())
        );
        informasiPerusahaanPanelLayout.setVerticalGroup(
            informasiPerusahaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informasiPerusahaanPanelLayout.createSequentialGroup()
                .addComponent(perusahaanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(perusahaanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jenisUsahaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jenisUsahaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alamatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alamatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teleponLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teleponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pesanPromosiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pesanPromosiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout perusahaanPanelLayout = new javax.swing.GroupLayout(perusahaanPanel);
        perusahaanPanel.setLayout(perusahaanPanelLayout);
        perusahaanPanelLayout.setHorizontalGroup(
            perusahaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perusahaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(informasiPerusahaanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        perusahaanPanelLayout.setVerticalGroup(
            perusahaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perusahaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(informasiPerusahaanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        contentTabbedPanel.addTab(resourceMap.getString("perusahaanPanel.TabConstraints.tabTitle"), perusahaanPanel); // NOI18N

        caraBayarPanel.setName("caraBayarPanel"); // NOI18N

        jenisBayarScrollPane.setName("jenisBayarScrollPane"); // NOI18N

        jenisBayarTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Jenis", "Negasi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jenisBayarTable.setName("jenisBayarTable"); // NOI18N
        jenisBayarTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jenisBayarTableMouseClicked(evt);
            }
        });
        jenisBayarScrollPane.setViewportView(jenisBayarTable);
        jenisBayarTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jenisBayarTable.columnModel.title0")); // NOI18N
        jenisBayarTable.getColumnModel().getColumn(1).setMaxWidth(50);
        jenisBayarTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jenisBayarTable.columnModel.title1")); // NOI18N

        jenisLabel.setDisplayedMnemonic('J');
        jenisLabel.setLabelFor(jenisField);
        jenisLabel.setText(resourceMap.getString("jenisLabel.text")); // NOI18N
        jenisLabel.setName("jenisLabel"); // NOI18N

        jenisField.setText(resourceMap.getString("jenisField.text")); // NOI18N
        jenisField.setName("jenisField"); // NOI18N
        jenisField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jenisFieldKeyPressed(evt);
            }
        });

        negasiCheckBox.setMnemonic('N');
        negasiCheckBox.setText(resourceMap.getString("negasiCheckBox.text")); // NOI18N
        negasiCheckBox.setName("negasiCheckBox"); // NOI18N
        negasiCheckBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                negasiCheckBoxKeyPressed(evt);
            }
        });

        tambahJenisBayarButton.setAction(actionMap.get("addJenisPembayaran")); // NOI18N
        tambahJenisBayarButton.setMnemonic('T');
        tambahJenisBayarButton.setText(resourceMap.getString("tambahJenisBayarButton.text")); // NOI18N
        tambahJenisBayarButton.setName("tambahJenisBayarButton"); // NOI18N
        tambahJenisBayarButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tambahJenisBayarButtonKeyPressed(evt);
            }
        });

        hapusButton.setAction(actionMap.get("deleteJenisPembayaran")); // NOI18N
        hapusButton.setMnemonic('H');
        hapusButton.setText(resourceMap.getString("hapusButton.text")); // NOI18N
        hapusButton.setName("hapusButton"); // NOI18N
        hapusButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout caraBayarPanelLayout = new javax.swing.GroupLayout(caraBayarPanel);
        caraBayarPanel.setLayout(caraBayarPanelLayout);
        caraBayarPanelLayout.setHorizontalGroup(
            caraBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caraBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caraBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jenisBayarScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .addComponent(jenisLabel)
                    .addGroup(caraBayarPanelLayout.createSequentialGroup()
                        .addComponent(jenisField, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(negasiCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tambahJenisBayarButton))
                    .addComponent(hapusButton))
                .addContainerGap())
        );
        caraBayarPanelLayout.setVerticalGroup(
            caraBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caraBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jenisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(caraBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jenisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(negasiCheckBox)
                    .addComponent(tambahJenisBayarButton))
                .addGap(11, 11, 11)
                .addComponent(jenisBayarScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(hapusButton)
                .addContainerGap())
        );

        contentTabbedPanel.addTab(resourceMap.getString("caraBayarPanel.TabConstraints.tabTitle"), caraBayarPanel); // NOI18N

        operatorPanel.setName("operatorPanel"); // NOI18N

        schemeTabbedPanel.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        schemeTabbedPanel.setName("schemeTabbedPanel"); // NOI18N
        schemeTabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                schemeTabbedPanelStateChanged(evt);
            }
        });

        userPanel.setName("userPanel"); // NOI18N

        operatorScrollPane.setName("operatorScrollPane"); // NOI18N

        operatorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama User", "Nama Asli", "Hak Akses"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        operatorTable.setName("operatorTable"); // NOI18N
        operatorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                operatorTableMouseClicked(evt);
            }
        });
        operatorScrollPane.setViewportView(operatorTable);
        operatorTable.getColumnModel().getColumn(0).setMaxWidth(100);
        operatorTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jenisBayarTable.columnModel.title0")); // NOI18N
        operatorTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jenisBayarTable.columnModel.title1")); // NOI18N
        operatorTable.getColumnModel().getColumn(2).setMaxWidth(100);
        operatorTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("operatorTable.columnModel.title2")); // NOI18N

        namaUserText.setDisplayedMnemonic('N');
        namaUserText.setLabelFor(namaUserField);
        namaUserText.setText(resourceMap.getString("namaUserText.text")); // NOI18N
        namaUserText.setName("namaUserText"); // NOI18N

        namaUserField.setText(resourceMap.getString("namaUserField.text")); // NOI18N
        namaUserField.setName("namaUserField"); // NOI18N
        namaUserField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaUserFieldKeyPressed(evt);
            }
        });

        namaAsliLabel.setDisplayedMnemonic('l');
        namaAsliLabel.setLabelFor(namaAsliField);
        namaAsliLabel.setText(resourceMap.getString("namaAsliLabel.text")); // NOI18N
        namaAsliLabel.setName("namaAsliLabel"); // NOI18N

        namaAsliField.setText(resourceMap.getString("namaAsliField.text")); // NOI18N
        namaAsliField.setName("namaAsliField"); // NOI18N
        namaAsliField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaAsliFieldKeyPressed(evt);
            }
        });

        passwordLabel.setDisplayedMnemonic('P');
        passwordLabel.setLabelFor(passwordField);
        passwordLabel.setText(resourceMap.getString("passwordLabel.text")); // NOI18N
        passwordLabel.setName("passwordLabel"); // NOI18N

        passwordField.setText(resourceMap.getString("passwordField.text")); // NOI18N
        passwordField.setName("passwordField"); // NOI18N

        hakAksesLabel.setDisplayedMnemonic('k');
        hakAksesLabel.setLabelFor(hakAksesField);
        hakAksesLabel.setText(resourceMap.getString("hakAksesLabel.text")); // NOI18N
        hakAksesLabel.setName("hakAksesLabel"); // NOI18N

        hakAksesField.setBackground(resourceMap.getColor("hakAksesField.background")); // NOI18N
        hakAksesField.setName("hakAksesField"); // NOI18N
        hakAksesField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hakAksesFieldKeyPressed(evt);
            }
        });

        tambahUserButton.setAction(actionMap.get("addOperator")); // NOI18N
        tambahUserButton.setMnemonic('T');
        tambahUserButton.setText(resourceMap.getString("tambahUserButton.text")); // NOI18N
        tambahUserButton.setName("tambahUserButton"); // NOI18N
        tambahUserButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tambahUserButtonKeyPressed(evt);
            }
        });

        hapusUserButton.setAction(actionMap.get("deleteUserLogin")); // NOI18N
        hapusUserButton.setMnemonic('H');
        hapusUserButton.setText(resourceMap.getString("hapusUserButton.text")); // NOI18N
        hapusUserButton.setName("hapusUserButton"); // NOI18N
        hapusUserButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusUserButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(operatorScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(userPanelLayout.createSequentialGroup()
                                .addComponent(passwordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hakAksesLabel))
                            .addGroup(userPanelLayout.createSequentialGroup()
                                .addComponent(namaUserText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaUserField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaAsliLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(userPanelLayout.createSequentialGroup()
                                .addComponent(hakAksesField, 0, 180, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tambahUserButton))
                            .addComponent(namaAsliField, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                    .addComponent(hapusUserButton, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        userPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {namaUserText, passwordLabel});

        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaUserText)
                    .addComponent(namaUserField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaAsliLabel)
                    .addComponent(namaAsliField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(hakAksesLabel)
                    .addComponent(tambahUserButton)
                    .addComponent(hakAksesField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(operatorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusUserButton)
                .addContainerGap())
        );

        schemeTabbedPanel.addTab(resourceMap.getString("userPanel.TabConstraints.tabTitle"), userPanel); // NOI18N

        groupPanel.setName("groupPanel"); // NOI18N

        userGroupScrollPane.setName("userGroupScrollPane"); // NOI18N

        userGroupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        userGroupList.setName("userGroupList"); // NOI18N
        userGroupList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userGroupListMouseClicked(evt);
            }
        });
        userGroupList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                userGroupListValueChanged(evt);
            }
        });
        userGroupScrollPane.setViewportView(userGroupList);

        systemGroupScrollPane.setName("systemGroupScrollPane"); // NOI18N

        systemGroupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        systemGroupList.setName("systemGroupList"); // NOI18N
        systemGroupScrollPane.setViewportView(systemGroupList);

        systemGroupLabel.setDisplayedMnemonic('G');
        systemGroupLabel.setLabelFor(systemGroupList);
        systemGroupLabel.setText(resourceMap.getString("systemGroupLabel.text")); // NOI18N
        systemGroupLabel.setName("systemGroupLabel"); // NOI18N

        userGroupLabel.setDisplayedMnemonic('U');
        userGroupLabel.setLabelFor(userGroupList);
        userGroupLabel.setText(resourceMap.getString("userGroupLabel.text")); // NOI18N
        userGroupLabel.setName("userGroupLabel"); // NOI18N

        tambahGroupField.setText(resourceMap.getString("tambahGroupField.text")); // NOI18N
        tambahGroupField.setName("tambahGroupField"); // NOI18N

        tambahGroupButton.setAction(actionMap.get("addUserGroup")); // NOI18N
        tambahGroupButton.setMnemonic('T');
        tambahGroupButton.setText(resourceMap.getString("tambahGroupButton.text")); // NOI18N
        tambahGroupButton.setName("tambahGroupButton"); // NOI18N
        tambahGroupButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tambahGroupButtonKeyPressed(evt);
            }
        });

        hapusGroupButton.setAction(actionMap.get("deleteUserGroup")); // NOI18N
        hapusGroupButton.setMnemonic('H');
        hapusGroupButton.setText(resourceMap.getString("hapusGroupButton.text")); // NOI18N
        hapusGroupButton.setName("hapusGroupButton"); // NOI18N
        hapusGroupButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusGroupButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout groupPanelLayout = new javax.swing.GroupLayout(groupPanel);
        groupPanel.setLayout(groupPanelLayout);
        groupPanelLayout.setHorizontalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupPanelLayout.createSequentialGroup()
                        .addComponent(tambahGroupField, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tambahGroupButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hapusGroupButton))
                    .addComponent(userGroupScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(userGroupLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(systemGroupLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(systemGroupScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                .addContainerGap())
        );

        groupPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {hapusGroupButton, tambahGroupButton});

        groupPanelLayout.setVerticalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userGroupLabel)
                    .addComponent(systemGroupLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(groupPanelLayout.createSequentialGroup()
                        .addComponent(userGroupScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hapusGroupButton)
                            .addComponent(tambahGroupButton)
                            .addComponent(tambahGroupField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(systemGroupScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addContainerGap())
        );

        schemeTabbedPanel.addTab(resourceMap.getString("groupPanel.TabConstraints.tabTitle"), groupPanel); // NOI18N

        skemaPanel.setName("skemaPanel"); // NOI18N

        skemaScrollPane.setName("skemaScrollPane"); // NOI18N

        skemaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kebijakan", "Group", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        skemaTable.setName("skemaTable"); // NOI18N
        skemaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                skemaTableMouseClicked(evt);
            }
        });
        skemaScrollPane.setViewportView(skemaTable);
        skemaTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("skemaTable.columnModel.title0")); // NOI18N
        skemaTable.getColumnModel().getColumn(1).setMaxWidth(100);
        skemaTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("skemaTable.columnModel.title1")); // NOI18N
        skemaTable.getColumnModel().getColumn(2).setMaxWidth(100);
        skemaTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("skemaTable.columnModel.title2")); // NOI18N

        kebijakanField.setBackground(resourceMap.getColor("kebijakanField.background")); // NOI18N
        kebijakanField.setMinimumSize(new java.awt.Dimension(22, 24));
        kebijakanField.setName("kebijakanField"); // NOI18N
        kebijakanField.setPreferredSize(new java.awt.Dimension(22, 24));

        kebijakanLabel.setDisplayedMnemonic('K');
        kebijakanLabel.setLabelFor(kebijakanField);
        kebijakanLabel.setText(resourceMap.getString("kebijakanLabel.text")); // NOI18N
        kebijakanLabel.setName("kebijakanLabel"); // NOI18N

        groupField.setBackground(resourceMap.getColor("kebijakanField.background")); // NOI18N
        groupField.setMinimumSize(new java.awt.Dimension(22, 24));
        groupField.setName("groupField"); // NOI18N
        groupField.setPreferredSize(new java.awt.Dimension(22, 24));

        groupLabel.setDisplayedMnemonic('G');
        groupLabel.setLabelFor(groupField);
        groupLabel.setText(resourceMap.getString("groupLabel.text")); // NOI18N
        groupLabel.setName("groupLabel"); // NOI18N

        blockButton.setAction(actionMap.get("blockUserPrivilages")); // NOI18N
        blockButton.setMnemonic('B');
        blockButton.setText(resourceMap.getString("blockButton.text")); // NOI18N
        blockButton.setName("blockButton"); // NOI18N
        blockButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                blockButtonKeyPressed(evt);
            }
        });

        hapusSkemaButton.setAction(actionMap.get("deleteUserPrivilages")); // NOI18N
        hapusSkemaButton.setMnemonic('H');
        hapusSkemaButton.setText(resourceMap.getString("hapusSkemaButton.text")); // NOI18N
        hapusSkemaButton.setName("hapusSkemaButton"); // NOI18N
        hapusSkemaButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusSkemaButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout skemaPanelLayout = new javax.swing.GroupLayout(skemaPanel);
        skemaPanel.setLayout(skemaPanelLayout);
        skemaPanelLayout.setHorizontalGroup(
            skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, skemaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(skemaScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                    .addGroup(skemaPanelLayout.createSequentialGroup()
                        .addGroup(skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kebijakanLabel)
                            .addComponent(kebijakanField, 0, 310, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(skemaPanelLayout.createSequentialGroup()
                                .addComponent(groupField, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blockButton))
                            .addComponent(groupLabel)))
                    .addComponent(hapusSkemaButton, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        skemaPanelLayout.setVerticalGroup(
            skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(skemaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kebijakanLabel)
                    .addComponent(groupLabel))
                .addGap(3, 3, 3)
                .addGroup(skemaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blockButton)
                    .addComponent(groupField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kebijakanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(skemaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusSkemaButton)
                .addContainerGap())
        );

        schemeTabbedPanel.addTab(resourceMap.getString("skemaPanel.TabConstraints.tabTitle"), skemaPanel); // NOI18N

        javax.swing.GroupLayout operatorPanelLayout = new javax.swing.GroupLayout(operatorPanel);
        operatorPanel.setLayout(operatorPanelLayout);
        operatorPanelLayout.setHorizontalGroup(
            operatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operatorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(schemeTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );
        operatorPanelLayout.setVerticalGroup(
            operatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operatorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(schemeTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
        );

        contentTabbedPanel.addTab(resourceMap.getString("operatorPanel.TabConstraints.tabTitle"), operatorPanel); // NOI18N

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(footerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(footerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void contentTabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contentTabbedPanelStateChanged
    fillNilaiKonfigurasi();
}//GEN-LAST:event_contentTabbedPanelStateChanged

private void perusahaanFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_perusahaanFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_perusahaanFieldKeyPressed

private void jenisUsahaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jenisUsahaFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_jenisUsahaFieldKeyPressed

private void alamatFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alamatFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_alamatFieldKeyPressed

private void teleponFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teleponFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_teleponFieldKeyPressed

private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    if (firstTime) {
        contentTabbedPanel.setSelectedIndex(0);
        firstTime = false;
    }
}//GEN-LAST:event_formWindowActivated

private void jenisFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jenisFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_jenisFieldKeyPressed

private void negasiCheckBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_negasiCheckBoxKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_negasiCheckBoxKeyPressed

    @SuppressWarnings("empty-statement")
private void jenisBayarTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jenisBayarTableMouseClicked
        if (jenisBayarTable.getSelectedRowCount() == 1) {
            if ((evt.getButton() == MouseEvent.BUTTON3) && (evt.getClickCount() == 1)) {
                jenisBayarPopup.show(jenisBayarTable, evt.getX(), evt.getY());
            }
            ;
        }
        ;
}//GEN-LAST:event_jenisBayarTableMouseClicked

    @SuppressWarnings("empty-statement")
private void operatorTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_operatorTableMouseClicked
        if (operatorTable.getSelectedRowCount() == 1) {
            if ((evt.getButton() == MouseEvent.BUTTON3) && (evt.getClickCount() == 1)) {
                userPopup.show(operatorTable, evt.getX(), evt.getY());
            }
            ;
        }
        ;
}//GEN-LAST:event_operatorTableMouseClicked

private void namaUserFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaUserFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_namaUserFieldKeyPressed

private void namaAsliFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaAsliFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_namaAsliFieldKeyPressed

private void hakAksesFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hakAksesFieldKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_hakAksesFieldKeyPressed

private void schemeTabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_schemeTabbedPanelStateChanged
    String actionTab;//GEN-LAST:event_schemeTabbedPanelStateChanged

        actionTab = schemeTabbedPanel.getTitleAt(schemeTabbedPanel.getSelectedIndex());

        if (actionTab.equals("User")) {
            fillNilaiOperator();
        }

        if (actionTab.equals("Group")) {
            fillNilaiGroup();
        }

        if (actionTab.equals("Skema")) {
            fillNilaiKebijakan();
            fillGroupList();
            fillSkemaTable();
            resetInputKebijakan();
        }
    }

    @SuppressWarnings("empty-statement")
private void userGroupListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userGroupListMouseClicked
        if (!userGroupList.getSelectedValue().toString().isEmpty()) {
            if ((evt.getButton() == MouseEvent.BUTTON3) && (evt.getClickCount() == 1)) {
                userGroupPopup.show(userGroupList, evt.getX(), evt.getY());
            }
            ;
        }
        ;
}//GEN-LAST:event_userGroupListMouseClicked

private void userGroupListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_userGroupListValueChanged
    hapusGroupButton.setEnabled(!userGroupList.getSelectedValue().toString().isEmpty());
}//GEN-LAST:event_userGroupListValueChanged

    @SuppressWarnings("empty-statement")
private void skemaTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_skemaTableMouseClicked
        if (skemaTable.getSelectedRowCount() == 1) {
            if ((evt.getButton() == MouseEvent.BUTTON3) && (evt.getClickCount() == 1)) {
                skemaPopup.show(skemaTable, evt.getX(), evt.getY());
            }
            ;
        }
        ;
}//GEN-LAST:event_skemaTableMouseClicked

private void browseButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_browseButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        browseReportLocation();
    }
}//GEN-LAST:event_browseButtonKeyPressed

private void simpanButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_simpanButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        saveKonfigurasi();
    }
}//GEN-LAST:event_simpanButtonKeyPressed

private void batalButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_batalButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        closeKonfigurasiView();
    }
}//GEN-LAST:event_batalButtonKeyPressed

private void tambahJenisBayarButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tambahJenisBayarButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        addJenisPembayaran();
    }
}//GEN-LAST:event_tambahJenisBayarButtonKeyPressed

private void hapusButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        deleteJenisPembayaran();
    }
}//GEN-LAST:event_hapusButtonKeyPressed

private void tambahUserButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tambahUserButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        addOperator();
    }
}//GEN-LAST:event_tambahUserButtonKeyPressed

private void hapusUserButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusUserButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        deleteUserLogin();
    }
}//GEN-LAST:event_hapusUserButtonKeyPressed

private void tambahGroupButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tambahGroupButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        addUserGroup();
    }
}//GEN-LAST:event_tambahGroupButtonKeyPressed

private void hapusGroupButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusGroupButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        deleteUserGroup();
    }
}//GEN-LAST:event_hapusGroupButtonKeyPressed

private void blockButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_blockButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        blockUserPrivilages();
    }
}//GEN-LAST:event_blockButtonKeyPressed

private void hapusSkemaButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusSkemaButtonKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        deleteUserPrivilages();
    }
}//GEN-LAST:event_hapusSkemaButtonKeyPressed

    @Action
    public void browseReportLocation() {
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setDialogTitle("Pilih lokasi directory report ...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lokasiReportField.setText(String.valueOf(chooser.getSelectedFile() + File.separator));
        }
    }

    @Action
    @SuppressWarnings("empty-statement")
    public void saveKonfigurasi() {
        if (JOptionPane.showConfirmDialog(this, "Simpan konfigurasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (saveNilaiKofigurasi()) {
                JOptionPane.showMessageDialog(this, "Konfigurasi telah disimpan");
            }
            ;
        }
    }

    @Action
    public void closeKonfigurasiView() {
        this.dispose();
    }

    @Action
    @SuppressWarnings("empty-statement")
    public void addJenisPembayaran() {
        String Jenis;
        Boolean Negasi;

        Jenis = jenisField.getText().toUpperCase();
        Negasi = negasiCheckBox.isSelected();

        if (Jenis.length() > 0) {
            if (cfg.addJenisBayar(Jenis, Negasi)) {
                JOptionPane.showMessageDialog(this, "Jenis bayar " + Jenis + " telah ditambahkan");
            }
            ;
        } else {
            JOptionPane.showMessageDialog(this, "Harap isi informasi jenis bayar");
        }
        resetInputJenisBayar();
        fillNilaiPembayaran();
    }

    private void resetInputJenisBayar() {
        jenisField.setText("");
        negasiCheckBox.setSelected(false);
        jenisField.requestFocus();
    }

    @Action
    @SuppressWarnings("empty-statement")
    public void deleteJenisPembayaran() {
        if (jenisBayarTable.getSelectedRowCount() == 1) {
            Object Jenis;

            Jenis = jenisBayarTable.getValueAt(jenisBayarTable.getSelectedRow(), 0);

            if (!Jenis.equals(null) && !Jenis.toString().isEmpty()) {
                if (JOptionPane.showConfirmDialog(this, "Hapus jenis bayar " + String.valueOf(Jenis) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (cfg.deleteJenisBayar(String.valueOf(Jenis))) {
                        JOptionPane.showMessageDialog(this, "Jenis bayar " + String.valueOf(Jenis) + " telah dihapus");
                    } else {
                        JOptionPane.showMessageDialog(this, "Jenis bayar " + String.valueOf(Jenis) + " tidak dapat dihapus");
                    }
                    ;
                }
            }

            resetInputJenisBayar();
            fillNilaiPembayaran();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih jenis bayar yang akan dihapus");
        }
    }

    @Action
    @SuppressWarnings("empty-statement")
    public void addOperator() {
        String NamaUser;
        String Password;
        String NamaAsli;
        String HakAkses;

        NamaUser = namaUserField.getText();
        Password = String.valueOf(passwordField.getPassword());
        NamaAsli = namaAsliField.getText();
        HakAkses = String.valueOf(hakAksesField.getSelectedItem());

        if (isValidUserInput()) {
            if (cfg.addOperator(NamaUser, Password, NamaAsli, HakAkses)) {
                JOptionPane.showMessageDialog(this, "Penambahan operator berhasil");
                fillNilaiOperator();
            } else {
                JOptionPane.showMessageDialog(this, "Penambahan operator gagal, silahkan coba kembali!");
            }
            ;
        }
    }

    @Action
    public void addUserGroup() {
        if (!tambahGroupField.getText().isEmpty()) {
            String group = tambahGroupField.getText().toUpperCase();
            cfg.addUserGroup(group);
            fillUserGroupList();
            resetInputUserGroup();
        } else {
            JOptionPane.showMessageDialog(this, "Harap isi data yang diperlukan");
        }
    }

    @Action
    public void deleteUserGroup() {
        if ((userGroupList.getSelectedIndex() != -1) && !userGroupList.getSelectedValue().toString().isEmpty()) {
            String group = userGroupList.getSelectedValue().toString();

            if (!group.isEmpty()) {
                if (JOptionPane.showConfirmDialog(this, "Hapus group " + group + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (!cfg.deleteUserGroup(group)) {
                        JOptionPane.showMessageDialog(this, "Group tidak dapat dihapus");
                    }
                }
                fillUserGroupList();
                resetInputUserGroup();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih satu group yang akan dihapus");
        }
    }

    @Action
    public void blockUserPrivilages() {
        String kebijakan = "";
        String group = "";
        if ((kebijakanField.getSelectedIndex() != -1) && (!kebijakanField.getSelectedItem().toString().isEmpty())) {
            kebijakan = kebijakanField.getSelectedItem().toString();
            kebijakan = kebijakan.substring(0, 4);
        }
        if ((groupField.getSelectedIndex() != -1) && (!groupField.getSelectedItem().toString().isEmpty())) {
            group = groupField.getSelectedItem().toString();
        }
        if (!kebijakan.isEmpty() && !group.isEmpty() && !kebijakan.equals("----")) {
            cfg.addRestrictedPolicy(Integer.valueOf(kebijakan), group);
            fillSkemaTable();
            resetInputKebijakan();
        } else {
            JOptionPane.showMessageDialog(this, "Harap isi seluruh data yang diperlukan");
        }
    }

    @Action
    public void deleteUserPrivilages() {
        if (skemaTable.getSelectedRowCount() == 1) {
            Vector result = (Vector) cfg.getSkemaList();
            Object[] row = (Object[]) result.get(skemaTable.getSelectedRow());

            String module = String.valueOf(row[0]);
            String group = String.valueOf(skemaTable.getValueAt(skemaTable.getSelectedRow(), 1));

            cfg.deleteRestrictedPolicy(Integer.valueOf(module), group);
            fillSkemaTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih satu skema yang akan dihapus");
        }
    }

    @Action
    public void deleteUserLogin() {
        if (operatorTable.getSelectedRowCount() == 1) {
            String user = String.valueOf(operatorTable.getValueAt(operatorTable.getSelectedRow(), 0));
            if (!user.equals("admin")) {
                if (JOptionPane.showConfirmDialog(this, "Hapus user " + user + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (cfg.deleteOperator(user)) {
                        fillNilaiOperator();
                        JOptionPane.showMessageDialog(this, "User " + user + " telah dihapus");
                    } else {
                        JOptionPane.showMessageDialog(this, "User " + user + " tidak bisa dihapus");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "User admin tidak boleh dihapus");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih satu user yang akan dihapus");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alamatField;
    private javax.swing.JLabel alamatLabel;
    private javax.swing.JButton batalButton;
    private javax.swing.JButton blockButton;
    private javax.swing.JButton browseButton;
    private javax.swing.JPanel caraBayarPanel;
    private javax.swing.JLabel catatanLabel;
    private javax.swing.JPanel catatanPanel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JTabbedPane contentTabbedPanel;
    private javax.swing.JTextField discountField;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JComboBox groupField;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JPanel groupPanel;
    private javax.swing.JComboBox hakAksesField;
    private javax.swing.JLabel hakAksesLabel;
    private javax.swing.JButton hapusButton;
    private javax.swing.JButton hapusGroupButton;
    private javax.swing.JMenuItem hapusMenuItem;
    private javax.swing.JMenuItem hapusSkema;
    private javax.swing.JButton hapusSkemaButton;
    private javax.swing.JMenuItem hapusUser;
    private javax.swing.JButton hapusUserButton;
    private javax.swing.JMenuItem hapusUserGroup;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JPanel informasiPerusahaanPanel;
    private javax.swing.JPopupMenu jenisBayarPopup;
    private javax.swing.JScrollPane jenisBayarScrollPane;
    private javax.swing.JTable jenisBayarTable;
    private javax.swing.JTextField jenisField;
    private javax.swing.JLabel jenisLabel;
    private javax.swing.JTextField jenisUsahaField;
    private javax.swing.JLabel jenisUsahaLabel;
    private javax.swing.JLabel judulLabel;
    private javax.swing.JTextField jumlahShiftField;
    private javax.swing.JLabel jumlahShiftLabel;
    private javax.swing.JComboBox kebijakanField;
    private javax.swing.JLabel kebijakanLabel;
    private javax.swing.JPanel komponenHargaPanel;
    private javax.swing.JPanel konfigurasiUmumPanel;
    private javax.swing.JPanel kuitansiPanel;
    private javax.swing.JTextField lokasiReportField;
    private javax.swing.JLabel lokasiReportLabel;
    private javax.swing.JTextField namaAsliField;
    private javax.swing.JLabel namaAsliLabel;
    private javax.swing.JTextField namaUserField;
    private javax.swing.JLabel namaUserText;
    private javax.swing.JCheckBox negasiCheckBox;
    private javax.swing.JTextField nomorKasField;
    private javax.swing.JLabel nomorKasLabel;
    private javax.swing.JPanel nomorKasPanel;
    private javax.swing.JTextField nomorKuitansiField;
    private javax.swing.JLabel nomorKuitansiLabel;
    private javax.swing.JTextField nomorTransaksiField;
    private javax.swing.JLabel nomorTransaksiLabel;
    private javax.swing.JPanel operatorPanel;
    private javax.swing.JScrollPane operatorScrollPane;
    private javax.swing.JTable operatorTable;
    private javax.swing.JTextField pajakField;
    private javax.swing.JLabel pajakLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField perusahaanField;
    private javax.swing.JLabel perusahaanLabel;
    private javax.swing.JPanel perusahaanPanel;
    private javax.swing.JTextField pesanPromosiField;
    private javax.swing.JLabel pesanPromosiLabel;
    private javax.swing.JTabbedPane schemeTabbedPanel;
    private javax.swing.JButton simpanButton;
    private javax.swing.JPanel skemaPanel;
    private javax.swing.JPopupMenu skemaPopup;
    private javax.swing.JScrollPane skemaScrollPane;
    private javax.swing.JTable skemaTable;
    private javax.swing.JLabel subJudulLabel;
    private javax.swing.JLabel systemGroupLabel;
    private javax.swing.JList systemGroupList;
    private javax.swing.JScrollPane systemGroupScrollPane;
    private javax.swing.JButton tambahGroupButton;
    private javax.swing.JTextField tambahGroupField;
    private javax.swing.JButton tambahJenisBayarButton;
    private javax.swing.JButton tambahUserButton;
    private javax.swing.JTextField teleponField;
    private javax.swing.JLabel teleponLabel;
    private javax.swing.JPanel transaksiPanel;
    private javax.swing.JLabel userGroupLabel;
    private javax.swing.JList userGroupList;
    private javax.swing.JPopupMenu userGroupPopup;
    private javax.swing.JScrollPane userGroupScrollPane;
    private javax.swing.JPanel userPanel;
    private javax.swing.JPopupMenu userPopup;
    // End of variables declaration//GEN-END:variables
}
