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
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.example.Constants.*;
import static org.example.api.UserApi.*;

public class CreateUserTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание нового уникального пользователя")
    public void createNewUser() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response response = responseSendPostCreateUser(newUser);
        verificationUserSuccessTrue(response);
        UserCreateResponse userCreateResponse = response.body().as(UserCreateResponse.class);
        Assert.assertEquals(USER_EMAIL, userCreateResponse.getUser().getEmail());
        Assert.assertEquals(USER_NAME, userCreateResponse.getUser().getName());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicationUser() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        Response response = responseSendPostCreateUser(newUser);
        verificationUserSuccessFalse(response, "User already exists", SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void createNewUserWithoutEmail() {
        CreateUser newUser = new CreateUser(null, USER_PASSWORD, USER_NAME);
        Response response = responseSendPostCreateUser(newUser);
        verificationUserSuccessFalse(response, "Email, password and name are required fields", SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    public void createNewUserWithoutPassword() {
        CreateUser newUser = new CreateUser(USER_EMAIL, null, USER_NAME);
        Response response = responseSendPostCreateUser(newUser);
        verificationUserSuccessFalse(response, "Email, password and name are required fields", SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    public void createNewUserWithoutName() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, null);
        Response response = responseSendPostCreateUser(newUser);
        verificationUserSuccessFalse(response, "Email, password and name are required fields", SC_FORBIDDEN);
    }

    @After
    public void deleteUser() {
        Login login = new Login(USER_EMAIL, USER_PASSWORD);
        Response authorization = responseSendPostLoginUser(login);
        String accessToken = getAccessToken(authorization);
        if (accessToken != null) {
            Response delete = sendDeleteUser(accessToken);
            verificationUserSuccessTrue(delete, "User successfully removed", SC_ACCEPTED);
        }
    }
}
