
package services;

import data.InventoryManager;
import models.Product;

import static services.Logger.eventTime;

public class InventoryService {


    private InventoryManager inventoryManager;
    private String SALES_UPDATE = "SalesReports/Log.txt";
    private int balance;
    private double totalSales = 0.0;

    // Getters & Setters
    public int getBalance() {
        return balance; // Balance in cents
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // Constructor
    public InventoryService() {
        this.balance = 0;
        this.inventoryManager = new InventoryManager();
    }

    // Methods
    public void addToBalance(int amountInCents) {
        this.balance += amountInCents;
    }


    public void resetBalance() {
        this.balance = 0;
    }




    public void dispenseProduct(String productName) {
        Product product = inventoryManager.getProductByName(productName);
        if (product != null && product.getStock() > 0) {
            double productPriceInCents = product.getPrice() * 100;
            if (balance >= productPriceInCents) {
                product.reduceStock(1);

                product.incrementSalesCount();

                inventoryManager.updateStock(productName, product.getStock());

                balance -= (int) productPriceInCents;

                // Update total sales
                addToTotalSales(productPriceInCents);

                // Log purchase and sales
                Logger.logPurchase(product.getName(), product.getButtonID(), productPriceInCents, balance);

                Logger.logSales(String.format("Total Sales as of %s: $%.2f", eventTime(), getTotalSales()), "SalesReports/filePath");

            } else {
                System.out.println("Insufficient balance to purchase " + productName);
            }
        } else {
            System.out.println("SOLD OUT");
        }
    }


    public void addToTotalSales(double productPriceInCents) {
        this.totalSales += productPriceInCents;
    }


    public double getTotalSales() {
        return totalSales / 100.0;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
}