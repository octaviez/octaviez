package Menus;

import services.InventoryService;
import services.Logger;

import java.io.IOException;

public class FeedMoneyMenu extends BaseMenu {

    // Fields
    private final InventoryService inventoryService;
    private final PurchaseMenu purchaseMenu;
    private String LOG_FILE = "src/Log.txt";

    // Constructor
    public FeedMoneyMenu(InventoryService inventoryService, PurchaseMenu purchaseMenu) {
        this.inventoryService = inventoryService;
        this.purchaseMenu = purchaseMenu;
    }

    @Override
    protected void displayMenuTitle() {
        System.out.println("   Feed Money Menu");
    }

    @Override
    protected void displayMenuOptions() {
        System.out.println("Current Balance: $" + String.format("%.2f", inventoryService.getBalance() / 100.0));
        System.out.println("Feed Money Amount:");
        System.out.println("(1) $1");
        System.out.println("(2) $5");
        System.out.println("(3) $10");
        System.out.println("(4) Return to Purchase Menu");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        switch (input) {
            case "1":
                feedMoney(100);  // Add $1 in cents
                break;
            case "2":
                feedMoney(500);  // Add $5 in cents
                break;
            case "3":
                feedMoney(1000); // Add $10 in cents
                break;
            case "4":
                purchaseMenu.restartMenu();
                return true;  // Exit the feed money menu
            default:
                System.out.println("Please enter a valid option.");
                break;


        }
        displayOptions();
        return false;
    }

     void feedMoney(int amountInCents) throws IOException {
        double previousBalance = inventoryService.getBalance();

        inventoryService.addToBalance(amountInCents);

        System.out.println("Money fed successfully. New Balance: $" + String.format("%.2f", inventoryService.getBalance() / 100.0));

         Logger.logFeedMoney(previousBalance, inventoryService.getBalance());
    }
}
