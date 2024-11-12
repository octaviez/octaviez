package Menus;

import models.Product;
import services.InventoryService;

import java.io.IOException;

public class SelectProductMenu extends BaseMenu {

    private final InventoryService inventoryService;
    private final PurchaseMenu purchaseMenu;

    // Constructor
    public SelectProductMenu(InventoryService inventoryService, PurchaseMenu purchaseMenu) {
        this.inventoryService = inventoryService;
        this.purchaseMenu = purchaseMenu;
    }

    @Override
    protected void displayMenuTitle() {
        System.out.println("   Select Product Menu");
    }

    @Override
    protected void displayMenuOptions() {
        inventoryService.getInventoryManager().loadInventory(inventoryService.getInventoryManager().VENDING_MACHINE_DATA);


        for (Product product : inventoryService.getInventoryManager().getInventory()) {
            System.out.printf("Name: %s, Price: $%.2f, Stock: %s, Button ID: %s%n",
                    product.getName(), product.getPrice(), product.getStock(), product.getButtonID());
        }

        System.out.println("Current Balance: $" + String.format("%.2f", inventoryService.getBalance() / 100.0));
        System.out.println("Select a product (Enter slot number or name):");
        System.out.println("(1) Return to Purchase Menu");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        if ("1".equals(input)) {
            purchaseMenu.restartMenu();
            return true;
        }

        if (input == null || input.isEmpty()) {
            System.out.println("Invalid input. Please enter a valid product code.");
            displayOptions();
            return false;
        }


        Product selectedProduct = inventoryService.getInventoryManager().getProductByButtonID(input);

        if (selectedProduct == null) {
            selectedProduct = inventoryService.getInventoryManager().getProductByName(input);
        }

        if (selectedProduct != null && selectedProduct.getStock() > 0) {
            double productPrice = selectedProduct.getPrice();
            double currentBalance = inventoryService.getBalance() / 100.0;

            if (currentBalance >= productPrice) {

                inventoryService.dispenseProduct(selectedProduct.getName());


                System.out.printf("Purchased %s for $%.2f%n", selectedProduct.getName(), productPrice);
                System.out.println("Remaining Balance: $" + String.format("%.2f", inventoryService.getBalance() / 100.0));

                printDispenseMessage(selectedProduct);

            } else {
                System.out.printf("Insufficient balance to purchase %s. Price: $%.2f, Balance: $%.2f%n",
                        selectedProduct.getName(), productPrice, currentBalance);
            }
        } else {
            System.out.println("Invalid product code or product is out of stock.");
        }

        displayOptions();
        return false;
    }

    private void printDispenseMessage(Product product) {
        switch (product.getType()) {
            case "Drink":
                System.out.println("Glug Glug, Yum!");
                break;
            case "Chip":
                System.out.println("Crunch Crunch, Yum!");
                break;
            case "Candy":
                System.out.println("Munch Munch, Yum!");
                break;
            case "Gum":
                System.out.println("Chew Chew, Yum!");
                break;
            default:
                System.out.println("Enjoy your snack!");
                break;
        }
    }
}
