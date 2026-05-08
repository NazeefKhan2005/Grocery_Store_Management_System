public class CartList {
    private CartNode head;
    private CartNode tail;
    private ItemStack undoStack; 

    public CartList() {
        this.undoStack = new ItemStack(); 
    }


    public void addItem(Product p, int qty) {
        CartNode existingNode = findItem(p.getId());
        
        if (existingNode != null) {
            updateQuantity(p.getId(), existingNode.getQuantity() + qty);
            System.out.println("Item quantity updated in the cart!");
        } else {
            CartNode newNode = new CartNode(p, qty);
            if (head == null) {
                head = newNode;
                tail = head;
            } else {
                tail.setNext(newNode);
                tail = newNode;
            }
            System.out.println("Item added to the cart successfully!");
        }

        undoStack.push(p, qty);
    }


    public void removeItem(int productId) {
        if (isEmpty()) {
            System.out.println("The cart is empty!");
            return;
        }

        if (head.getProduct().getId() == productId) {
            head = head.getNext();
            if (isEmpty()) { 
                clear();
            }
            System.out.println("Item removed from the cart!");
            return;
        }


        CartNode current = head;
        CartNode previous = null;

        while (current != null && current.getProduct().getId() != productId) {
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            previous.setNext(current.getNext());
            
            if (current == tail) {
                tail = previous; 
            }
            System.out.println("Item removed from the cart!");
        } else {
            System.out.println("Item not found in the cart!");
        }
    }


    public void updateQuantity(int productId, int newQty) {
        CartNode update = findItem(productId);

        if (update != null) {
            update.setQuantity(newQty);
        } else {
            System.out.println("Item not found in the cart!");
        }
    }

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

    public double calculateTotal() {
        double total = 0;
        CartNode current = head;

        while (current != null) { 
            total += current.getQuantity() * current.getProduct().getPrice();
            current = current.getNext();
        }

        return total;
    }

    public void clear() {
        head = null;
        tail = null;
    }


    public CartNode undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return null;
        }

        CartNode lastItem = undoStack.pop();
        int undoProductId = lastItem.getProduct().getId();
        int undoQty = lastItem.getQuantity();


        CartNode cartItem = findItem(undoProductId);

        if (cartItem != null) {
            if (cartItem.getQuantity() <= undoQty) {
                removeItem(undoProductId); 
            } 
            else {
                updateQuantity(undoProductId, cartItem.getQuantity() - undoQty);
                System.out.println("Undo: Reduced quantity of " + lastItem.getProduct().getName() + " by " + undoQty);
            }
        }
        
        return lastItem; 
    }

    public int getSize() {
        int size = 0;
        CartNode current = head;
        
        while (current != null) {
            size++;
            current = current.getNext();
        }

        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    class ItemStack {
    private CartNode top; 


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


    public CartNode pop() {
        if (isEmpty()) {
            return null; 
        }
        CartNode poppedItem = top;
        top = top.getNext(); 
        return poppedItem;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void clear() {
        head = null;
        tail = null;
        undoStack.clear();
    }
}
}


