package Menus;

import services.InventoryService;
import java.io.IOException;

public class DisplayMenu extends BaseMenu {

    private InventoryService inventoryService;

    // Constructor
    public DisplayMenu(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void displayMenuTitle() {
        banner();
        System.out.println("   Vending Machine - Items");
    }

    @Override
    public void displayMenuOptions() {
        // Display all items in the vending machine
        inventoryService.getInventoryManager().loadInventory(inventoryService.getInventoryManager().VENDING_MACHINE_DATA);

        System.out.println("Items displayed below:");

        inventoryService.getInventoryManager().getInventory().forEach(product -> {
            System.out.printf("%s | Price: $%.2f | Stock: %.0f%n", product.getName(), product.getPrice(), product.getStock());
        });

        // Prompt user to return to the main menu
        System.out.println("\nPress 1 to return to the Main Menu.");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        if ("1".equals(input)) {
            // Return to main menu
            return true;
        } else {

            System.out.println("Invalid option. Please press 1 to return to the Main Menu.");
            return false;
        }
    }
}
