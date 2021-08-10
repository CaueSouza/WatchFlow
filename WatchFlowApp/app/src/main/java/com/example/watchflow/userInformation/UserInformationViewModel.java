package com.example.watchflow.userInformation;

import static com.example.watchflow.Constants.GET_USER_INFO_CAM_HEADER_FIELDS;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.PHONE;
import static com.example.watchflow.Constants.USER_INFORMATIONS_ENDPOINT;

import android.app.Application;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.Mask;
import com.example.watchflow.R;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformationViewModel extends AndroidViewModel {
    public static final String TAG = UserInformationViewModel.class.getSimpleName();
    private final Application application;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> userPhone = new MutableLiveData<>();
    private final MutableLiveData<String> userLocation = new MutableLiveData<>();
    private final Geocoder geocoder;

    public UserInformationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        geocoder = new Geocoder(application, Locale.getDefault());
    }

    public void getUserInformation(String userName) {
        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();
        headers_data.add(userName);

        serverRepository.createRequest(userInformationCallback, USER_INFORMATIONS_ENDPOINT,
                GET_USER_INFO_CAM_HEADER_FIELDS, headers_data, true);
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getUserPhone() {
        return userPhone;
    }

    public MutableLiveData<String> getUserLocation() {
        return userLocation;
    }

    Callback<JsonObject> userInformationCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.camera_informations_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            String address;

            try {
                double latitude = response.body().get(LATITUDE).getAsDouble();
                double longitude = response.body().get(LONGITUDE).getAsDouble();
                address = geocoder.getFromLocation(latitude, longitude, 1).get(0).getAddressLine(0);

            } catch (Exception e) {
                address = "";
                e.printStackTrace();
            }

            userLocation.setValue(address);
            userPhone.setValue(Mask.mask(response.body().get(PHONE).getAsString(), "(##)#####-####", "()-"));

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
