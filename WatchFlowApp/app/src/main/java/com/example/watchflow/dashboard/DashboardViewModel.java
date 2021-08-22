package com.example.watchflow.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class DashboardViewModel extends AndroidViewModel {
    private final Application application;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }
}
