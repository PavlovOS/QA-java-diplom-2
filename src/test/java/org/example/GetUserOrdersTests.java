package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.order.CreateOrder;
import org.example.user.CreateUser;
import org.example.user.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.Constants.*;
import static org.example.api.OrderApi.*;
import static org.example.api.UserApi.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class GetUserOrdersTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getOrdersFromAuthUser() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseUser);
        CreateOrder newOrder = new CreateOrder(INGREDIENTS);
        sendPostCreateOrder(newOrder, accessToken);
        Response response = responseSendGetOrder(accessToken);
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("orders.size()", greaterThan(0))
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованного пользователя")
    public void getOrdersFromUnAuthUser() {
        CreateOrder newOrder = new CreateOrder(INGREDIENTS);
        sendPostCreateOrder(newOrder);
        Response response = responseSendGetOrder();
        verificationOrderSuccessFalse(response, "You should be authorised", SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser() {
        Login login = new Login(Constants.USER_EMAIL, Constants.USER_PASSWORD);
        Response authorization = responseSendPostLoginUser(login);
        String accessToken = getAccessToken(authorization);
        if (accessToken != null) {
            Response delete = sendDeleteUser(accessToken);
            verificationUserSuccessTrue(delete, "User successfully removed", SC_ACCEPTED);
        }
    }
}
