
package data;

import models.Product;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManager {

    // Fields
    private final List<Product> inventory; // Use this for the main inventory
    public final String VENDING_MACHINE_DATA = "/vendingmachine.csv";
    private final Scanner userInput;
    private boolean inventoryLoaded = false;


    // Getters & Setters
    public Product getProductByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }


        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        return null;
    }


    public Product getProductByButtonID(String buttonID) {
        for (Product product : inventory) {
            if (product.getButtonID().equals(buttonID)) {
                return product;
            }
        }
        return null;
    }


    // Constructor
    public InventoryManager() {
        this.inventory = new ArrayList<>(); // Initialize inventory
        this.userInput = new Scanner(System.in);

        // loadInventory(VENDING_MACHINE_DATA);
    }

    // Methods

    public void loadInventory(String vendingMachineData) {
        if (!inventory.isEmpty()) {
            return;
        }
        if (inventoryLoaded) {
            return; // Inventory already loaded
        }

        InputStream inventoryStream = getClass().getResourceAsStream(vendingMachineData);
        if (inventoryStream != null) {

            try (Scanner productScanner = new Scanner(inventoryStream)) {

                while (productScanner.hasNext()) {

                    String productData = productScanner.nextLine();

                    String[] productDetails = productData.split("\\|");

                    if (productDetails.length == 4) {
                        try {
                            Product product = new Product(productDetails[0], productDetails[1], Double.parseDouble(productDetails[2]), productDetails[3]);

                            product.setStock(5);

                            inventory.add(product);

                        } catch (NumberFormatException nfe) {
                            System.out.println("Error parsing product price: " + nfe.getMessage());
                        }
                    } else {
                        System.out.println("Invalid product data format: " + productData);
                    }
                    inventoryLoaded = true;
                }
            } catch (Exception e) {
                System.out.println("Error loading inventory: " + e.getMessage());
            }
        } else {
            System.out.println("Inventory file not found.");
        }
    }

    public boolean isInventoryLoaded() {
        return inventoryLoaded;
    }


    public void updateStock(String productName, double newStock) {
        Product product = getProductByName(productName);

        if (product != null) {

            product.setStock(newStock); // Update the stock

        } else {

            System.out.println("Product not found: " + productName);
        }
    }

    public List<Product> getInventory() {
        return inventory;
    }

}