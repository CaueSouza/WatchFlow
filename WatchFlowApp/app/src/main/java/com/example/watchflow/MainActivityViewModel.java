package com.example.watchflow;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.Constants.LOGIN_USER_BODY_FIELDS;
import static com.example.watchflow.Constants.LOGIN_USER_PARAMS_FIELDS;
import static com.example.watchflow.Constants.PASSWORD;
import static com.example.watchflow.Constants.USER_ID;
import static com.example.watchflow.Constants.USER_LOGIN_ENDPOINT;

public class MainActivityViewModel extends AndroidViewModel {
    public static final String TAG = MainActivityViewModel.class.getSimpleName();
    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final Application application;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void loginUser() {
        List<Object> params_data = new ArrayList<>();
        params_data.add(userName.getValue());
        params_data.add(password.getValue());

        List<Object> body_data = new ArrayList<>();
        body_data.add(gpsTracker.getLatitude());
        body_data.add(gpsTracker.getLongitude());

        serverRepository.createPost(loginUserCallback, USER_LOGIN_ENDPOINT,
                LOGIN_USER_PARAMS_FIELDS, params_data,
                LOGIN_USER_BODY_FIELDS, body_data);
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    Callback<JsonObject> loginUserCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.fail_message_login, Toast.LENGTH_SHORT).show();
                return;
            }

            UserIdPwd.getInstance().setUserId(response.body().get(USER_ID).getAsString());
            UserIdPwd.getInstance().setPassword(response.body().get(PASSWORD).getAsString());

            Intent intent = new Intent(application.getApplicationContext(), MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.getApplicationContext().startActivity(intent);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.invalid_user_message, Toast.LENGTH_SHORT).show();
        }
    };
}
