package ru.test.load.loginmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class LoginManager {

    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    public UserInfoResponse createUser(RequestInfo requestInfo) {
        logger.debug("Begin user creation");
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.username = "test";
        userInfoResponse.password = "testpass";
        userInfoResponse.uri = "test@test";
        return userInfoResponse;
    }

    public boolean deleteUser(RequestInfo requestInfo) {
        logger.debug("Begin user deletion");

        return true;
    }

}
