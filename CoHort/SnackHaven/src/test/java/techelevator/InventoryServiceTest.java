package techelevator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.InventoryService;

import static org.junit.Assert.*;

public class InventoryServiceTest {

    private InventoryService inventoryService;

    @Before
    public void setUp() {
        inventoryService = new InventoryService();
    }

    @After
    public void tearDown() {
        inventoryService = null;
    }

    @Test
    public void add_to_balance_v1() {
        // Arrange
        double initialBalance = 1000;

        // Act
        inventoryService.addToBalance(((int)initialBalance));

        // Assert
        assertEquals(initialBalance, inventoryService.getBalance(), 0.01);
    }

    @Test
    public void reset_balance_test() {
        // Arrange
        inventoryService.addToBalance(500);

        // Act
        inventoryService.resetBalance();

        // Assert
        assertEquals(0, inventoryService.getBalance());
    }

   /* @Test
    public void dispense_product_test() {
        // Arrange
        inventoryService.addToBalance(400);

        // Act
        inventoryService.dispenseProduct("Potato Crisps");

        // Assert
        assertEquals(95, inventoryService.getBalance());
    }
    */


}