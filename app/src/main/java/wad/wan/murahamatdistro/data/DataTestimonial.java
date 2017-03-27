package wad.wan.murahamatdistro.data;

/**
 * Created by user on 19/03/2017.
 */
public class DataTestimonial {
    private String id,nama,deskripsi,gambar;

    public DataTestimonial(){}

    public DataTestimonial(String id, String nama, String deskripsi, String gambar) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
