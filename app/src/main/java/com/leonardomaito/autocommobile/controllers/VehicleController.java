package com.leonardomaito.autocommobile.controllers;

import android.widget.EditText;


import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;

public class VehicleController {

    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleColor;
    private int vehicleYear;
    private double vehicleKm;
    private String vehicleChassi;

    public Vehicle returnNewVehicle(EditText etBrand, EditText etModel, MaskEditText
            etChassi, EditText etYear, EditText etColor, EditText etKm){

        vehicleBrand = etBrand.getText().toString();
        vehicleModel = etModel.getText().toString();
        vehicleColor = etColor.getText().toString();
        vehicleYear = etYear.getText().toString().isEmpty() ? 0 : Integer.parseInt(etYear.getText().toString());
        vehicleKm = etKm.getText().toString().isEmpty() ? 0 : Double.parseDouble(etKm.getText().toString());
        vehicleChassi = etChassi.getText().toString();

        Vehicle newVehicle = new Vehicle.VehicleBuilder(vehicleBrand,vehicleModel)
                .chassi(vehicleChassi)
                .year(vehicleYear)
                .color(vehicleColor)
                .km(vehicleKm)
                .build();

        return newVehicle;
    }
}
