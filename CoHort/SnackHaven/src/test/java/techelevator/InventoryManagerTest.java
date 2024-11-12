package techelevator;

import data.InventoryManager;
import models.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryManagerTest {

    private InventoryManager inventoryManager;

    @Before
    public void setUp() {
        inventoryManager = new InventoryManager();
    }

    @After
    public void tearDown() {
        inventoryManager = null;
    }

    @Test
    public void load_inventory_test() {
        // Arrange
        String vendingMachineData = inventoryManager.VENDING_MACHINE_DATA;

        // Act
        inventoryManager.loadInventory(vendingMachineData);

        // Assert
        assertFalse(inventoryManager.getProductByName("Heavy").getName().isEmpty());
    }

    @Test
    public void get_product_by_name_test() {
        // Arrange
        String productName = "Heavy";
        inventoryManager.loadInventory(inventoryManager.VENDING_MACHINE_DATA);

        // Act
        Product product = inventoryManager.getProductByName(productName);

        // Assert
        assertNotNull(product);
        assertEquals(productName, product.getName());
    }

    @Test
    public void get_product_by_button_id_test() {
        // Arrange
        String buttonID = "A1";
        inventoryManager.loadInventory(inventoryManager.VENDING_MACHINE_DATA);

        // Act
        Product product = inventoryManager.getProductByButtonID(buttonID);

        // Assert
        assertNotNull(product);
        assertEquals(buttonID, product.getButtonID());
    }

}