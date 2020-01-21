package com.example.webservices.accounts;

import com.example.webservices.accounts.models.Merchant;
import org.junit.Assert;
import org.junit.Test;


public class MerchantTest {

    /**
     * @author s164395
     */
    @Test
    public void constructorWithName(){
        // Arrange
        String name = "This is a name";
        // Act
        Merchant merch = new Merchant(name, "12345678");
        // Assert
        Assert.assertEquals(name, merch.getName());
    }

    /**
     * @author s164395
     */
    @Test
    public void constructorNullName(){
        // Arrange
        String name = null;
        // Act
        try {
            Merchant merch = new Merchant(name, "12345678");
            Assert.fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }

    /**
     * @author s164395
     */
    @Test
    public void constructorEmptyName(){
        // Arrange
        String name = "";
        // Act
        try {
            Merchant merch = new Merchant(name,"12345678");
            Assert.fail();

        }catch(IllegalArgumentException e){
            // Expected
        }
    }
}
