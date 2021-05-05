package com.example.watchflow;

import android.content.Context;

import androidx.lifecycle.ViewModel;

public class MapsViewModel extends ViewModel {

    public GpsTracker gpsTracker;
    public Double currentLatitude;
    public Double currentLongitude;

    public void createGpsTracker(Context context){
        gpsTracker = new GpsTracker(context);

        if (gpsTracker.canGetLocation){
            currentLatitude = gpsTracker.getLatitude();
            currentLongitude = gpsTracker.getLongitude();
        }
    }
}
