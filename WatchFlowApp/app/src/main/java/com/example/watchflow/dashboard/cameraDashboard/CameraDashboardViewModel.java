package com.example.watchflow.dashboard.cameraDashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.watchflow.dashboard.GraphCameraData;

public class CameraDashboardViewModel extends AndroidViewModel {
    public static final String TAG = CameraDashboardViewModel.class.getSimpleName();
    private final Application application;
    private GraphCameraData graphCameraData;

    public CameraDashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public GraphCameraData getGraphCameraData() {
        return graphCameraData;
    }

    public void setGraphCameraData(GraphCameraData graphCameraData) {
        this.graphCameraData = graphCameraData;
    }
}
