package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import entities.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoHelperApache implements ToDoHelper{// Чтобы проще было перейти с Apache на что то другуе, используем
                                                    // интерфейс

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private final HttpClient httpClient;

    public ToDoHelperApache() {// создаём конструктор
    httpClient = HttpClientBuilder.create().build();
    }

    public Task createTask() throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью HttpClient на Java\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);// Взяли строку,
        // которую хотим отправить и поместили в StringEntity и setEntity. После запятой хедеры указываем
        httpPost.setEntity(bodyToSendEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String body = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body, Task.class);
    }

    public List<Task> getTasks() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        // достаем тело из ответа и преобразуем в сущность
        String body = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();

        return List.of(objectMapper.readValue(body, Task[].class));

         }

    public void deleteTask(Task task) throws IOException {
        HttpDelete httpDelete = new HttpDelete(URL + task.getId());// В скобках обозначает, что при
                                                                      // удалении отпр. URL и id задачи
        HttpResponse httpResponse = httpClient.execute(httpDelete);
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());// Ассерт добавили, чтобы быть
        // уверенными, что тест отработает с кодом 200
    }


}
