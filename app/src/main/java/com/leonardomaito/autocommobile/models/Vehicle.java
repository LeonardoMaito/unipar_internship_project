package com.leonardomaito.autocommobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {

    private String brand;
    private String model;
    private String color;
    private int year;
    private double km;
    private double chassi;

    public Vehicle() {
    }

    private Vehicle(VehicleBuilder vehicleBuilder){
        this.brand = vehicleBuilder.brand;
        this.model = vehicleBuilder.model;
        this.chassi = vehicleBuilder.chassi;
        this.year = vehicleBuilder.year;
        this.color = vehicleBuilder.color;
        this.km = vehicleBuilder.km;;
    }


    protected Vehicle(Parcel in) {
        brand = in.readString();
        model = in.readString();
        chassi = in.readDouble();
        year = in.readInt();
        color = in.readString();
        km = in.readDouble();

    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getChassi() {
        return chassi;
    }

    public void setChassi(double chassi) {
        this.chassi = chassi;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(brand);
        parcel.writeString(model);
        parcel.writeDouble(chassi);
        parcel.writeInt(year);
        parcel.writeString(color);
        parcel.writeDouble(km);

    }

    @Override
    public String toString() {
        return "Veículo" +
                "brand='" + brand +
                "\n Modelo= " + model +
                "\n Cor= " + color +
                "\n Ano= " + year +
                "\n KM= " + km +
                "\n Chassi= " + chassi;
    }

    public static class VehicleBuilder{

        private final String brand;
        private final String model;
        private double chassi;
        private int year;
        private String color;
        private double km;

        public VehicleBuilder(String brand,String model){
            this.brand = brand;
            this.model = model;
        }

        public Vehicle.VehicleBuilder chassi(double chassi){
            this.chassi = chassi;
            return this;
        }

        public Vehicle.VehicleBuilder year(int year){
            this.year = year;
            return this;
        }

        public Vehicle.VehicleBuilder color(String color){
            this.color = color;
            return this;
        }

        public Vehicle.VehicleBuilder km(double km){
            this.km = km;
            return this;
        }

        public Vehicle build(){
            Vehicle vehicle = new Vehicle(this);
            return vehicle;
        }
    }
}
