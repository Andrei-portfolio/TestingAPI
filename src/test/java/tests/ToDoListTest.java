package tests;


import entities.Task;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoListTest {
    private final static String URL = "https://todo-app-sky.herokuapp.com/";// URL выносим в константу. Ну а на будующее,
    // URL мы не храним здесь, а необходимо вытаскивать в отдельный файл. Но об этом на друом уроке. Ну а пока так

    private final static int CODE_OK = 200;// тоже выносим в константу. На будующее, храним тоже не здесь

    private HttpClient httpClient;

    @BeforeEach
    public void setUp(){
        httpClient = HttpClientBuilder.create().build();// Чтобы отправить запрос, нужно создать HttpClient
}

    @Test
    public void getTasksCodeOk() throws IOException {// Ожидаем код 200
        HttpGet httpGet = new HttpGet(URL);// Здесь обзначаем, какой из запросов отравляем Get, Post и т.д., и указываем URL
        HttpResponse httpResponse = httpClient.execute(httpGet);// выбрасывает исключение
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());// пров. ож. и фактич. рез.: код 200
    }

    @Test
    public void getTasksJson() throws IOException {// Возвращается Json
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        Header contentType = httpResponse.getFirstHeader("Content-Type");// Возвращает значение хэдера "Content-Type" .
                                                                            // Если их несколько, то возвращает первый попавшийся
        assertEquals("application/json; charset=utf-8", contentType.getValue());
    }

    /*
фэйкер создаёт/генерирует строки, числа (персонажей из фильмов, имена, фамилии, улицы, номера домов....) и т.д.,
а instancio может создать объект вашего класса. Т.е когда мы не хотим сами замарачиваться, через него можно это
сделать быстрее, чем через фэйкер. Скачать instancio можем на сайте
https://mvnrepository.com/artifact/org.instancio/instancio-junit/5.0.2. Далее устанавливаем в pom.xml
Почитать же про неё можно на сайте https://www.instancio.org/user-guide/.


 НИЖЕ ПРИВЕДЕН ТЕСТ С ИСПОЛЬЗОВАНИЕМ БИБЛИОТЕКИ instancio И ГЕНЕРАЦИИ СЛУЧАЙНЫХ ДАННЫХ ПО ЗАДАЧЕ
 (id, title, completed).

 */

    @Test
    public void instancioTest() {
        // Создать случ. задачу
        Task task = Instancio.create(Task.class);
        System.out.println(task);

        // Более гибкая настройка объекта
        Task task1 = Instancio.of(Task.class).generate(field("title"), generators -> generators.text().loremIpsum()).create();
        System.out.println(task1);

        // Создать 10 случ. задач
        List<Task> tasks = Instancio.ofList(Task.class).size(10).create();
        //System.out.println(tasks);
    }
}


