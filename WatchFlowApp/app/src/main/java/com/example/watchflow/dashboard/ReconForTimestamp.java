package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReconForTimestamp implements Serializable {
    @SerializedName("timestamp")
    @Expose
    private final int timestamp;
    @SerializedName("total")
    @Expose
    private final int total;
    @SerializedName("articulated_truck")
    @Expose
    private final int articulated_truck;
    @SerializedName("bicycle")
    @Expose
    private final int bicycle;
    @SerializedName("bus")
    @Expose
    private final int bus;
    @SerializedName("car")
    @Expose
    private final int car;
    @SerializedName("motorcycle")
    @Expose
    private final int motorcycle;
    @SerializedName("motorized_vehicle")
    @Expose
    private final int motorized_vehicle;
    @SerializedName("non_motorized_vehicle")
    @Expose
    private final int non_motorized_vehicle;
    @SerializedName("pedestrian")
    @Expose
    private final int pedestrian;
    @SerializedName("pickup_truck")
    @Expose
    private final int pickup_truck;
    @SerializedName("single_unit_truck")
    @Expose
    private final int single_unit_truck;
    @SerializedName("work_van")
    @Expose
    private final int work_van;

    public ReconForTimestamp(int timestamp, int total, int articulated_truck, int bicycle, int bus, int car, int motorcycle, int motorized_vehicle, int non_motorized_vehicle, int pedestrian, int pickup_truck, int single_unit_truck, int work_van) {
        this.timestamp = timestamp;
        this.total = total;
        this.articulated_truck = articulated_truck;
        this.bicycle = bicycle;
        this.bus = bus;
        this.car = car;
        this.motorcycle = motorcycle;
        this.motorized_vehicle = motorized_vehicle;
        this.non_motorized_vehicle = non_motorized_vehicle;
        this.pedestrian = pedestrian;
        this.pickup_truck = pickup_truck;
        this.single_unit_truck = single_unit_truck;
        this.work_van = work_van;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getTotal() {
        return total;
    }

    public int getArticulated_truck() {
        return articulated_truck;
    }

    public int getBicycle() {
        return bicycle;
    }

    public int getBus() {
        return bus;
    }

    public int getCar() {
        return car;
    }

    public int getMotorcycle() {
        return motorcycle;
    }

    public int getMotorized_vehicle() {
        return motorized_vehicle;
    }

    public int getNon_motorized_vehicle() {
        return non_motorized_vehicle;
    }

    public int getPedestrian() {
        return pedestrian;
    }

    public int getPickup_truck() {
        return pickup_truck;
    }

    public int getSingle_unit_truck() {
        return single_unit_truck;
    }

    public int getWork_van() {
        return work_van;
    }
}
