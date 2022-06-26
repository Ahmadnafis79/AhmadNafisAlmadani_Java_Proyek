package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class LaptopViewFrame extends JFrame{
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JLabel Cari;

    public LaptopViewFrame() {
        tutupButton.addActionListener(e -> {
            dispose();
        });

        batalButton.addActionListener(e -> {
            isiTable();
        });

        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih Data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id_brand = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            LaptopInputFrame inputFrame = new LaptopInputFrame();
            inputFrame.setId(id_brand);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });


        tambahButton.addActionListener(e -> {
            LaptopInputFrame inputFrame = new LaptopInputFrame();
            inputFrame.setVisible(true);
        });

        cariButton.addActionListener(e -> {
            if (cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String seachSQL = "SELECT * FROM Laptop WHERE nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(seachSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];
                while (rs.next()) {
                    row[0] = rs.getInt("kd_laptop");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,
                    "Yakin mau hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );
            if (pilihan == 0) {
                TableModel tm = viewTable.getModel();
                int kd_laptop = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM laptop WHERE kd_laptop = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, kd_laptop);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        isiTable();
        init();
    }


    public void init() {
        setContentPane(mainPanel);
        setTitle("Data Laptop");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT K.*, B.nama AS nama_brand FROM laptop K " +
                "LEFT JOIN brand B ON K.kd_Laptop = B.id_brand";
        try {
            Statement s =  c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String[] header = {"Kode","Nama Brand","Nama Laptop"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            Object[] row = new Object[3];
            while (rs.next()){
                row[0] = rs.getInt("kd_laptop");
                row[1] =rs.getString("nama");
                row[2] =rs.getString("nama_brand");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





