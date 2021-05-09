package com.example.watchflow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CameraInformations {
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public CameraInformations(String ip, Double latitude, Double longitude) {
        this.ip = ip;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
