package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LaptopInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField kodeTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JComboBox brandComboBox;
    private int kd_laptop;
    public void setId(int kd_laptop){
        this.kd_laptop = kd_laptop;
    }

    public LaptopInputFrame(){
        batalButton.addActionListener(e -> dispose());
        kustomisasiKomponen();
        init();

        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            if (nama.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Laptop",
                        "validasi Combobox",JOptionPane.WARNING_MESSAGE);
                brandComboBox.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) brandComboBox.getSelectedItem();
            int idBrand = item.getValue();
            if (idBrand == 0) {
                JOptionPane.showMessageDialog(null, "Pilih Brand", "Validasi ComboBox", JOptionPane.WARNING_MESSAGE);
                brandComboBox.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (kd_laptop == 0) {
                    String cekSQL = "SELECT * FROM laptop WHERE nama= ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                } else {
                        String insertSQL = "INSERT INTO laptop (kd_laptop,nama,id_brand) " + "VALUES (NULL, ?,?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, idBrand);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM laptop WHERE nama = ? AND kd_laptop != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, kd_laptop);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM laptop WHERE kd_laptop= ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, kd_laptop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kodeTextField.setText(String.valueOf(rs.getInt("kd_laptop")));
                namaTextField.setText(rs.getString("nama"));
                int id_brand = rs.getInt("id_brand");
                for (int i = 0; i < brandComboBox.getItemCount(); i++) {
                    brandComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) brandComboBox.getSelectedItem();
                    if ( id_brand == item.getValue()){
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void kustomisasiKomponen(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM brand ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            brandComboBox.addItem(new ComboBoxItem(0,"Pilih Brand"));
            while (rs.next()){
                brandComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id_brand"),
                        rs.getString("nama")));
            }
        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Laptop");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}