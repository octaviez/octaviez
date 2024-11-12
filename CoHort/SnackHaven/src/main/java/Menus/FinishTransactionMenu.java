package Menus;

import services.InventoryService;
import services.PaymentService;
import services.Logger;
import java.io.IOException;

public class FinishTransactionMenu extends BaseMenu {

    // Fields
    private final InventoryService inventoryService;

    // Constructor
    public FinishTransactionMenu(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void displayMenuTitle() {
        System.out.println("   Finish Transaction Menu");
    }

    @Override
    protected void displayMenuOptions() {
        double currentBalance = inventoryService.getBalance();

        if (currentBalance > 0) {
            System.out.println("Thank you for using the vending machine.");
            System.out.println("Please take your change:");
            String changeMessage = PaymentService.PennyConverter.calculateCoins((int) currentBalance);
            System.out.println(changeMessage);
            Logger.logChange(currentBalance, 0);
            inventoryService.resetBalance();
        } else {
            System.out.println("No balance to return. Thank you for using the vending machine.");
        }
        System.out.println("Press 1 to return to the main menu.");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        if ("1".equals(input)) {
            MainMenu mainMenu = new MainMenu();
            mainMenu.main(new String[]{});
            return true;
        } else {
            System.out.println("Invalid option, please try again.");
            return false;
        }
    }

}
