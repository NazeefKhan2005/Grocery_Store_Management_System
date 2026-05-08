public class CartNode {
    private Product product;
    private int quantity;
    private CartNode next;
   
    public CartNode(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct(){
        return product;
    }
    public int getQuantity(){
        return quantity;
    }
    public CartNode getNext(){
        return next;
    }

    public void setProduct(Product p){
        product = p;
    }
    public void setQuantity(int qty){
        quantity = qty;
    }
    public void setNext(CartNode next){
        this.next = next;
    }
}
