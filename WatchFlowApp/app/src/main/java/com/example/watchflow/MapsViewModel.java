package com.example.watchflow;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Callback;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS;

public class MapsViewModel extends AndroidViewModel {

    public GpsTracker gpsTracker;
    public Double currentLatitude;
    public Double currentLongitude;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private MutableLiveData<List<CameraInformations>> allCameras = new MutableLiveData<>();

    public MapsViewModel(@NonNull Application application){
        super(application);

    }

    public void createGpsTracker(Context context){
        gpsTracker = new GpsTracker(context);

        if (gpsTracker.canGetLocation){
            currentLatitude = gpsTracker.getLatitude();
            currentLongitude = gpsTracker.getLongitude();
        }
    }


    public void getAllRunningCameras(Callback<JsonObject> objectCallback){
        serverRepository.createGet(objectCallback, ALL_RUNNING_CAMERAS);
    }

    public void setAllCameras(List<CameraInformations> allCameras){
        this.allCameras.setValue(allCameras);
    }

    public LiveData<List<CameraInformations>> getAllCameras(){
        return allCameras;
    }
}
