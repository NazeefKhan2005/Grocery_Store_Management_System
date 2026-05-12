import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The entry point for the Grocery Store Management System.
 * This class contains the main method which initializes the system 
 * and begins the application's execution.
 */
class Main {
    
    /**
     * The main method that serves as the starting point for the Java application.
     * It creates an instance of the main system controller and triggers its core loop.
     */
    public static void main(String[] args) {
        
        // Instantiate the central controller for the grocery store system
        GroceryStoreSystem system = new GroceryStoreSystem();
        
        // Start the main application loop, which handles the UI menus and user interactions
        system.runSystem();
    }
}

/**
 * The central controller for the Grocery Store Management System.
 * This class ties together the InventoryManager and CartList,
 * providing an interactive command-line interface for the user to manage 
 * inventory, build a shopping cart, and process checkouts.
 */
public class GroceryStoreSystem {
    
    /**
     * The inventory manager instance responsible for handling all product data.
     */
    private InventoryManager inventory;
    
    /**
     * The shopping cart instance responsible for holding the user's selected items.
     */
    private CartList cart;
    
    /**
     * A Scanner object used to read input from the user via the console.
     */
    private Scanner scanner;

    /**
     * Constructs a new GroceryStoreSystem.
     * Initializes the internal inventory manager, an empty shopping cart, 
     * and the scanner for reading standard input.
     */
    public GroceryStoreSystem() {
        inventory = new InventoryManager();
        cart = new CartList();
        scanner = new Scanner(System.in);
    }

    /**
     * Starts and runs the main loop of the grocery store application.
     * It first loads the inventory data from a predefined text file. 
     * Then, it enters a continuous loop that displays the main menu, captures 
     * user input, and routes the user to the appropriate sub-menus (Inventory, 
     * Cart, or Checkout) until the user decides to exit.
     */
    public void runSystem() {
        // Load initial inventory data from the specified file path
        inventory.loadFromFile("/home/nazeef05/Desktop/Individual Assignment 2/src/inventory.txt");
        
        boolean running = true;

        // Main application loop
        while (running) {
            printMainMenu();

            try {
                System.out.print("\nEnter your choice: ");
                int choice = scanner.nextInt();
                
                // Consume the leftover newline character after reading the integer
                scanner.nextLine(); 

                // Route the application flow based on the user's selection
                switch (choice) {
                    case 1:
                        handleInventoryMenu();
                        break;
                    case 2:
                        handleCartMenu();
                        break;
                    case 3:
                        // handleCheckoutMenu() is expected to return false when the user chooses to exit, terminating the loop
                        running = handleCheckoutMenu();
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } 
            catch (InputMismatchException e) {
                // Handle cases where the user types non-integer inputs (like letters or symbols)
                System.out.println("Invalid input. Please enter a valid number.");
                
                // Clear the invalid input from the scanner's buffer to prevent an infinite loop
                scanner.nextLine(); 
            }
        }
        
        // Close the scanner resource when the system is fully exiting
        scanner.close();
    }
    


    /**
     * Displays the primary navigation menu for the application.
     * Shows the top-level options available to the user: navigating to the 
     * Inventory menu, the Shopping Cart menu, or proceeding to Checkout.
     */
    private void printMainMenu() {
        System.out.println("   GROCERY STORE MANAGEMENT SYSTEM");
        System.out.println("1.  Inventory Management");
        System.out.println("2.  Shopping Cart Management");
        System.out.println("3.  Checkout and Exit");
    }

    /**
     * Handles the interactive loop for the Inventory Management sub-menu.
     * This method displays the inventory options, captures the user's choice, 
     * gathers necessary input (like product IDs or names), and executes the 
     * corresponding actions on the internal InventoryManager instance.
     * The menu loops continuously until the user selects the option to return.
     */
    private void handleInventoryMenu() {
        boolean inInventoryMenu = true;
        
        while (inInventoryMenu) {
            System.out.println("\n--- Inventory Management ---");
            System.out.println("1. Display all products");
            System.out.println("2. Search product by ID");
            System.out.println("3. Search product by name");
            System.out.println("4. Add new product");
            System.out.println("5. Remove product");
            System.out.println("6. Update stock");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                
                // Consume the leftover newline character
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        // Show all items currently in the system
                        inventory.displayAll();
                        break;
                        
                    case 2:
                        // Search for a specific product using its unique ID
                        System.out.print("Enter Product ID to search: ");
                        int searchId = scanner.nextInt();
                        Product found = inventory.searchById(searchId);
                        if (found != null) {
                            System.out.println("Product Found -> " + found.toString());
                        } 
                        else {
                            System.out.println("Product with ID " + searchId + " not found.");
                        }
                        break;
                        
                    case 3:
                        // Search for products that match or partially match a given name
                        System.out.print("Enter Product Name to search: ");
                        String searchName = scanner.nextLine();
                        
                        ArrayList<Product> foundProducts = inventory.searchByName(searchName);
                        
                        if (foundProducts.isEmpty()) {
                            System.out.println("No matching products found for: " + searchName);
                        } 
                        else {
                            System.out.println("Matching Products:");
                            for (Product p : foundProducts) {
                                System.out.println(p.toString());
                            }
                        }
                        break;
                        
                    case 4:
                        // Gather data to create and add a entirely new product to the system
                        System.out.print("Enter new Product ID: ");
                        int newId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        System.out.print("Enter Product Name: ");
                        String newName = scanner.nextLine();
                        
                        System.out.print("Enter Product Price: ");
                        double newPrice = scanner.nextDouble();
                        
                        System.out.print("Enter Initial Stock: ");
                        int newStock = scanner.nextInt();
                        
                        Product newProduct = new Product(newId, newName, newPrice, newStock);
                        inventory.addProduct(newProduct);
                        break;
                        
                    case 5:
                        // Remove a product entirely from the inventory based on its ID
                        System.out.print("Enter Product ID to remove: ");
                        int removeId = scanner.nextInt();
                        inventory.removeProduct(removeId);
                        break;
                        
                    case 6:
                        // Modify the available quantity of an existing product
                        System.out.print("Enter Product ID to update: ");
                        int updateId = scanner.nextInt();
                        System.out.print("Enter new stock quantity: ");
                        int updatedStock = scanner.nextInt();
                        
                        if(inventory.searchById(updateId) != null) {
                            inventory.updateStock(updateId, updatedStock);
                            System.out.println("Stock updated successfully.");
                        } else {
                            System.out.println("Product not found! Cannot update stock.");
                        }
                        break;
                        
                    case 7:
                        // Break the while loop to return to the main menu
                        inInventoryMenu = false; 
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                // Catch non-integer inputs to prevent the application from crashing
                System.out.println("Invalid input format. Please try again.");
                scanner.nextLine(); // Clear the bad input from the buffer
            }
        }
    }

    /**
     * Handles the interactive loop for the Shopping Cart Management sub-menu.
     * This method displays the cart options, captures the user's choice, and executes 
     * corresponding actions. Crucially, it manages the synchronized relationship between 
     * the cart and the inventory (e.g., deducting stock when an item is added to the cart, 
     * and restoring stock when an item is removed or an action is undone).
     */
    private void handleCartMenu() {
        boolean inCartMenu = true;
        
        while (inCartMenu) {
            System.out.println("\n--- Shopping Cart Management ---");
            System.out.println("1. Add item to cart");
            System.out.println("2. View cart");
            System.out.println("3. Remove item from cart");
            System.out.println("4. Update quantity in cart");
            System.out.println("5. Undo last cart addition");
            System.out.println("6. Clear cart");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                
                // Consume the leftover newline character
                scanner.nextLine(); 

                switch (choice) {
                    case 1: { 
                        // Add an item to the cart, ensuring there is enough stock in the inventory first
                        System.out.print("Enter Product ID to add: ");
                        int addId = scanner.nextInt();
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();

                        Product productToAdd = inventory.getProductById(addId);

                        if (productToAdd == null) {
                            System.out.println("Error: Product not found in inventory.");
                        } 
                        else if (inventory.isAvailable(addId, qty)) {
                            // Deduct the requested quantity from the main inventory
                            inventory.updateStock(addId, productToAdd.getStock() - qty);
                            // Add the item to the user's cart
                            cart.addItem(productToAdd, qty);
                        } 
                        else {
                            System.out.println("Error: Insufficient stock. Only " + productToAdd.getStock() + " available.");
                        }
                        break;
                    }
                    case 2:
                        // Display all items currently in the cart and print the total formatted bill
                        cart.displayCart();
                        if (!cart.isEmpty()) {
                            System.out.println("-------------------------------------------");
                            System.out.println("Total Bill: $" + String.format("%.2f", cart.calculateTotal()));
                        }
                        break;
                        
                    case 3: {
                        // Remove an item entirely from the cart and restore its quantity back to the inventory
                        System.out.print("Enter Product ID to remove from cart: ");
                        int removeId = scanner.nextInt();
                        
                        CartNode itemToRemove = cart.findItem(removeId);
                        if (itemToRemove != null) {
                            int qtyToRestore = itemToRemove.getQuantity();
                            cart.removeItem(removeId);
                            
                            // Retrieve the product and add the removed quantity back to its stock
                            Product p = inventory.getProductById(removeId);
                            inventory.updateStock(removeId, p.getStock() + qtyToRestore);
                            System.out.println("Stock restored to inventory.");
                        } 
                        else {
                            System.out.println("Item not found in your cart.");
                        }
                        break;
                    }
                    case 4: {
                        // Modify the quantity of an item already in the cart, adjusting inventory stock up or down as needed
                        System.out.print("Enter Product ID to update in cart: ");
                        int updateId = scanner.nextInt();
                        
                        CartNode currentItem = cart.findItem(updateId);
                        if (currentItem == null) {
                            System.out.println("Item not found in your cart.");
                            break;
                        }

                        System.out.print("Enter new quantity: ");
                        int newQty = scanner.nextInt();
                        int currentQty = currentItem.getQuantity();
                        int difference = newQty - currentQty;

                        if (difference > 0) { 
                            // User wants MORE of the item; check if inventory can support the extra amount
                            if (inventory.isAvailable(updateId, difference)) {
                                Product p = inventory.getProductById(updateId);
                                inventory.updateStock(updateId, p.getStock() - difference);
                                cart.updateQuantity(updateId, newQty);
                                System.out.println("Cart updated successfully.");
                            }
                            else {
                                System.out.println("Insufficient stock to add " + difference + " more.");
                            }
                        } 
                        else if (difference < 0) { 
                            // User wants LESS of the item; put the extra amount back into inventory
                            Product p = inventory.getProductById(updateId);
                            inventory.updateStock(updateId, p.getStock() + Math.abs(difference));
                            cart.updateQuantity(updateId, newQty);
                            System.out.println("Cart updated successfully.");
                        } 
                        else {
                            // User entered the exact same quantity that is already in the cart
                            System.out.println("Quantity is the same. No changes made.");
                        }
                        break;
                    }
                    case 5: {
                        // Revert the most recent addition to the cart
                        CartNode undoneAction = cart.undo();
                        
                        if (undoneAction != null) {
                            int id = undoneAction.getProduct().getId();
                            int qtyRestored = undoneAction.getQuantity();
                            
                            // Ensure the undone quantity is returned to the inventory stock
                            Product p = inventory.getProductById(id);
                            inventory.updateStock(id, p.getStock() + qtyRestored);
                            System.out.println("Restored " + qtyRestored + " unit(s) of " + p.getName() + " back to inventory.");
                        }
                        break;
                    }
                    case 6: {
                        // Empty the entire cart and restore all items back to the inventory
                        if (cart.isEmpty()) {
                            System.out.println("Cart is already empty.");
                            break;
                        }
                        
                        System.out.println("Clearing cart and restoring all stock...");
                        
                        // Repeatedly undo actions until the cart is completely empty
                        // This ensures the stock for every item is accurately restored
                        while (!cart.isEmpty()) {
                            CartNode undone = cart.undo();
                            if (undone != null) {
                                Product p = inventory.getProductById(undone.getProduct().getId());
                                inventory.updateStock(p.getId(), p.getStock() + undone.getQuantity());
                            }
                        }
                        System.out.println("Cart completely cleared.");
                        break;
                    }
                    case 7:
                        // Break the while loop to return to the main menu
                        inCartMenu = false; 
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                // Catch non-integer inputs to prevent the application from crashing
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the bad input from the buffer
            }
        }
    }

    /**
     * Handles the interactive loop for the Checkout and Exit sub-menu.
     * This method provides options to finalize a purchase by generating a bill, 
     * or to save the current inventory state to a file and exit the application entirely.
     *
     * @return A boolean value indicating whether the main application loop should continue. 
     *         Returns false if the user chooses to exit the system; true otherwise.
     */
    private boolean handleCheckoutMenu() {
        boolean inCheckoutMenu = true;
        
        while (inCheckoutMenu) {
            System.out.println("\n--- Checkout and Exit ---");
            System.out.println("1. Generate Bill / Checkout");
            System.out.println("2. Save Inventory and Exit System");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                
                // Consume the leftover newline character
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        // Process the checkout if there are items in the cart
                        if (cart.isEmpty()) {
                            System.out.println("Your cart is empty. Nothing to checkout.");
                        } 
                        else {
                            System.out.println("                FINAL BILL                 ");

                            // Display all items and their individual subtotals
                            cart.displayCart();  
                            
                            // Calculate and format the total amount due to 2 decimal places
                            System.out.println("-------------------------------------------");
                            System.out.println("Total Amount Due: $" + String.format("%.2f", cart.calculateTotal())); 
                            System.out.println("===========================================");
                            
                            // Empty the cart after a successful checkout transaction
                            cart.clear(); 
                            
                            System.out.println("Checkout successful. Thank you for shopping!");
                        }
                        break;
                        
                    case 2:
                        // Persist the current inventory data to the text file before shutting down
                        System.out.println("Saving inventory...");
                        inventory.saveToFile("/home/nazeef05/Desktop/Individual Assignment 2/src/inventory.txt");
                        System.out.println("Exiting the system. Goodbye!");
                        
                        // Return false to signal the main runSystem() loop to terminate
                        return false; 
                        
                    case 3:
                        // Break the local while loop to return to the main menu
                        inCheckoutMenu = false; 
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                // Catch non-integer inputs to prevent the application from crashing
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the bad input from the buffer
            }
        }
        
        // Return true by default to keep the main application loop running
        return true; 
    }
}



