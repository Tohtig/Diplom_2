import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
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

public class UserLoginTest {
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
    @DisplayName("Логин под существующим пользователем")
    public void loginExistUserReturnRegisterResponse() {
        steps.create(account);
        steps.login(account).assertThat()
                .statusCode(HttpStatus.SC_OK).and()
                .body("success", equalTo(true)).and()
                .extract().body().as(RegisterResponse.class);
    }

    @Test
    @DisplayName("система вернет ошибку, если неправильно указан логин или пароль")
    public void loginIncorrectAccountShowError() {
        String expectMessage = "email or password are incorrect";
        steps.create(account);
        UserAccount wrongAccount = new UserAccount(faker.internet().emailAddress(), account.getPassword(), account.getName());
        testData.add(wrongAccount);
        steps.login(wrongAccount).assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and()
                .body("message", equalTo(expectMessage));
        wrongAccount = new UserAccount(account.getEmail(), faker.internet().password(), account.getName());
        testData.add(wrongAccount);
        steps.login(wrongAccount).assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and()
                .body("message", equalTo(expectMessage));
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
