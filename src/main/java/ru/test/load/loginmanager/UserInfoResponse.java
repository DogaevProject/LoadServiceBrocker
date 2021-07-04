package ru.test.load.loginmanager;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {

    @JsonProperty("Login")
    public String username;
    @JsonProperty("Password")
    public String password;
    @JsonProperty("Uri")
    public String uri;

}
