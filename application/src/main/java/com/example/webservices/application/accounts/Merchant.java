package com.example.webservices.application.accounts;

 class Merchant extends Account {

    private String cvr;

    public Merchant(String name, String cvr) {
        this(name, cvr, null);
    }

    public Merchant(String name, String cvr, String bankAccountId){
        super(name);
        setCvr(cvr);
        setBankAccountId(bankAccountId);
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

     @Override
     public String getIdentifier() {
         return this.getCvr();
     }
 }
