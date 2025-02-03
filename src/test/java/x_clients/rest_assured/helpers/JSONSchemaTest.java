package x_clients.rest_assured.helpers;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.Test;

public class JSONSchemaTest {

    @Test
    public void checkGetCompanyResponseBody() {
        given()
                .baseUri("https://x-clients-be.onrender.com/")
                .basePath("company")
                .when().get("{id}", 362)
                .then()
                .body("id", isA(Integer.class))
                .body("name", isA(String.class))
                .body("description", isA(String.class))
                .body("isActive", isA(Boolean.class));
    }

    @Test
    public void checkGetCompanyResponseBodyWithValidator() {
        String swaggerUrl = "https://x-clients-be.onrender.com/docs-json";

        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(swaggerUrl)
                )
                .baseUri("https://x-clients-be.onrender.com/")
                .basePath("company")
                .when().get("{id}", 362)
                .then()
                .statusCode(200);
    }

    @Test
    public void checkGetPetResponseBodyWithValidator() {
        String swaggerUrl = "https://petstore.swagger.io/v2/swagger.json";

        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(swaggerUrl)
                )
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet")
                .when().get("{id}", 12)
                .then()
                .statusCode(200);
    }
}