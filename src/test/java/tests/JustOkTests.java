package tests;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*Отличие двух клиентов (библиотек для тестирования) OkHttp и Apache: в синтаксисе. Скачиваем в pom.xml
клиент OkHttp на сайте https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/4.12.0
Подробнее документацию про данный клиент модно почитать на сайте https://square.github.io/okhttp/. Там прям
прописан код, как создать клиент и т.д.*/

public class JustOkTests {
    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private OkHttpClient httpClient;

    public static final MediaType JSON = MediaType.get("application/json");

    @BeforeEach
    public void setUp(){
        httpClient = new OkHttpClient.Builder().build();
    }

    @Test
    @DisplayName("Получение списка задач возвращает код 200 и json")
    public void getTasksCodeOk() throws IOException {
        Request request = new Request.Builder().url(URL).get().build();//метод гет
        Response response = httpClient.newCall(request).execute();//отправили запрос
        System.out.println(response.code());
        System.out.println(response.body().string());
        System.out.println(response.headers());

        /*Данный код не нужно раскомитивать, он здесь приведен для того, чтобы сравнить, код который получился
        выше в OkHttpClien и данный код в Apache:
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);*/
    }

    @Test
    @DisplayName("Создание таски")
    public void postTasksCodeOk() throws IOException {
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью OkHttpClient на Java\"\n"
                + "}";
        Request request = new Request.Builder().url(URL).post(RequestBody.create(jsonBodyToSend, JSON)).build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
