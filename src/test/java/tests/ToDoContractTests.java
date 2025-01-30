package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Task;
import helpers.ToDoHelperApache;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 /*
    Контрактные тесты - это тест который проверяет контракт, т.е. проверяет что мы должны отправить и что получить
    Т.е. проверяем, что при проверке валидных данных, мы получаем валидный ответ.
    Если отправим не валидные данные, то получаем не валидный ответ (400-404 код).
    Мы не смотрим какой вернулся список задач. Корректный ли этот список, сколько там элементов и сколько вернулось.

    Контрактный тест - это строгая наука, типа матиматики. Например открываем документацию, и смотрим что мы должны отправить и что
    должны получить. Остальное нас не волнует. Всё строго.
     */


public class ToDoContractTests {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    private ToDoHelperApache toDoHelperApache;// Объявили ToDoHelper, отдельно вынесенный класс в котором у нас создаётся задача

    @BeforeEach
    public void setUp(){
        toDoHelperApache = new ToDoHelperApache();//отдельно вынесенный класс в котором у нас создаётся задача
        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    @DisplayName("Получение списка задач возвращает код 200 и json")
    public void getTasksCodeOk() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        // достаем тело из ответа
        String body = EntityUtils.toString(httpResponse.getEntity());
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());
        assertTrue(body.startsWith("["));// Говорим, проверь, что body начинается квадр. скобки [
        assertTrue(body.endsWith("]"));// Говорим, проверь, что body заканчивается квадр. скобки ]
    }

    @Test
    @DisplayName("Получение списка задач возвращает все поля валидных типов")
    public void getTasksBodyValidTypes() throws IOException {

        /*HttpPost httpPost = new HttpPost(URL);
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью HttpClient на Java\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);// Взяли строку,
        // которую хотим отправить и поместили в StringEntity и setEntity. После запятой хедеры указываем
        httpPost.setEntity(bodyToSendEntity);
        httpClient.execute(httpPost);*/

        /*ВАЖНО!!! ВЕСЬ КОД, КОТОРЫЙ ВЫШЕ, ЭТО ПРЕДУСЛОВИЕ. Т.Е. ПРЕЖДЕ ЧЕМ, НАМ ПОЛУЧИТЬ СПИСОК ЗАДАЧ, НУЖНО
        С ПОМОЩЬЮ МЕТОДА ПОСТ СОЗДАТЬ В СПИСКЕ, ХОТЯ БЫ ОДНУ ЗАДАЧУ. ИНАЧЕ, МОЖЕТ ПОЛУЧИТЬСЯ, ЧТО ТЕСТ
        УПАДЁТ, ЕСЛИ ВДРУГ, КТО-ТО УДАЛИТ ВСЕ ЗАДАЧИ В ПРОГРАММЕ TO DO. НО МЫ ПОДСТРАХОВАЛИСЬ ОТ ЭТОГО
         И НИЖЕ ПРИСТУПИМ К САМОМУ АВТОТЕСТУ.
         КРОМЕ ТОГО, АВТОТЕСТ НАЗЫВАЕТСЯ ПОЛУЧЕНИЕ СПИСКА ЗАДАЧ...... А В ПРЕДУСЛОВИИ У НАС ПРИВЕДЕНО СОЗДАНИЕ
         СПИСКА (POST). ПОЛУЧАЕТСЯ ЕГО НЕОБХОДИМО КУДА-ТО ВЫНЕСТИ ОТДЕЛЬНО. А ДЛЯ ЭТОГО ВЫДЕЛЯЕМ НАШ КОД,
         КОТОРЫЙ НЕОБХОДИМО ВЫНЕСТИ. НАЖИМАЕМ ПКМ -- REFACTOR -- EXTRACT METHOD -- МЕНЯЕМ НАЗВАНИЕ extracted(),
         НАПРИМЕР НА createTask -- enter. ДАННЫЙ КОД СОХРАНИЛСЯ НИЖЕ В ВИДЕ
         МЕТОДА private void createTask() throws IOException. КУДА И КАК ЕГО ПЕРЕНЕСТИ ЕГО В ОТДЕЛЬНЫЙ КЛАСС,
         ВЕРНЁМСЯ ЧУТЬ ПОЗЖЕ*/

        toDoHelperApache.createTask();//отдельно вынесенный класс в котором у нас создаётся задача

        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        // достаем тело из ответа и преобразуем в сущность
        String body = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Task> tasks = List.of(objectMapper.readValue(body, Task[].class));
        assertFalse(tasks.isEmpty());// ПРОВЕРКА, ЧТО СПИСОК НЕ ПУСТ

        System.out.println(tasks);
    }

    /*
    Дебаг это способ, запускать и приостанавливать программу частями. Нажимаем  КРАСНУЮ ТОЧКУ скраю, в тех строчках,
    в которых хотим остановить программу. Помимо красных точек, данные строки выделится в красный цвет. Запускаем дебаг
    также, как и run, только нажав на правую клавишу мыши, выбираем Debug......... Там, где остановилась программа,
    эта строчка выделена синим цветом. Далее в левом нижнем углу находим и можем нажать Resume Program (F9)(зеленый треуг
    со столбиком). Обозначает, выполняй дальше, до следующего стоп-кадра. Программа выполняется поэтапно и в терминале
    можно посмотреть. Так, поствив точку на строке List<Task> tasks = List.of(objectMapper.readValue(body, Task[].class));
    , программа отрабатывает и в терминале появляется "task". Нажав на которую, мы можем посмотреть список задач.
    А плюс в том, что данный список не в одну строчку, если бы мы не дебажили,
    а каждаю задача отдельно. Можно посмотреть каждое поле
     */

    /*private void createTask() throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью HttpClient на Java\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);// Взяли строку,
        // которую хотим отправить и поместили в StringEntity и setEntity. После запятой хедеры указываем
        httpPost.setEntity(bodyToSendEntity);
        httpClient.execute(httpPost);
    }*/

    //В связи с тем, что данный код вынесли в отдельный класс, в котором у нас создаётся задача, код выше я
    //закомитил

}

