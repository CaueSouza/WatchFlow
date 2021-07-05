package com.example.watchflow.cameraInformation;

import android.app.Application;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.ImageUtil;
import com.example.watchflow.R;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.Constants.CAMERA_INFORMATIONS_ENDPOINT;
import static com.example.watchflow.Constants.GET_INFO_OR_DELETE_CAM_HEADER_FIELDS;
import static com.example.watchflow.Constants.IMAGE;

public class CameraInformationViewModel extends AndroidViewModel {
    public static final String TAG = CameraInformationViewModel.class.getSimpleName();
    private MutableLiveData<String> image = new MutableLiveData<>();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private Geocoder geocoder;

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

                Double latitude = response.body().get("latitude").getAsDouble();
                Double longitude = response.body().get("longitude").getAsDouble();
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

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
}
