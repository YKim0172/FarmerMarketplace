import javax.swing.*;

public class Sales {
    private String username;        // Username of user who is selling the object
    private String name;
    private double price;
    private String description;
    private int stock;
    private SaleType type;

    public Sales(String username, String name, double price, String description, int stock, SaleType type)
            throws IllegalArgumentException {
        this.username = username;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
        this.description = description;
        if (stock <= 0) {
            throw new IllegalArgumentException("You must have at least one to sell before posting");
        }
        this.stock = stock;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public SaleType getType() {
        return type;
    }

    public String toString() {
        String result = String.format("Item: %s   Price: %f   Number in Stock: %d", name, price, stock);
        switch (type) {
            case CROP:
                result += "   Type: Crop";
            case LAND:
                result += "   Type: Land";
            case TOOL:
                result += "   Type: Tool";
            case LIVESTOCK:
                result += "   Type: Livestock";
        }
        return result;
    }

    public void buyItem(int amountBought) {
        if (amountBought > stock) {
            JOptionPane.showMessageDialog(null, "The amount you want to buy is greater than " +
                    "the stock of the item", "Not Enough Stock", JOptionPane.ERROR_MESSAGE);
        } else {
            stock -= amountBought;
        }
    }
}
