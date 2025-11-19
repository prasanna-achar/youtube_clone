package com.nts.users.DTO.RequestSchema;

public class ForgotPasswordSchema {
    private String email;

    public ForgotPasswordSchema(){}

    public ForgotPasswordSchema(String email){
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
