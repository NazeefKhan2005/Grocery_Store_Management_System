import java.util.ArrayList;
import java.io.*;

/**
 * Manages a collection of Product objects, acting as the central inventory system.
 * This class provides functionalities to add, remove, search, and update products,
 * as well as save and load the inventory data to and from a CSV file.
 */
public class InventoryManager {
    
    /**
     * The list storing all products currently in the inventory.
     */
    ArrayList<Product> inventory = new ArrayList<>();

    /**
     * Loads product data from a specified CSV file into the inventory list.
     * Expects the file to have a header row, which is skipped, followed by data 
     * in the format: id,name,price,stock.
     *
     * @param fileName The path and name of the file to read from.
     */
    public void loadFromFile(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Skip the header line
            reader.readLine();

            // Read the first line of data
            String line = reader.readLine();

            while(line != null){
                // Split the CSV line by commas
                String[] arr = line.split(",");

                // Parse the string values into their respective data types
                int id = Integer.parseInt(arr[0]);
                String name = arr[1];
                double price = Double.parseDouble(arr[2]);
                int stock = Integer.parseInt(arr[3]);

                // Create a new Product and add it to the inventory
                inventory.add(new Product(id, name, price, stock));

                // Read the next line
                line = reader.readLine();
            }
            
            System.out.println("Inventory loaded successfully!");
            reader.close();
        }
        catch(IOException e){
            System.out.println("An error occurred while loading the inventory: " + e.getMessage());
        }
    }

    /**
     * Saves the current state of the inventory to a specified CSV file.
     * Writes a header row first, followed by the details of each product.
     * If the file already exists, it will be overwritten.
     *
     * @param filename The path and name of the file to write to.
     */
    public void saveToFile(String filename){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            
            // Write the CSV header
            writer.write("id,name,price,stock");
            writer.newLine();

            // Iterate through the inventory and write each product's details
            for(int i = 0; i < inventory.size(); i++){
                Product product = inventory.get(i);
                writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getStock());
                writer.newLine();
            }

            System.out.println("Inventory saved successfully!");
            writer.close();
        }
        catch(IOException e){
            System.out.println("An error occurred while saving the inventory: " + e.getMessage());
        }
    }

    /**
     * Adds a new product to the inventory. 
     * The product will only be added if no other product with the same ID currently exists.
     *
     * @param p The Product object to be added to the inventory.
     */
    public void addProduct(Product p){
        if(searchById(p.getId()) == null){
            inventory.add(p);
            System.out.println(p.toString() + " added in the inventory!");
        }
        else{
            System.out.println("A product with same id " + p.getId() + " already exists in the inventory!");
        }
    }

    /**
     * Removes a product from the inventory based on its unique ID.
     * Prints a success message if removed, or an error message if the ID is not found.
     *
     * @param id The unique identifier of the product to remove.
     */
    public void removeProduct(int id){
        Product productToRemove = getProductById(id);
        
        if(productToRemove != null){
            inventory.remove(productToRemove);
            System.out.println(productToRemove.toString() + " removed successfully!");
        }
        else{
            System.out.println("Product not found!");
        }
    }

    /**
     * Searches for a product in the inventory using its unique ID.
     *
     * @param id The ID to search for.
     * @return The matching Product object, or null if no product is found.
     */
    public Product searchById(int id){
        Product match = null;
        for(int i = 0; i < inventory.size(); i++){
            Product product = inventory.get(i);
            if(product.getId() == id){
                match = product;
                break;
            }
        }
        return match;
    }

    /**
     * Searches for products whose names contain the specified search string.
     * The search is case-insensitive and matches partial strings.
     *
     * @param name The name (or partial name) to search for.
     * @return An ArrayList containing all Product objects that match the search criteria.
     */
    public ArrayList<Product> searchByName(String name){
        ArrayList<Product> matchingList = new ArrayList<>();

        String searchString = name.toLowerCase();

        for(int i = 0; i < inventory.size(); i++){
            Product currentProduct = inventory.get(i);

            // Check if the product's name contains the search string (case-insensitive)
            if(currentProduct.getName().toLowerCase().contains(searchString)){
                matchingList.add(currentProduct);
            }
        }

        return matchingList;
    }

    /**
     * Updates the stock quantity of a specific product.
     *
     * @param id       The unique ID of the product to update.
     * @param newStock The new stock quantity to assign to the product.
     */
    public void updateStock(int id, int newStock){
        Product update = getProductById(id);
        if(update == null){
            System.out.println("Product not found!");
            return;
        }
        update.setStock(newStock);
    }

    /**
     * Displays all products currently in the inventory in a formatted tabular view.
     * If the inventory list is null (though initialized empty by default), it prints a warning.
     */
    public void displayAll(){
        if(inventory == null){
            System.out.println("The inventory is empty!");
        }
        else{
            System.out.println("Id\t\t\tName\t\t\tPrice\t\t\tStock");
            for(Product p : inventory){
                System.out.println(p.getId() + "\t\t\t" + p.getName() + "\t\t\t" + p.getPrice() + "\t\t\t" + p.getStock());
            }
        }
    }

    /**
     * A helper method that wraps the searchById functionality.
     * Retrieves a product by its unique ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The Product object if found, otherwise null.
     */
    public Product getProductById(int id){
        return searchById(id);
    }

    /**
     * Checks if a requested quantity of a specific product is currently available in stock.
     *
     * @param id           The unique ID of the product to check.
     * @param requestedQty The number of units requested.
     * @return true if the product exists and its stock is greater than or equal to the requested quantity; false otherwise.
     */
    public boolean isAvailable(int id, int requestedQty){
        Product product = getProductById(id);
        if(product != null){
            return product.getStock() >= requestedQty;
        }

        return false;
    }
}