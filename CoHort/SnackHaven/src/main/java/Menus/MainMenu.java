package Menus;

import services.InventoryService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {

    static String banner = "*********************************************";

    public interface Menu {
        void displayOptions() throws IOException;
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        InventoryService inventoryService = new InventoryService();

        System.out.println(banner);
        System.out.println("   Welcome to Snack Haven vending");
        System.out.println(banner);

        boolean exit = false;

        while (!exit) {
            System.out.println("Please choose one of the following options: \n(1) Display Vending Machine Items\n(2) Purchase\n(3) Exit");
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    new DisplayMenu(inventoryService).displayOptions();
                    break;
                case "2":
                    new PurchaseMenu(inventoryService).displayOptions();
                    break;
                case "3":
                    exit = true;
                    System.out.println("Thank you for using Snack Haven vending machine.");
                    System.exit(0);
                    break;
                case "4":
                    new HiddenMenu(inventoryService).displayOptions();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
