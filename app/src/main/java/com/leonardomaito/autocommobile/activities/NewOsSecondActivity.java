package com.leonardomaito.autocommobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.leonardomaito.autocommobile.controllers.VehicleController;
import com.leonardomaito.autocommobile.models.Client;
import autocommobile.R;

public class NewOsSecondActivity extends AppCompatActivity {

    private Button startOsSecond;
    private EditText etBrand;
    private EditText etModel;
    private EditText etChassi;
    private EditText etYear;
    private EditText etColor;
    private EditText etKm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os_second);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");

        startOsSecond = findViewById(R.id.btNextOs2);
        etBrand = findViewById(R.id.etBrandInput);
        etModel = findViewById(R.id.etModelNumber);
        etChassi = findViewById(R.id.etChassiInput);
        etYear = findViewById(R.id.etYearInput);
        etColor = findViewById(R.id.etColorInput);
        etKm = findViewById(R.id.etKmInput);

        VehicleController vehicleController = new VehicleController();

        startOsSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newOsStepThirdIntent  = new Intent(getApplicationContext(), NewOsThirdActivity.class);

                newOsStepThirdIntent.putExtra("novoCliente", newClient);
                newOsStepThirdIntent.putExtra("novoCarro", vehicleController.returnNewVehicle(etBrand,etModel,
                        etChassi, etYear, etColor, etKm));

                startActivity(newOsStepThirdIntent);
            }
        });
    }
}