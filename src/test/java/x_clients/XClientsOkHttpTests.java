package x_clients;

import static helpers.ToDoHelperOkHttp.JSON;

import helpers.LoggingInterceptor;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*Свагер - это набор инструментов. И самый основной это свагер Editor. В нём можно открыть страничку, и можно
описать своё Api. В формате OpenApi. Свагера это не касается

Свагер Editor - это инструмент, который преобразует OpenApi документацию разработчика (слева) в красивый
читабельный вид (справа).

Сервис X-Clients (https://x-clients-be.onrender.com/docs/#/), с которым мы будем работать ближайшее время,
через API и БД. Так, например, в рамках д/з необходимо создать компанию, и мы можем спокойно скопировать код.
Это допускается.
 */


public class XClientsOkHttpTests {

    private final static String URL = "https://x-clients-be.onrender.com/";

    private OkHttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
    }

    @Test
    @DisplayName("Получить список компаний")
    public void getCompanyList() throws IOException {
        String token = ""; // ВЗЯТЬ ТОКЕН ИЗ POST АВТОРИЗАЦИИ
        Request request = new Request.Builder().url(URL + "company").addHeader("x-client-token", token).get().build();
        Response response = httpClient.newCall(request).execute();

        // достаём тело из ответа и преобразуем в сущьность
        String body = response.body().string();
        System.out.println(body);
    }

    /*В документации написано, чтобы послать post запрос, нужно во первых авторизироваться и во вторых
    послать токен. Токен авторизации (наш индификатор) передаётся в get запросе в заголовке. Чтобы
    авторизироваться, нам нужно куда то послать логин и пароль и получить взамен токен, и второе, нам куда то
    нужно подставить токен (В ЗАГОЛОВОК).
    В нашем случае, авторизация расписана в документации в свагере (post / auth / login Авторизация)
    Авторизируемся по логинам и паролям admin (не перепутать с клиентами), которые отправил Семён в учебный чат курса
    17 января 2025 года в 16:12. Так и есть, после отправки логина и пароля в post запросе, на вернулся токен

    */

    @Test
    @DisplayName("Проверка авторизации")
    public void authorization() throws IOException {
        String jsonBodyToSend = """
                 {
                   "username": "leonardo",
                   "password": "leads"
                 }
                """;// ВАЖНО!!! три кавычки обозначают многострочную строка, т.е. переменная будет многострочной
        Request request = new Request.Builder().url(URL + "auth/login").post(RequestBody.create(jsonBodyToSend, JSON))
                .build();
        Response response = httpClient.newCall(request).execute();

        String body = response.body().string();
        System.out.println(body);
    }
    /*В д/з проверку авторизации не нужно расписывать в каждом тесте, а необходимо вынести в одно место,
     * например в helper*/

}
