package helpers;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class LoggingInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            System.out.println("METHOD: " + request.method());
            System.out.println("URL: " + request.url());

            Response response = chain.proceed(request);
            System.out.println("CODE: " + response.code());
            System.out.println("-------------------------");
            return response;
        }
    }

