package com.nts.users.DTO.RequestSchema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetOTPSchema {
    @JsonProperty("OTP")
    private String OTP;

    public GetOTPSchema(){}
    public GetOTPSchema(String OTP){
        this.OTP =OTP;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
