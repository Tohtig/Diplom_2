import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Login;
import model.User;
import model.UserAccount;
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
    public ValidatableResponse create(UserAccount account) {
        return doPostRequest(baseUrl + "/auth/register", account);
    }

    @Step("Изменение данных о пользователе")
    public ValidatableResponse patch(User modify, String accessToken) {
        return doPatchRequest(baseUrl + "/auth/user", modify, accessToken);
    }

    @Step("Изменение данных о пользователе")
    public ValidatableResponse patch(User modify) {
        return doPatchRequest(baseUrl + "/auth/user", modify);
    }

    @Step("Удаление пользователей")
    public void delete(List<UserAccount> accounts) {
        ValidatableResponse loginResp;
        if(!accounts.isEmpty()) {
            for (UserAccount account: accounts) {
                loginResp = login(account);
                if (loginResp.extract().statusCode() == HttpStatus.SC_OK) {
                    doDeleteRequest(baseUrl + "/auth/user", loginResp.extract().body().jsonPath().getString("accessToken").substring(7));
                }
            }
        }
    }
}
