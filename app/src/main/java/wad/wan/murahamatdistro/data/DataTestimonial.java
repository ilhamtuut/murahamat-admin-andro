package wad.wan.murahamatdistro.data;

/**
 * Created by user on 19/03/2017.
 */
public class DataTestimonial {
    private String id,name,image,testi,created_at,updated_at;

    public DataTestimonial(){}

    public DataTestimonial(String created_at, String id, String image, String name, String testi, String updated_at) {
        this.created_at = created_at;
        this.id = id;
        this.image = image;
        this.name = name;
        this.testi = testi;
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTesti() {
        return testi;
    }

    public void setTesti(String testi) {
        this.testi = testi;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
