package com.example.watchflow.dashboard;

import android.graphics.Camera;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphCameraData {
    @SerializedName("ip")
    @Expose
    private final String ip;
    @SerializedName("color")
    @Expose
    private final int color;
    @SerializedName("address")
    @Expose
    private final String address;
    @SerializedName("historic")
    @Expose
    private final CameraHistoric historic;

    public GraphCameraData(String ip, int color, String address, CameraHistoric historic) {
        this.ip = ip;
        this.color = color;
        this.address = address;
        this.historic = historic;
    }

    public String getIp() {
        return ip;
    }

    public int getColor() {
        return color;
    }

    public String getAddress() {
        return address;
    }

    public CameraHistoric getHistoric() {
        return historic;
    }
}
