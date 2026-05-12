/**
 * Represents a product within the system's inventory.
 * This class encapsulates the core details of a product, including its 
 * unique identifier, name, pricing, and available stock quantity.
 */
public class Product {
    
    /**
     * The unique identifier for the product.
     */
    private int id;
    
    /**
     * The display name of the product.
     */
    private String name;
    
    /**
     * The retail price of the product.
     */
    private double price;
    
    /**
     * The current number of units available in inventory.
     */
    private int stock;

    /**
     * Constructs a new Product instance with the specified details.
     *
     * @param id    The unique identifier to assign to the product.
     * @param name  The name of the product.
     * @param price The cost of the product.
     * @param stock The initial quantity of the product in stock.
     */
    public Product(int id, String name, double price, int stock){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Retrieves the unique identifier of the product.
     *
     * @return An integer representing the product's ID.
     */
    public int getId(){
        return id;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return A string representing the product's name.
     */
    public String getName(){
        return name;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return A double representing the product's price.
     */
    public double getPrice(){
        return price;
    }

    /**
     * Retrieves the current stock level of the product.
     *
     * @return An integer representing how many units are currently in stock.
     */
    public int getStock(){
        return stock;
    }

    /**
     * Updates the available stock quantity for the product.
     *
     * @param newStock The new stock quantity to set for the product.
     */
    public void setStock(int newStock){
        this.stock = newStock;
    }

    /**
     * Returns a string representation of the Product object.
     * This is useful for logging, debugging, or displaying the object's state in a readable format.
     *
     * @return A string containing the product's id, name, price, and stock values.
     */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", stock=" + stock + '}';
    }
}