package client;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class BaseHttpClient {
    private final String JSON = "application/json";

    private final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));

    protected Response doGetRequest(String uri) {
        return given().config(config)
                .header("Content-Type", JSON)
                .get(uri);
    }

    protected ValidatableResponse doPostRequest(String uri, Object body) {
        return given().config(config)
                .header("Content-Type", JSON)
                .body(body)
                .post(uri).then();
    }

    protected ValidatableResponse doPatchRequest(String uri, Object body) {
        return given().config(config)
                .header("Content-Type", JSON)
                .body(body)
                .patch(uri).then();
    }

    protected ValidatableResponse doPatchRequest(String uri, Object body, String accessToken) {
        return given().config(config)
                .header("Content-Type", JSON)
                .auth().oauth2(accessToken)
                .body(body)
                .patch(uri).then();
    }

    protected void doDeleteRequest(String uri, String accessToken) {
        given().config(config)
                .header("Content-Type", JSON)
                .auth().oauth2(accessToken)
                .get(uri);
    }
}
