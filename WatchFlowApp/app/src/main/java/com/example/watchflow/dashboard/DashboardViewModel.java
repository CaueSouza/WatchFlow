package com.example.watchflow.dashboard;

import static com.example.watchflow.Constants.ADDRESS;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends AndroidViewModel {
    public static final String TAG = DashboardViewModel.class.getSimpleName();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final SingleLiveEvent<Void> dashboardDataError = new SingleLiveEvent<>();
    private final MutableLiveData<ArrayList<CameraHistoric>> allCamerasHistoricMutableLiveData = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public SingleLiveEvent<Void> getDashboardDataError() {
        return dashboardDataError;
    }

    public LiveData<ArrayList<CameraHistoric>> getAllCamerasHistoricMutableLiveData() {
        return allCamerasHistoricMutableLiveData;
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
            ArrayList<CameraHistoric> allCameraHistoric = new ArrayList<>();

            for (JsonElement cameraElement : camerasArray) {
                JsonObject camera = cameraElement.getAsJsonObject();
                JsonArray fullCameraHistoric = camera.get(HISTORIC).getAsJsonArray();
                String cameraIp = camera.get(IP).getAsString();
                String address = camera.get(ADDRESS).getAsString();
                ArrayList<ReconForTimestamp> reconForTimestamps = new ArrayList<>();

                for (JsonElement historicAtomElement : fullCameraHistoric) {
                    JsonObject historicAtom = historicAtomElement.getAsJsonObject();

                    ReconForTimestamp reconForTimestamp = new ReconForTimestamp(
                            historicAtom.get(TIMESTAMP).getAsInt(),
                            historicAtom.get(TOTAL).getAsInt(),
                            historicAtom.get(ARTICULATED_TRUCK).getAsInt(),
                            historicAtom.get(BICYCLE).getAsInt(),
                            historicAtom.get(BUS).getAsInt(),
                            historicAtom.get(CAR).getAsInt(),
                            historicAtom.get(MOTORCYCLE).getAsInt(),
                            historicAtom.get(MOTORIZED_VEHICLE).getAsInt(),
                            historicAtom.get(NON_MOTORIZED_VEHICLE).getAsInt(),
                            historicAtom.get(PEDESTRIAN).getAsInt(),
                            historicAtom.get(PICKUP_TRUCK).getAsInt(),
                            historicAtom.get(SINGLE_UNIT_TRUCK).getAsInt(),
                            historicAtom.get(WORK_VAN).getAsInt()
                    );

                    reconForTimestamps.add(reconForTimestamp);
                }

                allCameraHistoric.add(new CameraHistoric(cameraIp, address, reconForTimestamps));
            }

            allCamerasHistoricMutableLiveData.setValue(allCameraHistoric);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
