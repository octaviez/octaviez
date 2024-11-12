package Menus;

import services.InventoryService;
import java.io.IOException;

public class PurchaseMenu extends BaseMenu {

    private InventoryService inventoryService;

    // Constructor
    public PurchaseMenu(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void displayMenuTitle() {
        System.out.println("   Purchase Menu");
    }

    @Override
    protected void displayMenuOptions() {
        System.out.println("(1) Feed Money");
        System.out.println("(2) Select Product");
        System.out.println("(3) Finish Transaction");
        System.out.println("(4) Back to Main Menu");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        try {
            int option = Integer.parseInt(input);

            switch (option) {
                case 1:
                    new FeedMoneyMenu(inventoryService, this).displayOptions();
                    break;

                case 2:
                    new SelectProductMenu(inventoryService, this).displayOptions();
                    break;

                case 3:
                    new FinishTransactionMenu(inventoryService).displayOptions();
                    break;

                case 4:
                    // Return to main menu
                    return true;

                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        return false;
    }

    // Restart the purchase menu
    public void restartMenu() throws IOException {
        displayOptions();
    }
}
