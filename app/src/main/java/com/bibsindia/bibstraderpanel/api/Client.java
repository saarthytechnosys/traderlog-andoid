package com.bibsindia.bibstraderpanel.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
//    private static final String BASE_URL = "https://endpoint.bibsindia.com/api/v1/";
    private static final String BASE_URL = "https://ccapi.thewowl.com/api/v1/";
    private static Client mInstance;
    public static Retrofit retrofit;

    private Client() {
        HttpLoggingInterceptor.Level mLevel;
//        if (isLogged)
        mLevel = HttpLoggingInterceptor.Level.BODY;
//        else
//        mLevel = HttpLoggingInterceptor.Level.NONE;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000, TimeUnit.SECONDS)
                .readTimeout(15000, TimeUnit.SECONDS)
                .writeTimeout(15000, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(mLevel))
                .addInterceptor(
                        chain -> {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .addHeader("Content-Type", "application/json")
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                ).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized Client getInstance() {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    public webInterface getApi() {
        return retrofit.create(webInterface.class);
    }
}
