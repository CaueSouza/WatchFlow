package com.example.watchflow.dashboard.configurations;

import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.IS_SELECTED;
import static com.example.watchflow.Constants.MESSAGE;
import static com.example.watchflow.Constants.MY_DASHBOARD_CAMS_ENDPOINT;
import static com.example.watchflow.Constants.SAVE_DASHBOARD_SELECTED_CAMERAS_BODY_FIELD;
import static com.example.watchflow.Constants.SAVE_DASHBOARD_SELECTED_IPS_ENDPOINT;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
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
    private final MutableLiveData<ArrayList<DashboardConfigurationCamData>> allCamerasIPs = new MutableLiveData<>();
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();

    public DashboardConfigurationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void saveAllChanges(ArrayList<String> selectedIPs) {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());

        List<Object> body_data = new ArrayList<>();
        body_data.add(selectedIPs);

        serverRepository.createRequest(updateDashboardSelectedCamerasCallback, SAVE_DASHBOARD_SELECTED_IPS_ENDPOINT,
                COMMON_HEADER_FIELDS, headers_data,
                SAVE_DASHBOARD_SELECTED_CAMERAS_BODY_FIELD, body_data);
    }

    public void requestMyDashboardCameras() {
        serverRepository.createRequest(myDashboardCamerasCallback, MY_DASHBOARD_CAMS_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void setAllCamerasIPs(ArrayList<DashboardConfigurationCamData> allCameras) {
        this.allCamerasIPs.setValue(allCameras);
    }

    public MutableLiveData<ArrayList<DashboardConfigurationCamData>> getAllCamerasIPs() {
        return allCamerasIPs;
    }

    public SingleLiveEvent<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    Callback<JsonObject> myDashboardCamerasCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_running_cams_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<DashboardConfigurationCamData> allCameraInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray camerasArray = (JsonArray) responseData.get(CAMERAS);

            for (JsonElement cameraJson : camerasArray) {
                JsonObject object = cameraJson.getAsJsonObject();

                String cameraIP = object.get(IP).getAsString();
                boolean isSelected = object.get(IS_SELECTED).getAsInt() == 1;

                allCameraInformations.add(new DashboardConfigurationCamData(cameraIP, isSelected));
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

    Callback<JsonObject> updateDashboardSelectedCamerasCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.failure_saving_text, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
