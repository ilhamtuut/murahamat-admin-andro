package wad.wan.murahamatdistro.data;

/**
 * Created by user on 03/04/2017.
 */
public class Transaksi {
    private String id, name, name_product, total_product,total_price,created_at,updated_at;

    public Transaksi(){}

    public Transaksi(String created_at, String id, String name, String name_product, String total_price, String total_product, String updated_at) {
        this.created_at = created_at;
        this.id = id;
        this.name = name;
        this.name_product = name_product;
        this.total_price = total_price;
        this.total_product = total_product;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_product() {
        return total_product;
    }

    public void setTotal_product(String total_product) {
        this.total_product = total_product;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
