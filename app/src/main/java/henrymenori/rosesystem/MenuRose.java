package henrymenori.rosesystem;

/**
 * Created by A 46 CB i3 on 4/16/2015.
 */
public class MenuRose {

    // attribute
    private String nama;
    private int harga;
    private boolean ketersediaan;

    // constructor
    public MenuRose() {}

    // getter

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public boolean isKetersediaan() {
        return ketersediaan;
    }

    public void setKetersediaan(boolean ketersediaan) {
        this.ketersediaan = ketersediaan;
    }
}
