package com.example.watchflow.dashboard;

import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.DASHBOARD_INFORMATION_ENDPOINT;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.retrofit.ServerRepository;
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

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public SingleLiveEvent<Void> getDashboardDataError() {
        return dashboardDataError;
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

            //TODO TRATAR DADOS RECEBIDOS DA API
            //response.body.getAsJsonObject().get("cameras").getAsJsonArray().get(0).getAsJsonObject().get("127.0.0.1").getAsJsonArray().get(0).getAsJsonObject().get("bus")
            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
