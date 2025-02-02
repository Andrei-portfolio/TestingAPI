package helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Task;
import java.io.IOException;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

public class ToDoHelperOkHttp implements ToDoHelper {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private final OkHttpClient httpClient;

    public static final MediaType JSON = MediaType.get("application/json");

    public ToDoHelperOkHttp() {
        //Interceptor interceptor= new LoggingInterceptor();
        //httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    /* Чтобы усовершенствовать код, поработаем с Interceptor не через OkHttpClient, а скачаем Interceptor в pom.xml
    https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor/4.12.0. А код выше, закомитим */

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        httpLoggingInterceptor.setLevel(Level.BASIC);
        httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(httpLoggingInterceptor).build();
}

    public Task createTask() throws IOException {
        //System.out.println("Создание объекта");
        /*Это конечно неплохо, что в терминале видно о чем тест, но как и
        говорил раньше, в тесте ничего не должно быть лишнего, код не должен быть объемный и содержать sout.
        А поэтому, т.е. для этого у нас есть Interceptor - это интерфейс, который перехватывает трафик, т.е.
        ч/з него идёт запрос и ответ. Т.е. любой запрос проходит ч/з Interceptor и мы можем посмотреть в нём,
        что было нами отправлено и можем перехватить body, которое вернётся. Поэтому sout мы можем прописать
        в Interceptor и он не будет нам мозолить глаза. Это что-то типо снифера чарльза, которые перехватывают
        трафик. Поэтому закомитим в данном классе sout и создадим класс LoggingInterceptor */

        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью OkHttpClient на Java\"\n"
                + "}";
        Request request = new Request.Builder().url(URL).post(RequestBody.create(jsonBodyToSend, JSON)).build();
        Response response = httpClient.newCall(request).execute();
        String body = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body, Task.class);
    }

    public List<Task> getTasks() throws IOException {
        //System.out.println("Получение списка объекта"); Закомитил, т.к. пропишем в Interceptor
        Request request = new Request.Builder().url(URL).get().build();
        Response response = httpClient.newCall(request).execute();

        // достаем тело из ответа и преобразуем в сущность
        String body = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();

        return List.of(objectMapper.readValue(body, Task[].class));
    }

    public void deleteTask(Task task) throws IOException {
        //System.out.println("Удаление объекта"); Закомитил, т.к. пропишем в Interceptor
        Request request = new Request.Builder().url(URL + task.getId()).delete().build();
        Response response = httpClient.newCall(request).execute();

        assertEquals(CODE_OK, response.code());
    }
}