import main.models.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void constructorWithName(){
        // Arrange
        String name = "This is a name";
        String cpr = "123";
        // Act
        Customer cust = new Customer(name,cpr);
        // Assert
        assertEquals(name, cust.getName());
    }

    @Test
    public void constructorNullName(){
        // Arrange
        String name = null;
        String cpr = "123";

        // Act
        try {
            Customer cust = new Customer(name,cpr);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }
    @Test
    public void constructorNullCpr(){
        // Arrange
        String name = "navn";
        String cpr = null;

        // Act
        try {
            Customer cust = new Customer(name,cpr);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }

    @Test
    public void constructorEmptyName(){
        // Arrange
        String name = "";
        String cpr = "123";
        // Act
        try {
            Customer cust = new Customer(name, cpr);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }

    @Test
    public void constructorEmptyCpr(){
        // Arrange
        String name = "navn";
        String cpr = "";

        // Act
        try {
            Customer cust = new Customer(name,cpr);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }
}
