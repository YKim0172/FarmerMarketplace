import java.io.*;

public class Selling implements Serializable {
    private String sellerUsername;
    private String nameOfItem;
    private double sellingPrice;

    public Selling(String sellerUsername, String nameOfItem, double sellingPrice) {
        this.sellerUsername = sellerUsername;
        this.nameOfItem = nameOfItem;
        this.sellingPrice = sellingPrice;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getNameOfItem() {
        return nameOfItem;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

}
