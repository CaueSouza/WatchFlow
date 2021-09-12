package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GraphCameraData implements Serializable {
    @SerializedName("historic")
    @Expose
    private final CameraHistoric historic;
    @SerializedName("color")
    @Expose
    private final int color;

    public GraphCameraData(CameraHistoric historic, int color) {
        this.historic = historic;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public CameraHistoric getHistoric() {
        return historic;
    }
}
