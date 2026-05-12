/**
 * Represents a shopping cart implemented as a custom linked list.
 * This class handles adding, removing, updating, and displaying items in the cart.
 * It also includes an undo feature supported by an internal stack to revert recent additions.
 */
public class CartList {
    
    /**
     * The first node (head) in the linked list of cart items.
     */
    private CartNode head;
    
    /**
     * The last node (tail) in the linked list of cart items.
     * Maintained for efficient additions to the end of the list.
     */
    private CartNode tail;
    
    /**
     * A stack used to keep track of added items, allowing for an undo operation.
     */
    private ItemStack undoStack; 

    /**
     * Constructs an empty CartList and initializes the undo stack.
     */
    public CartList() {
        this.undoStack = new ItemStack(); 
    }

    /**
     * Adds a product to the cart or updates its quantity if it already exists.
     * Every addition is recorded in the undo stack.
     *
     * @param p   The product to add to the cart.
     * @param qty The quantity of the product to add.
     */
    public void addItem(Product p, int qty) {
        CartNode existingNode = findItem(p.getId());
        
        // If the item is already in the cart, just increase its quantity
        if (existingNode != null) {
            updateQuantity(p.getId(), existingNode.getQuantity() + qty);
            System.out.println("Item quantity updated in the cart!");
        } else {
            // Otherwise, create a new node and append it to the linked list
            CartNode newNode = new CartNode(p, qty);
            if (head == null) {
                head = newNode;
                tail = head;
            } else {
                tail.setNext(newNode);
                tail = newNode; // Update tail pointer
            }
            System.out.println("Item added to the cart successfully!");
        }

        // Record the action in the stack for potential undo operations
        undoStack.push(p, qty);
    }

    /**
     * Removes a product entirely from the cart based on its ID.
     *
     * @param productId The unique ID of the product to remove.
     */
    public void removeItem(int productId) {
        if (isEmpty()) {
            System.out.println("The cart is empty!");
            return;
        }

        // Case 1: The item to remove is the first item (head)
        if (head.getProduct().getId() == productId) {
            head = head.getNext();
            if (isEmpty()) { 
                clear(); // Ensure tail is also reset if the cart becomes empty
            }
            System.out.println("Item removed from the cart!");
            return;
        }

        // Case 2: The item is somewhere else in the list
        CartNode current = head;
        CartNode previous = null;

        // Traverse the list to find the item
        while (current != null && current.getProduct().getId() != productId) {
            previous = current;
            current = current.getNext();
        }

        // If the item was found, unlink it
        if (current != null) {
            previous.setNext(current.getNext());
            
            // If we removed the last item, update the tail pointer
            if (current == tail) {
                tail = previous; 
            }
            System.out.println("Item removed from the cart!");
        } else {
            System.out.println("Item not found in the cart!");
        }
    }

    /**
     * Overrides the current quantity of a specific product in the cart.
     *
     * @param productId The unique ID of the product to update.
     * @param newQty    The new exact quantity to set for this product.
     */
    public void updateQuantity(int productId, int newQty) {
        CartNode update = findItem(productId);

        if (update != null) {
            update.setQuantity(newQty);
        } else {
            System.out.println("Item not found in the cart!");
        }
    }

    /**
     * Traverses the linked list to find a specific product by its ID.
     *
     * @param productId The ID to search for.
     * @return The CartNode containing the product if found; otherwise, null.
     */
    public CartNode findItem(int productId) {
        CartNode current = head;

        while (current != null) {
            if (current.getProduct().getId() == productId) {
                return current; 
            }
            current = current.getNext();
        }
        return null; 
    }

    /**
     * Prints a formatted table of all items currently in the cart, 
     * including their individual subtotals.
     */
    public void displayCart() {
        if (isEmpty()) {
            System.out.println("The cart is empty!");
            return;
        }
        
        System.out.println("Id\t\tName\t\tQuantity\t\t\tTotal");

        CartNode current = head;
        while (current != null) { 
            Product p = current.getProduct();
            double subtotal = p.getPrice() * current.getQuantity();
            System.out.println(p.getId() + "\t\t" + p.getName() + "\t\t" + current.getQuantity() + "\t\t\t" + subtotal);
            current = current.getNext();
        }
    }

    /**
     * Calculates the total cost of all items in the cart.
     *
     * @return The total price as a double.
     */
    public double calculateTotal() {
        double total = 0;
        CartNode current = head;

        while (current != null) { 
            total += current.getQuantity() * current.getProduct().getPrice();
            current = current.getNext();
        }

        return total;
    }

    /**
     * Empties the cart completely and clears the undo history.
     */
    public void clear() {
        head = null;
        tail = null;
        undoStack.clear();
    }

    /**
     * Reverts the most recent addition to the cart.
     * If an item was added, this reduces its quantity by the added amount.
     * If reducing the quantity drops it to zero or below, the item is removed entirely.
     *
     * @return The CartNode representing the action that was undone, or null if the stack is empty.
     */
    public CartNode undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return null;
        }

        // Pop the most recent action off the stack
        CartNode lastItem = undoStack.pop();
        int undoProductId = lastItem.getProduct().getId();
        int undoQty = lastItem.getQuantity();

        // Find that item in the actual cart
        CartNode cartItem = findItem(undoProductId);

        if (cartItem != null) {
            // If the cart has less or equal quantity to what was recently added, remove it entirely
            if (cartItem.getQuantity() <= undoQty) {
                removeItem(undoProductId); 
            } 
            // Otherwise, just subtract the recently added amount
            else {
                updateQuantity(undoProductId, cartItem.getQuantity() - undoQty);
                System.out.println("Undo: Reduced quantity of " + lastItem.getProduct().getName() + " by " + undoQty);
            }
        }
        
        return lastItem; 
    }

    /**
     * Counts the number of distinct products (nodes) currently in the cart.
     *
     * @return The size of the linked list.
     */
    public int getSize() {
        int size = 0;
        CartNode current = head;
        
        while (current != null) {
            size++;
            current = current.getNext();
        }

        return size;
    }

    /**
     * Checks if the shopping cart is currently empty.
     *
     * @return true if there are no items in the cart, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * An inner class representing a Last-In-First-Out (LIFO) stack.
     * This stack leverages the CartNode structure to remember items added to the cart
     * in order to support the undo functionality.
     */
    class ItemStack {
        
        /**
         * The top node of the stack.
         */
        private CartNode top; 

        /**
         * Pushes a record of an added item onto the top of the stack.
         *
         * @param product The product that was added.
         * @param qty     The quantity that was added.
         */
        public void push(Product product, int qty) {
            CartNode newItem = new CartNode(product, qty);
            if(isEmpty()){
                top = newItem;
            }
            else{
                newItem.setNext(top);
                top = newItem;
            }
        }

        /**
         * Pops the most recent item record off the top of the stack.
         *
         * @return The CartNode removed from the top of the stack, or null if empty.
         */
        public CartNode pop() {
            if (isEmpty()) {
                return null; 
            }
            CartNode poppedItem = top;
            top = top.getNext(); // Move the top pointer down the stack
            return poppedItem;
        }

        /**
         * Checks if the undo stack is currently empty.
         *
         * @return true if the stack is empty, false otherwise.
         */
        public boolean isEmpty() {
            return top == null;
        }

        /**
         * Clears all history from the undo stack.
         */
        public void clear() {
            top = null;
        }
    }
}