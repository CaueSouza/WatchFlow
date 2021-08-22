package com.example.watchflow.maps;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LOCATIONS;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.MESSAGE;
import static com.example.watchflow.Constants.USERNAME;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGOUT_ENDPOINT;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.CameraInformations;
import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.UserInformations;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MapsViewModel.class.getSimpleName();
    private GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<ArrayList<CameraInformations>> allCameras = new MutableLiveData<>();
    private final MutableLiveData<List<UserInformations>> allUsers = new MutableLiveData<>();
    private final SingleLiveEvent<Void> toggleTraffic = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openDashboardEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> refreshEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();
    private final Application application;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void updateAllRunningCameras() {
        serverRepository.createRequest(allRunningCamerasCallback, ALL_RUNNING_CAMERAS_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void updateAllLoggedUsers() {
        serverRepository.createRequest(allLoggedUsersCallback, USERS_POSITIONS_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void logoutUser() {
        serverRepository.createRequest(logoutUserCallback, USER_LOGOUT_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void setAllCameras(ArrayList<CameraInformations> allCameras) {
        this.allCameras.setValue(allCameras);
    }

    public LiveData<ArrayList<CameraInformations>> getAllCameras() {
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

    public SingleLiveEvent<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    public SingleLiveEvent<Void> getToggleTraffic() {
        return toggleTraffic;
    }

    public SingleLiveEvent<Void> getOpenDashboardEvent() {
        return openDashboardEvent;
    }

    public GpsTracker getGpsTracker() {
        return gpsTracker;
    }

    Callback<JsonObject> allRunningCamerasCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_running_cams_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<CameraInformations> allCameraInformations = new ArrayList<>();
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
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> allLoggedUsersCallback = new Callback<>() {

        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
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
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> logoutUserCallback = new Callback<>() {

        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.logout_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.logout_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
