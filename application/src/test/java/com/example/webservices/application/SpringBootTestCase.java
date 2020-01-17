package com.example.webservices.application;

import com.example.webservices.application.bank.IBank;
import gherkin.deps.com.google.gson.Gson;
import com.example.webservices.application.transfers.PaymentController;
import com.example.webservices.application.exceptions.InvalidTokenException;
import com.example.webservices.application.transfers.PaymentService;
import com.example.webservices.application.transfers.TransactionDto;
import io.cucumber.java.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = PaymentController.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration( classes = Application.class, loader = SpringBootContextLoader.class)
public class SpringBootTestCase {

    @MockBean
    public IBank bank;

    @Before
    public void SpringTest(){
        
    }
/*

@Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void testTransferControllerReturnBadRequest() throws Exception {

        when(paymentService.transfer(any(), any(), any(), any())).thenThrow(new InvalidTokenException());

        TransactionDto s = new TransactionDto();
        s.setAmount(new BigDecimal(10));
        s.setMerchantId(UUID.randomUUID());
        s.setTokenId(UUID.randomUUID());
        s.setDescription("");

        mvc.perform(post("/payment/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(s)))
            .andExpect(status().isBadRequest());

    }

*/

}
