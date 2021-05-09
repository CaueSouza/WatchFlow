package com.example.watchflow.retrofit;

import com.example.watchflow.retrofit.WatchFlowServerApiInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.watchflow.Constants.BASE_URL;
import static com.example.watchflow.Constants.RETROFIT_TIME_OUT_LIMIT;

public class WatchFlowService {

    private static WatchFlowServerApiInterface instance = null;

    private WatchFlowService(){    }

    public static synchronized WatchFlowServerApiInterface getInstance(){
        if (instance != null){
            return instance;
        }

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(RETROFIT_TIME_OUT_LIMIT, TimeUnit.SECONDS)
                .writeTimeout(RETROFIT_TIME_OUT_LIMIT, TimeUnit.SECONDS)
                .connectTimeout(RETROFIT_TIME_OUT_LIMIT, TimeUnit.SECONDS);

        return instance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WatchFlowServerApiInterface.class);
    }
}
