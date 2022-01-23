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
        String result = String.format("Item: %s   Price: %.2f", name, price);
        switch (type) {
            case CROP:
                result += "   Type: Crop";
                break;
            case LAND:
                result += "   Type: Land";
                break;
            case TOOL:
                result += "   Type: Tool";
                break;
            case LIVESTOCK:
                result += "   Type: Livestock";
                break;
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof Sales) {
            Sales s = (Sales) o;
            if (this.price == s.getPrice() && this.name.equals(s.getName())) {
                if (this.description.equals(s.getDescription())) {
                    if (this.username.equals(s.getUsername())) {
                        return this.type == s.getType();
                    }
                }
            }
        }
        return false;
    }
}
