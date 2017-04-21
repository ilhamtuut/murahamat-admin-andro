package wad.wan.murahamatdistro.data;

/**
 * Created by user on 02/04/2017.
 */
public class Promo {
    private String id,name,description,image,created_at,updated_at;

    public Promo(){}

    public Promo(String id, String name, String description, String image, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
