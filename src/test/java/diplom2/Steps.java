package diplom2;

import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Login;
import model.User;
import model.UserAccount;
import model.orders.Orders;
import org.apache.http.HttpStatus;

import java.util.List;

public class Steps extends BaseHttpClient {
    private final String baseUrl = "https://stellarburgers.nomoreparties.site/api";
//    private final String mock = "http://localhost:8082/api";

    @Step("Авторизация пользователя в системе")
    public ValidatableResponse login(UserAccount account) {
        Login body = new Login(account);
        return doPostRequest(baseUrl + "/auth/login", body);
    }

    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserAccount account) {
        return doPostRequest(baseUrl + "/auth/register", account);
    }

    @Step("Изменение данных о пользователе")
    public ValidatableResponse patchUser(User modify, String accessToken) {
        return doPatchRequest(baseUrl + "/auth/user", modify, accessToken);
    }

    @Step("Изменение данных о пользователе без авторизации")
    public ValidatableResponse patchUser(User modify) {
        return doPatchRequest(baseUrl + "/auth/user", modify);
    }

    @Step("Удаление пользователей")
    public void delete(List<UserAccount> accounts) {
        ValidatableResponse loginResp;
        if (!accounts.isEmpty()) {
            for (UserAccount account : accounts) {
                loginResp = login(account);
                if (loginResp.extract().statusCode() == HttpStatus.SC_OK) {
                    doDeleteRequest(baseUrl + "/auth/user", loginResp.extract().body().jsonPath().getString("accessToken").substring(7));
                }
            }
        }
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(Orders orders) {
        return doPostRequest(baseUrl + "/orders", orders);
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Orders orders, String accessToken) {
        return doPostRequest(baseUrl + "/orders", orders, accessToken);
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse getOrders(String accessToken) {
        return doGetRequest(baseUrl + "/orders", accessToken);
    }

    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrders() {
        return doGetRequest(baseUrl + "/orders");
    }
}
