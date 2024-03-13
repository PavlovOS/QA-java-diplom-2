package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.user.CreateUser;
import org.example.user.Login;
import org.example.user.UserCreateResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.Constants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserApi {
    @Step("Send POST create to /api/auth/register")
    public static void sendPostCreateUser(CreateUser newUser) {
        given().header(HEADER_TYPE, HEADER_JSON).body(newUser).post(CREATE_USER)
                .then().body("success", equalTo(true)).statusCode(SC_OK);
    }

    @Step("Get response to send POST create to /api/auth/register")
    public static Response responseSendPostCreateUser(CreateUser newUser) {
        return given()
                .header(HEADER_TYPE, HEADER_JSON)
                .body(newUser)
                .post(CREATE_USER);
    }

    @Step("Request with body and status verification")
    public static void verificationUserSuccessTrue(Response response) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Request with body and status verification")
    public static void verificationUserSuccessTrue(Response response, String message, int statusCode) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("message", equalTo(message))
                .statusCode(statusCode);
    }

    @Step("Request with body and status verification")
    public static void verificationUserSuccessFalse(Response response, String message, int statusCode) {
        response.then().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo(message))
                .statusCode(statusCode);
    }

    @Step("Get response to send POST login to /api/auth/login")
    public static Response responseSendPostLoginUser(Login login) {
        return given()
                .header(HEADER_TYPE, HEADER_JSON)
                .body(login)
                .post(LOGIN_USER);
    }

    @Step("Get user accessToken")
    public static String getAccessToken(Response response) {
        return response.body().as(UserCreateResponse.class).getAccessToken();
    }


    @Step("Get response to send PATCH to /api/auth/user")
    public static Response responseSendPatchChangUser(CreateUser newUser) {
        return given().header(HEADER_TYPE, HEADER_JSON).body(newUser).patch(USER);
    }

    @Step("Get response to send PATCH to /api/auth/user")
    public static Response responseSendPatchChangUser(CreateUser newUser, String accessToken) {
        return given().header(HEADER_TYPE, HEADER_JSON).header(HEADER_AUTHORIZATION, accessToken)
                .body(newUser).patch(USER);
    }

    @Step("Get response to send PATCH to /api/auth/user")
    public static void sendPatchChangUserToDefault(CreateUser user, String accessToken) {
        given().header(HEADER_TYPE, HEADER_JSON).header(HEADER_AUTHORIZATION, accessToken)
                .body(user).patch(USER)
                .then().body("success", equalTo(true)).statusCode(SC_OK);
    }

    @Step("Request with body and status verification")
    public static void verificationChangTrue(Response response, String email, String name) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .statusCode(SC_OK);
    }

    @Step("Deleting a user")
    public static Response sendDeleteUser(String accessToken) {
        return given().header(HEADER_AUTHORIZATION, accessToken).delete(USER);
    }
}
