package com.example.webservices.application;

import com.example.webservices.library.interfaces.IBank;
import io.cucumber.java.Before;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

//@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = PaymentController.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = Application.class, loader = SpringBootContextLoader.class)
public class SpringBootTestCase {

    @MockBean
    public IBank bank;



    @Before
    public void SpringTest(){
        
    }

}
