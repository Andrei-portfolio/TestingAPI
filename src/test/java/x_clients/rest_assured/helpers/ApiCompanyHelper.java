package x_clients.rest_assured.helpers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.http.ContentType;
import x_clients.rest_assured.entity.CreateCompanyRequest;
import x_clients.rest_assured.entity.CreateCompanyResponse;

public class ApiCompanyHelper {

    private AuthHelper authHelper;

    public ApiCompanyHelper(){
        authHelper = new AuthHelper();
    }
    public CreateCompanyResponse createCompany(){
                CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Entity company", "with entity");

        return given()// ДАНО
                .basePath("company")
                .body(createCompanyRequest)//плюсы REST - assured в том, что в нём уже встроен ObjectMapper от Jackson и он
                // преобразует его в JSON.
                .contentType(ContentType.JSON)
                //.header("x-client-token", userToken)// ВАЖНО: обязательно прокидываем токен, иначе автотест не отработает
                .when()// КОГДА
                .post()// ШЛЁШЬ ПОСТ ЗАПРОС
                .then()
                .statusCode(201)
                .body("id", is(greaterThan(0)))// это в REST - assured  есть hamcrest.Matchers.is.
                // проверяем, что id больше 0
                //.extract().jsonPath().getString("id");// В терминал видим id нашей компании
                .extract().as(CreateCompanyResponse.class);
    }


    public int deleteCompany(int id) {

        return given()  // ДАНО:
                .basePath("company/delete")
                //.header("x-client-token", userToken)
                .when()     // КОГДА
                .get("{id}", id) // ШЛЕШЬ ПОСТ ЗАПРОС
                .jsonPath().getInt("id");
    }

}