package x_clients.rest_assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

//import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.Test;

/*проверим в тесте, что нам возвращаются 4 поля. Проверим, что поля вообще пришли, т.к. они могут и не придти.
        Далее проверим, что типы соответствуют. Используем 2 метода в классе JSONSchemaTest*/

public class JSONSchemaTest {

    @Test
    public void checkGetCompanyResponseBody() {//проверим в тесте, что в теле нам возвращаются 4 поля заданных типов
        //String, Boolean и т.д. Но кстати, здесь баг. В документации написано, что в теле 4 поля, а по факту их 7
        given()
                .baseUri("https://x-clients-be.onrender.com/")
                .basePath("company")
                .when().get("{id}", 13)// Чтобы не гадать, можно запустить АТ по созд. компании и посм id
                // потом вбить его вместо "13"
                .then()
                .body("id", isA(Integer.class))
                .body("name", isA(String.class))
                .body("description", isA(String.class))
                .body("isActive", isA(Boolean.class));
    }
/*
Минусы данного теста, что количество полей много, а может быть и больше. Кроме того, он проверяет только соответствуют ли
поля заданным типам. Но не проверяет соответствует ли фактическое количество полей, количеству которое прописано
в документации. А это мы проверим в след тесте, который ниже. А для этого установим в pom.xml Swagger Request Validator RestAssured на
https://mvnrepository.com/artifact/com.atlassian.oai/swagger-request-validator-restassured
 */

    @Test
    public void checkGetCompanyResponseBodyWithValidator() {//Тест не отработал, из-за бага
        String swaggerUrl = "https://x-clients-be.onrender.com/docs-json";// Обратить вниманее, что ссылка не на гл. стр
        // свагера, а на json документацию с данного сайта. Т.е. мы документацию будем сравнивать с тем, что придёт по факту

        given()
                .filters(// как я понял, это аналог интерсептора - проксировщик запроса, который позволяет
                        // доп логику прикрутить к запросу. Например, логирование Request, Response

                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(swaggerUrl)// Принимает ссылку на свагер. И как раз падает здесь.
                        // При сравнении документации и то что приходит в теле. Из ошибки
                        //"Object instance has properties which are not allowed by the schema: [\"createDateTime\",\"deletedAt\",\"lastChangedDateTime\"]"
                        // видно, что есть несоответствие схеме
                )
                .baseUri("https://x-clients-be.onrender.com/")
                .basePath("company")
                .when().get("{id}", 13)
                .then()
                .statusCode(200);
    }

    @Test
    public void checkGetPetResponseBodyWithValidator() {// Тест тоже не отработал, из-за какого-то бага (имя приходит ч/з раз и ещё что-то)
        String swaggerUrl = "https://petstore.swagger.io/v2/swagger.json";//ссылка на документацию

        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(swaggerUrl)
                )
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet")
                .when().get("{id}", 13)
                .then()
                .statusCode(200);
    }
}
