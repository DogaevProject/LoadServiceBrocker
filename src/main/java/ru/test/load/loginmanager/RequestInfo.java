package ru.test.load.loginmanager;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestInfo {

    @JsonProperty("LoginRequest")
    public String userLogin;
    @JsonProperty("Project")
    public String solCode;

    public RequestInfo(String userLogin, String solCode) {
        this.userLogin = userLogin;
        this.solCode = solCode;
    }

}

