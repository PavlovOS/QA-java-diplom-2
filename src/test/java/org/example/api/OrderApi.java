package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.order.CreateOrder;
import org.example.order.CreateOrderResponse;
import org.example.order.IngredientInformation;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.Constants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderApi {
    @Step("Send POST create to /api/orders")
    public static void sendPostCreateOrder(CreateOrder newOrder) {
        given().header(HEADER_TYPE, HEADER_JSON).body(newOrder).post(ORDERS)
                .then().body("success", equalTo(true)).statusCode(SC_OK);
    }

    @Step("Send POST create to /api/orders")
    public static void sendPostCreateOrder(CreateOrder newOrder, String accessToken) {
        given().header(HEADER_TYPE, HEADER_JSON).header(HEADER_AUTHORIZATION, accessToken)
                .body(newOrder).post(ORDERS)
                .then().body("success", equalTo(true)).statusCode(SC_OK);
    }

    @Step("Send POST create to /api/orders")
    public static Response responseSendPostCreateOrder(CreateOrder newOrder) {
        return given().header(HEADER_TYPE, HEADER_JSON).body(newOrder).post(ORDERS);
    }

    @Step("Send POST create to /api/orders")
    public static Response responseSendPostCreateOrder(CreateOrder newOrder, String accessToken) {
        return given().header(HEADER_TYPE, HEADER_JSON).header(HEADER_AUTHORIZATION, accessToken)
                .body(newOrder).post(ORDERS);
    }

    @Step("Send GET to /api/orders")
    public static Response responseSendGetOrder() {
        return given().get(ORDERS);
    }

    @Step("Send GET to /api/orders")
    public static Response responseSendGetOrder(String accessToken) {
        return given().header(HEADER_AUTHORIZATION, accessToken).get(ORDERS);
    }

    @Step("Request with body and status verification")
    public static void verificationOrderSuccessTrue(Response response) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Request with body and status verification")
    public static void verificationOrderSuccessFalse(Response response, String message, int statusCode) {
        response.then().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo(message))
                .statusCode(statusCode);
    }

    @Step("Verification ingredients in the order")
    public static void verificationIngredientsForCreateOrder(Response response, String[] ingredientsForOrder) {
        List<IngredientInformation> ingredientList = response.body().as(CreateOrderResponse.class).getOrder().getIngredients();
        String[] ingredientsInOrder = new String[ingredientList.size()];
        for (int i = 0; i < ingredientList.size(); i++) {
            ingredientsInOrder[i] = ingredientList.get(i).get_id();
        }
        Assert.assertArrayEquals(ingredientsForOrder, ingredientsInOrder);
    }
}
