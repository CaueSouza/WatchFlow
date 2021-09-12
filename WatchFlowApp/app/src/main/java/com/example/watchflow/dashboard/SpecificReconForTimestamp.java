package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificReconForTimestamp {
    @SerializedName("timestamp")
    @Expose
    private final int timestamp;
    @SerializedName("data")
    @Expose
    private final int data;

    public SpecificReconForTimestamp(int timestamp, int data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getData() {
        return data;
    }
}
