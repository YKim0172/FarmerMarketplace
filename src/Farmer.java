import java.io.*;

public class Farmer implements Serializable {
    private String name;  //name of farmer
    private String username;  //username of farmer
    private String password;  //password of farmer

    public Farmer(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
