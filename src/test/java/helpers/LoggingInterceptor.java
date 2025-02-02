package helpers;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/* Interceptor - это интерфейс, который перехватывает трафик, т.е. ч/з него идёт запрос и ответ. Т.е.
любой запрос проходит ч/з Interceptor и мы можем посмотреть в нём, что было нами отправлено и можем
перехватить body, которое вернётся. Поэтому sout мы можем прописать здесь в Interceptor и он не будет нам
 мозолить глаза в классе с тестами. Это что-то типо снифера чарльза, которые перехватывают трафик.
 Поэтому закомитим в тестовом классе sout и создадал класс LoggingInterceptor */

public class LoggingInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();// можем достать запрос, который мы шлём. Перевод chain - цепочка. Он
                                              // к ней обращается и ждёт своей очереди. Он срабатывает столько раз,
                                              // сколько отправляется запросов
            System.out.println("METHOD: " + request.method());
            System.out.println("URL: " + request.url());

            Response response = chain.proceed(request);
            System.out.println("CODE: " + response.code());
            System.out.println("-------------------------");
            return response;
        }
    }

