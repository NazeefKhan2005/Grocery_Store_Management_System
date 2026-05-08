import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GroceryStoreSystem {
    private InventoryManager inventory;
    private CartList cart;
    private Scanner scanner;

    public GroceryStoreSystem() {
        inventory = new InventoryManager();
        cart = new CartList();
        scanner = new Scanner(System.in);
    }

    public void runSystem() {
        inventory.loadFromFile("/home/nazeef05/Desktop/DS Project/Grocery_Store_Management_System/src/inventory.txt");
        boolean running = true;

        while (running) {
            printMainMenu();

            try {
                System.out.print("\nEnter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1:
                        handleInventoryMenu();
                        break;
                    case 2:
                        handleCartMenu();
                        break;
                    case 3:
                        // If handleCheckoutMenu returns false, it ends the main loop
                        running = handleCheckoutMenu();
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear bad input
            }
        }
        scanner.close();
    }

    // ==========================================
    // MAIN MENU
    // ==========================================
    private void printMainMenu() {
        System.out.println("\n===========================================");
        System.out.println("   GROCERY STORE MANAGEMENT SYSTEM");
        System.out.println("===========================================");
        System.out.println("1.  Inventory Management");
        System.out.println("2.  Shopping Cart Management");
        System.out.println("3.  Checkout and Exit");
    }

    // ==========================================
    // 1. INVENTORY MANAGEMENT
    // ==========================================
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
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1:
                        inventory.displayAll();
                        break;
                        
                    case 2:
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
                        System.out.print("Enter Product Name to search: ");
                        String searchName = scanner.nextLine();
                        
                        ArrayList<Product> foundProducts = inventory.searchByName(searchName);
                        
                        if (foundProducts.isEmpty()) {
                            System.out.println("No matching products found for: " + searchName);
                        } else {
                            System.out.println("Matching Products:");
                            for (Product p : foundProducts) {
                                System.out.println(p.toString());
                            }
                        }
                        break;
                        
                    case 4:
                        System.out.print("Enter new Product ID: ");
                        int newId = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer before reading a String
                        
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
                        System.out.print("Enter Product ID to remove: ");
                        int removeId = scanner.nextInt();
                        inventory.removeProduct(removeId);
                        break;
                        
                    case 6:
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
                        inInventoryMenu = false; // Exits this loop, goes back to main menu
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format. Please try again.");
                scanner.nextLine(); // Clear bad input to prevent infinite loop
            }
        }
    }

    // ==========================================
    // 2. SHOPPING CART MANAGEMENT
    // ==========================================
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
                scanner.nextLine(); 

                switch (choice) {
                    case 1: { 
                        System.out.print("Enter Product ID to add: ");
                        int addId = scanner.nextInt();
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();

                        Product productToAdd = inventory.getProductById(addId);

                        if (productToAdd == null) {
                            System.out.println("Error: Product not found in inventory.");
                        } 
                        else if (inventory.isAvailable(addId, qty)) {
                            // Temporarily reduce the stock
                            inventory.updateStock(addId, productToAdd.getStock() - qty);
                            // Add to cart
                            cart.addItem(productToAdd, qty);
                        } else {
                            System.out.println("Error: Insufficient stock. Only " + productToAdd.getStock() + " available.");
                        }
                        break;
                    }
                    case 2:
                        cart.displayCart();
                        if (!cart.isEmpty()) {
                            System.out.println("-------------------------------------------");
                            System.out.println("Total Bill: $" + String.format("%.2f", cart.calculateTotal()));
                        }
                        break;
                        
                    case 3: {
                        System.out.print("Enter Product ID to remove from cart: ");
                        int removeId = scanner.nextInt();
                        
                        CartNode itemToRemove = cart.findItem(removeId);
                        if (itemToRemove != null) {
                            // Find out how much stock we are putting back on the shelf
                            int qtyToRestore = itemToRemove.getQuantity();
                            cart.removeItem(removeId);
                            
                            // Restore stock in inventory
                            Product p = inventory.getProductById(removeId);
                            inventory.updateStock(removeId, p.getStock() + qtyToRestore);
                            System.out.println("Stock restored to inventory.");
                        } else {
                            System.out.println("Item not found in your cart.");
                        }
                        break;
                    }
                    case 4: {
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

                        if (difference > 0) { // Trying to add more to the cart
                            if (inventory.isAvailable(updateId, difference)) {
                                Product p = inventory.getProductById(updateId);
                                inventory.updateStock(updateId, p.getStock() - difference);
                                cart.updateQuantity(updateId, newQty);
                                System.out.println("Cart updated successfully.");
                            }
                            else {
                                System.out.println("Insufficient stock to add " + difference + " more.");
                            }
                        } else if (difference < 0) { // Reducing cart quantity (putting some back)
                            Product p = inventory.getProductById(updateId);
                            // Math.abs turns the negative difference into a positive number to add back
                            inventory.updateStock(updateId, p.getStock() + Math.abs(difference));
                            cart.updateQuantity(updateId, newQty);
                            System.out.println("Cart updated successfully.");
                        } 
                        else {
                            System.out.println("Quantity is the same. No changes made.");
                        }
                        break;
                    }
                    case 5: {
                        // Undo removes from the cart data structure and returns the action
                        CartNode undoneAction = cart.undo();
                        
                        if (undoneAction != null) {
                            // We must manually restore the stock that was undone
                            int id = undoneAction.getProduct().getId();
                            int qtyRestored = undoneAction.getQuantity();
                            
                            Product p = inventory.getProductById(id);
                            inventory.updateStock(id, p.getStock() + qtyRestored);
                            System.out.println("Restored " + qtyRestored + " unit(s) of " + p.getName() + " back to inventory.");
                        }
                        break;
                    }
                    case 6: {
                        if (cart.isEmpty()) {
                            System.out.println("Cart is already empty.");
                            break;
                        }
                        
                        // The safest way to clear the cart and restore all stock without needing 
                        // a custom iterator is to continuously trigger your undo feature until empty.
                        System.out.println("Clearing cart and restoring all stock...");
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
                        inCartMenu = false; 
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    // ==========================================
    // 3. CHECKOUT AND EXIT
    // ==========================================
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
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        if (cart.isEmpty()) {
                            System.out.println("Your cart is empty. Nothing to checkout.");
                        } else {
                            System.out.println("\n===========================================");
                            System.out.println("                FINAL BILL                 ");
                            System.out.println("===========================================");
                            
                            cart.displayCart(); // Itemized bill 
                            
                            System.out.println("-------------------------------------------");
                            System.out.println("Total Amount Due: $" + String.format("%.2f", cart.calculateTotal())); // Calculate total bill amount 
                            System.out.println("===========================================");
                            
                            // Make the purchase final by wiping the cart without restoring the stock 
                            cart.clear(); 
                            
                            System.out.println("Checkout successful. Thank you for shopping!");
                        }
                        break;
                        
                    case 2:
                        System.out.println("Saving inventory...");
                        // Save current inventory to file 
                        inventory.saveToFile("/home/nazeef05/Desktop/DS Project/Grocery_Store_Management_System/src/inventory.txt");
                        System.out.println("Exiting the system. Goodbye!");
                        return false; // Returns false to 'running' in main menu, terminating the app
                        
                    case 3:
                        inCheckoutMenu = false; 
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
        return true; // Keep the main program running if they selected "Back"
    }
}



class Main{
    public static void main(String[] args) {
        GroceryStoreSystem system = new GroceryStoreSystem();
        system.runSystem();
    }
}