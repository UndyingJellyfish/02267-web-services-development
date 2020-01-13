import main.models.Merchant;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class MerchantTest {

    @Test
    public void constructorWithName(){
        // Arrange
        String name = "This is a name";
        // Act
        Merchant merch = new Merchant(name);
        // Assert
        assertEquals(name, merch.getName());
    }

    @Test
    public void constructorNullName(){
        // Arrange
        String name = null;
        // Act
        try {
            Merchant merch = new Merchant(name);
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
            Merchant merch = new Merchant(name);
            fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }
}
