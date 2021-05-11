package com.example.watchflow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Callback;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_FIELDS;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MapsViewModel.class.getSimpleName();
    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private MutableLiveData<List<CameraInformations>> allCameras = new MutableLiveData<>();

    public MapsViewModel(@NonNull Application application) {
        super(application);
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void allRunningCameras(Callback<JsonObject> objectCallback) {
        serverRepository.createPost(objectCallback, ALL_RUNNING_CAMERAS_ENDPOINT, ALL_RUNNING_CAMERAS_FIELDS, UserId.getInstance().getUserId(), UserId.getInstance().getPassword());
    }

    public void setAllCameras(List<CameraInformations> allCameras) {
        this.allCameras.setValue(allCameras);
    }

    public LiveData<List<CameraInformations>> getAllCameras() {
        return allCameras;
    }
}
