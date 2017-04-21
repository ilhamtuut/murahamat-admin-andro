package wad.wan.murahamatdistro.data;

/**
 * Created by user on 02/04/2017.
 */
public class Image {
    private String id,gambar;

    public Image(){}

    public Image(String gambar, String id) {
        this.gambar = gambar;
        this.id = id;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
