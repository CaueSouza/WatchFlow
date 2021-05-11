package com.example.watchflow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInformations {
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public UserInformations(String userName, Double latitude, Double longitude) {
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setIp(String userName) {
        this.userName = userName;
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
