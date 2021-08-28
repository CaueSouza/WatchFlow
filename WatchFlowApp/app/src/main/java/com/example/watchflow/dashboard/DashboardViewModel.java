package com.example.watchflow.dashboard;

import static com.example.watchflow.Constants.ARTICULATED_TRUCK;
import static com.example.watchflow.Constants.BICYCLE;
import static com.example.watchflow.Constants.BUS;
import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.CAR;
import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.DASHBOARD_INFORMATION_ENDPOINT;
import static com.example.watchflow.Constants.HISTORIC;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.MOTORCYCLE;
import static com.example.watchflow.Constants.MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.NON_MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.PEDESTRIAN;
import static com.example.watchflow.Constants.PICKUP_TRUCK;
import static com.example.watchflow.Constants.SINGLE_UNIT_TRUCK;
import static com.example.watchflow.Constants.TIMESTAMP;
import static com.example.watchflow.Constants.TOTAL;
import static com.example.watchflow.Constants.WORK_VAN;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends AndroidViewModel {
    public static final String TAG = DashboardViewModel.class.getSimpleName();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final SingleLiveEvent<Void> dashboardDataError = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> updateGraphs = new SingleLiveEvent<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public SingleLiveEvent<Void> getDashboardDataError() {
        return dashboardDataError;
    }

    public SingleLiveEvent<Void> getUpdateGraphs() {
        return updateGraphs;
    }

    public void getDashboardInformation() {
        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();

        serverRepository.createRequest(dashboardInformationCallback, DASHBOARD_INFORMATION_ENDPOINT,
                COMMON_HEADER_FIELDS, headers_data, true);
    }

    final Callback<JsonObject> dashboardInformationCallback = new Callback<>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                dashboardDataError.call();
                return;
            }

            JsonArray camerasArray = response.body().get(CAMERAS).getAsJsonArray();

            for (JsonElement cameraElement : camerasArray) {
                JsonObject camera = cameraElement.getAsJsonObject();
                JsonArray fullCameraHistoric = camera.get(HISTORIC).getAsJsonArray();
                String cameraIp = camera.get(IP).getAsString();

                for (JsonElement uniqueHistoricRecordElement: fullCameraHistoric) {
                    JsonObject uniqueHistoricRecord = uniqueHistoricRecordElement.getAsJsonObject();
                    int timestamp = uniqueHistoricRecord.get(TIMESTAMP).getAsInt();
                    int total = uniqueHistoricRecord.get(TOTAL).getAsInt();
                    int articulated_truck = uniqueHistoricRecord.get(ARTICULATED_TRUCK).getAsInt();
                    int bicycle = uniqueHistoricRecord.get(BICYCLE).getAsInt();
                    int bus = uniqueHistoricRecord.get(BUS).getAsInt();
                    int car = uniqueHistoricRecord.get(CAR).getAsInt();
                    int motorcycle = uniqueHistoricRecord.get(MOTORCYCLE).getAsInt();
                    int motorized_vehicle = uniqueHistoricRecord.get(MOTORIZED_VEHICLE).getAsInt();
                    int non_motorized_vehicle = uniqueHistoricRecord.get(NON_MOTORIZED_VEHICLE).getAsInt();
                    int pedestrian = uniqueHistoricRecord.get(PEDESTRIAN).getAsInt();
                    int pickup_truck = uniqueHistoricRecord.get(PICKUP_TRUCK).getAsInt();
                    int single_unit_truck = uniqueHistoricRecord.get(SINGLE_UNIT_TRUCK).getAsInt();
                    int work_van = uniqueHistoricRecord.get(WORK_VAN).getAsInt();

                    /*CREATE AN CUSTOM OBJECT THAT STORES THE TIMESTAMP AND RECON
                      THEN CREATE A LIST WITH ALL OF THIS DATA, THEN LOAD AT THE GRAPH */
                }
            }

            updateGraphs.call();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
