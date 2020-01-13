package main.models;

public class Customer extends Account {

    private String cpr;

    public Customer(String name, String cpr){
        super(name);
        setCpr(cpr);
    }

    public String getCpr() {
        return cpr;
    }

    private void setCpr(String cpr) {
        // TODO: Enforce stricter cpr format
        if (cpr == null || cpr.isEmpty()) {
        //if (cpr.length() == 10 || cpr.contains("-") && cpr.length() == 11) {
            throw new IllegalArgumentException("Provided cpr did not suffice cpr format ");
        }

        this.cpr = cpr;
    }
}
