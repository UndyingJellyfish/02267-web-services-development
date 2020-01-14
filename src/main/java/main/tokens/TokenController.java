package main.tokens;

import main.models.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenManager tokenManager;


    public TokenController(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Token> requestTokens(@RequestBody RequestTokenDto dto) {
        return tokenManager.RequestTokens(dto.getCustomerId(),dto.getAmount() );
    }
}
