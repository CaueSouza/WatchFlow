package com.example.watchflow.operations;

import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.DELETE_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_USER_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_USER_HEADER_FIELDS;
import static com.example.watchflow.Constants.GET_INFO_OR_DELETE_CAM_HEADER_FIELDS;
import static com.example.watchflow.Constants.REGISTER_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.REGISTER_CAM_BODY_FIELDS;
import static com.example.watchflow.Constants.REGISTER_USER_BODY_FIELDS;
import static com.example.watchflow.Constants.REGISTER_USER_ENDPOINT;
import static com.example.watchflow.Constants.UPDATE_PHONE_BODY_FIELDS;
import static com.example.watchflow.Constants.UPDATE_PHONE_ENDPOINT;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.Mask;
import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationViewModel extends AndroidViewModel {
    public static final String TAG = OperationViewModel.class.getSimpleName();

    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<String> newUserName = new MutableLiveData<>();
    private final MutableLiveData<String> newPassword = new MutableLiveData<>();
    private final MutableLiveData<String> oldUserName = new MutableLiveData<>();
    private final MutableLiveData<String> newTelephone = new MutableLiveData<>();
    private final MutableLiveData<CameraAddressPOJO> cameraInfo = new MutableLiveData<>();
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();
    private final Application application;

    public OperationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        cameraInfo.setValue(new CameraAddressPOJO());
    }

    public void registerUser(boolean isAdm) {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());

        List<Object> body_data = new ArrayList<>();
        body_data.add(newUserName.getValue());
        body_data.add(newPassword.getValue());
        body_data.add(Mask.unmask(newTelephone.getValue()));
        body_data.add(isAdm ? 1 : 0);

        serverRepository.createRequest(registerUserCallback, REGISTER_USER_ENDPOINT,
                COMMON_HEADER_FIELDS, headers_data,
                REGISTER_USER_BODY_FIELDS, body_data);
    }

    public void deleteUser() {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());
        headers_data.add(oldUserName.getValue());

        serverRepository.createRequest(deleteUserCallback, DELETE_USER_ENDPOINT,
                DELETE_USER_HEADER_FIELDS, headers_data, null, null);
    }

    public void registerCam() {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());

        List<Object> body_data = new ArrayList<>();
        body_data.add(cameraInfo.getValue().getIP());
        body_data.add(cameraInfo.getValue().getStreet());
        body_data.add(cameraInfo.getValue().getNumber());
        body_data.add(cameraInfo.getValue().getNeighborhood());
        body_data.add(cameraInfo.getValue().getCity());
        body_data.add(cameraInfo.getValue().getCountry());


        serverRepository.createRequest(registerCamCallback, REGISTER_CAMERA_ENDPOINT,
                COMMON_HEADER_FIELDS, headers_data,
                REGISTER_CAM_BODY_FIELDS, body_data);
    }

    public void deleteCam() {
        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();
        headers_data.add(cameraInfo.getValue().getIP());

        serverRepository.createRequest(deleteCamCallback, DELETE_CAMERA_ENDPOINT,
                GET_INFO_OR_DELETE_CAM_HEADER_FIELDS, headers_data, null, null);
    }

    public void updatePhone() {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());

        List<Object> body_data = new ArrayList<>();
        body_data.add(Mask.unmask(newTelephone.getValue()));

        serverRepository.createRequest(updatePhoneCallback, UPDATE_PHONE_ENDPOINT,
                COMMON_HEADER_FIELDS, headers_data,
                UPDATE_PHONE_BODY_FIELDS, body_data);
    }

    public MutableLiveData<String> getNewUserName() {
        return newUserName;
    }

    public MutableLiveData<String> getNewPassword() {
        return newPassword;
    }

    public MutableLiveData<String> getOldUserName() {
        return oldUserName;
    }

    public MutableLiveData<String> getNewTelephone() {
        return newTelephone;
    }

    public SingleLiveEvent<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    public MutableLiveData<CameraAddressPOJO> getCameraInfo() {
        return cameraInfo;
    }

    //CALLBACKS
    Callback<JsonObject> registerUserCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.register_user_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.register_user_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> deleteUserCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.delete_user_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.delete_user_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> registerCamCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.register_cam_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.register_cam_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> deleteCamCallback = new Callback<>() {
        @Override
        public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.delete_cam_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.delete_cam_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> updatePhoneCallback = new Callback<>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.update_phone_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.update_phone_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
