package com.example.watchflow.cameraInformation;

import static com.example.watchflow.Constants.ARTICULATED_TRUCK;
import static com.example.watchflow.Constants.BICYCLE;
import static com.example.watchflow.Constants.BUS;
import static com.example.watchflow.Constants.CAMERA_INFORMATIONS_ENDPOINT;
import static com.example.watchflow.Constants.CAR;
import static com.example.watchflow.Constants.GET_INFO_OR_DELETE_CAM_HEADER_FIELDS;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.MOTORCYCLE;
import static com.example.watchflow.Constants.MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.NON_MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.PEDESTRIAN;
import static com.example.watchflow.Constants.PICKUP_TRUCK;
import static com.example.watchflow.Constants.RECOGNITIONS;
import static com.example.watchflow.Constants.RECOGNITION_FIELDS;
import static com.example.watchflow.Constants.SINGLE_UNIT_TRUCK;
import static com.example.watchflow.Constants.TOTAL;
import static com.example.watchflow.Constants.WORK_VAN;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.ImageUtil;
import com.example.watchflow.R;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraInformationViewModel extends AndroidViewModel {
    public static final String TAG = CameraInformationViewModel.class.getSimpleName();
    private MutableLiveData<String> image = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Data>> localizationData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Data>> recognitionData = new MutableLiveData<>();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final Geocoder geocoder;

    public CameraInformationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        geocoder = new Geocoder(application, Locale.getDefault());
    }

    public MutableLiveData<String> getImage() {
        return image;
    }

    public void getCameraInformation(String cameraIp) {
        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();
        headers_data.add(cameraIp);

        serverRepository.createRequest(cameraInformationCallback, CAMERA_INFORMATIONS_ENDPOINT,
                GET_INFO_OR_DELETE_CAM_HEADER_FIELDS, headers_data, true);
    }

    Callback<JsonObject> cameraInformationCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.camera_informations_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String image = ImageUtil.saveImage(application, response.body().get("snapshot").getAsString());
                getImage().setValue(image);

                double latitude = response.body().get(LATITUDE).getAsDouble();
                double longitude = response.body().get(LONGITUDE).getAsDouble();
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

                ArrayList<Data> localizationArrayList = new ArrayList<>();
                localizationArrayList.add(new Data(application.getApplicationContext().getResources().getString(R.string.address_title), address));
                localizationData.setValue(localizationArrayList);

                JsonObject recognitions = response.body().get(RECOGNITIONS).getAsJsonObject();
                ArrayList<Data> recognitionArrayList = retriveJsonData(recognitions);
                recognitionData.setValue(recognitionArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    public MutableLiveData<ArrayList<Data>> getLocalizationData() {
        return localizationData;
    }

    public MutableLiveData<ArrayList<Data>> getRecognitionData() {
        return recognitionData;
    }

    private ArrayList<Data> retriveJsonData(JsonObject recognitions) {
        ArrayList<Data> recognitionArrayList = new ArrayList<>();

        for (String field : RECOGNITION_FIELDS) {
            Data data = verifyData(recognitions, findTitle(field), field);
            if (data != null) recognitionArrayList.add(data);
        }

        return recognitionArrayList;
    }

    @Nullable
    private Data verifyData(JsonObject recognitions, String title, String Tag) {
        if (Integer.parseInt(recognitions.get(Tag).getAsString()) != 0)
            return new Data(title, recognitions.get(Tag).getAsString());
        else return null;
    }

    private String findTitle(String name) {
        switch (name) {
            case TOTAL:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_total);
            case ARTICULATED_TRUCK:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_articulated_truck);
            case BICYCLE:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_bicycle);
            case BUS:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_bus);
            case CAR:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_car);
            case MOTORCYCLE:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_motorcycle);
            case MOTORIZED_VEHICLE:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_motorized_vehicle);
            case NON_MOTORIZED_VEHICLE:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_non_motorized_vehicle);
            case PEDESTRIAN:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_pedestrian);
            case PICKUP_TRUCK:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_pickup_truck);
            case SINGLE_UNIT_TRUCK:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_single_unit_truck);
            case WORK_VAN:
                return application.getApplicationContext().getResources().getString(R.string.recognitions_work_van);

            default:
                return "";
        }
    }
}
