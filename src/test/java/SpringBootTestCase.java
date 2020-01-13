
import gherkin.deps.com.google.gson.Gson;
import main.Program;
import main.controllers.PaymentController;
import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.services.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PaymentController.class)
@ContextConfiguration( classes = Program.class)
//@SpringBootTest(classes = Program.class)
public class SpringBootTestCase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void testtest() throws Exception {

        when(paymentService.transfer(any(), any(), any(), any())).thenThrow(new InvalidTokenException());

        PaymentController.TransactionDto s = new PaymentController.TransactionDto();
        s.setAmount(new BigDecimal(10));
        s.setMerchantId(UUID.randomUUID());
        s.setTokenId(UUID.randomUUID());
        s.setDescription("");

        mvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(s)))
            .andExpect(status().isBadRequest());

    }

}
