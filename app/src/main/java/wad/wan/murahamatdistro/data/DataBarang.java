package wad.wan.murahamatdistro.data;

/**
 * Created by user on 18/03/2017.
 */
public class DataBarang {
    public String id,nama_barang,id_kategori,harga,ukuran,merek,gambar,stock,deskripsi;

    public DataBarang(){}

    public DataBarang(String id, String nama_barang, String id_kategori, String harga, String ukuran, String merek, String gambar, String stock, String deskripsi) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.id_kategori = id_kategori;
        this.harga = harga;
        this.ukuran = ukuran;
        this.merek = merek;
        this.gambar = gambar;
        this.stock = stock;
        this.deskripsi = deskripsi;
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

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
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
}
