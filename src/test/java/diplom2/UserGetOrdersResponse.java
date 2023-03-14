package diplom2;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.junit4.DisplayName;
import model.UserAccount;
import model.orders.Orders;
import model.orders.OrdersResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserGetOrdersResponse {
    private static final String AUTH_ERROR = "You should be authorised";
    private static final Orders ORDERS_WITH_INGREDIENTS = new Orders().setIngredients(new String[]{"61c0c5a71d1f82001bdaaa7a",
            "61c0c5a71d1f82001bdaaa6c",
            "61c0c5a71d1f82001bdaaa71"});
    private final Faker faker = new Faker(new Locale("en"));
    private final Steps steps = new Steps();
    private UserAccount account;
    private List<UserAccount> testData;

    @Before
    public void setUp() {
        testData = new ArrayList<>();
        account = new UserAccount(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        testData.add(account);
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void GetOrdersWithoutAuthorization() {
        steps.createOrder(ORDERS_WITH_INGREDIENTS).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true));
        steps.getOrders().assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo(AUTH_ERROR));
    }

    @Test
    @DisplayName("Получение заказов с авторизацией")
    public void CreateOrderWithAuthorization() {
        steps.createUser(account);
        String accessToken = steps.login(account).extract().body().jsonPath().getString("accessToken").substring(7);
        String orderId = steps.createOrder(ORDERS_WITH_INGREDIENTS, accessToken)
                .body("order.status", equalTo("done")).and()
                .extract().body().as(OrdersResponse.class).getOrder().get_id();

        OrdersResponse ordersResponse = steps.getOrders(accessToken).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .body("orders[0]._id", equalTo(orderId))
                .extract().body().as(OrdersResponse.class);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        System.out.println(gson.toJson(ordersResponse));
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
