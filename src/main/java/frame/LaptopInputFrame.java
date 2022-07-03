package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class LaptopInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField kodeTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JComboBox brandComboBox;
    private JRadioButton chromebookRadioButton;
    private JRadioButton gamingRadioButton;
    private JRadioButton notebookRadioButton;
    private JRadioButton ultrabookRadioButton;
    private JRadioButton a4RadioButton;
    private JRadioButton a8RadioButton;
    private JRadioButton a16RadioButton;
    private JRadioButton a32RadioButton;
    private JTextField harddiskTextField;
    private JTextField prosesorTextField;
    private JTextField hargaTextField;
    private ButtonGroup tipeButtonGroup;
    private ButtonGroup ramButtonGroup;
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
                namaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) brandComboBox.getSelectedItem();
            int id_brand = item.getValue();
            if (id_brand == 0) {
                JOptionPane.showMessageDialog(null, "Pilih Brand", "Validasi ComboBox", JOptionPane.WARNING_MESSAGE);
                brandComboBox.requestFocus();
                return;
            }
            
            String tipe = "";
            if(chromebookRadioButton.isSelected()){
                tipe = "CHROMEBOOK";
            } else if (gamingRadioButton.isSelected()) {
                tipe = "GAMING";
            } else if (notebookRadioButton.isSelected()) {
                tipe = "NOTEBOOK";
            } else if (ultrabookRadioButton.isSelected()) {
                tipe = "ULTRABOOK";
            } else {
                JOptionPane.showMessageDialog(null, "pilih tipe", "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ram = "";
            if(a4RadioButton.isSelected()){
                ram = "4";
            } else if (a8RadioButton.isSelected()) {
                ram = "8";
            } else if (a16RadioButton.isSelected()) {
                ram = "16";
            } else if (a32RadioButton.isSelected()) {
                ram = "32";
            } else {
                JOptionPane.showMessageDialog(null, "pilih Ram", "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String harddisk = harddiskTextField.getText();
            if (harddisk.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Isi Ukuran Harddisk",
                        "validasi Kata Kunci Kosong", JOptionPane.WARNING_MESSAGE);
                harddiskTextField.requestFocus();
                return;
            }
            String prosesor = prosesorTextField.getText();
            if (prosesor.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Prosesor",
                        "validasi Kata Kunci Kosong", JOptionPane.WARNING_MESSAGE);
                prosesorTextField.requestFocus();
                return;
            }
            String harga = hargaTextField.getText();
            if (harga.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Isi Harga",
                        "validasi Kata Kunci Kosong", JOptionPane.WARNING_MESSAGE);
                hargaTextField.requestFocus();
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
                        String insertSQL = "INSERT INTO laptop (kd_laptop,nama,id_brand,tipe,ram,harddisk,prosesor,harga) " + "VALUES (NULL, ?,?,?,?,?,?,?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, id_brand);
                        ps.setString(3, tipe);
                        ps.setString(4, ram);
                        ps.setString(5, harddisk);
                        ps.setString(6, prosesor);
                        ps.setString(7, harga);
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
                    } else {
                        String updateSQL = "UPDATE laptop SET nama = ?, id_brand = ?, tipe = ?, ram = ?, harddisk = ?, prosesor = ?, harga" +
                                "WHERE kd_laptop = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, id_brand);
                        ps.setString(3, tipe);
                        ps.setString(4, ram);
                        ps.setString(5, harddisk);
                        ps.setString(6, prosesor);
                        ps.setString(7, harga);
                        ps.setInt(5, kd_laptop);
                        ps.executeUpdate();
                        dispose();
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
                String tipe = rs.getString("Tipe");
                if(tipe != null){
                    if(tipe.equals("CHROMEBOOK")){
                        chromebookRadioButton.setSelected(true);
                    } else if (tipe.equals("GAMING")) {
                        gamingRadioButton.setSelected(true);
                    } else if (tipe.equals("NOTEBOOK")) {
                        notebookRadioButton.setSelected(true);
                    } else if (tipe.equals("ULTRABOOK")) {
                        ultrabookRadioButton.setSelected(true);
                    }
                }
                String ram = rs.getString("Ram");
                if(ram != null){
                    if(ram.equals("4")){
                        a4RadioButton.setSelected(true);
                    } else if (ram.equals("8")) {
                        a8RadioButton.setSelected(true);
                    } else if (ram.equals("16")) {
                        a8RadioButton.setSelected(true);
                    } else if (ram.equals("32")) {
                        a8RadioButton.setSelected(true);
                    }
                }

                hargaTextField.setText(rs.getString("harddisk"));
                prosesorTextField.setText(rs.getString("prosesor"));
                hargaTextField.setText(rs.getString("harddisk"));
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
        tipeButtonGroup = new ButtonGroup();
        tipeButtonGroup.add(chromebookRadioButton);
        tipeButtonGroup.add(gamingRadioButton);
        tipeButtonGroup.add(notebookRadioButton);
        tipeButtonGroup.add(ultrabookRadioButton);

        ramButtonGroup = new ButtonGroup();
        ramButtonGroup.add(a4RadioButton);
        ramButtonGroup.add(a8RadioButton);
        ramButtonGroup.add(a16RadioButton);
        ramButtonGroup.add(a32RadioButton);
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Laptop");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}