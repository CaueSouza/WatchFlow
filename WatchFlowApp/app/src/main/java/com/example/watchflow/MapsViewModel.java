package com.example.watchflow;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.BuildConfig.APPLICATION_ID;
import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_FIELDS;
import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LOCATIONS;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.MESSAGE;
import static com.example.watchflow.Constants.PASSWORD_KEY;
import static com.example.watchflow.Constants.USERNAME;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USERS_POSITIONS_FIELDS;
import static com.example.watchflow.Constants.USER_ID_KEY;
import static com.example.watchflow.Constants.USER_LOGOUT_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGOUT_FIELDS;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MapsViewModel.class.getSimpleName();
    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<List<CameraInformations>> allCameras = new MutableLiveData<>();
    private final MutableLiveData<List<UserInformations>> allUsers = new MutableLiveData<>();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private final SingleLiveEvent<Void> refreshEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> addUserEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeUserEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> addCamEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeCamEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> logoutEvent = new SingleLiveEvent<>();
    private final Application application;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        mPreferences = application.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void updateAllRunningCameras() {
        String userId = mPreferences.getString(USER_ID_KEY, "");
        String pwd = mPreferences.getString(PASSWORD_KEY, "");
        serverRepository.createPost(allRunningCamerasCallback, ALL_RUNNING_CAMERAS_ENDPOINT, ALL_RUNNING_CAMERAS_FIELDS, userId, pwd);
    }

    public void updateAllLoggedUsers() {
        String userId = mPreferences.getString(USER_ID_KEY, "");
        String pwd = mPreferences.getString(PASSWORD_KEY, "");
        serverRepository.createPost(allLoggedUsersCallback, USERS_POSITIONS_ENDPOINT, USERS_POSITIONS_FIELDS, userId, pwd);
    }

    public void logoutUser(){
        String userId = mPreferences.getString(USER_ID_KEY, "");
        String pwd = mPreferences.getString(PASSWORD_KEY, "");
        serverRepository.createPost(logoutUserCallback, USER_LOGOUT_ENDPOINT, USER_LOGOUT_FIELDS, userId, pwd);
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

    SingleLiveEvent<Void> getRefreshEvent() {
        return refreshEvent;
    }

    SingleLiveEvent<Void> getAddUserEvent() {
        return addUserEvent;
    }

    SingleLiveEvent<Void> getRemoveUserEvent() {
        return removeUserEvent;
    }

    SingleLiveEvent<Void> getAddCamEvent() {
        return addCamEvent;
    }

    SingleLiveEvent<Void> getRemoveCamEvent() {
        return removeCamEvent;
    }

    SingleLiveEvent<Void> getLogoutEvent() {
        return logoutEvent;
    }

    Callback<JsonObject> allRunningCamerasCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_running_cams_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            List<CameraInformations> allCameraInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray camerasArray = (JsonArray) responseData.get(CAMERAS);

            for (JsonElement cameraJson : camerasArray) {
                JsonObject object = cameraJson.getAsJsonObject();
                allCameraInformations.add(
                        new CameraInformations(
                                object.get(IP).getAsString(),
                                object.get(LATITUDE).getAsDouble(),
                                object.get(LONGITUDE).getAsDouble()));
            }

            setAllCameras(allCameraInformations);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
        }
    };

    Callback<JsonObject> allLoggedUsersCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_logged_users_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            List<UserInformations> allUsersInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray usersArray = (JsonArray) responseData.get(LOCATIONS);

            for (JsonElement userJson : usersArray) {
                JsonObject object = userJson.getAsJsonObject();
                allUsersInformations.add(
                        new UserInformations(
                                object.get(USERNAME).getAsString(),
                                object.get(LATITUDE).getAsDouble(),
                                object.get(LONGITUDE).getAsDouble()));
            }

            setAllUsers(allUsersInformations);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
        }
    };

    Callback<JsonObject> logoutUserCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.logout_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(application.getApplicationContext(), R.string.logout_success_message, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.getApplicationContext().startActivity(intent);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
        }
    };
}
