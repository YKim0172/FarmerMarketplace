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
}
