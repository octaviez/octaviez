package techelevator;

import org.junit.Test;
import services.PaymentService;

import static org.junit.Assert.*;

public class PaymentServiceTest {


    @Test
    public void calculate_change_test() {
        // Arrange
        int totalAmount = 500;
        int paidAmount = 350;

        // Act
        int change = totalAmount - paidAmount;

        // Assert
        assertEquals(150, change);
    }

    @Test
    public void penny_converter_test() {
        // Arrange
        int pennies = 87;

        // Act
        String result = PaymentService.PennyConverter.calculateCoins(pennies);

        // Assert
        assertTrue(result.contains("Quarters: 3"));

        assertTrue(result.contains("Dimes: 1"));

        assertTrue(result.contains("Nickels: 0"));

        assertTrue(result.contains("Pennies: 2"));
    }
}