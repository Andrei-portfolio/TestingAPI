package x_clients.rest_assured.helpers;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x_clients.rest_assured.entity.AuthRequest;
import x_clients.rest_assured.entity.AuthResponse;
import x_clients.rest_assured.entity.CreateCompanyRequest;
import x_clients.rest_assured.entity.CreateCompanyResponse;

/* Класс №1
REST - assured это инструмент для тестирования. И это не аналог Apache и OkHttp Это больше чем клиент.
Нужно научиться разграничивать. У нас есть Apache и OkHttp, предназначенные для отправки запросов и получения
ответов. А далее с ответами, мы делаем всё что захотим, тестируем, но для этого нам нужно устанавливать
с junit. А REST - assured, это не только клиент, но и инструмент для тестирования. Он позволяет посылать
запросы и валидировать ответы. И это как плюс, так и минус. С его помощью тестировать легче, чем ч/з
Apache и OkHttp, кроме того не нужны асерты от джиюнита.

Скачаем через https://mvnrepository.com/artifact/io.rest-assured/rest-assured/5.5.0 инструмент REST - assured.
Документацию можно посмотреть на https://rest-assured.io/. В docs в Getting started и Usage Guide
можно почитать документацию до 7-го пункта. Там очень много возможностей
В REST - assured принято переносить длинный код на несколько строчек. Самое главное, при переносе,
точку оставлять не в конце строки, а в начале следующей.
*/

public class CompanyContractTest {

    private final static String URL = "https://x-clients-be.onrender.com/";

    private static ApiCompanyHelper apiCompanyHelper;

    private static AuthHelper authHelper;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = URL;//чтобы не задавать в каждом тесте baseUri, пропишем
        // его здесь в виде переменной из RestAssured
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();// Логирование. Если тест упадёт, то в консоле будет риспунс и реквест
        //Если же хотим логировать всё, то в каждом тесте после given() пишем .log().all()
        apiCompanyHelper = new ApiCompanyHelper();
        authHelper = new AuthHelper();

        String userToken = authHelper.authAndGetToken("leonardo", "leads");
        RestAssured.requestSpecification = new RequestSpecBuilder().build().header("x-client-token", userToken);
    }

    /*Рассмотрим три основных метода
    // given - ДАНО (условия/настройки, которые даны: URL, хедеры и т.д.)
    // when - КОГДА (показываем, какой запрос мы отправляем: get, post и т.д.)
    // then - ТОГДА (тогда проверяй его на код 200, например. Валидирование)
    Проще говоря, у нас получается 1 этап, у нас есть URL, 2 этап

    */
    @Test
    @DisplayName("Код ответа при получении списка компаний")
    public void getCompanyList() {
        given().log().all()  // ДАНО:// Поставили логирование для данного теста .log().all(). Даже если он не упадёт, в консоль выдаст данные
                //.baseUri(URL + "company")// Данный код закомитим, т.к мы задали URL выше в
                // @BeforeAll. Поэтому, теперь нижней строкой пропишем basePath, без URL
                .basePath ("company")
                .when()     // КОГДА
                .get() // ШЛЕШЬ ГЕТ ЗАПРОС (Response)
                .then() // ТОГДА ПРОВЕРЬ СЛЕДУЮЩЕЕ: статус код и хэдер
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8");

//        А теперь всё тоже самое, но в другом менее восприимчивом стиле.
//        RequestSpecification requestSpecification = given().baseUri(URL + "company");
//        Response response = requestSpecification.when().get();
//        ValidatableResponse validatableResponse = response
//            .then()
//            .statusCode(200)
//            .header("Content-Type","application/json; charset=utf-8");;
    }

    @Test
    @DisplayName("Авторизация")
    public void authorization () {
        String jsonBodyToSend = """
                 {
                   "username": "leonardo",
                   "password": "leads"
                 }
                """;

        String userToken = given()// ДАНО
                .basePath("auth/login")
                .body(jsonBodyToSend)
                .contentType(ContentType.JSON)
                .when()// КОГДА
                .post()// ШЛЁШЬ ПОСТ ЗАПРОС
                .jsonPath().getString("userToken");/* использ. механизм JSONPath. Что это такое?
                В REST - assured уже вставлен данный механизм. Который позволяет обратиться к
                какому - либо из полей JSON файла. А можно это сделать с помощью "$.userToken" (но его
                 не нужно ставить, он установлен по умолчанию), после точки ставим как раз путь (ключ) и
                получаем значение. Особенно удобен данный метод, если мы обращаемся к полю один раз,
                но если придётся обращаться неоднократно, то лучьше создать объект класса.
                Тем самым в REST - assured, мы можем работать с JSON, без создания
                обжект маппера, как мы это делали ранее.
                Попрактиваться с JSONPath можно например на сайте https://jsonpath.com/*/
        System.out.println(userToken);// В данном тесте приведен пример, как по итогам теста мы не будем ничего
        // сравнивать, а просто вытащим в терминал токен
    }

    @Test
    @DisplayName("Создание компании")
    public void createCompany() {

//        String jsonBodyToSend = """
//                {
//                   "name": "RestAssuredCompany",
//                   "description": "MyRestAssuredCompany"
//                 }
//                """;
        /*Чтобы вынести наш json также, как мы вынесли его ниже в методе authAndGetToken. То нам нужно также
         создать класс рекорд (не требует делать конструктор и геттеры, т.к они автоматически создаются, хоть
         мы их и не видим) CreateCompanyRequest в entity и там пропишем данные поля name и description. Тем самым
         приведём всё к единобразию. А код выше, закомитим */


        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();
        System.out.println(createCompanyResponse.id());// В данном тесте приведен пример, как по итогам теста мы кроме сравнения
        // вытащим в терминал id. В свагере проверяем, на самом ли деле создалась наша компания с данным id
    }



        @Test
        @Disabled("Разобразться с авторизацией")
        @DisplayName("Создание компании выдает код 401 за клиента")
        public void createCompanyWithClientUser() {
            String userToken = authHelper.authAndGetToken("stella", "sun-fairy");
            CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Entity company", "with entity");

            String errorMessage = given()  // ДАНО:
                    .basePath("company")
                    .body(createCompanyRequest)
                    .contentType(ContentType.JSON)
                    .header("x-client-token", userToken)
                    .when()     // КОГДА
                    .post() // ШЛЕШЬ ПОСТ ЗАПРОС
                    .then()
                    .statusCode(403).extract().asPrettyString();

            System.out.println(errorMessage);
        }

        /*CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();*/


        @Test
        @DisplayName("Удаление компании")
        public void deleteCompany() {
            CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();//Чтобы удалить, сначала создаём
            int deletedObjectId = apiCompanyHelper.deleteCompany(createCompanyResponse.id());
            assertEquals(createCompanyResponse.id(), deletedObjectId);
        }

    @Test
    @DisplayName("Получить компанию по id")
    public void getCompany() {
        int id = 1294;
        given()  // ДАНО:
                .basePath("company")
                .when()     // КОГДА
                .get("{id}", id).prettyPrint(); // ШЛЕШЬ ГЕТ ЗАПРОС
    }
        /*prettyPrint - помогает нам вывести в консоль необходимые данные. Так, чтобы увидеть данные о компании,
        необходимо сначала в тесте "Создание компании" посмотреть id компании, вбить этот id в данный тест
        и после запуска получаем информацию о компании. Обращу вниманее, что в данном тесте нет никаких
        асертов, мы просто выводим в консоль информацию и видим, как выглядит тело ответа

        Рефактор - это когда берём код, который требует визуальных доработок и от этого сами тесты не должны
        поменяться, т.е. появляется новая структура, выносятся методы и т.д. Т.е. облагораживание кода
        */



}