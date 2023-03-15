package diplom2;

import client.Steps;
import com.github.javafaker.Faker;
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

public class OrderCreateTest {
    private static final Orders ORDERS_WITH_INGREDIENTS = new Orders().setIngredients(new String[]{"61c0c5a71d1f82001bdaaa7a"});
    private static final Orders ORDERS_WITHOUT_INGREDIENTS = new Orders().setIngredients(new String[]{});
    private static final Orders ORDERS_WITH_INGREDIENTS_BAD_HASH = new Orders().setIngredients(new String[]{"badHash"});
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
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    public void CreateOrderWithoutAuthorization() {
        steps.createOrder(ORDERS_WITH_INGREDIENTS).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .body("order.status", equalTo(null))
                .extract().body().as(OrdersResponse.class);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с ингредиентами")
    public void CreateOrderWithAuthorization() {
        steps.createUser(account);
        String accessToken = steps.login(account).extract().body().jsonPath().getString("accessToken").substring(7);
        OrdersResponse ordersResponse = steps.createOrder(ORDERS_WITH_INGREDIENTS, accessToken).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .body("order.status", equalTo("done")).and()
                .extract().body().as(OrdersResponse.class);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void CreateOrderWithoutIngredients() {
        steps.createUser(account);
        String accessToken = steps.login(account).extract().body().jsonPath().getString("accessToken").substring(7);
        steps.createOrder(ORDERS_WITHOUT_INGREDIENTS, accessToken).assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void CreateOrderWithIngredientsBadHash() {
        steps.createUser(account);
        String accessToken = steps.login(account).extract().body().jsonPath().getString("accessToken").substring(7);
        steps.createOrder(ORDERS_WITH_INGREDIENTS_BAD_HASH, accessToken).assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
