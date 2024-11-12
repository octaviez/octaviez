package techelevator;

import org.junit.Test;
import services.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

public class LoggerTest {

    @Test
    public void log_transaction_test() {
        // Arrange
        String logEntry = "Test transaction entry";
        File logFile = new File(Logger.DEFAULT_LOG_FILE);

        // Act
        Logger.logTransaction(logEntry);

        // Assert
        assertTrue(logFile.exists());
        assertTrue(logFile.length() > 0);
    }

    @Test
    public void log_sales_test() {
        // Arrange
        String salesEntry = "Test sales entry";
        File salesFile = new File(Logger.DEFAULT_SALES_FILE);

        // Act
        Logger.logSales(salesEntry);

        // Assert
        assertTrue(salesFile.exists());
        assertTrue(salesFile.length() > 0);
    }

    @Test
    public void read_last_x_lines_test() throws IOException {
        // Arrange
        Logger.logSales("Sales Entry 1");
        Logger.logSales("Sales Entry 2");

        // Act
        List<String> lines = Logger.readLastXLines(2);

        // Assert
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("Sales Entry 1"));
        assertTrue(lines.get(1).contains("Sales Entry 2"));
    }
}