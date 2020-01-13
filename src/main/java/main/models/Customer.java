package main.models;

public class Customer extends Account {

    public final String cpr;
    public Customer(String name, String cpr){
        super(name);
        if(cpr == null || cpr.isEmpty()){
            throw new IllegalArgumentException("cpr must be not null");
        }
        this.cpr = cpr;
    }
}
