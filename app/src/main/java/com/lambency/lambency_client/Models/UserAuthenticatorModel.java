package com.lambency.lambency_client.Models;

/**
 * Created by kevin on 2/15/2018.
 */

public class UserAuthenticatorModel {

    public enum Status{
        SUCCESS,NON_UNIQUE_EMAIL,NON_DETERMINANT_ERROR

    }

    private Status status;
    private String oAuthCode;


    UserAuthenticatorModel(Status s, String oAuthCode){
        this.status = s;
        this.oAuthCode = oAuthCode;
    }


    public Status getStatus() {
        return status;
    }

    public String getoAuthCode() {
        return oAuthCode;
    }

}
