package com.example.watchflow.dashboard.configurations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardConfigurationCamData {
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("checked")
    @Expose
    private boolean isChecked;

    public DashboardConfigurationCamData(String ip, boolean isSelected) {
        this.ip = ip;
        this.isChecked = isSelected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
