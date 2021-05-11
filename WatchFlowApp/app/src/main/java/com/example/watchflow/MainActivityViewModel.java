package com.example.watchflow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import retrofit2.Callback;

import static com.example.watchflow.Constants.LOGIN_USER_FIELDS;
import static com.example.watchflow.Constants.USER_LOGIN_ENDPOINT;

public class MainActivityViewModel extends AndroidViewModel {

    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void loginUser(Callback<JsonObject> objectCallback) {
        serverRepository.createPost(objectCallback, USER_LOGIN_ENDPOINT, LOGIN_USER_FIELDS, userName.getValue(), password.getValue(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

}
