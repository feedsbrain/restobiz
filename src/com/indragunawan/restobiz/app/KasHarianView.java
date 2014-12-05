/*
 * KasHarianView.java
 *
 * Created on September 7, 2008, 12:28 PM
 */
package com.indragunawan.restobiz.app;

import com.indragunawan.restobiz.app.model.KasHarianRD;
import com.indragunawan.restobiz.app.model.KasHarianTM;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author  igoens
 */
public class KasHarianView extends javax.swing.JDialog {

    private static final long serialVersionUID = -6998176928735110283L;

    private EntityManager kasHarianEntity;
    private GeneralConfig cfg = new GeneralConfig();
    private SimpleDateFormat dateDisplay = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat floatDisplay = new DecimalFormat("#,##0.00");
    private Query kasHarianQuery;
    private List kelompokKasList;
    private int i;

    /** Creates new form KasHarianView */
    public KasHarianView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        kasHarianEntity = Persistence.createEntityManagerFactory("RestobizPU", cfg.getPersistanceDbProperties()).createEntityManager();

        initVariables();
        fillKelompokKasField();
        try {
            refreshTableKas(dateDisplay.parse(tanggalField.getText()));
        } catch (ParseException ex) {
            ex.printStackTrace();
            Logger.getLogger(KasHarianView.class.getName()).log(Level.SEVERE, null, ex);
        }
        resetInputKas();
    }

    private Boolean execHapusKasHarian(Integer Transaksi, Date Tanggal) {
        Boolean result = false;
        String Uraian;
        Double Nilai;

        Uraian = String.valueOf(kasHarianTable.getValueAt(kasHarianTable.getSelectedRow(), 2));
        Nilai = Double.valueOf(String.valueOf(kasHarianTable.getValueAt(kasHarianTable.getSelectedRow(), 3)));

        if (JOptionPane.showConfirmDialog(this, "Hapus " + Uraian + " sejumlah " + floatDisplay.format(Nilai) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                kasHarianEntity.getTransaction().begin();
                kasHarianQuery = kasHarianEntity.createNativeQuery("DELETE FROM kas WHERE transaksi = #transaksi AND tanggal = #tanggal").setParameter("transaksi", Transaksi).setParameter("tanggal", dateSQL.format(Tanggal));
                kasHarianQuery.executeUpdate();
                kasHarianEntity.getTransaction().commit();
                result = true;
            } catch (Exception ex) {
                kasHarianEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private void initVariables() {
        Date today = new Date();
        tanggalField.setText(dateDisplay.format(today));
        shiftField.setText(String.valueOf(cfg.getCurrentShift()));
    }

    private void fillKelompokKasField() {
        try {
            kelompokKasList = cfg.getKelompokKas();

            kelompokField.removeAllItems();
            for (i = 0; i <= kelompokKasList.size() - 1; i++) {
                Vector result = (Vector) kelompokKasList.get(i);
                kelompokField.addItem(result.get(0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshTableKas(Date Tanggal) {
        kasHarianTable.setModel(new KasHarianTM(openKasHarian(Tanggal)));
        for (i = 0; i < kasHarianTable.getColumnCount(); i++) {
            kasHarianTable.getColumnModel().getColumn(i).setCellRenderer(new KasHarianRD());
        }
    }

    private void resetInputKas() {
        kelompokField.setSelectedIndex(-1);
        uraianField.setText("");
        debetField.setText("");
        kreditField.setText("");
        kelompokField.requestFocus();
    }

    private List openKasHarian(Date Tanggal) {
        List kasHarianList = null;
        try {
            kasHarianList = kasHarianEntity.createQuery("SELECT k.kasPK.transaksi, k.kelompok, k.keterangan, k.jumlah FROM Kas k WHERE k.kasPK.tanggal = :tanggal").setParameter("tanggal", Tanggal).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kasHarianList;
    }

    @Action
    public void closeKasHarian() {
        this.dispose();
    }

    @Action
    public void addKasHarian() {
        String Kelompok = "";
        String Uraian = "";
        Double Debet = 0.0;
        Double Kredit = 0.0;

        if (kelompokField.getSelectedIndex() != -1) {
            Kelompok = kelompokField.getSelectedItem().toString();
        }

        if (Kelompok.isEmpty()) {
            Kelompok = kelompokField.getEditor().getItem().toString();
        }

        Uraian = uraianField.getText();

        try {
            if (!debetField.getText().isEmpty()) {
                Debet = Double.valueOf(debetField.getText());
            }
            if (!kreditField.getText().isEmpty()) {
                Kredit = Double.valueOf(kreditField.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!Kelompok.isEmpty() && !uraianField.getText().isEmpty() && !(debetField.equals(0.0) && kreditField.equals(0.0))) {
            execTambahPembayaran(Kelompok, Uraian, (Debet - Kredit));
            fillKelompokKasField();
        } else {
            JOptionPane.showMessageDialog(this, "Harap isi seluruh field yang dibutuhkan");
        }
    }

    private void execTambahPembayaran(String Kelompok, String Keterangan, Double Jumlah) {
        Date Tanggal = null;
        Integer Transaksi = cfg.getNomorKas();

        try {
            Tanggal = dateDisplay.parse(tanggalField.getText());
            kasHarianEntity.getTransaction().begin();
            kasHarianQuery = kasHarianEntity.createNativeQuery("INSERT INTO kas VALUES (#transaksi, #tanggal, #kelompok, #keterangan, #jumlah, #namauser, #shift)").setParameter("transaksi", Transaksi).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("kelompok", Kelompok).setParameter("keterangan", Keterangan).setParameter("jumlah", Jumlah).setParameter("namauser", cfg.getLoggedUser()).setParameter("shift", cfg.getCurrentShift());
            kasHarianQuery.executeUpdate();
            cfg.incNomorKas();
            kasHarianEntity.getTransaction().commit();
            resetInputKas();
            refreshTableKas(Tanggal);
        } catch (ParseException ex) {
            kasHarianEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(PembayaranView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action
    public void deleteKasHarian() {
        if (kasHarianTable.getSelectedColumnCount() == 1) {
            Integer Transaksi = Integer.valueOf(String.valueOf(kasHarianTable.getValueAt(kasHarianTable.getSelectedRow(), 0)));
            Date Tanggal = null;
            try {
                Tanggal = dateDisplay.parse(tanggalField.getText());
            } catch (ParseException ex) {
                ex.printStackTrace();
                Logger.getLogger(KasHarianView.class.getName()).log(Level.SEVERE, null, ex);
            }

            execHapusKasHarian(Transaksi, Tanggal);
            refreshTableKas(Tanggal);
            resetInputKas();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih transaksi kas yang akan dihapus");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kasHarianPopup = new javax.swing.JPopupMenu();
        hapusMenuItem = new javax.swing.JMenuItem();
        headerPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        subHeaderLabel = new javax.swing.JLabel();
        tanggalLabel = new javax.swing.JLabel();
        shiftLabel = new javax.swing.JLabel();
        tanggalField = new javax.swing.JLabel();
        shiftField = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        kelompokField = new javax.swing.JComboBox();
        kelompokLabel = new javax.swing.JLabel();
        uraianLabel = new javax.swing.JLabel();
        uraianField = new javax.swing.JTextField();
        debetLabel = new javax.swing.JLabel();
        debetField = new javax.swing.JTextField();
        kreditLabel = new javax.swing.JLabel();
        kreditField = new javax.swing.JTextField();
        buttonLabel = new javax.swing.JButton();
        kasHarianScrollPane = new javax.swing.JScrollPane();
        kasHarianTable = new javax.swing.JTable();
        footerPanel = new javax.swing.JPanel();
        tutupButton = new javax.swing.JButton();
        hapusButton = new javax.swing.JButton();

        kasHarianPopup.setName("kasHarianPopup"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getActionMap(KasHarianView.class, this);
        hapusMenuItem.setAction(actionMap.get("hapusKasHarian")); // NOI18N
        hapusMenuItem.setName("hapusMenuItem"); // NOI18N
        kasHarianPopup.add(hapusMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("kasHarian"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.indragunawan.restobiz.app.MainApp.class).getContext().getResourceMap(KasHarianView.class);
        headerPanel.setBackground(resourceMap.getColor("headerPanel.background")); // NOI18N
        headerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headerPanel.setName("headerPanel"); // NOI18N

        headerLabel.setFont(resourceMap.getFont("headerLabel.font")); // NOI18N
        headerLabel.setText(resourceMap.getString("headerLabel.text")); // NOI18N
        headerLabel.setName("headerLabel"); // NOI18N

        subHeaderLabel.setText(resourceMap.getString("subHeaderLabel.text")); // NOI18N
        subHeaderLabel.setName("subHeaderLabel"); // NOI18N

        tanggalLabel.setFont(resourceMap.getFont("tanggalLabel.font")); // NOI18N
        tanggalLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tanggalLabel.setText(resourceMap.getString("tanggalLabel.text")); // NOI18N
        tanggalLabel.setName("tanggalLabel"); // NOI18N

        shiftLabel.setFont(resourceMap.getFont("tanggalLabel.font")); // NOI18N
        shiftLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        shiftLabel.setText(resourceMap.getString("shiftLabel.text")); // NOI18N
        shiftLabel.setName("shiftLabel"); // NOI18N

        tanggalField.setText(resourceMap.getString("tanggalField.text")); // NOI18N
        tanggalField.setName("tanggalField"); // NOI18N

        shiftField.setText(resourceMap.getString("shiftField.text")); // NOI18N
        shiftField.setName("shiftField"); // NOI18N

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerLabel)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(subHeaderLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                        .addComponent(tanggalLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tanggalField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                        .addComponent(shiftLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shiftField)))
                .addContainerGap())
        );

        headerPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {shiftField, tanggalField});

        headerPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {shiftLabel, tanggalLabel});

        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerLabel)
                    .addComponent(tanggalField)
                    .addComponent(tanggalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subHeaderLabel)
                    .addComponent(shiftField)
                    .addComponent(shiftLabel))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        contentPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        contentPanel.setName("contentPanel"); // NOI18N

        controlPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controlPanel.setName("controlPanel"); // NOI18N

        kelompokField.setEditable(true);
        kelompokField.setName("kelompokField"); // NOI18N
        kelompokField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kelompokFieldKeyPressed(evt);
            }
        });

        kelompokLabel.setDisplayedMnemonic('e');
        kelompokLabel.setLabelFor(kelompokField);
        kelompokLabel.setText(resourceMap.getString("kelompokLabel.text")); // NOI18N
        kelompokLabel.setName("kelompokLabel"); // NOI18N

        uraianLabel.setDisplayedMnemonic('U');
        uraianLabel.setLabelFor(uraianField);
        uraianLabel.setText(resourceMap.getString("uraianLabel.text")); // NOI18N
        uraianLabel.setName("uraianLabel"); // NOI18N

        uraianField.setText(resourceMap.getString("uraianField.text")); // NOI18N
        uraianField.setName("uraianField"); // NOI18N
        uraianField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                uraianFieldKeyPressed(evt);
            }
        });

        debetLabel.setDisplayedMnemonic('D');
        debetLabel.setLabelFor(debetField);
        debetLabel.setText(resourceMap.getString("debetLabel.text")); // NOI18N
        debetLabel.setName("debetLabel"); // NOI18N

        debetField.setText(resourceMap.getString("debetField.text")); // NOI18N
        debetField.setName("debetField"); // NOI18N
        debetField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                debetFieldKeyPressed(evt);
            }
        });

        kreditLabel.setDisplayedMnemonic('K');
        kreditLabel.setLabelFor(kreditField);
        kreditLabel.setText(resourceMap.getString("kreditLabel.text")); // NOI18N
        kreditLabel.setName("kreditLabel"); // NOI18N

        kreditField.setText(resourceMap.getString("kreditField.text")); // NOI18N
        kreditField.setName("kreditField"); // NOI18N
        kreditField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kreditFieldKeyPressed(evt);
            }
        });

        buttonLabel.setAction(actionMap.get("addKasHarian")); // NOI18N
        buttonLabel.setMnemonic('a');
        buttonLabel.setText(resourceMap.getString("buttonLabel.text")); // NOI18N
        buttonLabel.setName("buttonLabel"); // NOI18N
        buttonLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buttonLabelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kelompokLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addComponent(debetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addGap(22, 22, 22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(debetField, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(kelompokField, 0, 152, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(uraianLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addComponent(kreditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(uraianField, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                        .addComponent(kreditField, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonLabel)))
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kelompokLabel)
                    .addComponent(uraianLabel)
                    .addComponent(uraianField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kelompokField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(debetLabel)
                    .addComponent(kreditLabel)
                    .addComponent(buttonLabel)
                    .addComponent(kreditField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(debetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kasHarianScrollPane.setName("kasHarianScrollPane"); // NOI18N

        kasHarianTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaksi", "Kelompok", "Uraian", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        kasHarianTable.setName("kasHarianTable"); // NOI18N
        kasHarianTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kasHarianTableMouseClicked(evt);
            }
        });
        kasHarianScrollPane.setViewportView(kasHarianTable);
        kasHarianTable.getColumnModel().getColumn(0).setMaxWidth(100);
        kasHarianTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("kasHarianTable.columnModel.title0")); // NOI18N
        kasHarianTable.getColumnModel().getColumn(1).setMaxWidth(200);
        kasHarianTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("kasHarianTable.columnModel.title1")); // NOI18N
        kasHarianTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("kasHarianTable.columnModel.title2")); // NOI18N
        kasHarianTable.getColumnModel().getColumn(3).setMaxWidth(200);
        kasHarianTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("kasHarianTable.columnModel.title3")); // NOI18N

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kasHarianScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kasHarianScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );

        footerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        footerPanel.setName("footerPanel"); // NOI18N

        tutupButton.setAction(actionMap.get("closeKasHarian")); // NOI18N
        tutupButton.setMnemonic('T');
        tutupButton.setText(resourceMap.getString("tutupButton.text")); // NOI18N
        tutupButton.setName("tutupButton"); // NOI18N
        tutupButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tutupButtonKeyPressed(evt);
            }
        });

        hapusButton.setAction(actionMap.get("deleteKasHarian")); // NOI18N
        hapusButton.setMnemonic('H');
        hapusButton.setText(resourceMap.getString("hapusButton.text")); // NOI18N
        hapusButton.setName("hapusButton"); // NOI18N
        hapusButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout footerPanelLayout = new javax.swing.GroupLayout(footerPanel);
        footerPanel.setLayout(footerPanelLayout);
        footerPanelLayout.setHorizontalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hapusButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 493, Short.MAX_VALUE)
                .addComponent(tutupButton)
                .addContainerGap())
        );
        footerPanelLayout.setVerticalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tutupButton)
                    .addComponent(hapusButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

private void kelompokFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kelompokFieldKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_kelompokFieldKeyPressed

private void uraianFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_uraianFieldKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_uraianFieldKeyPressed

private void debetFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_debetFieldKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_debetFieldKeyPressed

private void kreditFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kreditFieldKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        evt.getComponent().transferFocus();
    }
}//GEN-LAST:event_kreditFieldKeyPressed

private void buttonLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buttonLabelKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        addKasHarian();
    }
}//GEN-LAST:event_buttonLabelKeyPressed

    @SuppressWarnings("empty-statement")
private void kasHarianTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kasHarianTableMouseClicked

        if (kasHarianTable.getSelectedRowCount() == 1) {
            if ((evt.getButton() == MouseEvent.BUTTON3) && (evt.getClickCount() == 1)) {
                kasHarianPopup.show(kasHarianTable, evt.getX(), evt.getY());
            }
            ;
        }
        ;
}//GEN-LAST:event_kasHarianTableMouseClicked

private void hapusButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusButtonKeyPressed

    deleteKasHarian();
}//GEN-LAST:event_hapusButtonKeyPressed

private void tutupButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tutupButtonKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        closeKasHarian();
    }
}//GEN-LAST:event_tutupButtonKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLabel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTextField debetField;
    private javax.swing.JLabel debetLabel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JButton hapusButton;
    private javax.swing.JMenuItem hapusMenuItem;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPopupMenu kasHarianPopup;
    private javax.swing.JScrollPane kasHarianScrollPane;
    private javax.swing.JTable kasHarianTable;
    private javax.swing.JComboBox kelompokField;
    private javax.swing.JLabel kelompokLabel;
    private javax.swing.JTextField kreditField;
    private javax.swing.JLabel kreditLabel;
    private javax.swing.JLabel shiftField;
    private javax.swing.JLabel shiftLabel;
    private javax.swing.JLabel subHeaderLabel;
    private javax.swing.JLabel tanggalField;
    private javax.swing.JLabel tanggalLabel;
    private javax.swing.JButton tutupButton;
    private javax.swing.JTextField uraianField;
    private javax.swing.JLabel uraianLabel;
    // End of variables declaration//GEN-END:variables
}
