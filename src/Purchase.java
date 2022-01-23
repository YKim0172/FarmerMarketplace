import java.io.*;

public class Purchase implements Serializable {
    private String usernameWhoBought;
    private String nameOfItem;
    private double pricePaid;

    public Purchase(String usernameWhoBought, String nameOfItem, double pricePaid) {
        this.usernameWhoBought = usernameWhoBought;
        this.nameOfItem = nameOfItem;
        this.pricePaid = pricePaid;
    }

    public String getUsernameWhoBought() {
        return usernameWhoBought;
    }

    public String getNameOfItem() {
        return nameOfItem;
    }

    public double getPricePaid() {
        return pricePaid;
    }
}
