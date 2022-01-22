import javax.swing.*;
import java.io.Serializable;

public class Sales implements Serializable {
    private String username;        // Username of user who is selling the object
    private String name;
    private double price;
    private String description;
    private SaleType type;

    public Sales(String username, String name, double price, String description, SaleType type)
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

    public SaleType getType() {
        return type;
    }

    public String toString() {
        String result = String.format("Item: %s   Price: %f", name, price);
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
}
