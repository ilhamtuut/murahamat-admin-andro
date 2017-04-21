package wad.wan.murahamatdistro.data;

/**
 * Created by user on 18/03/2017.
 */
public class DataBarang {
    public String id,nama_barang,id_kategori,kategori,harga1,harga2,harga3,harga4,harga5,harga6,ukuran,merek,gambar,stock,deskripsi;

    public DataBarang(){}

    public DataBarang(String id, String nama_barang, String id_kategori,String kategori, String harga1,String harga2,String harga3,String harga4,String harga5,String harga6, String ukuran, String merek, String gambar, String stock, String deskripsi) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.id_kategori = id_kategori;
        this.kategori = kategori;
        this.harga1 = harga1;
        this.harga2 = harga2;
        this.harga3 = harga3;
        this.harga4 = harga4;
        this.harga5 = harga5;
        this.harga6 = harga6;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHarga1() {
        return harga1;
    }

    public void setHarga1(String harga1) {
        this.harga1 = harga1;
    }

    public String getHarga2() {
        return harga2;
    }

    public void setHarga2(String harga2) {
        this.harga2 = harga2;
    }

    public String getHarga3() {
        return harga3;
    }

    public void setHarga3(String harga3) {
        this.harga3 = harga3;
    }

    public String getHarga4() {
        return harga4;
    }

    public void setHarga4(String harga4) {
        this.harga4 = harga4;
    }

    public String getHarga5() {
        return harga5;
    }

    public void setHarga5(String harga5) {
        this.harga5 = harga5;
    }

    public String getHarga6() {
        return harga6;
    }

    public void setHarga6(String harga6) {
        this.harga6 = harga6;
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
