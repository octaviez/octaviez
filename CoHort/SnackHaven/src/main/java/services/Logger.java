package services;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    // STATIC MEMBERS
    public static final String DEFAULT_LOG_FILE = "SalesReports/Log.txt";         // Default log file path
    public static final String DEFAULT_SALES_FILE = "SalesReports/Log.txt";       // Default sales log file path
    public static final String DEFAULT_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";   // Default time format
    public static final String DEFAULT_ENTRY_FORMAT = "%s %s\n";     // Default log entry format

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);  // Formatter for timestamp
    private final InventoryService inventoryService;

    private String customLogFile;
    private File customLog;

    // Getters & Setters
    public String getCustomLogFile() {
        return customLogFile;
    }

    public void setCustomLogFile(String customLogFile) {
        this.customLogFile = customLogFile;
        this.customLog = new File(customLogFile);
    }

    // Constructor
    public Logger(InventoryService inventoryService, String logFilePath) {
        this.inventoryService = inventoryService;
        this.customLogFile = logFilePath;
        this.customLog = new File(customLogFile);
    }

    // STATIC METHODS

    // Reads the last X lines of the log file
    public static List<String> readLastXLines(int numLines) throws IOException {
        List<String> lines = new ArrayList<>();
        String filePath = "SalesReports/Log.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        int start = Math.max(0, lines.size() - numLines);
        return lines.subList(start, lines.size());
    }


    public static void logTransaction(double amountInDollars, double oldBalance, double newBalance) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

        String formattedDate = now.format(formatter);

        String logEntry = String.format("%s PURCHASED: $%.2f | Old Balance: $%.2f | New Balance: $%.2f",
                formattedDate, amountInDollars, oldBalance, newBalance);

        try (FileWriter fileWriter = new FileWriter(DEFAULT_LOG_FILE, true);

             PrintWriter logWriter = new PrintWriter(fileWriter)) {
            logWriter.println(logEntry);  // Write log entry
        } catch (IOException e) {
            throw new RuntimeException("Error logging transaction", e);
        }
    }

    public static void logSales(String logEntry, String filePath) {
        filePath = "Log.txt";
        File logFile = new File(filePath);

        try (FileWriter fileWriter = new FileWriter(logFile, true);  // Append to the sales log
             PrintWriter printLog = new PrintWriter(fileWriter)) {

            // Log the entry with timestamp and format
            printLog.printf(DEFAULT_ENTRY_FORMAT, eventTime(), logEntry);

            // Flush and close the print log
            try {
                printLog.flush();
            } finally {
                printLog.close();
            }

        } catch (IOException e) {
            System.err.println("Error logging sales: " + e.getMessage());
        }
    }


    public static void logFeedMoney(double amountInCents, double newBalanceInCents) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        String formattedDate = now.format(formatter);

        // Convert amounts to dollars by dividing by 100
        String logEntry = String.format("%s FEED MONEY: $%.2f $%.2f", formattedDate, amountInCents / 100.0, newBalanceInCents / 100.0);

        try (FileWriter fileWriter = new FileWriter(DEFAULT_LOG_FILE, true);
             PrintWriter logWriter = new PrintWriter(fileWriter)) {
            logWriter.println(logEntry);
        } catch (IOException e) {
            throw new RuntimeException("Error logging feed money", e);
        }
    }


    public static void logTransaction(String logEntry) {
        try (FileWriter fileWriter = new FileWriter(DEFAULT_LOG_FILE, true);
             PrintWriter printLog = new PrintWriter(fileWriter)) {
            //inputting price, user selection, and current/new balance
            printLog.printf(DEFAULT_ENTRY_FORMAT, eventTime(), logEntry);
            try {
                printLog.flush();
            } finally {
                printLog.close();
            }
        } catch (IOException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }
    }

    public static void logPurchase(String productName, String slot, double amountSpentInCents, double newBalanceInCents) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

        String formattedDate = now.format(formatter);


        String logEntry = String.format("%s %s %s $%.2f $%.2f", formattedDate, productName, slot, amountSpentInCents / 100.0, newBalanceInCents / 100.0);

        try (FileWriter fileWriter = new FileWriter(DEFAULT_LOG_FILE, true);
             PrintWriter logWriter = new PrintWriter(fileWriter)) {
            logWriter.println(logEntry);
        } catch (IOException e) {
            throw new RuntimeException("Error logging purchase", e);
        }
    }


    public static void logChange(double amountGiven, double newBalance) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

        String formattedDate = now.format(formatter);


        String logEntry = String.format("%s GIVE CHANGE: $%.2f $%.2f", formattedDate, amountGiven / 100, newBalance);


        try (FileWriter fileWriter = new FileWriter(DEFAULT_LOG_FILE, true);
             PrintWriter logWriter = new PrintWriter(fileWriter)) {
            logWriter.println(logEntry);
        } catch (IOException e) {
            throw new RuntimeException("Error logging change", e);
        }
    }


    // Generates a timestamp for events
    static String eventTime() {

        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

}
