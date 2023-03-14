package diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.RegisterResponse;
import model.User;
import model.UserAccount;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(JUnitParamsRunner.class)
public class UserModifyAccountTest {
    private static final String AUTH_ERROR = "You should be authorised";
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

    public User[] testData() {
        return new User[]{
                new User().setEmail(faker.internet().emailAddress()).setName(faker.name().firstName()),
                new User().setEmail(faker.internet().emailAddress()),
                new User().setName(faker.name().firstName()),
                new User()

        };
    }

    @Test
    @Parameters(method = "testData")
    @DisplayName("Изменение данных пользователя с авторизацией и без авторизации")
    public void modifyAnyFieldSuccessful(User user) {
        steps.createUser(account);
        String accessToken = steps.login(account).extract().body().jsonPath().getString("accessToken").substring(7);
        if (user.getEmail() != null) {
            account.setEmail(user.getEmail());
        }
        steps.patchUser(user).assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo(AUTH_ERROR));
        steps.patchUser(user, accessToken).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .extract().body().as(RegisterResponse.class);
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
