package x_clients.rest_assured.helpers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.http.ContentType;
import x_clients.rest_assured.entity.AuthRequest;
import x_clients.rest_assured.entity.AuthResponse;
import x_clients.rest_assured.entity.CreateCompanyRequest;
import x_clients.rest_assured.entity.CreateCompanyResponse;

public class ApiCompanyHelper {

    private AuthHelper authHelper;

    public ApiCompanyHelper(){
        authHelper = new AuthHelper();
    }
    public CreateCompanyResponse createCompany(){
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Entity company", "with entity");

        return given()  // ДАНО:
                .basePath("company")
                .body(createCompanyRequest)
                .contentType(ContentType.JSON)
                .when()     // КОГДА
                .post() // ШЛЕШЬ ПОСТ ЗАПРОС
                .then()
                .statusCode(201)
                .body("id", is(greaterThan(0)))
                .extract().as(CreateCompanyResponse.class);
    }


    public int deleteCompany(int id) {

        return given()  // ДАНО:
                .basePath("company/delete")
                .when()     // КОГДА
                .get("{id}", id) // ШЛЕШЬ ПОСТ ЗАПРОС
                .jsonPath().getInt("id");
    }

}