package wad.wan.murahamatdistro.data;

/**
 * Created by user on 20/03/2017.
 */
public class DataPromo {
    public String id,nama_barang,kategori,harga,ukuran,merek,gambar,stock,deskripsi,promo;

    public DataPromo(){}

    public DataPromo(String id, String nama_barang, String kategori, String harga, String ukuran, String merek, String gambar, String stock, String deskripsi, String promo) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.kategori = kategori;
        this.harga = harga;
        this.ukuran = ukuran;
        this.merek = merek;
        this.gambar = gambar;
        this.stock = stock;
        this.deskripsi = deskripsi;
        this.promo = promo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }
}
