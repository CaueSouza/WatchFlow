package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CameraHistoric implements Serializable {
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("reconForTimestamps")
    @Expose
    private ArrayList<ReconForTimestamp> reconForTimestamps;

    public CameraHistoric(String ip, String address, ArrayList<ReconForTimestamp> reconForTimestamps) {
        this.ip = ip;
        this.address = address;
        this.reconForTimestamps = reconForTimestamps;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<ReconForTimestamp> getHistoricAtomUnits() {
        return reconForTimestamps;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
