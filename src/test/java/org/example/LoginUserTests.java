package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.user.CreateUser;
import org.example.user.Login;
import org.example.user.UserCreateResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.Constants.*;
import static org.example.api.UserApi.*;

public class LoginUserTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginActiveUser() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        Login login = new Login(USER_EMAIL, USER_PASSWORD);
        Response response = responseSendPostLoginUser(login);
        verificationUserSuccessTrue(response);
        UserCreateResponse userCreateResponse = response.body().as(UserCreateResponse.class);
        Assert.assertEquals(USER_EMAIL, userCreateResponse.getUser().getEmail());
        Assert.assertEquals(USER_NAME, userCreateResponse.getUser().getName());
    }

    @Test
    @DisplayName("Логин с неправильным email пользователя")
    public void userLoginWithInvalidEmail() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        Login login = new Login(WRONG_USER_EMAIL, USER_PASSWORD);
        Response response = responseSendPostLoginUser(login);
        verificationUserSuccessFalse(response, "email or password are incorrect", SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Логин с неправильным password пользователя")
    public void userLoginWithInvalidPassword() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        Login login = new Login(USER_EMAIL, WRONG_USER_PASSWORD);
        Response response = responseSendPostLoginUser(login);
        verificationUserSuccessFalse(response, "email or password are incorrect", SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser() {
        Login login = new Login(Constants.USER_EMAIL, Constants.USER_PASSWORD);
        Response authorization = responseSendPostLoginUser(login);
        String accessToken = getAccessToken(authorization);
        Response delete = sendDeleteUser(accessToken);
        verificationUserSuccessTrue(delete, "User successfully removed", SC_ACCEPTED);
    }
}
