package com.example.watchflow.retrofit;

import androidx.lifecycle.LiveData;

import com.example.watchflow.CameraInformations;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS;
import static com.example.watchflow.Constants.BASE_URL;

public class ServerRepository {
    public static final String TAG = ServerRepository.class.getSimpleName();

    private static ServerRepository instance = null;
    private WatchFlowServerApiInterface watchFlowServerApiInterface;

    private ServerRepository(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        watchFlowServerApiInterface = retrofit.create(WatchFlowServerApiInterface.class);
    }

    public static synchronized ServerRepository getInstance(){
        if (instance != null){
            return instance;
        }

        instance = new ServerRepository();
        return instance;
    }

    public void createGet(Callback<JsonObject> objectCallback, String getRequest){
        Call<JsonObject> call = null;

        switch (getRequest){
            case ALL_RUNNING_CAMERAS:
                call = watchFlowServerApiInterface.getAllCamerasIps();
                break;
        }

        if (call != null){
            call.enqueue(objectCallback);
        }
    }
}
