package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CameraHistoric {
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("reconForTimestamps")
    @Expose
    private ArrayList<ReconForTimestamp> reconForTimestamps;

    public CameraHistoric(String ip, ArrayList<ReconForTimestamp> reconForTimestamps) {
        this.ip = ip;
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

    public void setHistoricAtomUnits(ArrayList<ReconForTimestamp> reconForTimestamps) {
        this.reconForTimestamps = reconForTimestamps;
    }
}
