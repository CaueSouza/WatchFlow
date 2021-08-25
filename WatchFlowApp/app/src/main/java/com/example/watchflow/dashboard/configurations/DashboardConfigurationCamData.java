package com.example.watchflow.dashboard.configurations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardConfigurationCamData {
    @SerializedName("ip")
    @Expose
    private final String ip;
    @SerializedName("checked")
    @Expose
    private final boolean isChecked;
    @SerializedName("address")
    @Expose
    private final String address;

    public DashboardConfigurationCamData(String ip, String address, boolean isSelected) {
        this.ip = ip;
        this.address = address;
        this.isChecked = isSelected;
    }

    public String getIp() {
        return ip;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getAddress() {
        return address;
    }

}
