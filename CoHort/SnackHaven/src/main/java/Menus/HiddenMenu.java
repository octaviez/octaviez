package Menus;

import models.Product;
import services.InventoryService;
import services.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HiddenMenu extends BaseMenu {

    // Fields
    private final InventoryService inventoryService;

    // Constructor
    public HiddenMenu(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void displayMenuTitle() {
        System.out.println("   Hidden Menu: Sales Log Viewer");
    }

    @Override
    protected void displayMenuOptions() {
        System.out.println("Enter the number of lines you want to view from the sales log:");
        System.out.println("(1) Generate Sales Report");
        System.out.println("(2) Return to Main Menu");
    }

    @Override
    protected boolean handleUserInput(String input) throws IOException {
        switch (input) {
            case "1":
                // Call the generateSalesReport method
                generateSalesReport();
                System.out.println("Sales report generated successfully.");
                return false;  // Stay in the hidden menu after generating the report

            case "2":
                return true;  // Return to main menu

            default:
                try {
                    int numLines = Integer.parseInt(input);
                    if (numLines > 0) {
                        System.out.println("Displaying last " + numLines + " lines from sales log:");
                        Logger.readLastXLines(numLines).forEach(System.out::println);
                    } else {
                        System.out.println("Please enter a positive number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
                return false;
        }
    }


    public void generateSalesReport() {

        String directoryPath = "SalesReports";


        String reportFileName = generateSalesReportFileName(directoryPath);
        String reportFilePath = directoryPath + "/" + reportFileName;


        File salesReportDir = new File(directoryPath);
        if (!salesReportDir.exists()) {
            salesReportDir.mkdirs();
        }


        File reportFile = new File(reportFilePath);

        try (FileWriter fileWriter = new FileWriter(reportFile);
             PrintWriter reportWriter = new PrintWriter(fileWriter)) {


            for (Product product : inventoryService.getInventoryManager().getInventory()) {

                reportWriter.printf("%s | %d%n", product.getName(), product.getSalesCount());
            }


            reportWriter.println();

            // Print total sales at the end of the report
            reportWriter.println("**TOTAL SALES** $" + String.format("%.2f", inventoryService.getTotalSales()));

            // Flush and close the writer
            reportWriter.flush();


            Logger.logSales("Sales report generated: " + reportFilePath, "SalesReports/Log.txt");

            System.out.println("Sales report generated at: " + reportFile.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Error generating sales report", e);
        }
    }


    private String generateSalesReportFileName(String directoryPath) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "SalesReport_" + now.format(formatter) + ".txt";  // Generate unique name for each report
    }
}


