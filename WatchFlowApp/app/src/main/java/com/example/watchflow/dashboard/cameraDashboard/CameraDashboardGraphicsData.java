package com.example.watchflow.dashboard.cameraDashboard;

import com.example.watchflow.dashboard.SpecificReconForTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CameraDashboardGraphicsData {
    @SerializedName("title")
    @Expose
    private final String title;
    @SerializedName("total")
    @Expose
    private final int total;
    @SerializedName("reconForTimestamps")
    @Expose
    private ArrayList<SpecificReconForTimestamp> reconForTimestamps;

    public CameraDashboardGraphicsData(String title, int total, ArrayList<SpecificReconForTimestamp> reconForTimestamps) {
        this.title = title;
        this.total = total;
        this.reconForTimestamps = reconForTimestamps;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<SpecificReconForTimestamp> getReconForTimestamps() {
        return reconForTimestamps;
    }

    public int getTotal() {
        return total;
    }
}
