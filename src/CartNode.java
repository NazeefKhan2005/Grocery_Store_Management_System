/**
 * Represents a single node within a linked list implementation of a shopping cart.
 * Each node holds a specific Product, the desired quantity of that product, 
 * and a reference (or link) to the next node in the cart.
 */
public class CartNode {
    
    /**
     * The product item stored in this specific node of the cart.
     */
    private Product product;
    
    /**
     * The number of units of the product that the user wants to purchase.
     */
    private int quantity;
    
    /**
     * A reference to the next CartNode in the linked list. 
     * If this is the last node in the cart, this value will be null.
     */
    private CartNode next;
   
    /**
     * Constructs a new CartNode with the specified product and quantity.
     * The 'next' reference is implicitly set to null upon creation.
     *
     * @param product  The Product object to store in this node.
     * @param quantity The amount of the product being added to the cart.
     */
    public CartNode(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Retrieves the product stored in this node.
     *
     * @return The Product object associated with this node.
     */
    public Product getProduct(){
        return product;
    }

    /**
     * Retrieves the current quantity of the product in this node.
     *
     * @return An integer representing the quantity of the product.
     */
    public int getQuantity(){
        return quantity;
    }

    /**
     * Retrieves the next node in the linked list.
     *
     * @return The next CartNode, or null if this is the end of the list.
     */
    public CartNode getNext(){
        return next;
    }

    /**
     * Updates the product stored in this node.
     *
     * @param p The new Product object to be assigned to this node.
     */
    public void setProduct(Product p){
        product = p;
    }

    /**
     * Updates the quantity of the product in this node.
     *
     * @param qty The new quantity to set.
     */
    public void setQuantity(int qty){
        quantity = qty;
    }

    /**
     * Sets the reference to the next node in the linked list.
     * This is used to link nodes together or break connections within the cart.
     *
     * @param next The CartNode to be set as the next node in the sequence.
     */
    public void setNext(CartNode next){
        this.next = next;
    }
}