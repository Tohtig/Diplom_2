import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.RegisterResponse;
import model.UserAccount;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

public class UserCreateTest {
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
    @DisplayName("создать уникального пользователя")
    public void createNewUserReturnBodyWithOk() {
        steps.create(account).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .extract().body().as(RegisterResponse.class);
    }

    @Test
    @DisplayName("создать пользователя который уже зарегистрирован")
    public void createIdenticalAccountsForbidden() {
        ValidatableResponse createFirst = steps.create(account);
        int statusCode;
        statusCode = createFirst.extract().statusCode();
        assertThat("Пользователь создан. Код 200", statusCode, equalTo(HttpStatus.SC_OK));
        ValidatableResponse createSecond = steps.create(account);
        statusCode = createSecond.extract().statusCode();
        assertNotEquals("Статус код не должен быть 201", statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей")
    public void createFieldlessReturnsError() {
        String expectMessage = "Email, password and name are required fields";
        account = new UserAccount();
        testData.add(account);
        account.setEmail(faker.internet().emailAddress());
        account.setName(faker.name().firstName());
        steps.create(account).assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN).and()
                .body("message", equalTo(expectMessage));
        account = new UserAccount();
        testData.add(account);
        account.setPassword(faker.internet().password());
        account.setName(faker.name().firstName());
        steps.create(account).assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN).and()
                .body("message", equalTo(expectMessage));
        account = new UserAccount();
        testData.add(account);
        account.setEmail(faker.internet().emailAddress());
        account.setPassword(faker.internet().password());
        steps.create(account).assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN).and()
                .body("message", equalTo(expectMessage));
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
