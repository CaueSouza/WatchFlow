package com.example.watchflow.cameraInformation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CameraInformationViewModel extends AndroidViewModel {
    private MutableLiveData<String> image = new MutableLiveData<>();

    public CameraInformationViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<String> getImage() {
        return image;
    }
}
