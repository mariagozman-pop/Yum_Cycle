// ApiClient.java
package com.example.yumcycle.api;

import android.content.Context;
import com.example.yumcycle.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String OLLAMA_BASE_URL = "http://127.0.0.1:11434/";
    private static Retrofit ollamaRetrofit;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create a logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Build OkHttpClient with logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Configure Retrofit with ScalarsConverterFactory for plain text
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create()) // Handle plain text first
                    .addConverterFactory(GsonConverterFactory.create())    // Handle JSON second
                    .build();
        }
        return retrofit;
    }
}
