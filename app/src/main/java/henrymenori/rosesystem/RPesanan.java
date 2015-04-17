package henrymenori.rosesystem;

/**
 * Created by A 46 CB i3 on 4/17/2015.
 */
public class RPesanan {

    // attribute
    private String nama;
    private int kuantitas;
    private String status;

    // constructor
    public RPesanan(String nama, int kuantitas, String status) {
        this.nama = nama;
        this.kuantitas = kuantitas;
        this.status = status;
    }

    // getter
    public String getNama() {
        return nama;
    }
    public int getKuantitas() {
        return kuantitas;
    }
    public String getStatus() {
        return status;
    }
}
