package models;

public class Merchant extends Account {

    private String cvr;

    public Merchant(String name, String cvr){
        super(name);
        setCvr(cvr);
    }

    public String getCvr() {
        return cvr;
    }

    private void setCvr(String cvr) {
        // TODO: Enforce stricter cvr format
        if (cvr == null || cvr.isEmpty()) {
        //if (cvr.length() == 8 ) {
            throw new IllegalArgumentException("provided cvr did not suffice cvr format ");
        }
        this.cvr = cvr;
    }
}
