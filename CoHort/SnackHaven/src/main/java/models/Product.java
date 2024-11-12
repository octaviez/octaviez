
package models;

public class Product {

    public String name;
    private final String buttonID;
    private final double price;
    private final String type;
    private double stock;
    private int salesCount;

    // Getters & Setters
    public String getName() {
        return name;
    }


    public String getButtonID() {
        return buttonID;
    }


    public double getPrice() {
        return price;
    }


    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getType() {
        return type;
    }
    public void incrementSalesCount() {
        this.salesCount++;
    }

    public int getSalesCount() {
        return salesCount;
    }



    public void reduceStock(int amount) {
        this.stock -= amount;
        incrementSalesCount();  // Increment sales count when stock is reduced
    }

    // Constructor
    public Product(String buttonID, String name, double price, String type) {
        this.buttonID = buttonID;
        this.name = name;
        this.price = price;
        this.type = type;
    }


    public void reduceStock(double amount) {
        this.stock -= amount;
    }
}
