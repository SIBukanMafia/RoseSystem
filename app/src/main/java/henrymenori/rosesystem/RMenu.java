package henrymenori.rosesystem;

/**
 * Created by A 46 CB i3 on 4/17/2015.
 */
public class RMenu {

    // attribute
    private String nama;
    private String harga;
    private String ketersediaan;
    private int jumlah;

    // constructor
    public RMenu(String nama, String harga, String ketersediaan, int jumlah) {
        this.nama = nama;
        this.harga = harga;
        this.ketersediaan = ketersediaan;
        this.jumlah = jumlah;
    }

    // getter
    public String getNama() {
        return nama;
    }
    public String getHarga() {
        return harga;
    }
    public String getKetersediaan() {
        return ketersediaan;
    }
    public int getJumlah() {
        return jumlah;
    }

    // setter
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(String harga) {
        this.harga = harga;
    }
    public void setKetersediaan(String ketersediaan) {
        this.ketersediaan = ketersediaan;
    }
}
