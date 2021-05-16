package com.example.watchflow.operations;

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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.DELETE_USER_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_USER_HEADER_FIELDS;
import static com.example.watchflow.Constants.REGISTER_USER_BODY_FIELDS;
import static com.example.watchflow.Constants.REGISTER_USER_ENDPOINT;

public class OperationViewModel extends AndroidViewModel {
    public static final String TAG = OperationViewModel.class.getSimpleName();

    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<String> newUserName = new MutableLiveData<>();
    private final MutableLiveData<String> newPassword = new MutableLiveData<>();
    private final MutableLiveData<String> oldUserName = new MutableLiveData<>();
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();
    private Application application;

    public OperationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void registerUser(boolean isAdm) {
        List<Object> headers_data = new ArrayList<>();
        headers_data.add(UserIdPwd.getInstance().getUserId());
        headers_data.add(UserIdPwd.getInstance().getPassword());

        List<Object> body_data = new ArrayList<>();
        body_data.add(newUserName.getValue());
        body_data.add(newPassword.getValue());
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
        //TODO create register cam procedure
    }

    public void deleteCam() {
        //TODO create delete cam procedure
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

    public SingleLiveEvent<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    //CALLBACKS
    Callback<JsonObject> registerUserCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.register_user_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.register_user_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> deleteUserCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.delete_user_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.delete_user_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
