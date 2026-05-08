import java.util.ArrayList;
import java.io.*;

public class InventoryManager {
    ArrayList<Product> inventory = new ArrayList<>();

    public void loadFromFile(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            reader.readLine();

            String line = reader.readLine();

            while(line != null){
                String[] arr = line.split(",");

                int id = Integer.parseInt(arr[0]);
                String name = arr[1];
                double price = Double.parseDouble(arr[2]);
                int stock = Integer.parseInt(arr[3]);

                inventory.add(new Product(id, name, price, stock));

                line = reader.readLine();
            }
            
            System.out.println("Inventory loaded successfully!");
            reader.close();
        }
        catch(IOException e){
            System.out.println("An error occurred while loading the inventory: " + e.getMessage());
        }
    }

    public void saveToFile(String filename){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            
            writer.write("id,name,price,stock");
            writer.newLine();

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

    public void addProduct(Product p){
        if(searchById(p.getId()) == null){
            inventory.add(p);
            System.out.println(p.toString() + " added in the inventory!");
        }
        else{
            System.out.println("A product with same id " + p.getId() + " already exists in the inventory!");
        }
    }

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

    public ArrayList<Product> searchByName(String name){
        ArrayList<Product> matchingList = new ArrayList<>();

        String searchString = name.toLowerCase();

        for(int i = 0; i < inventory.size(); i++){
            Product currentProduct = inventory.get(i);

            if(currentProduct.getName().toLowerCase().contains(searchString)){
                matchingList.add(currentProduct);
            }
        }

        return matchingList;
    }

    public void updateStock(int id, int newStock){
        Product update = getProductById(id);
        if(update == null){
            System.out.println("Product not found!");
            return;
        }
        update.setStock(newStock);
    }

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

    public Product getProductById(int id){
        return searchById(id);
    }

    public boolean isAvailable(int id, int requestedQty){
        Product product = getProductById(id);
        if(product != null){
            return product.getStock() >= requestedQty;
        }

        return false;
    }
}

