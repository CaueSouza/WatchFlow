package com.example.watchflow;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Callback;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_FIELDS;
import static com.example.watchflow.Constants.PASSWORD_KEY;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USERS_POSITIONS_FIELDS;
import static com.example.watchflow.Constants.USER_ID_KEY;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MapsViewModel.class.getSimpleName();
    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<List<CameraInformations>> allCameras = new MutableLiveData<>();
    private final MutableLiveData<List<UserInformations>> allUsers = new MutableLiveData<>();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public MapsViewModel(@NonNull Application application) {
        super(application);

        mPreferences = application.getSharedPreferences("com.example.watchflow", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void allRunningCameras(Callback<JsonObject> objectCallback) {
        String userId = mPreferences.getString(USER_ID_KEY, "");
        String pwd = mPreferences.getString(PASSWORD_KEY, "");
        serverRepository.createPost(objectCallback, ALL_RUNNING_CAMERAS_ENDPOINT, ALL_RUNNING_CAMERAS_FIELDS, userId, pwd);
    }

    public void allLoggedUsers(Callback<JsonObject> objectCallback) {
        String userId = mPreferences.getString(USER_ID_KEY, "");
        String pwd = mPreferences.getString(PASSWORD_KEY, "");
        serverRepository.createPost(objectCallback, USERS_POSITIONS_ENDPOINT, USERS_POSITIONS_FIELDS, userId, pwd);
    }

    public void setAllCameras(List<CameraInformations> allCameras) {
        this.allCameras.setValue(allCameras);
    }

    public LiveData<List<CameraInformations>> getAllCameras() {
        return allCameras;
    }


    public void setAllUsers(List<UserInformations> allUsers) {
        this.allUsers.setValue(allUsers);
    }

    public LiveData<List<UserInformations>> getAllUsers() {
        return allUsers;
    }
}
