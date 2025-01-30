package org.example;

/*      ОТМЕЧЕНО, ЧТО КОГДА МЫ ПИШЕМ UNIT ТЕСТЫ, ТО НАМ НЕОБХОДИМО РАЗВЕРНУТЬ У СЕБЯ КОД К ДАННОЙ ПРОГРАММЕ
        (ФУНКЦИОНАЛУ И Т.Д.). НО КОГДА МЫ ПИШЕ API ТЕСТЫ НИКАКОГО КОДА НЕ НУЖНО. НУЖНА ТОЛЬКО ДОКУМЕНТАЦИЯ И ВКЛАДКА
        NETWORK  КОНСОЛИ РАЗРАБОТЧИКА.

        API ТЕСТЫ БУДЕМ ИЗУЧАТЬ НА ПРИМЕРЕ 3-Х ИНСТРУМЕНТОВ, НО ИХ БОЛЬШЕ. ДВА ИЗ НИХ БУДУТ ПОПРОЩЕ И ОНИ НУЖНЫ ДЛЯ
        ОСВОЕНИЯ БАЗЫ. НУ А НАЧНЁМ МЫ С APACHE HttpClient. ЧТОБЫ ЕГО СКАЧАТЬ, КАК ОБЫЧНО ЗАХОДИМ В MAVEN.
        УСТАНАВЛИВАЕМ (ВЕРСИЮ 4.5.14) В POM.XML.
        APACHE HttpClient - ЭТО НЕ БИБЛИОТЕКА ДЛЯ ТЕСТОВ, А HttpClient, ИНСТРУМЕНТ, С ПОМОЩЬЮ КОТОРОГО МЫ СМОЖЕМ
        ПОСЫЛАТЬ API ЗАПРОС И ПОЛУЧАТЬ ОТВЕТ. А ДЛЯ НАПИСАНИЯ ТЕСТОВ, МЫ ПО-ПРЕЖНЕМУ ПРОДОЛЖИМ РАБОТАТЬ С JUNIT.
        ПОЭТОМУ НАМ НЕ ОБЯЗАТЕЛЬНО ПИСАТЬ В ПАПКЕ test, можем начать и в main.

        В консоли разработчика во вкладке Fetch/XHR мо можем посмотреть только запросы с body, т.е. без картинок.

 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

public class GetHttpDemo {
    public static void main(String[] args) throws IOException {//написали throws IOException, чтобы выкидывал,
                                                                // если пойдет что-то не так
        //СОЗДАТЬ КЛИЕНТ (Чтобы отправить запрос, нужно создать HttpClient)
HttpClient httpClient = HttpClientBuilder.create().build(); /* нас интересует клиент с пометкой appache.http.client,
НЕ java.net.http. Кроме того, при создании объекта, мы НЕ делаем HttpClient httpClient = new HttpClient,
т.к. выдаст очень много лишнего. А посмотрев документацию, видно, что объект создаётся следующим
образом HttpClientBuilder.create().build().
ЧТО ЭТО ЗА СТРАННАЯ СТРОЧКА У НАС ВЫШЛА? ЭТО билдер паттерн, ОЗНАЧАЮЩИЙ "СОЗДАТЬ СБОРКУ". ПРИ РАЗГОВОРЕ С
ИНТЕРФЕЙСАМИ, МЫ ГОВОРИЛИ ПРО ПАТЕРН fabrica. Теперь время пришло для ПАТЕРНА билдер. Это полезный патерн,
который нужно сначала понять ЗАЧЕМ использовать, а потом уже КАК. ОН пригождается как в тестировании,
 так и в разработке. Особенно в API тестах.

 Если мы зайдём в паттерн HttpClientBuilder.class, то он ОГРОМНЫЙ и в нём много if и else, т.е много ветвлений.

 Суть ПАТЕРНА билдер в том, что у нас может быть огромное количество параметров и какие угодно. И если
 такому пользователю, как нам, какие то параметры не нужны, он может их не использовать и не отправлять.
 Билдер сам разберётся, задаст параметры по умолчанию, либо вообще не задаст их.
 Пример создания билдера приведём в пакете org.example.BuilderDemo

 HttpClientBuilder.create(). означает начни создавать клиент. Появился строитель. Появился билд, говорит, завершай
 стройку и возвращай HttpClient */


    // СОЗДАТЬ ЗАПРОС (КАКОЙ-ТО РЕКВЕСТ)
        HttpGet httpGet = new HttpGet("https://todo-app-sky.herokuapp.com/");//Можно разные выбрать (PUT, DELETE)

    // ОТПРАВИТЬ ЗАПРОС
        HttpResponse httpResponse = httpClient.execute(httpGet);// Заставил нас сделать исключение. А для этого
        // напишем в самом начале, после psvm - throws IOException. Т.к. может быть, что интернет не подключен,
        // сервер недоступен и т.д.

        //System.out.println(httpResponse);//чтобы ПРОВЕРИТЬ, какой приходит ответ. В терминале, видим много инфо.
        //Теперь будем разбираться, как оттуда вытащить нужное нам, чтобы потом сравнить ожидаемый и фактический
        //результат с помощью иквилс. А сделаем это, написав после httpResponse, например getStatusLine().
        // В терминале получаем, версию прокола, статус код и можно много ещё что выбрать. Приведем пример ниже

    // ПОЛУЧИТЬ ИНФОРМАЦИЮ ПРО КОД И ПРОТОКОЛ
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        System.out.println(httpResponse.getStatusLine().getProtocolVersion());
        System.out.println(httpResponse.getStatusLine().getReasonPhrase());

    // ПОЛУЧИТЬ ТЕЛО ЗАПРОСА
        HttpEntity body = httpResponse.getEntity();// Переводится Entity, как сущьность
        //System.out.println(EntityUtils.toString(body));// Теперь видно наше тело запроса
        String bodyString = EntityUtils.toString(body);// Чтобы увидеть строку, разраб. добавили спец. хелперс  EntityUtils

        ObjectMapper objectMapper = new ObjectMapper();// с помощью библиотеки джексон.
        //Так как, мы выше увидели, что в теле передаётся id,"title" и "completed", то создадим спец. класс ToDoEntity
        ToDoEntity[] toDoEntity = objectMapper.readValue(bodyString, ToDoEntity[].class); //скобки [] нужны т.к. в туду объектов несколько
            Arrays.stream(toDoEntity).forEach(System.out::println);// быстрый способ вывести весь массив в консоль
           /*
           Теперь можно обращаться к этим полям и писать тесты, в т.ч. делать AssertEquals.
           ВАЖНО!!! Но конечно, же НЕ в пакете main. AssertEquals возможно использовать только в пакете test.
           Поэтому чуть позже, мы весь код перенесем в пакет test. А для этого, установим в pom.xml.
            */
            System.out.println(toDoEntity[0].getId()); // выведем в терминал номер id первого объекта из туду

     // ПОЛУЧИТЬ ЗАГОЛОВКИ
            Header[] headers = httpResponse.getAllHeaders();// поместим заголовки в массив хэдерс инструмаппачи
            Arrays.stream(headers).forEach(System.out::println);//быстрый способ вывести все хэдеры в консоль
    }
}
