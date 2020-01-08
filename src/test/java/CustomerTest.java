import models.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void constructorWithName(){
        // Arrange
        String name = "This is a name";
        // Act
        Customer cust = new Customer(name);
        // Assert
        assertEquals(name, cust.getName());
    }

    @Test
    public void constructorNullName(){
        // Arrange
        String name = null;
        // Act
        try {
            Customer cust = new Customer(name);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }

    @Test
    public void constructorEmptyName(){
        // Arrange
        String name = "";
        // Act
        try {
            Customer cust = new Customer(name);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }
}
