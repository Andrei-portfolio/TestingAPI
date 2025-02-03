package x_clients.rest_assured.helpers;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import x_clients.rest_assured.entity.AuthRequest;
import x_clients.rest_assured.entity.AuthResponse;

public class AuthHelper {
    public String authAndGetToken(String username, String password) {

        AuthRequest authRequest = new AuthRequest(username, password);

        AuthResponse authResponse = given()
                .basePath("auth/login")
                .body(authRequest)
                .contentType(ContentType.JSON)
                .header("x-client-token", "")
                .when()
                .post()
                .as(AuthResponse.class);

        return authResponse.userToken();
    }
}