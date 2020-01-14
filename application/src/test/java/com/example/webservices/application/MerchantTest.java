package com.example.webservices.application;

import com.example.webservices.library.models.Merchant;
import org.junit.Assert;
import org.junit.Test;


public class MerchantTest {

    @Test
    public void constructorWithName(){
        // Arrange
        String name = "This is a name";
        // Act
        Merchant merch = new Merchant(name, "12345678");
        // Assert
        Assert.assertEquals(name, merch.getName());
    }

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
