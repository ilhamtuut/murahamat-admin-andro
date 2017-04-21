package wad.wan.murahamatdistro.data;

/**
 * Created by user on 19/03/2017.
 */
public class DataUser {
    private String id,username,email,password;

    public DataUser(){}

    public DataUser(String id, String username,String email,String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword (String password){
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
