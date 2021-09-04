package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphCameraData {
    @SerializedName("ip")
    @Expose
    private final String ip;
    @SerializedName("address")
    @Expose
    private final String address;
    @SerializedName("historic")
    @Expose
    private final CameraHistoric historic;
    @SerializedName("color")
    @Expose
    private final int color;

    public GraphCameraData(String ip, String address, CameraHistoric historic, int color) {
        this.ip = ip;
        this.address = address;
        this.historic = historic;
        this.color = color;
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
