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

public class CreateOrderTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание нового заказа с ингредиентами авторизованным пользователем")
    public void createNewOrderWithAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseUser);
        CreateOrder newOrder = new CreateOrder(INGREDIENTS);
        Response responseOrder = responseSendPostCreateOrder(newOrder, accessToken);
        verificationOrderSuccessTrue(responseOrder);
        verificationIngredientsForCreateOrder(responseOrder, INGREDIENTS);
    }

    @Test
    @DisplayName("Создание нового заказа с ингредиентами без авторизации")
    public void createNewOrderWithoutAuth() {
        CreateOrder newOrder = new CreateOrder(INGREDIENTS);
        Response responseOrder = responseSendPostCreateOrder(newOrder);
        verificationOrderSuccessTrue(responseOrder);
    }

    @Test
    @DisplayName("Создание нового заказа без ингредиентов авторизованным пользователем")
    public void createNewOrderWithoutIngredients() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseUser);
        String[] ingredients = new String[]{};
        CreateOrder newOrder = new CreateOrder(ingredients);
        Response responseOrder = responseSendPostCreateOrder(newOrder, accessToken);
        verificationOrderSuccessFalse(responseOrder, "Ingredient ids must be provided", SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание нового заказа с неверным хешем ингредиентов авторизованным пользователем")
    public void createNewOrderWithWrongIngredients() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseUser);
        String[] ingredients = new String[]{"61c0c2a71d1d82003bdaaa70", "61c9c5a71d1f82001bdaga6d"};
        CreateOrder newOrder = new CreateOrder(ingredients);
        Response responseOrder = responseSendPostCreateOrder(newOrder, accessToken);
        responseOrder.then().statusCode(SC_INTERNAL_SERVER_ERROR);
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
