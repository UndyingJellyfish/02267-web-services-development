package Models;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private List<Token> tokens = new ArrayList<>();

    public List<Token> getTokens(){
        return this.tokens;
    }
}
