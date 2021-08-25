package com.example.watchflow;

import static com.example.watchflow.Constants.ADM_TYPE;
import static com.example.watchflow.Constants.LOGIN_USER_BODY_FIELDS;
import static com.example.watchflow.Constants.LOGIN_USER_HEADER_FIELDS;
import static com.example.watchflow.Constants.PASSWORD;
import static com.example.watchflow.Constants.USER_ID;
import static com.example.watchflow.Constants.USER_LOGIN_ENDPOINT;
import static com.example.watchflow.Constants.USER_TYPE;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.maps.GpsTracker;
import com.example.watchflow.maps.MapsActivity;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    public static final String TAG = MainActivityViewModel.class.getSimpleName();
    public final GpsTracker gpsTracker;
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

        serverRepository.createRequest(loginUserCallback, USER_LOGIN_ENDPOINT,
                LOGIN_USER_HEADER_FIELDS, params_data,
                LOGIN_USER_BODY_FIELDS, body_data);
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    final Callback<JsonObject> loginUserCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.fail_message_login, Toast.LENGTH_SHORT).show();
                return;
            }

            UserIdPwd.getInstance().setUserId(response.body().getAsJsonObject().get(USER_ID).getAsString());
            UserIdPwd.getInstance().setPassword(response.body().getAsJsonObject().get(PASSWORD).getAsString());
            UserIdPwd.getInstance().setAdm(response.body().getAsJsonObject().get(USER_TYPE).getAsInt() == ADM_TYPE);

            Intent intent = new Intent(application.getApplicationContext(), MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.getApplicationContext().startActivity(intent);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
