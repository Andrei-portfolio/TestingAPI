package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

public class PostHttpDemo {
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


    // СОЗДАТЬ ЗАПРОС (КАКОЙ-ТО РЕКВЕСТ). POST запрос отличается только тем, там где было в коде get, меняем на post.
        // ПЛЮС ДОБАВЛЯЕМ ТЕЛО BODY

        HttpPost httpPost = new HttpPost("https://todo-app-sky.herokuapp.com/");//Можно разные выбрать (PUT, DELETE)
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Я создан с помощью HttpClient на Java\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);// Взяли строку, которую хотим отправить и
                                                                                                     // поместили в StringEntity и setEntity. После запятой хедеры указываем
        httpPost.setEntity(bodyToSendEntity);

   // ОТПРАВИТЬ ЗАПРОС
        HttpResponse httpResponse = httpClient.execute(httpPost);// Заставил нас сделать исключение. А для этого
        // напишем в самом начале, после psvm - throws IOException. Т.к. может быть, что интернет не подключен,
        // сервер недоступен и т.д.

        //System.out.println(httpResponse);//чтобы ПРОВЕРИТЬ, какой приходит ответ. В терминале, видим много инфо.
        //Теперь будем разбираться, как оттуда вытащить нужное нам, чтобы потом сравнить ожидаемый и фактический
        //результат с помощью иквилс. А сделаем это, написав после httpResponse, например getStatusLine().
        // В терминале получаем, версию прокола, статус код и можно много ещё что выбрать. Приведем пример ниже

    // ПОЛУЧИТЬ ИНФОРМАЦИЮ ПРО КОД И ПРОТОКОЛ
        System.out.println(httpResponse.getStatusLine().getStatusCode());

    // ПОЛУЧИТЬ ТЕЛО ЗАПРОСА
        HttpEntity body = httpResponse.getEntity();// Переводится Entity, как сущьность
        //System.out.println(EntityUtils.toString(body));// Теперь видно наше тело запроса
        String bodyString = EntityUtils.toString(body);// Чтобы увидеть строку, разраб. добавили спец. хелперс  EntityUtils
        System.out.println(bodyString);
    }
}
