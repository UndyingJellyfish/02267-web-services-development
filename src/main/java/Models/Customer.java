package Models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Account {

    private List<Token> tokens = new ArrayList<>();

    public List<Token> getTokens(){
        return this.tokens;
    }

    public Customer(String name){
        super(name);
    }
}
