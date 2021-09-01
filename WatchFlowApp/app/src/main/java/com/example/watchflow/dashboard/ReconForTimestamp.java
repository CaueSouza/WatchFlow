package com.example.watchflow.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReconForTimestamp {
    @SerializedName("timestamp")
    @Expose
    private int timestamp;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("articulated_truck")
    @Expose
    private int articulated_truck;
    @SerializedName("bicycle")
    @Expose
    private int bicycle;
    @SerializedName("bus")
    @Expose
    private int bus;
    @SerializedName("car")
    @Expose
    private int car;
    @SerializedName("motorcycle")
    @Expose
    private int motorcycle;
    @SerializedName("motorized_vehicle")
    @Expose
    private int motorized_vehicle;
    @SerializedName("non_motorized_vehicle")
    @Expose
    private int non_motorized_vehicle;
    @SerializedName("pedestrian")
    @Expose
    private int pedestrian;
    @SerializedName("pickup_truck")
    @Expose
    private int pickup_truck;
    @SerializedName("single_unit_truck")
    @Expose
    private int single_unit_truck;
    @SerializedName("work_van")
    @Expose
    private int work_van;

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

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getArticulated_truck() {
        return articulated_truck;
    }

    public void setArticulated_truck(int articulated_truck) {
        this.articulated_truck = articulated_truck;
    }

    public int getBicycle() {
        return bicycle;
    }

    public void setBicycle(int bicycle) {
        this.bicycle = bicycle;
    }

    public int getBus() {
        return bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    public int getMotorcycle() {
        return motorcycle;
    }

    public void setMotorcycle(int motorcycle) {
        this.motorcycle = motorcycle;
    }

    public int getMotorized_vehicle() {
        return motorized_vehicle;
    }

    public void setMotorized_vehicle(int motorized_vehicle) {
        this.motorized_vehicle = motorized_vehicle;
    }

    public int getNon_motorized_vehicle() {
        return non_motorized_vehicle;
    }

    public void setNon_motorized_vehicle(int non_motorized_vehicle) {
        this.non_motorized_vehicle = non_motorized_vehicle;
    }

    public int getPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(int pedestrian) {
        this.pedestrian = pedestrian;
    }

    public int getPickup_truck() {
        return pickup_truck;
    }

    public void setPickup_truck(int pickup_truck) {
        this.pickup_truck = pickup_truck;
    }

    public int getSingle_unit_truck() {
        return single_unit_truck;
    }

    public void setSingle_unit_truck(int single_unit_truck) {
        this.single_unit_truck = single_unit_truck;
    }

    public int getWork_van() {
        return work_van;
    }

    public void setWork_van(int work_van) {
        this.work_van = work_van;
    }
}
