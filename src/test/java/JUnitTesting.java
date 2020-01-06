import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JUnitTesting {

    @Nested
    @DisplayName("Sample nested tests")
    class NestedClass{

        @Test
        @DisplayName("Example of AssertThrows")
        void test1(){
            assertThrows(ArithmeticException.class, () -> {
                int x = 0/0;
            });
        }

        @Test
        @DisplayName("Empty test which always succeeds")
        void test2(){

        }
    }

    @Test
    @DisplayName("Sample test for Jenkins")
     void testTest(){
        assertEquals(25, Math.pow(5,2),0.00001);
    }


    @Test
    @DisplayName("Fail for Jenkins")
    void failTest(){
        fail("Trying to fail");
    }
}
