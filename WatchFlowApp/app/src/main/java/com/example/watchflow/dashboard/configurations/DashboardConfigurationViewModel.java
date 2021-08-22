package com.example.watchflow.dashboard.configurations;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.MESSAGE;
import static com.example.watchflow.Constants.ONLY_IPS;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.CameraDashboardConfigurationData;
import com.example.watchflow.CameraInformations;
import com.example.watchflow.R;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.maps.MapsViewModel;
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

public class DashboardConfigurationViewModel extends AndroidViewModel {
    private static final String TAG = DashboardConfigurationViewModel.class.getSimpleName();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<List<CameraDashboardConfigurationData>> allCamerasIPs = new MutableLiveData<>();

    public DashboardConfigurationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void requestAllRunningCameras() {
        List<String> headerFields = COMMON_HEADER_FIELDS;
        headerFields.add(ONLY_IPS);

        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();
        headers_data.add(1);

        serverRepository.createRequest(allRunningCamerasCallback, ALL_RUNNING_CAMERAS_ENDPOINT,
                headerFields, headers_data, true);
    }

    public void setAllCamerasIPs(List<CameraDashboardConfigurationData> allCameras) {
        this.allCamerasIPs.setValue(allCameras);
    }

    Callback<JsonObject> allRunningCamerasCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_running_cams_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            List<CameraDashboardConfigurationData> allCameraInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray camerasArray = (JsonArray) responseData.get(CAMERAS);

            for (JsonElement cameraJson : camerasArray) {
                JsonObject object = cameraJson.getAsJsonObject();
                allCameraInformations.add(
                        new CameraDashboardConfigurationData(object.get(IP).getAsString()));
            }

            setAllCamerasIPs(allCameraInformations);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
