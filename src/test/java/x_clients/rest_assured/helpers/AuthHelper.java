package x_clients.rest_assured.helpers;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import x_clients.rest_assured.entity.AuthRequest;
import x_clients.rest_assured.entity.AuthResponse;

public class AuthHelper {

    String authAndGetToken(String username, String password) {// Метод который авторизуется и проверяет токен. Данный метод будем
        // использовать перед каждым тестом. Тем самым код не будет повторяться
//        String jsonBodyToSend = """
//                 {
//                   "username": "leonardo",
//                   "password": "leads"
//                 }
//                """;
        /*Код выше закомитили, т.к. мы создали класс рекорд (не требует делать конструктор и геттеры, т.к
        они автоматически создаются, хоть мы их и не видим) AuthRequest и там прописали данные поля
        username и password. Ну а в коде ниже создали объект класса AuthRequest. В результате кода станет меньше
         */

        AuthRequest authRequest = new AuthRequest(username, password);//

        AuthResponse authResponse = given()// ДАНО
                .basePath("auth/login")
                .body(authRequest)// плюсы REST - assured в том, что в нём уже встроен ObjectMapper от Jackson и он
                // преобразует его в JSON.
                .contentType(ContentType.JSON)
                .header("x-client-token", "")
                .when()// КОГДА
                .post()// ШЛЁШЬ ПОСТ ЗАПРОС
                //.jsonPath().getString("userToken");/* использ. механизм JSONPath. Что это такое?*/
                /*В REST - assured уже вставлен данный механизм. Который позволяет обратиться к
                какому - либо из полей JSON файла. А можно это сделать с помощью "$.userToken" (но его
                 не нужно ставить, он установлен по умолчанию), после точки ставим как раз путь (ключ) и
                получаем значение. Особенно удобен данный метод, если мы обращаемся к полю один раз,
                но если придётся обращаться неоднократно, то лучьше создать объект класса.
                Тем самым в REST - assured, мы можем работать с JSON, без создания
                обжект маппера, как мы это делали ранее.
                Попрактиваться с JSONPath можно например на сайте https://jsonpath.com/*/
                .as(AuthResponse.class);

        return authResponse.userToken(); //так как поле userToken мы создавали не в классе, в Record, то здесь
        //мы не пишем get userToken, а пишем просто userToken. Необходимо не забывать об этом
    }



    /*public String authAndGetToken(String username, String password) {

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
    }*/
}